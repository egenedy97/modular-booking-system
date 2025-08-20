package com.example.modular_booking_system.flight_booking.service;

import com.example.modular_booking_system.flight_booking.dto.BookingContext;
import com.example.modular_booking_system.flight_booking.service.handler.FlightBookingHandler;
import com.example.modular_booking_system.flight_booking.service.handler.PaymentCreationHandler;
import com.example.modular_booking_system.flight_booking.service.handler.FlightPriceConfirmationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final FlightPriceConfirmationHandler flightPriceConfirmationHandler;
    private final PaymentCreationHandler paymentCreationHandler;
    private final FlightBookingHandler flightBookingHandler;

//    private final PaymentExecutionHandler paymentExecutionHandler;

    public BookingContext initiateBooking(BookingContext context) {

        // First chain: Price confirmation and payment creation
        flightPriceConfirmationHandler.setNext(paymentCreationHandler);

        return flightPriceConfirmationHandler.handle(context);
    }


    public BookingContext completeBooking(BookingContext context) {

        // Second chain: flight booking
        return flightBookingHandler.handle(context);
    }
}

