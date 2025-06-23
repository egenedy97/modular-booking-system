package com.example.modular_booking_system.external_api_integration.aggregator.flight.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class FlightSearchAggregatorService {
    private final List<FlightSearchProvider> flightSearchProviders;
    private final ObjectMapper mapper;

    public FlightSearchAggregatorService(List<FlightSearchProvider> flightSearchProviders, ObjectMapper mapper) {
        this.flightSearchProviders = flightSearchProviders;
        this.mapper = mapper;
    }

    public JsonNode searchAllProviders(
            String origin,
            String destination,
            LocalDate departureDate,
            LocalDate returnDate,
            Integer adults,
            Integer max
    ) {
        List<CompletableFuture<JsonNode>> futures = new ArrayList<>();

        // Scatter: Send requests to all providers
        for (FlightSearchProvider provider : flightSearchProviders) {
            futures.add(provider.searchFlights(origin, destination, departureDate, returnDate, adults, max));
        }

        // Gather: Combine all results
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        try {
            // Wait for all providers to respond (with timeout)
            allOf.get(10, TimeUnit.SECONDS);

            // Create combined data array
            ArrayNode combinedData = mapper.createArrayNode();

            // Combine results from all providers
            for (CompletableFuture<JsonNode> future : futures) {
                JsonNode result = future.get();
                if (result != null && result.has("data")) {
                    JsonNode data = result.get("data");
                    if (data.isArray()) {
                        data.forEach(combinedData::add);
                    } else {
                        combinedData.add(data);
                    }
                }
            }

            // Create the final response structure
            return mapper.createObjectNode()
                    .set("data", combinedData);

        } catch (Exception e) {
            throw new RuntimeException("Error aggregating flight search results", e);
        }
    }
}