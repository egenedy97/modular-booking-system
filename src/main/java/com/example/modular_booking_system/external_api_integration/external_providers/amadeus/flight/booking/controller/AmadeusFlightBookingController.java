package com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.booking.controller;

import com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.booking.service.AmadeusFlightBookingService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/amadeus/flight-booking")
public class AmadeusFlightBookingController {
    private final AmadeusFlightBookingService amadeusFlightBookingService;

    @PostMapping("/orders")
    public ResponseEntity<JsonNode> createBooking(@RequestBody JsonNode bookingRequestData) {
        JsonNode bookingResponse = amadeusFlightBookingService.createBooking(bookingRequestData);
        return ResponseEntity.ok(bookingResponse);
    }

    @GetMapping("/orders/{flightOrderId}")
    public ResponseEntity<JsonNode> getBookingById(@PathVariable String flightOrderId) {
        JsonNode booking = amadeusFlightBookingService.getBookingById(flightOrderId);
        return ResponseEntity.ok(booking);
    }
    
    @DeleteMapping("/orders/{flightOrderId}")
    public ResponseEntity<JsonNode> deleteBookingById(@PathVariable String flightOrderId) {
        JsonNode result = amadeusFlightBookingService.deleteBookingById(flightOrderId);
        return ResponseEntity.ok(result);
    }
}
