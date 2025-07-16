package com.example.modular_booking_system.payment.service.impl;

import com.example.modular_booking_system.payment.config.PayPalConfig.PayPalProperties;
import com.example.modular_booking_system.payment.dto.paypal.*;
import com.example.modular_booking_system.payment.exception.PaymentException;
import com.example.modular_booking_system.payment.model.PaymentDetails;
import com.example.modular_booking_system.payment.service.PaymentService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayPalServiceImpl implements PaymentService {

    private final WebClient webClient;
    private final String paypalAuthHeader;
    private final PayPalProperties payPalProperties;
    
    private String accessToken;
    private long tokenExpiryTime;

    @Override
    public PaymentDetails createPayment(
            Double total,
            String currency,
            String method,
            String intent,
            String description,
            String cancelUrl,
            String successUrl) throws PaymentException {

        // Ensure we have a valid PayPal access token
        String token = getAccessToken();

        // Format the total amount with 2 decimal precision
        BigDecimal totalAmount = formatAmount(total);

        // Build the order request object including item, amount, and application context
        PayPalCreateOrderRequest request = buildCreateOrderRequest(
                intent, currency, totalAmount, description, cancelUrl, successUrl);

        try {
            // Send the request to PayPal to create an order
            PayPalOrderResponse orderResponse = sendCreateOrderRequest(token, request);

            // If order creation is successful, map the response to our internal PaymentDetails object
            if (orderResponse != null && orderResponse.getId() != null) {
                return buildPaymentDetails(orderResponse, totalAmount, currency, intent, cancelUrl, successUrl);
            }

            // If response is invalid, throw an exception
            throw new PaymentException("Failed to create PayPal order");

        } catch (WebClientResponseException e) {
            // Handle specific HTTP response errors from PayPal
            String errorMsg = "Failed to create PayPal order: " + e.getResponseBodyAsString();
            log.error(errorMsg, e);
            throw new PaymentException(errorMsg, e);
        } catch (Exception e) {
            // Handle general errors
            log.error("Error creating PayPal order", e);
            throw new PaymentException("Failed to create PayPal order: " + e.getMessage(), e);
        }
    }

    // Formats the total amount to 2 decimal places using HALF_UP rounding
    private BigDecimal formatAmount(Double total) {
        return BigDecimal.valueOf(total).setScale(2, RoundingMode.HALF_UP);
    }

    // Builds a full PayPal order request object including purchase unit, item, amount, and application context
    private PayPalCreateOrderRequest buildCreateOrderRequest(
            String intent,
            String currency,
            BigDecimal totalAmount,
            String description,
            String cancelUrl,
            String successUrl) {

        PayPalCreateOrderRequest request = new PayPalCreateOrderRequest();
        request.setIntent(intent);

        // Create and populate the purchase unit
        PurchaseUnit purchaseUnit = new PurchaseUnit();
        purchaseUnit.setDescription(description);
        purchaseUnit.setReference_id(UUID.randomUUID().toString());

        // Set the amount to be charged
        purchaseUnit.setAmount(PurchaseUnit.Amount.of(currency, totalAmount.doubleValue()));

        // Create and add item details
        PurchaseUnit.Item item = new PurchaseUnit.Item();
        item.setName("Booking");
        item.setDescription("Hotel reservation");
        item.setQuantity("1");
        item.setUnit_amount(new PurchaseUnit.UnitAmount(currency, totalAmount.toString()));
        purchaseUnit.setItems(Collections.singletonList(item));

        // Add purchase unit to request
        request.setPurchaseUnits(Collections.singletonList(purchaseUnit));

        // Set application context (redirect URLs, branding, etc.)
        PayPalCreateOrderRequest.ApplicationContext context = new PayPalCreateOrderRequest.ApplicationContext();
        context.setReturn_url(successUrl);
        context.setCancel_url(cancelUrl);
        context.setBrand_name("Booking System");
        context.setLanding_page("NO_PREFERENCE");

        request.setApplicationContext(context);

        return request;
    }

    // Makes the actual HTTP POST request to PayPal to create the order
    private PayPalOrderResponse sendCreateOrderRequest(String token, PayPalCreateOrderRequest request) {
        return webClient.post()
                .uri(payPalProperties.baseUrl() + "/v2/checkout/orders")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(PayPalOrderResponse.class)
                .block();
    }

    // Converts the PayPal order response into our internal PaymentDetails structure
    private PaymentDetails buildPaymentDetails(
            PayPalOrderResponse orderResponse,
            BigDecimal totalAmount,
            String currency,
            String intent,
            String cancelUrl,
            String successUrl) {

        PaymentDetails paymentDetails = PaymentDetails.builder()
                .id(orderResponse.getId())
                .state(orderResponse.getStatus())
                .intent(intent)
                .approvalUrl(orderResponse.getApprovalLink())
                .cancelUrl(cancelUrl)
                .returnUrl(successUrl)
                .createTime(Instant.now().toString())
                .build();

        // Set amount inside PaymentDetails
        PaymentDetails.Amount amountDetails = PaymentDetails.Amount.builder()
                .currency(currency)
                .total(totalAmount)
                .build();

        paymentDetails.setAmount(amountDetails);
        return paymentDetails;
    }

    @Override
    public JsonNode executePayment(String paymentId, String payerId) throws PaymentException {
        try {
            String token = getAccessToken();
            
            // Execute payment
            JsonNode response = webClient.post()
                    .uri(payPalProperties.baseUrl() + "/v2/checkout/orders/" + paymentId + "/capture")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            return response;
            // Create and return payment details
//            return PaymentDetails.builder()
//                .id(paymentId)
//                .state("COMPLETED")
//                .updateTime(Instant.now().toString())
//                .payer(PaymentDetails.Payer.builder()
//                    .paymentMethod("paypal")
//                    .status("VERIFIED")
//                    .payerId(payerId)
//                    .build())
//                .build();
            
        } catch (WebClientResponseException e) {
            String errorMsg = "Failed to execute PayPal payment: " + e.getResponseBodyAsString();
            log.error(errorMsg, e);
            throw new PaymentException(errorMsg, e);
        } catch (Exception e) {
            log.error("Error executing PayPal payment", e);
            throw new PaymentException("Failed to execute PayPal payment: " + e.getMessage(), e);
        }
    }

    private synchronized String getAccessToken() throws PaymentException {
        // Check if token is still valid (with 5-minute buffer)
        if (accessToken != null && System.currentTimeMillis() < tokenExpiryTime - 300000) {
            return accessToken;
        }

        try {
            // Request new access token
            PayPalTokenResponse tokenResponse = webClient.post()
                    .uri(payPalProperties.baseUrl() + "/v1/oauth2/token")
                    .header(HttpHeaders.AUTHORIZATION, paypalAuthHeader)
                    .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                    .bodyValue("grant_type=client_credentials")
                    .retrieve()
                    .bodyToMono(PayPalTokenResponse.class)
                    .block();

            if (tokenResponse != null && tokenResponse.getAccessToken() != null) {
                this.accessToken = tokenResponse.getAccessToken();
                // Set token expiry time (with 5-minute buffer)
                this.tokenExpiryTime = System.currentTimeMillis() + (tokenResponse.getExpiresIn() * 1000L);
                return this.accessToken;
            }

            throw new PaymentException("Failed to get PayPal access token");

        } catch (Exception e) {
            log.error("Error getting PayPal access token", e);
            throw new PaymentException("Failed to get PayPal access token: " + e.getMessage(), e);
        }
    }
}
