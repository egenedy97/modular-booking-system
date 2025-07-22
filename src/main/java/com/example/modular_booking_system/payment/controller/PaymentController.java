package com.example.modular_booking_system.payment.controller;

import com.example.modular_booking_system.payment.exception.PaymentException;
import com.example.modular_booking_system.payment.model.PaymentDetails;
import com.example.modular_booking_system.payment.service.paypal.PayPalPayment;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PayPalPayment paymentService;

    // localhost:8090/api/payment/pay?amount=10.00&currency=USD&method=paypal&intent=CAPTURE&description=Test payment
    @PostMapping("/pay")
    public ResponseEntity<?> createPayment(
            @RequestParam("amount") double amount,
            @RequestParam("currency") String currency,
            @RequestParam(value = "method", defaultValue = "paypal") String method,
            @RequestParam(value = "intent", defaultValue = "capture") String intent,
            @RequestParam("description") String description,
            HttpServletRequest request) {
        
        log.info("Creating payment request for amount: {} {}", amount, currency);
        
        String cancelUrl = getBaseUrl(request) + "/api/payment/cancel";
        String successUrl = getBaseUrl(request) + "/api/payment/success";

        try {
            PaymentDetails paymentDetails = paymentService.createPayment(
                    amount,
                    currency,
                    method,
                    intent,
                    description,
                    cancelUrl,
                    successUrl);

            if (paymentDetails.getApprovalUrl() != null) {
                log.info("Payment created successfully. Redirecting to: {}", paymentDetails.getApprovalUrl());
                return ResponseEntity.ok(paymentDetails);
            }
            
            log.error("Failed to create payment. No approval URL returned.");
            return ResponseEntity.badRequest().body("Failed to create payment. Please try again.");
            
        } catch (Exception e) {
            log.error("Error creating payment: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body("An error occurred while processing your payment: " + e.getMessage());
        }
    }

    @GetMapping("/success")
    public ResponseEntity<?> paymentSuccess(@RequestParam("token") String orderId) throws PaymentException {
        log.info("Processing approved payment. Order ID: {}", orderId);

        try {
            PaymentDetails response = paymentService.executePayment(orderId, null); // payerId not needed
            return ResponseEntity.ok(response);
//            if ("COMPLETED".equalsIgnoreCase(paymentDetails.getState())) {
//                log.info("Payment {} executed successfully", orderId);
//                return ResponseEntity.ok(paymentDetails);
//            } else {
//                log.warn("Payment {} execution failed. State: {}", orderId, paymentDetails.getState());
//                return ResponseEntity.badRequest()
//                        .body("Payment failed. Status: " + paymentDetails.getState());
//            }

        } catch (Exception e) {
            log.error("Error executing payment {}: {}", orderId, e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body("Error occurred: " + e.getMessage());
        }
    }
    // http://localhost:8090/api/payment/details?orderId=ORDER_ID
    @GetMapping("/details")
    public ResponseEntity<?> getPaymentDetails(@RequestParam("orderId") String orderId) {
        try {
            PaymentDetails orderDetails = paymentService.getOrderDetails(orderId);
            return ResponseEntity.ok(orderDetails);
        } catch (PaymentException e) {
            log.error("Payment failed: {}", e.getMessage());

            // Create a proper JSON error response
            ObjectNode errorNode = JsonNodeFactory.instance.objectNode();
            errorNode.put("status", "error");
            errorNode.put("message", "Payment failed: " + e.getMessage());

            return ResponseEntity.badRequest().body(errorNode);
        }
    }


    @GetMapping("/cancel")
    public ResponseEntity<?> paymentCancel(@RequestParam(value = "token") String token) {
        log.info("Payment was cancelled. Token: {}", token);
        return ResponseEntity.ok().body("Payment was cancelled. You can safely close this page.");
    }

    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();

        return scheme + "://" + serverName + (serverPort != 80 ? ":" + serverPort : "") + contextPath;
    }
}
