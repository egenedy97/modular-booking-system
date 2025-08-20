package com.example.modular_booking_system.auditing.listener;

import com.example.modular_booking_system.auditing.service.AuditService;
import com.example.modular_booking_system.core.config.RabbitMQConfig;
import com.example.modular_booking_system.core.events.AuditEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FlightBookingAuditEventListener {

    private final AuditService auditService;

    // Method that listens for messages from the FLIGHT_BOOKING_AUDIT_QUEUE .
    // When an AuditEvent is received, it creates and saves an AuditLog entity.
    @RabbitListener(queues = RabbitMQConfig.FLIGHT_BOOKING_AUDIT_QUEUE)
    public void handleFlightBookingAuditEvent(AuditEvent event) {
        auditService.saveAuditLog(event);
    }
}
