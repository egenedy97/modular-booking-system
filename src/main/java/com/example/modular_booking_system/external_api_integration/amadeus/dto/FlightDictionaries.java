package com.example.modular_booking_system.external_api_integration.amadeus.dto;

import lombok.Data;

import java.util.Map;

@Data
public class FlightDictionaries {
    private Map<String, Location> locations;
    private Map<String, String> aircraft;
    private Map<String, String> currencies;
    private Map<String, String> carriers;
}