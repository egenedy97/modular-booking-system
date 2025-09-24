package com.example.modular_booking_system.flight_booking.service.handler;

import com.example.modular_booking_system.core.config.RabbitMQConfig;
import com.example.modular_booking_system.flight_booking.dto.BookingRequest;
import com.example.modular_booking_system.flight_booking.service.FlightBookingAuditEventPublisher;
import com.example.modular_booking_system.payment.model.PaymentDetails;
import com.example.modular_booking_system.payment.service.paypal.PayPalCreatePaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentCreationHandler extends BookingHandler {

    private final PayPalCreatePaymentService payPalCreatePaymentService;
    private final FlightBookingAuditEventPublisher flightBookingAuditEventPublisher;

    @Override
    public BookingRequest handle(BookingRequest bookingRequest) {

        log.info("Creating payment for booking: {}", bookingRequest.getBookingId());

        try {
            // Create payment with PayPal
            PaymentDetails paymentDetails = payPalCreatePaymentService.createPayment(
                    bookingRequest.getPaymentRequest().getTotal(),
                    bookingRequest.getPaymentRequest().getCurrency(),
                    bookingRequest.getPaymentRequest().getMethod(),
                    bookingRequest.getPaymentRequest().getIntent(),
                    bookingRequest.getPaymentRequest().getDescription(),
                    bookingRequest.getPaymentRequest().getCancelUrl(),
                    bookingRequest.getPaymentRequest().getSuccessUrl()
            );

            bookingRequest.setPaymentDetails(paymentDetails);
            bookingRequest.setPaymentUrl(paymentDetails.getApprovalUrl());
            bookingRequest.setStatus("PAYMENT_CREATED");

            log.info("Payment created for booking: {}, payment ID: {}", bookingRequest.getBookingId(), paymentDetails.getId());

            // Publish audit event for created payment
            flightBookingAuditEventPublisher.publishPaymentCreated(
                    bookingRequest.getBookingId(),
                    "PAYMENT_CREATED",
                    "FLIGHT_SERVICE",
                    RabbitMQConfig.FLIGHT_BOOKING_AUDIT_QUEUE,
                    bookingRequest,
                    "SYSTEM",
                    bookingRequest.getBookingTimestamp()
            );

            return processNext(bookingRequest);

        } catch (Exception e) {
            log.error("Error creating payment for booking: {}", bookingRequest.getBookingId(), e);
            bookingRequest.setStatus("PAYMENT_CREATION_FAILED");
            bookingRequest.setErrorMessage(e.getMessage());
            return bookingRequest;
        }
    }
}
