package com.example.modular_booking_system.flight_search.controller;

import com.example.modular_booking_system.flight_search.model.FlightSearchResult;
import com.example.modular_booking_system.flight_search.service.FlightSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;

import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/flights")
public class FlightSearchController {

    private final FlightSearchService flightSearchService;

    //localhost:8090/api/v1/flights/search?origin=LON&destination=CAI&departureDate=2025-07-10&returnDate=2025-07-20&adults=1&max=5
    @GetMapping("/search")
    public ResponseEntity<FlightSearchResult> searchFlights(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate,
            @RequestParam(defaultValue = "1") Integer adults,
            @RequestParam(defaultValue = "10") Integer max,
            @RequestHeader(value = "Guest-User-Id", required = false) String guestUserId) {

        FlightSearchResult result = flightSearchService.searchFlightsForGuestUser(
                guestUserId,
                origin,
                destination,
                departureDate,
                returnDate,
                adults,
                max
        );
        return ResponseEntity.ok(result);
    }
}
