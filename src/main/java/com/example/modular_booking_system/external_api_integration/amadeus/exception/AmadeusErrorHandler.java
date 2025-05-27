package com.example.modular_booking_system.external_api_integration.amadeus.exception;

import com.amadeus.exceptions.ResponseException;

public class AmadeusErrorHandler {

    public static RuntimeException handleError(ResponseException e) {
        String errorMessage = switch (e.getCode()) {
            case "38187" -> "Departure date must be in the future";
            case "38189" -> "Return date must be after departure date";
            case "38190" -> "Invalid location code";
            case "38191" -> "Invalid number of adults";
            default -> "Error searching flights: " + e.getMessage();
        };
        return new RuntimeException(errorMessage, e);
    }



}
