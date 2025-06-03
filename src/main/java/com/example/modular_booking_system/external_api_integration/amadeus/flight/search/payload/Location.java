package com.example.modular_booking_system.external_api_integration.amadeus.flight.search.payload;

import lombok.Data;

@Data
public class Location {
    private String cityCode;
    private String countryCode;
}
