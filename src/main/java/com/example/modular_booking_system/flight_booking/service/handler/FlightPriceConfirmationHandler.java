package com.example.modular_booking_system.flight_booking.service.handler;

import com.example.modular_booking_system.core.config.RabbitMQConfig;
import com.example.modular_booking_system.flight_booking.dto.BookingContext;
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
    public BookingContext handle(BookingContext context) {

        log.info("Confirming price for booking: {}", context.getBookingId());

        try {
            // Confirm price with Amadeus
            JsonNode confirmedPrice = pricingService.confirmPrice(context.getFlightOffer());

            context.setFlightOfferPriceConfirmationResponse(confirmedPrice);
            double flightBookingTotalPrice = flightPriceExtractor.extractTotalPrice(confirmedPrice);
            context.getPaymentRequest().setTotal(flightBookingTotalPrice);

            log.info("Price confirmed for booking: {}, amount: {}", context.getBookingId(), flightBookingTotalPrice);

            context.setStatus("PRICE_CONFIRMED");

            // Publish audit event for price confirmed
            flightBookingAuditEventPublisher.publishFlightPriceConfirmed(
                    context.getBookingId(),
                    "PRICE_CONFIRMED",
                    "FLIGHT_SERVICE",
                    RabbitMQConfig.FLIGHT_BOOKING_AUDIT_QUEUE,
                    context,
                    "SYSTEM",
                    LocalDateTime.now()
            );

            return processNext(context);

        } catch (Exception e) {
            log.error("Error confirming price for booking: {}", context.getBookingId(), e);
            context.setStatus("PRICE_CONFIRMATION_FAILED");
            context.setErrorMessage(e.getMessage());
            return context;
        }
    }
}
