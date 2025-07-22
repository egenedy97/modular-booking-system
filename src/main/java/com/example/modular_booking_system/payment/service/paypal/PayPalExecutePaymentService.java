package com.example.modular_booking_system.payment.service.paypal;

import com.example.modular_booking_system.core.events.PaymentAuditEvent;
import com.example.modular_booking_system.payment.config.PayPalConfig;
import com.example.modular_booking_system.payment.exception.PaymentException;
import com.example.modular_booking_system.payment.model.PaymentDetails;
import com.example.modular_booking_system.payment.service.AuditEventPublisher;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayPalExecutePaymentService {

    private final WebClient webClient;
    private final PayPalAccessTokenService accessTokenService;
    private final PayPalConfig.PayPalProperties payPalProperties;

    private final AuditEventPublisher auditEventPublisher;

    public PaymentDetails executePayment(String paymentId, String payerId) throws PaymentException {
        try {
            JsonNode response = capturePayment(paymentId);

            PaymentDetails paymentDetails = buildPaymentDetailsFromCapture(response);

            // Add audit event
            auditEventPublisher.publish(new PaymentAuditEvent(
                    "PAYMENT_COMPLETED",
                    paymentDetails.getId(),
                    paymentDetails.getPayer().getPayerId(),
                    paymentDetails.getAmount().getTotal(),
                    LocalDateTime.now()
            ));

            return paymentDetails;

        } catch (WebClientResponseException e) {
            String errorMsg = "Failed to execute PayPal payment: " + e.getResponseBodyAsString();
            log.error(errorMsg, e);
            throw new PaymentException(errorMsg, e);
        } catch (Exception e) {
            log.error("Error executing PayPal payment", e);
            throw new PaymentException("Failed to execute PayPal payment: " + e.getMessage(), e);
        }
    }

    private JsonNode capturePayment(String paymentId) throws PaymentException {
        String token = accessTokenService.getAccessToken();
        return webClient.post()
                .uri(payPalProperties.baseUrl() + "/v2/checkout/orders/" + paymentId + "/capture")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }

    private PaymentDetails buildPaymentDetailsFromCapture(JsonNode response) {
        PaymentDetails.Amount amount = extractAmountDetails(response);
        PaymentDetails.Payer payer = extractPayerDetails(response);

        return PaymentDetails.builder()
                .id(response.path("id").asText())
                .state(response.path("status").asText())
                .intent("CAPTURE")
                .createTime(Instant.now().toString())
                .amount(amount)
                .payer(payer)
                .build();
    }

    private PaymentDetails.Amount extractAmountDetails(JsonNode response) {
        JsonNode amountNode = response.path("purchase_units").get(0)
                .path("payments").path("captures").get(0)
                .path("amount");

        return PaymentDetails.Amount.builder()
                .currency(amountNode.path("currency_code").asText())
                .total(new BigDecimal(amountNode.path("value").asText()))
                .build();
    }

    private PaymentDetails.Payer extractPayerDetails(JsonNode response) {
        JsonNode payerNode = response.path("payer");
        JsonNode nameNode = payerNode.path("name");
        String firstName = nameNode.path("given_name").asText("");
        String lastName = nameNode.path("surname").asText("");
        String fullName = (firstName + " " + lastName).trim();

        return PaymentDetails.Payer.builder()
                .payerId(payerNode.path("payer_id").asText())
                .paymentMethod("paypal")
                .payerEmail(payerNode.path("email_address").asText())
                .payerName(fullName)
                .payerCountryCode(payerNode.path("address").path("country_code").asText(""))
                .build();
    }
}
