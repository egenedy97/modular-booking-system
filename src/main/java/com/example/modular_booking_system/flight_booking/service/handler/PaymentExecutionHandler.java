package com.example.modular_booking_system.flight_booking.service.handler;

import com.example.modular_booking_system.core.config.RabbitMQConfig;
import com.example.modular_booking_system.flight_booking.dto.BookingContext;
import com.example.modular_booking_system.flight_booking.service.FlightBookingAuditEventPublisher;
import com.example.modular_booking_system.payment.service.paypal.PayPalExecutePaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentExecutionHandler extends BookingHandler{

    private final PayPalExecutePaymentService payPalExecutePaymentService;
    private final FlightBookingAuditEventPublisher flightBookingAuditEventPublisher;


    @Override
    public BookingContext handle(BookingContext context) {

        log.info("Executing payment for booking: {}", context.getBookingId());

        try {
            payPalExecutePaymentService.executePayment(
                    context.getPaymentDetails().getId(),
                    context.getPaymentDetails().getPayer().getPayerId());

            context.setStatus("PAYMENT_EXECUTED");

            // Publish audit event for an excuted payment
            flightBookingAuditEventPublisher.publishPaymentExecuted(
                    context.getBookingId(),
                    "PAYMENT_EXECUTED",
                    "FLIGHT_SERVICE",
                    RabbitMQConfig.FLIGHT_BOOKING_AUDIT_QUEUE,
                    context,
                    "SYSTEM",
                    LocalDateTime.now()
            );

            return processNext(context);

        } catch (Exception e) {
            log.error("Error executing payment for booking: {}", context.getBookingId(), e);
            context.setStatus("PAYMENT_EXECUTION_FAILED");
            context.setErrorMessage(e.getMessage());
            return context;
        }
    }
}
