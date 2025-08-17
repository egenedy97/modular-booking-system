package com.example.modular_booking_system.flight_booking.controller;

import com.example.modular_booking_system.flight_booking.dto.BookingContext;
import com.example.modular_booking_system.flight_booking.service.BookingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    // localhost:8090/api/booking/initiate
    @PostMapping("/initiate")
    public ResponseEntity<BookingContext> initiateBooking(@RequestBody BookingContext context, HttpServletRequest request) {


        String cancelUrl = getBaseUrl(request) + "/api/payment/cancel";
        String successUrl = getBaseUrl(request) + "/api/payment/success";

        context.getPaymentRequest().setCancelUrl(cancelUrl);
        context.getPaymentRequest().setSuccessUrl(successUrl);

        var result = bookingService.initiateBooking(context);
        return ResponseEntity.ok(result);
    }

    // localhost:8090/api/booking/complete
    @PostMapping("/complete")
    public ResponseEntity<BookingContext> completeBooking(@RequestBody BookingContext context) {
//        var context = bookingService.getBookingContext(bookingId);
        var result = bookingService.completeBooking(context);
        return ResponseEntity.ok(result);
    }


    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();

        return scheme + "://" + serverName + (serverPort != 80 ? ":" + serverPort : "") + contextPath;
    }
}