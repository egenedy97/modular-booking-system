package com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.search.service;

import com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.search.exception.FlightSearchException;
import com.example.modular_booking_system.external_api_integration.external_providers.amadeus.shared.AccessTokenService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

@Service
public class AmadeusFlightSearchService {

    // WebClient instance for making HTTP requests
    private final WebClient webClient;
    private final AccessTokenService accessTokenService;
    private final String amadeusApiUrl = "https://test.api.amadeus.com/v2/shopping/flight-offers";



    @Autowired
    public AmadeusFlightSearchService(WebClient webClient, AccessTokenService accessTokenService) {
        this.webClient = webClient;
        this.accessTokenService = accessTokenService;
    }

    public JsonNode searchFlights(String originLocationCode,
                                             String destinationLocationCode,
                                             LocalDate departureDate,
                                             LocalDate returnDate,
                                             Integer adults,
                                             Integer max) {
        try {
            // Obtain OAuth2 access token
            String accessToken = accessTokenService.fetchAccessToken();

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


}
