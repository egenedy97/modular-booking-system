package com.example.modular_booking_system.payment.service.paypal;

import com.example.modular_booking_system.payment.config.PayPalConfig;
import com.example.modular_booking_system.payment.dto.paypal.PayPalTokenResponse;
import com.example.modular_booking_system.payment.exception.PaymentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayPalAccessTokenService {

    private final WebClient webClient;
    private final String paypalAuthHeader;
    private final PayPalConfig.PayPalProperties payPalProperties;

    private String accessToken;
    private long tokenExpiryTime;

    public synchronized String getAccessToken() throws PaymentException {
        // Reuse token if valid (5-minute buffer)
        if (accessToken != null && System.currentTimeMillis() < tokenExpiryTime - 300_000) {
            return accessToken;
        }

        try {
            // Request new token from PayPal
            PayPalTokenResponse tokenResponse = webClient.post()
                    .uri(payPalProperties.baseUrl() + "/v1/oauth2/token")
                    .header(HttpHeaders.AUTHORIZATION, paypalAuthHeader)
                    .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                    .bodyValue("grant_type=client_credentials")
                    .retrieve()
                    .bodyToMono(PayPalTokenResponse.class)
                    .block();

            if (tokenResponse != null && tokenResponse.getAccessToken() != null) {
                this.accessToken = tokenResponse.getAccessToken();
                this.tokenExpiryTime = System.currentTimeMillis() + tokenResponse.getExpiresIn() * 1000L;
                return this.accessToken;
            }

            throw new PaymentException("Failed to get PayPal access token");

        } catch (Exception e) {
            log.error("Error retrieving PayPal access token", e);
            throw new PaymentException("Failed to get PayPal access token: " + e.getMessage(), e);
        }
    }
}
