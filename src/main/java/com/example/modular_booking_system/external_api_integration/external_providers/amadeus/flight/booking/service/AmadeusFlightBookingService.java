package com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.booking.service;

import com.example.modular_booking_system.external_api_integration.external_providers.amadeus.shared.AccessTokenService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class AmadeusFlightBookingService {

    private final WebClient webClient;
    private final AccessTokenService accessTokenService;
    private final ObjectMapper mapper;

    @Value("${amadeus.api.flight-booking-url}")
    private String flightBookingUrl;

    private JsonNode wrapWithData(JsonNode bookingRequestData) {
        ObjectNode root = mapper.createObjectNode();
        root.set("data", bookingRequestData);
        return root;
    }

    public JsonNode createBooking(JsonNode bookingRequestData) {
        String accessToken = accessTokenService.getAccessToken();
        JsonNode wrappedRequest = wrapWithData(bookingRequestData);
        System.out.println("Wrapped request: " + wrappedRequest.toPrettyString());

        try {
            return webClient.post()
                    .uri(flightBookingUrl)
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
        String accessToken = accessTokenService.getAccessToken();
        String url = flightBookingUrl + "/" + flightOrderId;
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
        String accessToken = accessTokenService.getAccessToken();
        String url = flightBookingUrl + "/" + flightOrderId;
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
