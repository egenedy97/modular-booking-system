package com.example.modular_booking_system.flight_booking.repository;

import com.example.modular_booking_system.flight_booking.model.BookingTraveler;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingTravelerRepository extends JpaRepository<BookingTraveler, Long> {

}