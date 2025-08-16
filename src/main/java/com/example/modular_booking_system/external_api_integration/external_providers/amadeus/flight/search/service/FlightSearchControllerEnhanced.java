package com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.search.service;

import com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.dto.FlightOfferSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
public class FlightSearchControllerEnhanced {

    private final FlightSearchServiceEnhanced flightSearchServiceEnhanced;


    // localhost:8090/api/flights/search?originLocationCode=CAI&destinationLocationCode=DXB&departureDate=2025-08-10&returnDate=2025-08-20&adults=1&max=5
    @GetMapping("/search")
    public ResponseEntity<FlightOfferSearchResponse> searchFlights(
            @RequestParam String originLocationCode,
            @RequestParam String destinationLocationCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate,
            @RequestParam Integer adults,
            @RequestParam(defaultValue = "10") Integer max
    ) {
        FlightOfferSearchResponse response = flightSearchServiceEnhanced.searchFlights(
                originLocationCode,
                destinationLocationCode,
                departureDate,
                returnDate,
                adults,
                max
        );

        return ResponseEntity.ok(response);
    }
}
