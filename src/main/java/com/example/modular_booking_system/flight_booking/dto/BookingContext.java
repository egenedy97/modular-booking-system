package com.example.modular_booking_system.flight_booking.dto;

import com.example.modular_booking_system.payment.model.PaymentDetails;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingContext {

    private String bookingId;
    private String userId;
//    private FlightBookingRequest flightBookingRequest;
//    private JsonNode flightBookingResponse;

    private JsonNode flightOffer;
    private JsonNode FlightOfferPriceConfirmationResponse;

    private PaymentRequestDto paymentRequest;
    private String paymentUrl;
    private PaymentDetails paymentDetails;
    private String status;
    private String errorMessage;
}
