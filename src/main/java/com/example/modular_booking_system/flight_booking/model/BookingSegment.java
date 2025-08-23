package com.example.modular_booking_system.flight_booking.model;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "booking_segment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingSegment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to booking
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private FlightBooking booking;

    // Flight info
    private String departureAirport; // IATA code
    private String arrivalAirport;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    private String carrierCode; // Airline code
    private String flightNumber;

    private String cabin; // ECONOMY, BUSINESS
    private String bookingClass; // e.g., K, L
    private Integer baggageQuantity;

    private String duration; // ISO 8601 like PT4H30M

}
