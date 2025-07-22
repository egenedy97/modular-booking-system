package com.example.modular_booking_system.payment.service;

import com.example.modular_booking_system.core.config.RabbitMQConfig;
import com.example.modular_booking_system.core.events.PaymentAuditEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(PaymentAuditEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PAYMENT_AUDIT_EXCHANGE,
                RabbitMQConfig.PAYMENT_AUDIT_ROUTING_KEY,
                event
        );
    }
}