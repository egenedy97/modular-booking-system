package com.example.modular_booking_system.payment.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transaction_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "status_name", nullable = false, unique = true)
    private String statusName;

    @Column(name = "description", nullable = true, length = 255)
    private String description;
}
