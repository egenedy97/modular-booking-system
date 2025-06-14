package com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.search.controller;

import com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.search.service.AmadeusFlightSearchWebClientService;
import com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.search.service.CityCodeService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v2/amadeus/flights")
public class AmadeusFlightSearchWebClientController {
    private final AmadeusFlightSearchWebClientService amadeusFlightSearchWebClientService;
    private final CityCodeService cityCodeService;

    public AmadeusFlightSearchWebClientController(AmadeusFlightSearchWebClientService amadeusFlightSearchWebClientService
            , CityCodeService cityCodeService) {
        this.amadeusFlightSearchWebClientService = amadeusFlightSearchWebClientService;
        this.cityCodeService = cityCodeService;
    }

    //localhost:8090/api/v2/amadeus/flights/search?origin=CAI&destination=LON&departureDate=2025-06-10&returnDate=2025-06-20&adults=1&max=5
    @GetMapping("/search")
    public ResponseEntity<JsonNode> searchFlights(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate,
            @RequestParam Integer adults,
            @RequestParam Integer max
    ) {
        JsonNode response = amadeusFlightSearchWebClientService.searchFlights(
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
