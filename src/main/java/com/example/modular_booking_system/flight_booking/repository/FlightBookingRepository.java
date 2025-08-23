package com.example.modular_booking_system.flight_booking.repository;

import com.example.modular_booking_system.flight_booking.model.FlightBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightBookingRepository extends JpaRepository<FlightBooking, Long> {
    FlightBooking findByPnr(String pnr);
    FlightBooking findByOrderId(String orderId);
}