package com.example.modular_booking_system.user.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Phone implements Serializable {
    @Column(name = "deviceType", nullable = false)
    private String deviceType;

    @Column(name = "countryCallingCode", nullable = false)
    private String countryCallingCode;

    @Column(name = "number", nullable = false, unique = true)
    private String number;

}
