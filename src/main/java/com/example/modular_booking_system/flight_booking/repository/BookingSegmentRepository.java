package com.example.modular_booking_system.flight_booking.repository;

import com.example.modular_booking_system.flight_booking.model.BookingSegment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingSegmentRepository extends JpaRepository<BookingSegment, Long> {

}