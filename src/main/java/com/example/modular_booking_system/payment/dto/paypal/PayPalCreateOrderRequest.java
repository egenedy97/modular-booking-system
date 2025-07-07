package com.example.modular_booking_system.payment.dto.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayPalCreateOrderRequest {
    private String intent;
    @JsonProperty("purchase_units")
    private List<PurchaseUnit> purchaseUnits;
    @JsonProperty("application_context")
    private ApplicationContext applicationContext;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplicationContext {
        private String return_url;
        private String cancel_url;
        private String brand_name;
        private String landing_page;
        @JsonProperty("user_action")
        private String userAction = "PAY_NOW";
        @JsonProperty("shipping_preference")
        private String shippingPreference = "NO_SHIPPING";
    }
}
