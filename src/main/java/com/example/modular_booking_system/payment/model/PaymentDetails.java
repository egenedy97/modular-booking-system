package com.example.modular_booking_system.payment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Represents the details of a payment transaction.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetails {
    /**
     * The unique identifier for the payment.
     */
    private String id;
    
    /**
     * The current state of the payment.
     */
    private String state;
    
    /**
     * The intent of the payment (sale, authorize, order).
     */
    private String intent;
    
    /**
     * The payer information.
     */
    private Payer payer;
    
    /**
     * The amount of the payment.
     */
    private Amount amount;
    
    /**
     * The URL to redirect the user to for payment approval.
     */
    private String approvalUrl;
    
    /**
     * The URL to redirect to if the user cancels the payment.
     */
    private String cancelUrl;
    
    /**
     * The URL to redirect to after successful payment.
     */
    private String returnUrl;
    
    /**
     * The time when the payment was created.
     */
    private String createTime;
    
    /**
     * The time when the payment was last updated.
     */
    private String updateTime;
    
    /**
     * Represents the payer information.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Payer {
        /**
         * The payment method (e.g., "paypal").
         */
        private String paymentMethod;
        
        /**
         * The status of the payer's payment.
         */
        private String status;
        
        /**
         * The payer's email address.
         */
        private String email;
        
        /**
         * The payer's first name.
         */
        private String firstName;
        
        /**
         * The payer's last name.
         */
        private String lastName;
        
        /**
         * The payer's ID.
         */
        private String payerId;
    }
    
    /**
     * Represents the amount of the payment.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Amount {
        /**
         * The three-character ISO-4217 currency code.
         */
        private String currency;
        
        /**
         * The total amount charged.
         */
        private BigDecimal total;
        
        /**
         * The details of the amount.
         */
        private Details details;
        
        /**
         * Represents the details of the amount.
         */
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Details {
            /**
             * The subtotal of all items.
             */
            private BigDecimal subtotal;
            
            /**
             * The shipping amount.
             */
            private BigDecimal shipping;
            
            /**
             * The tax amount.
             */
            private BigDecimal tax;
            
            /**
             * The handling fee.
             */
            private BigDecimal handlingFee;
            
            /**
             * The shipping discount.
             */
            private BigDecimal shippingDiscount;
            
            /**
             * The insurance amount.
             */
            private BigDecimal insurance;
        }
    }
    
    /**
     * Represents a link in the HATEOAS pattern.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Link {
        /**
         * The URL of the link.
         */
        private String href;
        
        /**
         * The relationship of the link to the containing object.
         */
        private String rel;
        
        /**
         * The HTTP method to use when accessing the link.
         */
        private String method;
    }
}
