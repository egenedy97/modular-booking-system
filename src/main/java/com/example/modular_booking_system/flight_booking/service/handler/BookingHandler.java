package com.example.modular_booking_system.flight_booking.service.handler;

import com.example.modular_booking_system.flight_booking.dto.BookingRequest;

public abstract class BookingHandler {

    private BookingHandler nextHandler;

    public BookingHandler setNext(BookingHandler handler) {
        this.nextHandler = handler;
        return handler;
    }

    public abstract BookingRequest handle(BookingRequest bookingRequest);


    public BookingRequest processNext(BookingRequest bookingRequest) {
        if (nextHandler != null) {
            return nextHandler.handle(bookingRequest);
        }
        return bookingRequest;
    }

}
