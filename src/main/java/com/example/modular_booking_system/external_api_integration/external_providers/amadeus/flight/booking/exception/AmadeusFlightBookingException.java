package com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.booking.exception;

public class AmadeusFlightBookingException extends RuntimeException {
    public AmadeusFlightBookingException(String message) {
        super(message);
    }

    public AmadeusFlightBookingException(String message, Throwable cause) {
        super(message, cause);
    }
}
