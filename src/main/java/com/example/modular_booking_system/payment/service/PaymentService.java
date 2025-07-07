package com.example.modular_booking_system.payment.service;

import com.example.modular_booking_system.payment.exception.PaymentException;
import com.example.modular_booking_system.payment.model.PaymentDetails;

public interface PaymentService {
    /**
     * Creates a payment request
     * @param total Total amount to charge
     * @param currency Currency code (e.g., USD, EUR)
     * @param method Payment method (e.g., "paypal")
     * @param intent Payment intent (sale, authorize, order)
     * @param description Payment description
     * @param cancelUrl URL to redirect to if payment is cancelled
     * @param successUrl URL to redirect to after successful payment
     * @return Payment details including approval URL
     * @throws PaymentException if there's an error creating the payment
     */
    PaymentDetails createPayment(
        Double total,
        String currency,
        String method,
        String intent,
        String description,
        String cancelUrl,
        String successUrl) throws PaymentException;

    /**
     * Executes an approved payment
     * @param paymentId The payment ID from PayPal
     * @param payerId The Payer ID from PayPal
     * @return Payment details with execution status
     * @throws PaymentException if there's an error executing the payment
     */
    PaymentDetails executePayment(String paymentId, String payerId) throws PaymentException;
}
