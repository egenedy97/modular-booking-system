package com.example.modular_booking_system.payment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Base64;

@Configuration
public class PayPalConfig {

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.base-url}")
    private String baseUrl;

    @Bean
    public PayPalProperties payPalProperties() {
        return new PayPalProperties(clientId, clientSecret, baseUrl);
    }

    @Bean
    public String paypalAuthHeader() {
        String auth = clientId + ":" + clientSecret;
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes());
    }

    public record PayPalProperties(
            String clientId,
            String clientSecret,
            String baseUrl
    ) {}
}
