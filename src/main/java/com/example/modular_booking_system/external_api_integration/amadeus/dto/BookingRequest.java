package com.example.modular_booking_system.external_api_integration.amadeus.dto;

import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.FlightOrder;
import lombok.Data;

@Data
public class BookingRequest {
    private FlightOfferSearch flightOffer;
    private FlightOrder.Traveler[] travelers;


}