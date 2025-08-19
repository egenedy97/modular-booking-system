package com.example.modular_booking_system.core.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    /* =======================
       Payment Audit
    ======================== */
    public static final String PAYMENT_AUDIT_QUEUE = "payment.audit.queue";
    public static final String PAYMENT_AUDIT_EXCHANGE = "payment.audit.exchange";
    public static final String PAYMENT_AUDIT_ROUTING_KEY = "payment.audit.routingkey";

    @Bean
    public Queue paymentAuditQueue() {
        return new Queue(PAYMENT_AUDIT_QUEUE, true);
    }

    @Bean
    public TopicExchange paymentAuditExchange() {
        return new TopicExchange(PAYMENT_AUDIT_EXCHANGE);
    }

    @Bean
    public Binding paymentAuditBinding(
            @Qualifier("paymentAuditQueue") Queue paymentQueue,
            @Qualifier("paymentAuditExchange") TopicExchange paymentExchange) {
        return BindingBuilder.bind(paymentQueue)
                .to(paymentExchange)
                .with(PAYMENT_AUDIT_ROUTING_KEY);
    }

    /* =======================
       Flight Booking Audit
    ======================== */

    public static final String FLIGHT_BOOKING_AUDIT_QUEUE = "flight.booking.audit.queue";
    public static final String FLIGHT_BOOKING_AUDIT_EXCHANGE = "flight.booking.audit.exchange";
    public static final String FLIGHT_BOOKING_AUDIT_ROUTING_KEY = "flight.booking.audit.routingkey";

    @Bean
    public Queue flightBookingAuditQueue() {
        return new Queue(FLIGHT_BOOKING_AUDIT_QUEUE, true);
    }

    @Bean
    public TopicExchange flightBookingAuditExchange() {
        return new TopicExchange(FLIGHT_BOOKING_AUDIT_EXCHANGE);
    }

    @Bean
    public Binding flightBookingAuditBinding(
            @Qualifier("flightBookingAuditQueue") Queue bookingQueue,
            @Qualifier("flightBookingAuditExchange") TopicExchange bookingExchange) {
        return BindingBuilder.bind(bookingQueue)
                .to(bookingExchange)
                .with(FLIGHT_BOOKING_AUDIT_ROUTING_KEY);
    }

    /* =======================
       Common Beans
    ======================== */

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        RabbitTemplate template = new RabbitTemplate(connectionFactory);
//        template.setMessageConverter(new Jackson2JsonMessageConverter());
//        template.setMandatory(true);
//        return template;
//    }
}