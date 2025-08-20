package com.example.modular_booking_system.payment.repository;

import com.example.modular_booking_system.payment.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}