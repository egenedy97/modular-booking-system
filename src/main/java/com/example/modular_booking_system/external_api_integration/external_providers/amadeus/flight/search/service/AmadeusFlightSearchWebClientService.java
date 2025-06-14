package com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.search.service;

import com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.search.exception.FlightSearchException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

@Service
public class AmadeusFlightSearchWebClientService {

    // WebClient instance for making HTTP requests
    private final WebClient webClient;
    private final String amadeusApiUrl = "https://test.api.amadeus.com/v2/shopping/flight-offers";
    private final String authUrl = "https://test.api.amadeus.com/v1/security/oauth2/token";

    @Value("${amadeus.api.key}")
    private String apiKey;

    @Value("${amadeus.api.secret}")
    private String apiSecret;


    @Autowired
    public AmadeusFlightSearchWebClientService(WebClient webClient) {
        this.webClient = webClient;
    }

    public JsonNode searchFlights(String originLocationCode,
                                             String destinationLocationCode,
                                             LocalDate departureDate,
                                             LocalDate returnDate,
                                             Integer adults,
                                             Integer max) {
        try {
            // Obtain OAuth2 access token
            String accessToken = fetchAccessToken();

            // Build the request URL with query parameters
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(amadeusApiUrl)
                    .queryParam("originLocationCode", originLocationCode)
                    .queryParam("destinationLocationCode", destinationLocationCode)
                    .queryParam("departureDate", departureDate.toString())
                    .queryParam("adults", adults)
                    .queryParam("max", max)
                    .queryParam("currencyCode", "USD")
                    .queryParam("nonStop", "false")
                    .queryParam("travelClass", "ECONOMY");

            // Add return date if provided
            if (returnDate != null) {
                builder.queryParam("returnDate", returnDate.toString());
            }

            String url = builder.toUriString();

            // Make GET request to Amadeus API with Bearer token
            JsonNode response = webClient.get()
                    .uri(url)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            // Check if response contains flight data
            if (response == null || !response.has("data") || response.get("data").isEmpty()) {
                throw new FlightSearchException("No flights found for the given criteria");
            }

            return response;
        } catch (Exception e) {
            throw new FlightSearchException("Unexpected error while searching flights", e);
        }
    }

    private String fetchAccessToken() {
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
