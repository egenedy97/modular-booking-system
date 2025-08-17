package com.example.modular_booking_system.flight_booking.service.handler;

import com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.booking.service.AmadeusFlightBookingService;
import com.example.modular_booking_system.flight_booking.dto.BookingContext;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FlightBookingHandler extends BookingHandler{

    private final AmadeusFlightBookingService amadeusFlightBookingService;

    @Override
    public BookingContext handle(BookingContext context) {
        try {
            JsonNode bookingResponse = amadeusFlightBookingService.createBooking(context.getFlightBookingRequest());
            context.setFlightBookingResponse(bookingResponse);
            context.setStatus("FLIGHT_BOOKED");

            return processNext(context);

        } catch (Exception e) {
            context.setStatus("FLIGHT_BOOKING_FAILED");
            context.setErrorMessage(e.getMessage());
            return context;
        }
    }

}
