package com.example.modular_booking_system.auditing.listener;

import com.example.modular_booking_system.auditing.model.AuditLog;
import com.example.modular_booking_system.auditing.repository.AuditLogRepository;
import com.example.modular_booking_system.auditing.service.AuditService;
import com.example.modular_booking_system.core.config.RabbitMQConfig;
import com.example.modular_booking_system.core.events.PaymentAuditEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentAuditEventListener {

//    private final AuditLogRepository repository;
    private final AuditService auditService;
    // Method that listens for messages from the PAYMENT_AUDIT_QUEUE.
    // When a PaymentAuditEvent is received, it creates and saves an AuditLog entity.
    @RabbitListener(queues = RabbitMQConfig.PAYMENT_AUDIT_QUEUE)
    public void handleAuditEvent(PaymentAuditEvent event) {
        auditService.saveAuditLog(event);
    }
}
