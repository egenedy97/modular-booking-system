package com.example.modular_booking_system.payment.controller;

import com.example.modular_booking_system.payment.exception.PaymentException;
import com.example.modular_booking_system.payment.model.PaymentDetails;
import com.example.modular_booking_system.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/pay")
    public ResponseEntity<?> createPayment(
            @RequestParam("amount") double amount,
            @RequestParam("currency") String currency,
            @RequestParam(value = "method", defaultValue = "paypal") String method,
            @RequestParam(value = "intent", defaultValue = "sale") String intent,
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
    public ResponseEntity<?> paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId) throws PaymentException {
        
        log.info("Processing successful payment. Payment ID: {}, Payer ID: {}", paymentId, payerId);
        
        try {
            PaymentDetails paymentDetails = paymentService.executePayment(paymentId, payerId);
            
            if ("COMPLETED".equalsIgnoreCase(paymentDetails.getState())) {
                log.info("Payment {} executed successfully", paymentId);
                return ResponseEntity.ok(paymentDetails);
            } else {
                log.warn("Payment {} execution failed. State: {}", paymentId, paymentDetails.getState());
                return ResponseEntity.badRequest()
                        .body("Payment processing failed. Status: " + paymentDetails.getState());
            }
            
        } catch (Exception e) {
            log.error("Error executing payment {}: {}", paymentId, e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body("An error occurred while processing your payment: " + e.getMessage());
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
