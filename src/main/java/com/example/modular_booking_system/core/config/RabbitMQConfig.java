package com.example.modular_booking_system.core.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@EnableRabbit
public class RabbitMQConfig {

    // ----------------------- SMS Notification -----------------------
    public static final String SMS_NOTIFICATION_EXCHANGE = "sms.notification.exchange";
    public static final String SMS_NOTIFICATION_DELAY_QUEUE = "sms.notification.delay.queue";
    public static final String SMS_NOTIFICATION_DELAY_ROUTING_KEY = "sms.notification.delay.routingkey";

    // ----------------------- Email Notification -----------------------
    public static final String EMAIL_NOTIFICATION_EXCHANGE = "email.notification.exchange";
    public static final String EMAIL_NOTIFICATION_DELAY_QUEUE = "email.notification.delay.queue";
    public static final String EMAIL_NOTIFICATION_DELAY_ROUTING_KEY = "email.notification.delay.routingkey";

    /*
     * =======================
     * Payment Audit
     * ========================
     */
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
    public TopicExchange smsNotificationExchange() {
        return new TopicExchange(SMS_NOTIFICATION_EXCHANGE);
    }

    @Bean
    public TopicExchange emailNotificationExchange() {
        return new TopicExchange(EMAIL_NOTIFICATION_EXCHANGE);
    }

    @Bean
    public Queue smsNotificationDelayQueue() {
        return QueueBuilder.durable(SMS_NOTIFICATION_DELAY_QUEUE)
                .withArgument("x-dead-letter-exchange", SMS_NOTIFICATION_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", SMS_NOTIFICATION_DELAY_ROUTING_KEY)
                .withArgument("x-message-ttl", 10000) // 10 seconds delay
                .build();
    }

    @Bean
    public Queue emailNotificationDelayQueue() {
        return QueueBuilder.durable(EMAIL_NOTIFICATION_DELAY_QUEUE)
                .withArgument("x-dead-letter-exchange", EMAIL_NOTIFICATION_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", EMAIL_NOTIFICATION_DELAY_ROUTING_KEY)
                .withArgument("x-message-ttl", 10000) // 10 seconds delay
                .build();
    }

    @Bean
    public Binding paymentAuditBinding(
            @Qualifier("paymentAuditQueue") Queue paymentQueue,
            @Qualifier("paymentAuditExchange") TopicExchange paymentExchange) {
        return BindingBuilder.bind(paymentQueue)
                .to(paymentExchange)
                .with(PAYMENT_AUDIT_ROUTING_KEY);
    }


    @Bean
    public Binding smsNotificationDelayBinding(
            @Qualifier("smsNotificationDelayQueue") Queue smsDelayQueue,
            @Qualifier("smsNotificationExchange") TopicExchange smsExchange) {
        return BindingBuilder.bind(smsDelayQueue)
                .to(smsExchange)
                .with(SMS_NOTIFICATION_DELAY_ROUTING_KEY);
    }

    @Bean
    public Binding emailNotificationDelayBinding(
            @Qualifier("emailNotificationDelayQueue") Queue emailDelayQueue,
            @Qualifier("emailNotificationExchange") TopicExchange emailExchange) {
        return BindingBuilder.bind(emailDelayQueue)
                .to(emailExchange)
                .with(EMAIL_NOTIFICATION_DELAY_ROUTING_KEY);
    }

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

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        template.setMandatory(true);
        return template;
    }
}
