package com.example.modular_booking_system.flight_booking.service.handler;

import com.example.modular_booking_system.flight_booking.dto.BookingContext;
import com.example.modular_booking_system.payment.service.paypal.PayPalExecutePaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentExecutionHandler extends BookingHandler{

    private final PayPalExecutePaymentService payPalExecutePaymentService;

    @Override
    public BookingContext handle(BookingContext context) {

        log.info("Executing payment for booking: {}", context.getBookingId());

        try {
            payPalExecutePaymentService.executePayment(
                    context.getPaymentDetails().getId(),
                    context.getPaymentDetails().getPayer().getPayerId());

            context.setStatus("PAYMENT_EXECUTED");

            return processNext(context);

        } catch (Exception e) {
            log.error("Error executing payment for booking: {}", context.getBookingId(), e);
            context.setStatus("PAYMENT_EXECUTION_FAILED");
            context.setErrorMessage(e.getMessage());
            return context;
        }
    }
}
