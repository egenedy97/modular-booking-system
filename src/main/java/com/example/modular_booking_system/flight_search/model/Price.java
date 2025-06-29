package com.example.modular_booking_system.flight_search.model;

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
