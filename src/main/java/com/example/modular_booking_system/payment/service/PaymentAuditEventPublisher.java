package com.example.modular_booking_system.payment.service;

import com.example.modular_booking_system.core.config.RabbitMQConfig;
import com.example.modular_booking_system.core.events.AuditEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentAuditEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    // Publishes a generic PaymentAuditEvent to the configured exchange and routing key
    public void publishPaymentEvent(AuditEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PAYMENT_AUDIT_EXCHANGE,
                RabbitMQConfig.PAYMENT_AUDIT_ROUTING_KEY,
                event
        );
    }

    // Publishes an audit event for when a payment is created
    public void publishPaymentCreated(String action, String messageId, String serviceName, String topicName, Object data, String auditBy, LocalDateTime auditDate) {
        AuditEvent event = new AuditEvent(action, messageId, serviceName, topicName, data, auditBy, auditDate);
        publishPaymentEvent(event);
    }

    // Publishes an audit event for when a payment is retrieved
    public void publishPaymentRetrieved(String action, String messageId, String serviceName, String topicName, Object data, String auditBy, LocalDateTime auditDate) {
        AuditEvent event = new AuditEvent(action, messageId, serviceName, topicName, data, auditBy, auditDate);
        publishPaymentEvent(event);
    }

    // Publishes an audit event for when a payment is executed
    public void publishPaymentExecuted(String action, String messageId, String serviceName, String topicName, Object data, String auditBy, LocalDateTime auditDate) {
        AuditEvent event = new AuditEvent(action, messageId, serviceName, topicName, data, auditBy, auditDate);
        publishPaymentEvent(event);
    }

}