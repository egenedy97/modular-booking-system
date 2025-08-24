package com.example.modular_booking_system.flight_booking.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.example.modular_booking_system.user.model.Contact;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"booking\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "bookingType", nullable = false)
    private BookingType bookingType;

    @Column(name = "bookingNumber", nullable = false, unique = true)
    private String bookingNumber;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "ExternalBookingId", nullable = false)
    private Long externalBookingId;

    @Column(name = "booking_status", nullable = false)
    private BookingStatus bookingStatus;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_id", referencedColumnName = "id")
    private Contact contact;

    @Column(name = "more_details", nullable = true)
    private String moreDetails;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "flight_id", nullable = false)
    private Long flightId;

    @Column(name = "pnr", nullable = false)
    private String pnr;

    @Column(name = "price_details", columnDefinition = "TEXT")
    private String priceDetailsJson;

    @Column(name = "from_location", nullable = false)
    private String fromLocation;

    @Column(name = "to_location", nullable = false)
    private String toLocation;

    @Column(name = "arrival_date", nullable = false)
    private LocalDateTime arrivalDate;

    @Column(name = "departure_date", nullable = false)
    private LocalDateTime departureDate;

}
