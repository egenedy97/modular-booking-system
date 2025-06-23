package com.example.modular_booking_system.external_api_integration.aggregator.flight.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

public interface FlightSearchProvider {
    CompletableFuture<JsonNode> searchFlights(
            String origin,
            String destination,
            LocalDate departureDate,
            LocalDate returnDate,
            Integer adults,
            Integer max
    );
}