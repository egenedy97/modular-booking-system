package com.example.modular_booking_system.auditing.service;

import com.example.modular_booking_system.auditing.model.AuditLog;
import com.example.modular_booking_system.auditing.repository.AuditLogRepository;
import com.example.modular_booking_system.core.events.PaymentAuditEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public void saveAuditLog(PaymentAuditEvent event) {
        AuditLog auditLog = new AuditLog(null,
                event.getAction(),
                event.getPaymentId(),
                event.getUserId(),
                event.getUsername(),
                event.getAmount(),
                event.getTimestamp());

        auditLogRepository.save(auditLog);
    }
}
