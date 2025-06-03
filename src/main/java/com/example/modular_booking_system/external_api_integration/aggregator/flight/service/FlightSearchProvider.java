package com.example.modular_booking_system.external_api_integration.aggregator.flight.service;

import com.example.modular_booking_system.external_api_integration.amadeus.flight.search.payload.FlightOffer;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface FlightSearchProvider {
    CompletableFuture<List<FlightOffer>> searchFlights(
            String origin,
            String destination,
            LocalDate departureDate,
            LocalDate returnDate,
            Integer adults,
            Integer max
    );
}