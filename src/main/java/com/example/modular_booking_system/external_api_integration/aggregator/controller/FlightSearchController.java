package com.example.modular_booking_system.external_api_integration.aggregator.controller;

import com.example.modular_booking_system.external_api_integration.aggregator.service.FlightSearchAggregatorService;
import com.example.modular_booking_system.external_api_integration.amadeus.dto.FlightOffer;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

//http://localhost:8090/api/v1/flights/search?origin=CAI&destination=LON&departureDate=2025-06-01&returnDate=2025-06-10&adults=1&max=5

@RestController
@RequestMapping("/api/v1/flights")
public class FlightSearchController {

    private final FlightSearchAggregatorService aggregateFlightSearchService;

    public FlightSearchController(FlightSearchAggregatorService aggregateFlightSearchService) {
        this.aggregateFlightSearchService = aggregateFlightSearchService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<FlightOffer>> searchFlights(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate,
            @RequestParam(defaultValue = "1") Integer adults,
            @RequestParam(defaultValue = "10") Integer max) {

        List<FlightOffer> flightOffers = aggregateFlightSearchService.searchAllProviders(
                origin, destination, departureDate, returnDate, adults, max);

        return ResponseEntity.ok(flightOffers);
    }


}