package com.example.modular_booking_system.user.model;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "document")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Document implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    @Column(name = "birthPlace", nullable = false)
    private String birthPlace;

    @Column(name = "issuanceLocation", nullable = false)
    private String issuanceLocation;

    @Column(name = "issuanceDate", nullable = false)
    private LocalDate issuanceDate;

    @Column(name = "number", nullable = false, length = 20, unique = true)
    private String number;

    @Column(name = "expiryDate", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "issuanceCountry", nullable = false)
    private String issuanceCountry;

    @Column(name = "validityCountry", nullable = false)
    private String validityCountry;

    @Column(name = "nationality", nullable = false)
    private String nationality;

    @Column(name = "holder", nullable = false)
    private boolean holder;
}
