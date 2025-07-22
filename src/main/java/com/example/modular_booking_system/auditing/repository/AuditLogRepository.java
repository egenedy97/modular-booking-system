package com.example.modular_booking_system.auditing.repository;

import com.example.modular_booking_system.auditing.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}