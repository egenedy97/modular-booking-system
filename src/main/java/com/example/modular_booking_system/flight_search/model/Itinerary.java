package com.example.modular_booking_system.flight_search.model;

import lombok.Data;

import java.util.List;

@Data
public class Itinerary {
    private String duration;
    private List<Segment> segments;
}
