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
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayPalPaymentDetailsService {

    private final WebClient webClient;
    private final PayPalAccessTokenService accessTokenService;
    private final PayPalConfig.PayPalProperties payPalProperties;

    private final AuditEventPublisher auditEventPublisher;

    public PaymentDetails getOrderDetails(String orderId) throws PaymentException {
        try {
            String token = accessTokenService.getAccessToken();
            JsonNode response = fetchOrderDetailsFromPayPal(orderId, token);

            PaymentDetails paymentDetails = buildPaymentDetailsFromResponse(response);

            // Add audit event
            auditEventPublisher.publishPaymentRetrieved(
                    "PAYMENT_DETAILS_RETRIEVED",
                    paymentDetails.getId(),
                    paymentDetails.getPayer() != null ? paymentDetails.getPayer().getPayerId() : "UNKNOWN",
                    paymentDetails.getAmount().getTotal(),
                    LocalDateTime.now()
            );

            return paymentDetails;
        } catch (WebClientResponseException e) {
            String errorMsg = "Failed to get PayPal order details: " + e.getResponseBodyAsString();
            log.error(errorMsg, e);
            throw new PaymentException(errorMsg, e);
        } catch (Exception e) {
            log.error("Error getting PayPal order details", e);
            throw new PaymentException("Failed to get PayPal order details: " + e.getMessage(), e);
        }
    }

    private JsonNode fetchOrderDetailsFromPayPal(String orderId, String token) {
        return webClient.get()
                .uri(payPalProperties.baseUrl() + "/v2/checkout/orders/" + orderId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }

    private PaymentDetails buildPaymentDetailsFromResponse(JsonNode response) {
        JsonNode amountNode = response.path("purchase_units").get(0).path("amount");
        BigDecimal totalAmount = new BigDecimal(amountNode.path("value").asText("0.00"));
        String currency = amountNode.path("currency_code").asText("USD");

        PaymentDetails.Payer payer = extractPayerDetails(response.path("payer"));

        return PaymentDetails.builder()
                .id(response.path("id").asText())
                .state(response.path("status").asText())
                .intent(response.path("intent").asText())
                .createTime(response.path("create_time").asText())
                .amount(PaymentDetails.Amount.builder()
                        .total(totalAmount)
                        .currency(currency)
                        .build())
                .payer(payer)
                .build();
    }

    private PaymentDetails.Payer extractPayerDetails(JsonNode payerNode) {
        if (payerNode.isMissingNode() || payerNode.isEmpty()) return null;

        String firstName = payerNode.path("name").path("given_name").asText("");
        String lastName = payerNode.path("name").path("surname").asText("");
        String fullName = (firstName + " " + lastName).trim();

        return PaymentDetails.Payer.builder()
                .payerId(payerNode.path("payer_id").asText())
                .payerEmail(payerNode.path("email_address").asText())
                .payerName(fullName)
                .payerCountryCode(payerNode.path("address").path("country_code").asText(""))
                .paymentMethod("paypal")
                .build();
    }
}