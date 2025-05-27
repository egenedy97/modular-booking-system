package com.example.modular_booking_system.external_api_integration.aggregator.service;

import com.example.modular_booking_system.external_api_integration.amadeus.dto.FlightOffer;
import com.example.modular_booking_system.external_api_integration.amadeus.service.AmadeusFlightSearchService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class AmadeusFlightProvider implements FlightProvider {

    private final AmadeusFlightSearchService flightSearchService;

    public AmadeusFlightProvider(AmadeusFlightSearchService flightSearchService) {
        this.flightSearchService = flightSearchService;
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
                flightSearchService.searchFlights(origin, destination, departureDate, returnDate, adults, max)
        );
    }
}
