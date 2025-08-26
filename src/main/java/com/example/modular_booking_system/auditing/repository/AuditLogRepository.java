package com.example.modular_booking_system.auditing.repository;

import com.example.modular_booking_system.auditing.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}