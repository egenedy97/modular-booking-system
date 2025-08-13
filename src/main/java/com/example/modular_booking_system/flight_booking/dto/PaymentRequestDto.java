package com.example.modular_booking_system.flight_booking.dto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {

    private double total;
    private String currency;
    private String context;
    private String intent; // capture
    private String method; // paypal
    private String description;
    private HttpServletRequest request;
    private String cancelUrl;
    private String successUrl;
}
