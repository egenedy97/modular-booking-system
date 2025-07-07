package com.example.modular_booking_system.payment.dto.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseUnit {
    private List<Item> items;
    private Amount amount;
    private String description;
    private String reference_id;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Amount {
        private String currency_code;
        private String value;
        private Breakdown breakdown;
        
        public static Amount of(String currency, Double amount) {
            return new Amount(
                currency,
                String.format("%.2f", amount),
                new Breakdown(ItemAmount.of(amount))
            );
        }
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Breakdown {
        private ItemAmount item_total;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemAmount {
        private String currency_code;
        private String value;
        
        public static ItemAmount of(Double amount) {
            return new ItemAmount("USD", String.format("%.2f", amount));
        }
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        private String name;
        private String description;
        private String quantity;
        private UnitAmount unit_amount;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UnitAmount {
        private String currency_code;
        private String value;
    }
}
