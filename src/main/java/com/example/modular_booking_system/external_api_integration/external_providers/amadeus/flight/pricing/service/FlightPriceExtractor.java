package com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.pricing.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FlightPriceExtractor {

    // Extracts the total price from the Amadeus Flight Pricing API response
    public double extractTotalPrice(JsonNode jsonResponse) {
        JsonNode priceNode = jsonResponse
                .path("data")
                .path("flightOffers")
                .get(0)
                .path("price")
                .path("total");

        return priceNode.asDouble();
    }
}
