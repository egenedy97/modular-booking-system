package com.example.modular_booking_system.external_api_integration.amadeus.dto;

import lombok.Data;

import java.util.List;

@Data
public class FlightOffer {
    private String id;
    private String type;
    private Price price;
    private List<Itinerary> itineraries;
    private String validatingAirlineCodes;
    private Integer numberOfBookableSeats;
//    private FlightDictionaries dictionaries;

}