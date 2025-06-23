package com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.search.controller;

import com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.search.service.AmadeusFlightSearchService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/amadeus/flights")
public class AmadeusFlightSearchController {

    private final AmadeusFlightSearchService amadeusFlightSearchService;

    public AmadeusFlightSearchController(AmadeusFlightSearchService amadeusFlightSearchService) {
        this.amadeusFlightSearchService = amadeusFlightSearchService;
    }

    //localhost:8090/api/v1/amadeus/flights/search?origin=CAI&destination=LON&departureDate=2025-07-01&returnDate=2025-07-10&adults=1&max=5
    @GetMapping("/search")
    public ResponseEntity<JsonNode> searchFlights(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate,
            @RequestParam Integer adults,
            @RequestParam Integer max
    ) {
        JsonNode response = amadeusFlightSearchService.searchFlights(
                origin,
                destination,
                departureDate,
                returnDate,
                adults,
                max
        );
        return ResponseEntity.ok(response);
    }


}
