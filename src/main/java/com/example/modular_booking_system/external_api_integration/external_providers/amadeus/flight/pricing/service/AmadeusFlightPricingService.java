package com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.pricing.service;

import com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.pricing.exception.FlightPriceException;
import com.example.modular_booking_system.external_api_integration.external_providers.amadeus.shared.AccessTokenService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class AmadeusFlightPricingService {

    private final WebClient webClient;
    private final AccessTokenService accessTokenService;
    private final ObjectMapper mapper;

    @Value("${amadeus.api.flight-pricing-url}")
    private String flightPricingUrl;

    public JsonNode confirmPrice(JsonNode flightOffer) {
        // Obtain OAuth2 access token
        String accessToken = accessTokenService.getAccessToken();

        // Create pricing request
        JsonNode pricingRequest = createPricingRequest(flightOffer);

        try {
            return webClient.post()
                    .uri(flightPricingUrl)
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(pricingRequest)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (Exception e) {
            throw new FlightPriceException("Error confirming flight price via WebClient", e);
        }
    }

    private JsonNode createPricingRequest(JsonNode flightOffer) {
        if (flightOffer == null) {
            throw new IllegalArgumentException("Flight offer cannot be null");
        }

        ObjectNode pricingRequest = mapper.createObjectNode();
        ObjectNode data = mapper.createObjectNode();
        ArrayNode flightOffers = mapper.createArrayNode();

        flightOffers.add(flightOffer);
        data.put("type", "flight-offers-pricing");
        data.set("flightOffers", flightOffers);
        pricingRequest.set("data", data);

        return pricingRequest;
    }

}
