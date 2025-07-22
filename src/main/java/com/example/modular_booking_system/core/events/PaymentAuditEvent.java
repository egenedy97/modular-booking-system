package com.example.modular_booking_system.core.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAuditEvent {
    private String action;
    private String paymentId;
    private String userId;
    private BigDecimal amount;
    private LocalDateTime timestamp;
}