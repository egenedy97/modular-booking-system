package com.example.modular_booking_system.external_api_integration.external_providers.amadeus.shared;

import com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.search.exception.FlightSearchException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AccessTokenService {

    private final WebClient webClient;
    private final String authUrl="https://test.api.amadeus.com/v1/security/oauth2/token";

    @Value("${amadeus.api.key}")
    private String apiKey;

    @Value("${amadeus.api.secret}")
    private String apiSecret;

    public AccessTokenService(WebClient webClient) {
        this.webClient = webClient;
    }

    public String fetchAccessToken() {
        try {
            // Make POST request to obtain access token
            JsonNode response = webClient.post()
                    .uri(authUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .bodyValue("grant_type=client_credentials&client_id=" + apiKey + "&client_secret=" + apiSecret)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
            return response.get("access_token").asText();
        } catch (Exception e) {
            throw new FlightSearchException("Failed to fetch Amadeus access token", e);
        }
    }


}
