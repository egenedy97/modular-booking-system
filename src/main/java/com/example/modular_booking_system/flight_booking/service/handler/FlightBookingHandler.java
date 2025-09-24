package com.example.modular_booking_system.flight_booking.service.handler;

import com.example.modular_booking_system.core.config.RabbitMQConfig;
import com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.booking.service.AmadeusFlightBookingService;
import com.example.modular_booking_system.flight_booking.dto.BookingRequest;
import com.example.modular_booking_system.flight_booking.service.AmadeusFlightBookingMapper;
import com.example.modular_booking_system.flight_booking.service.FlightBookingRequestFormatter;
import com.example.modular_booking_system.flight_booking.service.FlightBookingAuditEventPublisher;
import com.example.modular_booking_system.user.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class FlightBookingHandler extends BookingHandler {

    private final AmadeusFlightBookingService amadeusFlightBookingService;
    private final FlightBookingAuditEventPublisher flightBookingAuditEventPublisher;
    private final FlightBookingRequestFormatter flightBookingRequestFormatter;
    private final AmadeusFlightBookingMapper amadeusFlightBookingMapper;
    private final UserService userService;

    @Override
    public BookingRequest handle(BookingRequest bookingRequest) {
        try {

//            User user = userService.findById(Long.parseLong(context.getUserId()));

//            JsonNode FlightBookingRequest = flightBookingRequestFormatter.formatFlightBookingRequest(context.getFlightOffer(), user);

//            context.setFlightBookingRequest(FlightBookingRequest);
            JsonNode bookingResponse = amadeusFlightBookingService.createBooking(bookingRequest.getFlightBookingRequest());
            bookingRequest.setFlightBookingResponse(bookingResponse);
            bookingRequest.setStatus("FLIGHT_BOOKED");

            // Publish audit event for a booked flight
            flightBookingAuditEventPublisher.publishFlightBooked(
                    bookingRequest.getBookingId(),
                    "FLIGHT_BOOKED",
                    "FLIGHT_SERVICE",
                    RabbitMQConfig.FLIGHT_BOOKING_AUDIT_QUEUE,
                    bookingRequest,
                    "SYSTEM",
                    bookingRequest.getBookingTimestamp()
            );

            return processNext(bookingRequest);

        } catch (Exception e) {
            bookingRequest.setStatus("FLIGHT_BOOKING_FAILED");
            bookingRequest.setErrorMessage(e.getMessage());
            return bookingRequest;
        }
    }

}
