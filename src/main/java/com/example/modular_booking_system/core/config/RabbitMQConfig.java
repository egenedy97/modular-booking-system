package com.example.modular_booking_system.core.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String PAYMENT_AUDIT_QUEUE = "payment.audit.queue";
    public static final String PAYMENT_AUDIT_EXCHANGE = "payment.audit.exchange";
    public static final String PAYMENT_AUDIT_ROUTING_KEY = "payment.audit.routingkey";

    @Bean
    public Queue queue() {
        return new Queue(PAYMENT_AUDIT_QUEUE,true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(PAYMENT_AUDIT_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(PAYMENT_AUDIT_ROUTING_KEY);
    }

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