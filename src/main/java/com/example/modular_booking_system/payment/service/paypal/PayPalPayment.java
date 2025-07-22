package com.example.modular_booking_system.payment.service.paypal;

import com.example.modular_booking_system.payment.exception.PaymentException;
import com.example.modular_booking_system.payment.model.PaymentDetails;
import com.example.modular_booking_system.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayPalPayment implements PaymentService {


    private final PayPalCreatePaymentService createService;
    private final PayPalExecutePaymentService executeService;
    private final PayPalPaymentDetailsService getDetailsService;


    @Override
    public PaymentDetails createPayment(
            Double total,
            String currency,
            String method,
            String intent,
            String description,
            String cancelUrl,
            String successUrl) throws PaymentException {
        return createService.createPayment(
                total,
                currency,
                method,
                intent,
                description,
                cancelUrl,
                successUrl);
    }

    @Override
    public PaymentDetails executePayment(String paymentId, String payerId) throws PaymentException {
        return executeService.executePayment(paymentId, payerId);
    }

    @Override
    public PaymentDetails getOrderDetails(String orderId) throws PaymentException {
        return getDetailsService.getOrderDetails(orderId);
    }
}
