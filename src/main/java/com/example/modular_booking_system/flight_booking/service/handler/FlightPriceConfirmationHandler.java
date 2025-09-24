package com.example.modular_booking_system.flight_booking.service.handler;

import com.example.modular_booking_system.core.config.RabbitMQConfig;
import com.example.modular_booking_system.flight_booking.dto.BookingRequest;
import com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.pricing.service.AmadeusFlightPricingService;
import com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.pricing.service.FlightPriceExtractor;
import com.example.modular_booking_system.flight_booking.service.FlightBookingAuditEventPublisher;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class FlightPriceConfirmationHandler extends BookingHandler {

    private final AmadeusFlightPricingService pricingService;
    private final FlightPriceExtractor flightPriceExtractor;
    private final FlightBookingAuditEventPublisher flightBookingAuditEventPublisher;

    @Override
    public BookingRequest handle(BookingRequest bookingRequest) {

        log.info("Confirming price for booking: {}", bookingRequest.getBookingId());

        try {
            // Confirm price with Amadeus
            JsonNode confirmedPrice = pricingService.confirmPrice(bookingRequest.getFlightOffer());

            bookingRequest.setFlightOfferPriceConfirmationResponse(confirmedPrice);
            double flightBookingTotalPrice = flightPriceExtractor.extractTotalPrice(confirmedPrice);
            bookingRequest.getPaymentRequest().setTotal(flightBookingTotalPrice);

            log.info("Price confirmed for booking: {}, amount: {}", bookingRequest.getBookingId(), flightBookingTotalPrice);

            bookingRequest.setStatus("PRICE_CONFIRMED");

            // Publish audit event for price confirmed
            flightBookingAuditEventPublisher.publishFlightPriceConfirmed(
                    bookingRequest.getBookingId(),
                    "PRICE_CONFIRMED",
                    "FLIGHT_SERVICE",
                    RabbitMQConfig.FLIGHT_BOOKING_AUDIT_QUEUE,
                    bookingRequest,
                    "SYSTEM",
                    bookingRequest.getBookingTimestamp()
            );

            return processNext(bookingRequest);

        } catch (Exception e) {
            log.error("Error confirming price for booking: {}", bookingRequest.getBookingId(), e);
            bookingRequest.setStatus("PRICE_CONFIRMATION_FAILED");
            bookingRequest.setErrorMessage(e.getMessage());
            return bookingRequest;
        }
    }
}
