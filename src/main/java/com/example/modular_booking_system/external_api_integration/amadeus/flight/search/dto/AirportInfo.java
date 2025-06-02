package com.example.modular_booking_system.external_api_integration.amadeus.flight.search.dto;

import lombok.Data;

@Data
public class AirportInfo {
    private String iataCode;
    private String terminal;
    private String at; // timestamp
}
