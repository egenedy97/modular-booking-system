package com.example.modular_booking_system.flight_booking.service;

import com.example.modular_booking_system.flight_booking.dto.BookingRequest;
import com.example.modular_booking_system.flight_booking.service.handler.FlightBookingHandler;
import com.example.modular_booking_system.flight_booking.service.handler.PaymentCreationHandler;
import com.example.modular_booking_system.flight_booking.service.handler.FlightPriceConfirmationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final FlightPriceConfirmationHandler flightPriceConfirmationHandler;
    private final PaymentCreationHandler paymentCreationHandler;
    private final FlightBookingHandler flightBookingHandler;

//    private final PaymentExecutionHandler paymentExecutionHandler;

    public BookingRequest initiateBooking(BookingRequest bookingRequest) {

        bookingRequest.setBookingTimestamp(LocalDateTime.now());

        // First chain: Price confirmation and payment creation
        flightPriceConfirmationHandler.setNext(paymentCreationHandler);

        return flightPriceConfirmationHandler.handle(bookingRequest);
    }


    public BookingRequest completeBooking(BookingRequest bookingRequest) {
        bookingRequest.getPaymentDetails().setState("COMPLETED");

        // Second chain: flight booking
        return flightBookingHandler.handle(bookingRequest);
    }
}

