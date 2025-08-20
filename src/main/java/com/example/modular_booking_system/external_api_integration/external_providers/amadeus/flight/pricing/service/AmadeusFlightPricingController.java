package com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.pricing.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/amadeus/flights/pricing")
public class AmadeusFlightPricingController{
    private final AmadeusFlightPricingService amadeusFlightPricingService;

    //localhost:8090/api/v1/amadeus/flights/pricing/confirm
    @PostMapping("/confirm")
    public ResponseEntity<JsonNode> confirmPrice(@RequestBody JsonNode flightOffer) {
        JsonNode flightPrice= amadeusFlightPricingService.confirmPrice(flightOffer);
        return ResponseEntity.ok(flightPrice);
    }
}
