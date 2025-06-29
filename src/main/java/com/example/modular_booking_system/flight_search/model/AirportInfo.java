package com.example.modular_booking_system.flight_search.model;

import lombok.Data;

@Data
public class AirportInfo {
    private String iataCode;
    private String terminal;
    private String at; // timestamp
}
