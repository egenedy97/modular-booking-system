package com.example.modular_booking_system.payment.service.paypal;

import com.example.modular_booking_system.payment.config.PayPalConfig;
import com.example.modular_booking_system.payment.exception.PaymentException;
import com.example.modular_booking_system.payment.model.PaymentDetails;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayPalPaymentDetailsService {

    private final WebClient webClient;
    private final PayPalAccessTokenService accessTokenService;
    private final PayPalConfig.PayPalProperties payPalProperties;

    public PaymentDetails getOrderDetails(String orderId) throws PaymentException {
        try {
            String token = accessTokenService.getAccessToken();
            JsonNode response = webClient.get()
                    .uri(payPalProperties.baseUrl() + "/v2/checkout/orders/" + orderId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            // Extract amount details
            JsonNode amountNode = response.path("purchase_units").get(0).path("amount");
            BigDecimal totalAmount = new BigDecimal(amountNode.path("value").asText());
            String currency = amountNode.path("currency_code").asText();

            // Extract payer details if available
            JsonNode payerNode = response.path("payer");
            PaymentDetails.Payer payer = null;
            if (!payerNode.isMissingNode()) {
                JsonNode nameNode = payerNode.path("name");
                String firstName = nameNode.path("given_name").asText("");
                String lastName = nameNode.path("surname").asText("");
                String fullName = (firstName + " " + lastName).trim();

                payer = PaymentDetails.Payer.builder()
                        .payerId(payerNode.path("payer_id").asText())
                        .paymentMethod("paypal")
                        .payerEmail(payerNode.path("email_address").asText())
                        .payerName(fullName)
                        .payerCountryCode(payerNode.path("address").path("country_code").asText(""))
                        .build();
            }

            return PaymentDetails.builder()
                    .id(response.path("id").asText())
                    .state(response.path("status").asText())
                    .intent(response.path("intent").asText())
                    .createTime(response.path("create_time").asText())
                    .amount(PaymentDetails.Amount.builder()
                            .currency(currency)
                            .total(totalAmount)
                            .build())
                    .payer(payer)
                    .build();

        } catch (WebClientResponseException e) {
            String errorMsg = "Failed to get PayPal order details: " + e.getResponseBodyAsString();
            log.error(errorMsg, e);
            throw new PaymentException(errorMsg, e);
        } catch (Exception e) {
            log.error("Error getting PayPal order details", e);
            throw new PaymentException("Failed to get PayPal order details: " + e.getMessage(), e);
        }
    }
}
