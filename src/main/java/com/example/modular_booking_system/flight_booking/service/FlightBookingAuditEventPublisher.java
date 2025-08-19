package com.example.modular_booking_system.flight_booking.service;

import com.example.modular_booking_system.core.config.RabbitMQConfig;
import com.example.modular_booking_system.core.events.AuditEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FlightBookingAuditEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    // Publishes a generic BookingAuditEvent to the configured exchange and routing key
    private void publishFlightBookingEvent(AuditEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.FLIGHT_BOOKING_AUDIT_EXCHANGE,
                RabbitMQConfig.FLIGHT_BOOKING_AUDIT_ROUTING_KEY,
                event
        );
    }

    // Publishes an audit event for when price is confirmed
    public void publishFlightPriceConfirmed(String messageId, String action, String serviceName, String topicName, Object data, String auditBy, LocalDateTime auditDate) {
        AuditEvent event = new AuditEvent(messageId, action, serviceName, topicName, data, auditBy, auditDate);
        publishFlightBookingEvent(event);
    }

    // Publishes an audit event for when payment is created
    public void publishPaymentCreated(String messageId, String action, String serviceName, String topicName, Object data, String auditBy, LocalDateTime auditDate) {
        AuditEvent event = new AuditEvent(messageId, action, serviceName, topicName, data, auditBy, auditDate);
        publishFlightBookingEvent(event);
    }

    // Publishes an audit event for when payment is executed
    public void publishPaymentExecuted(String messageId, String action, String serviceName, String topicName, Object data, String auditBy, LocalDateTime auditDate) {
        AuditEvent event = new AuditEvent(messageId, action, serviceName, topicName, data, auditBy, auditDate);
        publishFlightBookingEvent(event);
    }

    // Publishes an audit event for when Amadeus flight is booked
    public void publishFlightBooked(String messageId, String action, String serviceName, String topicName, Object data, String auditBy, LocalDateTime auditDate) {
        AuditEvent event = new AuditEvent(messageId, action, serviceName, topicName, data, auditBy, auditDate);
        publishFlightBookingEvent(event);
    }
}
