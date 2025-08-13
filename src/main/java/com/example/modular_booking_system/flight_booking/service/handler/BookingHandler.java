package com.example.modular_booking_system.flight_booking.service.handler;

import com.example.modular_booking_system.flight_booking.dto.BookingContext;

public abstract class BookingHandler {

    private BookingHandler nextHandler;

    public BookingHandler setNext(BookingHandler handler) {
        this.nextHandler = handler;
        return handler;
    }

    public abstract BookingContext handle(BookingContext context);


    public BookingContext processNext(BookingContext context) {
        if (nextHandler != null) {
            return nextHandler.handle(context);
        }
        return context;
    }

}
