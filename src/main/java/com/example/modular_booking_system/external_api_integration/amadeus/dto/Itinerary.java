package com.example.modular_booking_system.external_api_integration.amadeus.dto;

import lombok.Data;

import java.util.List;

@Data
public class Itinerary {
    private String duration;
    private List<Segment> segments;
}
