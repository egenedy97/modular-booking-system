package com.example.modular_booking_system.external_api_integration.aggregator.flight.service;

import com.example.modular_booking_system.external_api_integration.amadeus.flight.search.dto.FlightOffer;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class FlightSearchAggregatorService {
    private final List<FlightProvider> flightProviders;

    public FlightSearchAggregatorService(List<FlightProvider> flightProviders) {
        this.flightProviders = flightProviders;
    }

    public List<FlightOffer> searchAllProviders(
            String origin,
            String destination,
            LocalDate departureDate,
            LocalDate returnDate,
            Integer adults,
            Integer max
    ) {
        List<CompletableFuture<List<FlightOffer>>> futures = new ArrayList<>();

        // Scatter: Send requests to all providers
        for (FlightProvider provider : flightProviders) {
            futures.add(provider.searchFlights(origin, destination, departureDate, returnDate, adults, max));
        }

        // Gather: Combine all results
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        try {
            // Wait for all providers to respond (with timeout)
            allOf.get(5, TimeUnit.SECONDS);

            // Combine results
            List<FlightOffer> allResults = new ArrayList<>();
            for (CompletableFuture<List<FlightOffer>> future : futures) {
                allResults.addAll(future.get());
            }
            return allResults;

        } catch (Exception e) {
            throw new RuntimeException("Error aggregating flight search results", e);
        }
    }
}
