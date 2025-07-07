package com.example.modular_booking_system.external_api_integration.aggregator.flight.controller;

import com.example.modular_booking_system.external_api_integration.aggregator.flight.service.FlightSearchAggregatorService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/flights/aggregator")
public class FlightSearchAggregatorController {

    private final FlightSearchAggregatorService flightSearchAggregatorService;

    //localhost:8090/api/v1/flights/aggregator/search?origin=CAI&destination=LON&departureDate=2025-07-01&returnDate=2025-07-10&adults=1&max=5
    @GetMapping("/search")
    public ResponseEntity<JsonNode> searchFlights(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate,
            @RequestParam(defaultValue = "1") Integer adults,
            @RequestParam(defaultValue = "10") Integer max) {

        JsonNode flightOffers = flightSearchAggregatorService.searchAllProviders(
                origin, destination, departureDate, returnDate, adults, max);

        return ResponseEntity.ok(flightOffers);
    }
}