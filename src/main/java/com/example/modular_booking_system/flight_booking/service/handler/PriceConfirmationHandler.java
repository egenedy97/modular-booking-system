package com.example.modular_booking_system.flight_booking.service.handler;

import com.example.modular_booking_system.flight_booking.dto.BookingContext;
import com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.pricing.service.AmadeusFlightPricingService;
import com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.pricing.service.FlightPriceExtractor;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PriceConfirmationHandler extends BookingHandler {

    private final AmadeusFlightPricingService pricingService;
    private final FlightPriceExtractor flightPriceExtractor;

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
            return processNext(context);

        } catch (Exception e) {
            log.error("Error confirming price for booking: {}", context.getBookingId(), e);
            context.setStatus("PRICE_CONFIRMATION_FAILED");
            context.setErrorMessage(e.getMessage());
            return context;
        }
    }
}
