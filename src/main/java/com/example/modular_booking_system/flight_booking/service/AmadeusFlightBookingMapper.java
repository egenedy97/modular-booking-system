package com.example.modular_booking_system.flight_booking.service;

import com.example.modular_booking_system.flight_booking.model.FlightBooking;
import com.example.modular_booking_system.flight_booking.model.BookingStatus;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AmadeusFlightBookingMapper {

    private final ObjectMapper objectMapper;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    private static final DateTimeFormatter DATE_ONLY_FORMATTER = DateTimeFormatter.ISO_DATE;

    public AmadeusFlightBookingMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public FlightBooking mapToFlightBooking(JsonNode amadeusResponse, Long userId) {
        if (amadeusResponse == null || !amadeusResponse.has("data")) {
            throw new IllegalArgumentException("Invalid Amadeus response format");
        }

        JsonNode data = amadeusResponse.get("data");
        FlightBooking booking = new FlightBooking();

        // Set basic booking information
        booking.setOrderId(data.path("id").asText());
        booking.setPnr(extractPnr(data));
        booking.setOriginSystemCode(extractOriginSystemCode(data));
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        booking.setLastTicketingDate(parseDate(data.path("flightOffers").get(0).path("lastTicketingDate").asText()));

        // Set pricing information
        setPricingInfo(booking, data);

        // Set travelers and segments as JSON
        booking.setTravelers(data.path("travelers"));
        booking.setSegments(extractSegments(data));

        // Set audit fields
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());

        return booking;
    }

    private String extractPnr(JsonNode data) {
        if (data.has("associatedRecords") && data.get("associatedRecords").isArray()) {
            for (JsonNode record : data.get("associatedRecords")) {
                if (record.has("reference") && !record.get("reference").asText().isBlank()) {
                    return record.get("reference").asText();
                }
            }
        }
        return "";
    }

    private String extractOriginSystemCode(JsonNode data) {
        if (data.has("associatedRecords") && data.get("associatedRecords").isArray() &&
                !data.get("associatedRecords").isEmpty()) {
            return data.get("associatedRecords").get(0).path("originSystemCode").asText();
        }
        return "GDS"; // Default to GDS if not specified
    }

    private void setPricingInfo(FlightBooking booking, JsonNode data) {
        if (data.has("flightOffers") && data.get("flightOffers").isArray() &&
                !data.get("flightOffers").isEmpty()) {

            JsonNode priceNode = data.get("flightOffers").get(0).path("price");
            if (!priceNode.isMissingNode()) {
                booking.setCurrency(priceNode.path("currency").asText());
                booking.setBaseFare(new BigDecimal(priceNode.path("base").asText()));
                booking.setTotalAmount(new BigDecimal(priceNode.path("total").asText()));

                // Calculate taxes total
                BigDecimal taxes = new BigDecimal(priceNode.path("total").asText())
                        .subtract(new BigDecimal(priceNode.path("base").asText()));
                booking.setTaxesTotal(taxes);

                // Set refundable taxes if available
                if (priceNode.has("refundableTaxes")) {
                    booking.setRefundableTaxes(new BigDecimal(priceNode.path("refundableTaxes").asText()));
                }
            }
        }
    }

    private JsonNode extractSegments(JsonNode data) {
        ObjectNode segmentsNode = objectMapper.createObjectNode();
        ArrayNode segmentsArray = objectMapper.createArrayNode();

        if (data.has("flightOffers") && data.get("flightOffers").isArray()) {
            for (JsonNode offer : data.get("flightOffers")) {
                if (offer.has("itineraries")) {
                    for (JsonNode itinerary : offer.path("itineraries")) {
                        if (itinerary.has("segments")) {
                            for (JsonNode segment : itinerary.path("segments")) {
                                segmentsArray.add(segment);
                            }
                        }
                    }
                }
            }
        }

        segmentsNode.set("segments", segmentsArray);
        return segmentsNode;
    }

    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, DATE_ONLY_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }
}