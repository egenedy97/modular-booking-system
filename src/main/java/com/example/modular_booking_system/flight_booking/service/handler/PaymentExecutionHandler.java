package com.example.modular_booking_system.flight_booking.service.handler;

import com.example.modular_booking_system.core.config.RabbitMQConfig;
import com.example.modular_booking_system.flight_booking.dto.BookingRequest;
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
    public BookingRequest handle(BookingRequest bookingRequest) {

        log.info("Executing payment for booking: {}", bookingRequest.getBookingId());

        try {
            payPalExecutePaymentService.executePayment(
                    bookingRequest.getPaymentDetails().getId(),
                    bookingRequest.getPaymentDetails().getPayer().getPayerId());

            bookingRequest.setStatus("PAYMENT_EXECUTED");

            // Publish audit event for an excuted payment
            flightBookingAuditEventPublisher.publishPaymentExecuted(
                    bookingRequest.getBookingId(),
                    "PAYMENT_EXECUTED",
                    "FLIGHT_SERVICE",
                    RabbitMQConfig.FLIGHT_BOOKING_AUDIT_QUEUE,
                    bookingRequest,
                    "SYSTEM",
                    bookingRequest.getBookingTimestamp()
            );

            return processNext(bookingRequest);

        } catch (Exception e) {
            log.error("Error executing payment for booking: {}", bookingRequest.getBookingId(), e);
            bookingRequest.setStatus("PAYMENT_EXECUTION_FAILED");
            bookingRequest.setErrorMessage(e.getMessage());
            return bookingRequest;
        }
    }
}
