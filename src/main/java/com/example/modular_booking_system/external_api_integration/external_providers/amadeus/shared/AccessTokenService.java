package com.example.modular_booking_system.external_api_integration.external_providers.amadeus.shared;

import com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.search.exception.FlightSearchException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class AccessTokenService {

    private final WebClient webClient;

    @Value("${amadeus.api.auth-url}")
    private String authUrl;

    @Value("${amadeus.api.key}")
    private String apiKey;

    @Value("${amadeus.api.secret}")
    private String apiSecret;

    // Token cache fields
    private String accessToken;
    private long tokenExpiryTime;

    public synchronized String getAccessToken() {
        long buffer = 5 * 60 * 1000; // 5 minutes buffer
        if (accessToken != null && System.currentTimeMillis() < tokenExpiryTime - buffer) {
            return accessToken;
        }
        try {
            JsonNode response = webClient.post()
                    .uri(authUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .bodyValue("grant_type=client_credentials&client_id=" + apiKey + "&client_secret=" + apiSecret)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
            this.accessToken = response.get("access_token").asText();
            int expiresIn = response.get("expires_in").asInt(); // Should be 1799
            this.tokenExpiryTime = System.currentTimeMillis() + (expiresIn * 1000L);
            return this.accessToken;
        } catch (Exception e) {
            throw new FlightSearchException("Failed to fetch Amadeus access token", e);
        }
    }


}
