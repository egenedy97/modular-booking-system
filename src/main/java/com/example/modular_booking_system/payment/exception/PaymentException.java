package com.example.modular_booking_system.payment.exception;

/**
 * Custom exception for payment processing errors.
 */
public class PaymentException extends Exception {
    
    public PaymentException(String message) {
        super(message);
    }
    
    public PaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}
