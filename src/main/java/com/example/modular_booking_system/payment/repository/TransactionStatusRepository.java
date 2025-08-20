package com.example.modular_booking_system.payment.repository;

import com.example.modular_booking_system.payment.model.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionStatusRepository extends JpaRepository<TransactionStatus, Long> {
}