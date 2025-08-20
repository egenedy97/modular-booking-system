package com.example.modular_booking_system.payment.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
@Table(name = "transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description", nullable = true, length = 255)
    private String description;

    @Column(name = "total", nullable = false)
    private BigDecimal total;

    @Column(name = "currency", nullable = false)
    private Currency currency;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "intent", nullable = false)
    private String intent;

    @ManyToOne
    @JoinColumn(name = "payment_method_id", nullable = false)
    private PaymentMethod paymentMethod;

    @ManyToOne
    @JoinColumn(name = "transaction_status_id", nullable = false)
    private TransactionStatus transactionStatus;

    @Column(name = "cancelUrl", nullable = true)
    private String cancelUrl;

    @Column(name = "successUrl", nullable = true)
    private String successUrl;

    @Column(name = "approvalUrl", nullable = true)
    private String approvalUrl;

    @Column(name = "returnUrl", nullable = true)
    private String returnUrl;
}
