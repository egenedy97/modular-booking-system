package com.example.modular_booking_system.external_api_integration.aggregator.flight.service;

import com.example.modular_booking_system.external_api_integration.amadeus.flight.search.payload.FlightOffer;
import com.example.modular_booking_system.external_api_integration.amadeus.flight.search.service.AmadeusFlightSearchService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class AmadeusFlightSearchProvider implements FlightSearchProvider {

    private final AmadeusFlightSearchService amadeusFlightSearchService;

    public AmadeusFlightSearchProvider(AmadeusFlightSearchService amadeusFlightSearchService) {
        this.amadeusFlightSearchService = amadeusFlightSearchService;
    }

    @Override
    public CompletableFuture<List<FlightOffer>> searchFlights(
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
