package com.example.modular_booking_system.flight_booking.service.handler;

import com.example.modular_booking_system.core.config.RabbitMQConfig;
import com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.booking.service.AmadeusFlightBookingService;
import com.example.modular_booking_system.flight_booking.dto.BookingContext;
import com.example.modular_booking_system.flight_booking.service.FlightBookingAuditEventPublisher;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class FlightBookingHandler extends BookingHandler {

    private final AmadeusFlightBookingService amadeusFlightBookingService;
    private final FlightBookingAuditEventPublisher flightBookingAuditEventPublisher;

    @Override
    public BookingContext handle(BookingContext context) {
        try {
            JsonNode bookingResponse = amadeusFlightBookingService.createBooking(context.getFlightBookingRequest());
            context.setFlightBookingResponse(bookingResponse);
            context.setStatus("FLIGHT_BOOKED");

            // Publish audit event for a booked flight
            flightBookingAuditEventPublisher.publishFlightBooked(
                    context.getBookingId(),
                    "FLIGHT_BOOKED",
                    "FLIGHT_SERVICE",
                    RabbitMQConfig.FLIGHT_BOOKING_AUDIT_QUEUE,
                    context,
                    "SYSTEM",
                    LocalDateTime.now()
            );

            return processNext(context);

        } catch (Exception e) {
            context.setStatus("FLIGHT_BOOKING_FAILED");
            context.setErrorMessage(e.getMessage());
            return context;
        }
    }

}
