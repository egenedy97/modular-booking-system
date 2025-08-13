package com.example.modular_booking_system.flight_booking.service.handler;

import com.example.modular_booking_system.flight_booking.dto.BookingContext;
import com.example.modular_booking_system.payment.model.PaymentDetails;
import com.example.modular_booking_system.payment.service.paypal.PayPalCreatePaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentCreationHandler extends BookingHandler {

    private final PayPalCreatePaymentService payPalCreatePaymentService;

    @Override
    public BookingContext handle(BookingContext context) {

        log.info("Creating payment for booking: {}", context.getBookingId());

        try {
            // Create payment with PayPal
            PaymentDetails paymentDetails = payPalCreatePaymentService.createPayment(
                    context.getPaymentRequest().getTotal(),
                    context.getPaymentRequest().getCurrency(),
                    context.getPaymentRequest().getMethod(),
                    context.getPaymentRequest().getIntent(),
                    context.getPaymentRequest().getDescription(),
                    context.getPaymentRequest().getCancelUrl(),
                    context.getPaymentRequest().getSuccessUrl()
            );

            context.setPaymentDetails(paymentDetails);
            context.setPaymentUrl(paymentDetails.getApprovalUrl());
            context.setStatus("PAYMENT_CREATED");

            log.info("Payment created for booking: {}, payment ID: {}", context.getBookingId(), paymentDetails.getId());

            return processNext(context);

        } catch (Exception e) {
            log.error("Error creating payment for booking: {}", context.getBookingId(), e);
            context.setStatus("PAYMENT_CREATION_FAILED");
            context.setErrorMessage(e.getMessage());
            return context;
        }
    }
}
