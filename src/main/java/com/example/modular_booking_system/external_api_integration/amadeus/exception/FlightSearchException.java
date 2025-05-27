package com.example.modular_booking_system.external_api_integration.amadeus.exception;

public class FlightSearchException extends RuntimeException {
    public FlightSearchException(String message) {
        super(message);
    }

    public FlightSearchException(String message, Throwable cause) {
        super(message, cause);
    }
}
