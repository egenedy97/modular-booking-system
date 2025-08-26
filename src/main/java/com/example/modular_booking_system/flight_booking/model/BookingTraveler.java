package com.example.modular_booking_system.flight_booking.model;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "booking_traveler")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingTraveler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to booking
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private FlightBooking booking;

    // Personal info
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;

    // Passport
    private String passportNumber;
    private LocalDate passportExpiry;
    private String nationality;

    // Contact
    private String email;
    private String phone;
}
