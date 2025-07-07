package com.example.modular_booking_system.payment.dto.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PayPalTokenResponse {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("expires_in")
    private int expiresIn;
}
