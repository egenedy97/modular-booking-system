package com.example.modular_booking_system.flight_booking.model;

import com.example.modular_booking_system.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "flight_booking")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // link to user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Amadeus identifiers
    @Column(nullable = false)
    private String orderId; // Amadeus data.id

    @Column(nullable = false, length = 20)
    private String pnr; // associatedRecords.reference

    private String originSystemCode; // GDS, LO

    // Booking status
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus; // CREATED, CONFIRMED, TICKETED, CANCELLED
    private LocalDate lastTicketingDate;

    // Pricing
    private String currency;
    private BigDecimal baseFare;
    private BigDecimal taxesTotal;
    private BigDecimal totalAmount;
    private BigDecimal refundableTaxes;

    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingTraveler> travelers;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingSegment> segments;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
