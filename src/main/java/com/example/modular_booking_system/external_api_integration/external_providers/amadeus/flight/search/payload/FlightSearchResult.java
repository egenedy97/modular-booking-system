package com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.search.payload;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
public class FlightSearchResult {

    private List<FlightOffer> flightOffers;

    // The raw JSON response from the Amadeus API
    private JsonNode rawFlightOffer;

    public Optional<JsonNode> getFlightOfferById(String offerId) {
        if (rawFlightOffer == null || !rawFlightOffer.has("data")) {
            return Optional.empty();
        }

        JsonNode data = rawFlightOffer.get("data");
        if (data.isArray()) {
            for (JsonNode offer : data) {
                if (offer.has("id") && offer.get("id").asText().equals(offerId)) {
                    return Optional.of(offer);
                }
            }
        }

        return Optional.empty();
    }
}
