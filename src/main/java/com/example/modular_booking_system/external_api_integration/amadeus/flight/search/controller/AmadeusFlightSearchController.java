package com.example.modular_booking_system.external_api_integration.amadeus.flight.search.controller;

import com.example.modular_booking_system.external_api_integration.amadeus.flight.search.dto.FlightOffer;
import com.example.modular_booking_system.external_api_integration.amadeus.flight.search.service.AmadeusFlightSearchService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

//http://localhost:8090/api/v1/amadeus/flights/search?origin=CAI&destination=LON&departureDate=2025-06-01&returnDate=2025-06-10&adults=1&max=5

@RestController
@RequestMapping("/api/v1/amadeus/flights")
public class AmadeusFlightSearchController {
    private final AmadeusFlightSearchService flightSearchService;

    public AmadeusFlightSearchController(AmadeusFlightSearchService flightSearchService) {
        this.flightSearchService = flightSearchService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<FlightOffer>> searchFlights(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate,
            @RequestParam(defaultValue = "1") Integer adults,
            @RequestParam(defaultValue = "10") Integer max) {

        List<FlightOffer> flightOffers = flightSearchService.searchFlights(
                origin, destination, departureDate, returnDate, adults, max);

        return ResponseEntity.ok(flightOffers);
    }


}
