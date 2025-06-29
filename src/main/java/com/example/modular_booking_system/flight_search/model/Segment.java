package com.example.modular_booking_system.flight_search.model;

import lombok.Data;

@Data
public class Segment {
    private String departure;
    private String arrival;
    private String carrierCode;
    private String number;
    private AirportInfo departureInfo;
    private AirportInfo arrivalInfo;
    private String aircraft;
    private String duration;
}
