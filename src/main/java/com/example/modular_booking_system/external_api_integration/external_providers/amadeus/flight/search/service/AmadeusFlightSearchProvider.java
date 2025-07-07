package com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.search.service;

import com.example.modular_booking_system.external_api_integration.aggregator.flight.service.FlightSearchProvider;
import com.fasterxml.jackson.databind.JsonNode;
    import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class AmadeusFlightSearchProvider implements FlightSearchProvider {

    private final AmadeusFlightSearchService amadeusFlightSearchService;

    @Override
    public CompletableFuture<JsonNode> searchFlights(
            String origin,
            String destination,
            LocalDate departureDate,
            LocalDate returnDate,
            Integer adults,
            Integer max
    ) {
        return CompletableFuture.supplyAsync(() ->
                amadeusFlightSearchService.searchFlights(origin, destination, departureDate, returnDate, adults, max)
        );
    }
}
