package com.example.modular_booking_system.payment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Global exception handler for payment-related exceptions.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles PaymentException and returns a standardized error response.
     *
     * @param ex The PaymentException that was thrown
     * @param request The web request that caused the exception
     * @return A ResponseEntity containing the error details
     */
    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<Object> handlePaymentException(
            PaymentException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Payment Processing Error");
        body.put("message", ex.getMessage());
        
        // Log the error for debugging
        if (ex.getCause() != null) {
            body.put("details", ex.getCause().getMessage());
        }

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Handles all other exceptions and returns a standardized error response.
     *
     * @param ex The exception that was thrown
     * @param request The web request that caused the exception
     * @return A ResponseEntity containing the error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(
            Exception ex, WebRequest request) {
            
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", "An unexpected error occurred while processing your request.");
        
        // In production, you might not want to expose the actual exception message
        if (ex.getCause() != null) {
            body.put("details", ex.getCause().getMessage());
        }

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
