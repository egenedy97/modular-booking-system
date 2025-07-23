package com.example.modular_booking_system.payment.service;

import com.example.modular_booking_system.core.config.RabbitMQConfig;
import com.example.modular_booking_system.core.events.PaymentAuditEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    // Publishes a generic PaymentAuditEvent to the configured exchange and routing key
    public void publishPaymentEvent(PaymentAuditEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PAYMENT_AUDIT_EXCHANGE,
                RabbitMQConfig.PAYMENT_AUDIT_ROUTING_KEY,
                event
        );
    }

    // Publishes an audit event for when a payment is created
    public void publishPaymentCreated(String action, String paymentId, String userId, String username, BigDecimal amount, LocalDateTime timestamp) {
        PaymentAuditEvent event = new PaymentAuditEvent(action, paymentId, userId,username, amount, timestamp);
        publishPaymentEvent(event);
    }

    // Publishes an audit event for when a payment is retrieved
    public void publishPaymentRetrieved(String action, String paymentId, String userId, String username, BigDecimal amount, LocalDateTime timestamp) {
        PaymentAuditEvent event = new PaymentAuditEvent(action, paymentId, userId,username, amount, timestamp);
        publishPaymentEvent(event);
    }

    // Publishes an audit event for when a payment is executed
    public void publishPaymentExecuted(String action, String paymentId, String userId, String username, BigDecimal amount, LocalDateTime timestamp) {
        PaymentAuditEvent event = new PaymentAuditEvent(action, paymentId, userId,username, amount, timestamp);
        publishPaymentEvent(event);
    }

}