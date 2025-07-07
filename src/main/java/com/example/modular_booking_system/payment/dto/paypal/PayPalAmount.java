package com.example.modular_booking_system.payment.dto.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayPalAmount {
    private String currency_code;
    private String value;
    
    public static PayPalAmount of(String currency, Double amount) {
        BigDecimal value = BigDecimal.valueOf(amount).setScale(2, BigDecimal.ROUND_HALF_UP);
        return new PayPalAmount(currency, value.toString());
    }
}
