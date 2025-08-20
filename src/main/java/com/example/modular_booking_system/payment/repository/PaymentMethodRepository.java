package com.example.modular_booking_system.payment.repository;

import com.example.modular_booking_system.payment.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
}