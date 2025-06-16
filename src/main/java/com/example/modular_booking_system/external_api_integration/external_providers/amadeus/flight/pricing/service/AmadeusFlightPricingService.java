package com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.pricing.service;

import com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.pricing.exception.FlightPriceException;
import com.example.modular_booking_system.external_api_integration.external_providers.amadeus.shared.AccessTokenService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AmadeusFlightPricingService {

    private final WebClient webClient;
    private final AccessTokenService accessTokenService;
    private final String amadeusApiUrl="https://test.api.amadeus.com/v1/shopping/flight-offers/pricing";
    private final ObjectMapper mapper;

    @Autowired
    public AmadeusFlightPricingService (WebClient webClient, AccessTokenService accessTokenService, ObjectMapper mapper) {
        this.webClient = webClient;
        this.accessTokenService = accessTokenService;
        this.mapper = mapper;
    }

    public JsonNode confirmPrice(JsonNode flightOffer) {
        // Obtain OAuth2 access token
        String accessToken = accessTokenService.fetchAccessToken();

        // Create pricing request
        JsonNode pricingRequest = createPricingRequest(flightOffer);

        try {
            return webClient.post()
                    .uri(amadeusApiUrl)
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
