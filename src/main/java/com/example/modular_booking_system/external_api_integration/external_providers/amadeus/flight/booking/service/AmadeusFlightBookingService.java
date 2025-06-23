package com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.booking.service;

import com.example.modular_booking_system.external_api_integration.external_providers.amadeus.shared.AccessTokenService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AmadeusFlightBookingService {

    private final WebClient webClient;
    private final AccessTokenService accessTokenService;
    private final ObjectMapper mapper;
    private final String amadeusApiUrl = "https://test.api.amadeus.com/v1/booking/flight-orders";

    @Autowired
    public AmadeusFlightBookingService(WebClient webClient, AccessTokenService accessTokenService, ObjectMapper mapper) {
        this.webClient = webClient;
        this.accessTokenService = accessTokenService;
        this.mapper = mapper;
    }

    private JsonNode wrapWithData(JsonNode bookingRequestData) {
        ObjectNode root = mapper.createObjectNode();
        root.set("data", bookingRequestData);
        return root;
    }

    public JsonNode createBooking(JsonNode bookingRequestData) {
        String accessToken = accessTokenService.fetchAccessToken();
        JsonNode wrappedRequest = wrapWithData(bookingRequestData);
        System.out.println("Wrapped request: " + wrappedRequest.toPrettyString());

        try {
            return webClient.post()
                    .uri(amadeusApiUrl)
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(wrappedRequest)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("Error creating flight booking via WebClient", e);
        }
    }

    public JsonNode getBookingById(String flightOrderId) {
        String accessToken = accessTokenService.fetchAccessToken();
        String url = amadeusApiUrl + "/" + flightOrderId;
        System.out.println("URL: " + url);
        try {
            return webClient.get()
                    .uri(url)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving flight booking via WebClient", e);
        }
    }

    public JsonNode deleteBookingById(String flightOrderId) {
        String accessToken = accessTokenService.fetchAccessToken();
        String url = amadeusApiUrl + "/" + flightOrderId;
        try {
            return webClient.delete()
                    .uri(url)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("Error deleting flight booking via WebClient", e);
        }
    }

}
