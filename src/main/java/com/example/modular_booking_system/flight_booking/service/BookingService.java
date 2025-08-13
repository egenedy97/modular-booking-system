package com.example.modular_booking_system.flight_booking.service;

import com.example.modular_booking_system.flight_booking.dto.BookingContext;
import com.example.modular_booking_system.flight_booking.service.handler.PaymentCreationHandler;
import com.example.modular_booking_system.flight_booking.service.handler.PriceConfirmationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final PriceConfirmationHandler priceConfirmationHandler;
    private final PaymentCreationHandler paymentCreationHandler;
//    private final PaymentExecutionHandler paymentExecutionHandler;
//    private final FlightBookingHandler flightBookingHandler;

    public BookingContext initiateBooking(BookingContext context) {

        // First chain: Price confirmation and payment creation
        priceConfirmationHandler.setNext(paymentCreationHandler);

        return priceConfirmationHandler.handle(context);
    }


//    public BookingContext completeBooking(BookingContext context) {
//        // Second chain: Payment execution and flight booking
//        return paymentExecutionHandler.handle(context);
//    }
}

