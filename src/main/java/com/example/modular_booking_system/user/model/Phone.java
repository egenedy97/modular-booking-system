package com.example.modular_booking_system.user.model;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contact_phone")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Phone implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phone_id")
    private Long id;

    @Column(name = "device_type", nullable = false)
    private String deviceType;

    @Column(name = "country_calling_code", nullable = false)
    private String countryCallingCode;

    @Column(name = "number", nullable = false, unique = true)
    private String number;

    // Many phones belong to one contact
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;


}
