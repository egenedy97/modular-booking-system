package com.example.modular_booking_system.external_api_integration.amadeus.flight.search.payload;

import lombok.Data;

import java.util.List;
@Data
public class Price {
    private String currency;
    private String base;
    private String total;
    private List<Fee> fees;
    private String grandTotal;
}
