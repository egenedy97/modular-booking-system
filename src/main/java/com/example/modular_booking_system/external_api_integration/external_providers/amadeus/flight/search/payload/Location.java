package com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.search.payload;

import lombok.Data;

@Data
public class Location {
    private String cityCode;
    private String countryCode;
}
