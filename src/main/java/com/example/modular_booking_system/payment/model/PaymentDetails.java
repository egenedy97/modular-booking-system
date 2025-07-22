package com.example.modular_booking_system.payment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetails {

    private String id;
    private String state;
    private String intent;
    private Payer payer;
    private Amount amount;
    private String approvalUrl;
    private String cancelUrl;
    private String returnUrl;
    private String createTime;
    private String updateTime;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Payer {
        private String paymentMethod;
        private String payerId;
        private String payerName;
        private String payerEmail;
        private String payerCountryCode;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Amount {
        private String currency;
        private BigDecimal total;
    }
}
