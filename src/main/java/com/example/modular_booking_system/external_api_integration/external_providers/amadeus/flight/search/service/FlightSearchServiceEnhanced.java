package com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.search.service;

import com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.dto.FlightOfferSearchResponse;
import com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.search.exception.FlightSearchException;
import com.example.modular_booking_system.external_api_integration.external_providers.amadeus.shared.AccessTokenService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FlightSearchServiceEnhanced {

    // WebClient instance for making HTTP requests
    private final WebClient webClient;
    private final AccessTokenService accessTokenService;
    ObjectMapper mapper = new ObjectMapper();

    @Value("${amadeus.api.flight-search-url}")
    private String flightSearchUrl;

    public FlightOfferSearchResponse searchFlights(String originLocationCode,
                                                     String destinationLocationCode,
                                                     LocalDate departureDate,
                                                     LocalDate returnDate,
                                                     Integer adults,
                                                     Integer max) {
        try {
            // Obtain OAuth2 access token
            String accessToken = accessTokenService.getAccessToken();

            // Build the request URL with query parameters
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(flightSearchUrl)
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
            String jsonString = response.toString();

            FlightOfferSearchResponse flightOfferSearchResponse = mapper.readValue(jsonString, FlightOfferSearchResponse.class);
            return flightOfferSearchResponse;
        } catch (Exception e) {
            throw new FlightSearchException("Unexpected error while searching flights", e);
        }
    }
}
