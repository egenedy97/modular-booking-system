package com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.pricing.exception;

public class FlightPriceException extends RuntimeException {
    public FlightPriceException(String message) {
        super(message);
    }

    public FlightPriceException(String message, Throwable cause) {
        super(message, cause);
    }
}