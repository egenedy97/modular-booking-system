package com.example.modular_booking_system.payment.service.paypal;

import com.example.modular_booking_system.core.events.PaymentAuditEvent;
import com.example.modular_booking_system.payment.config.PayPalConfig;
import com.example.modular_booking_system.payment.dto.paypal.PayPalCreateOrderRequest;
import com.example.modular_booking_system.payment.dto.paypal.PayPalOrderResponse;
import com.example.modular_booking_system.payment.dto.paypal.PurchaseUnit;
import com.example.modular_booking_system.payment.exception.PaymentException;
import com.example.modular_booking_system.payment.model.PaymentDetails;
import com.example.modular_booking_system.payment.service.AuditEventPublisher;
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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayPalCreatePaymentService {

    private final WebClient webClient;
    private final PayPalAccessTokenService accessTokenService;
    private final PayPalConfig.PayPalProperties payPalProperties;

    private final AuditEventPublisher auditEventPublisher;


    public PaymentDetails createPayment(
            Double total,
            String currency,
            String method,
            String intent,
            String description,
            String cancelUrl,
            String successUrl) throws PaymentException {

        // Ensure we have a valid PayPal access token
        String token = accessTokenService.getAccessToken();

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
                PaymentDetails paymentDetails = buildPaymentDetails(orderResponse, totalAmount, currency, intent, cancelUrl, successUrl);

                // Publish audit event
                auditEventPublisher.publishPaymentCreated("PAYMENT_CREATED",
                        paymentDetails.getId(),
                        paymentDetails.getPayer() != null ? paymentDetails.getPayer().getPayerId() : "PENDING",
                        paymentDetails.getAmount().getTotal(),
                        LocalDateTime.now());

                return paymentDetails;

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

//    // Converts the PayPal order response into our internal PaymentDetails structure
//    private PaymentDetails buildPaymentDetails(
//            PayPalOrderResponse orderResponse,
//            BigDecimal totalAmount,
//            String currency,
//            String intent,
//            String cancelUrl,
//            String successUrl) {
//
//        PaymentDetails paymentDetails = PaymentDetails.builder()
//                .id(orderResponse.getId())
//                .state(orderResponse.getStatus())
//                .intent(intent)
//                .approvalUrl(orderResponse.getApprovalLink())
//                .cancelUrl(cancelUrl)
//                .returnUrl(successUrl)
//                .createTime(Instant.now().toString())
//                .build();
//
//        // Set amount inside PaymentDetails
//        PaymentDetails.Amount amountDetails = PaymentDetails.Amount.builder()
//                .currency(currency)
//                .total(totalAmount)
//                .build();
//
//        paymentDetails.setAmount(amountDetails);
//        return paymentDetails;
//    }

    private PaymentDetails buildPaymentDetails(
            PayPalOrderResponse orderResponse,
            BigDecimal totalAmount,
            String currency,
            String intent,
            String cancelUrl,
            String successUrl) {

        PaymentDetails.Amount amountDetails = PaymentDetails.Amount.builder()
                .currency(currency)
                .total(totalAmount)
                .build();

        PaymentDetails.Payer payer = PaymentDetails.Payer.builder()
                .payerId("PENDING")  // Default payer ID until payment is approved
                .build();

        return PaymentDetails.builder()
                .id(orderResponse.getId())
                .state(orderResponse.getStatus())
                .intent(intent)
                .approvalUrl(orderResponse.getApprovalLink())
                .cancelUrl(cancelUrl)
                .returnUrl(successUrl)
                .createTime(Instant.now().toString())
                .amount(amountDetails)
                .payer(payer)
                .build();
    }
}
