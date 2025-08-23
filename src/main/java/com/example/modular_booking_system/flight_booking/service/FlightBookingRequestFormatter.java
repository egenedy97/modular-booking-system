package com.example.modular_booking_system.flight_booking.service;

import com.example.modular_booking_system.flight_booking.dto.AmadeusFlightOrderRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

import com.example.modular_booking_system.user.model.User;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightBookingRequestFormatter {

    private final ObjectMapper objectMapper;

    /**
     * Formats a flight offer and user information into an Amadeus-compatible booking request
     *
     * @param flightOffer The flight offer to book
//     * @param userId      The ID of the user making the booking
     * @return JSON string formatted for Amadeus booking
     */

//    public String formatFlightBookingRequest(Object flightOffer, Long userId, User user)
    public JsonNode formatFlightBookingRequest(Object flightOffer, User user) {
        try {
            // Convert the flight offer to our DTO structure
            AmadeusFlightOrderRequest.FlightOffer offer = objectMapper.convertValue(flightOffer, AmadeusFlightOrderRequest.FlightOffer.class);

            // Create the booking request
            AmadeusFlightOrderRequest request = new AmadeusFlightOrderRequest();
            AmadeusFlightOrderRequest.Data data = new AmadeusFlightOrderRequest.Data();

            // Set flight offers
            data.setFlightOffers(List.of(offer));

            // Set traveler information
            data.setTravelers(createTravelers(user));

            // Set remarks
            data.setRemarks(createRemarks());

            // Set ticketing agreement
            data.setTicketingAgreement(createTicketingAgreement());

            // Set contact information
            data.setContacts(createContacts());

            request.setData(data);


            // Convert to JsonNode
            return objectMapper.readTree(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(request));

        } catch (JsonProcessingException e) {
            log.error("Error formatting flight booking request", e);
            throw new RuntimeException("Failed to format flight booking request", e);
        }
    }

    private List<AmadeusFlightOrderRequest.Traveler> createTravelers(User user) {
        // Create traveler from user information
        AmadeusFlightOrderRequest.Traveler traveler = new AmadeusFlightOrderRequest.Traveler();
        traveler.setId("1");

        // Format date of birth
        if (user.getDateOfBirth() != null) {
            traveler.setDateOfBirth(user.getDateOfBirth().format(DateTimeFormatter.ISO_LOCAL_DATE));
        }

        // Set name
        AmadeusFlightOrderRequest.Name name = new AmadeusFlightOrderRequest.Name();
        name.setFirstName(user.getFirstName() != null ? user.getFirstName().toUpperCase() : "");
        name.setLastName(user.getLastName() != null ? user.getLastName().toUpperCase() : "");
        traveler.setName(name);

        // Set gender (assuming User model has a gender field)
        if (user.getGender() != null) {
            traveler.setGender(user.getGender().name());
        }

        // Set contact information (simplified - in a real app, this would come from user profile)
        AmadeusFlightOrderRequest.TravelerContact contact = new AmadeusFlightOrderRequest.TravelerContact();
        contact.setEmailAddress(user.getEmail() != null ? user.getEmail() : "");

        AmadeusFlightOrderRequest.Phone phone = new AmadeusFlightOrderRequest.Phone();
        phone.setDeviceType("MOBILE");
        phone.setCountryCallingCode("20"); // Default to Egypt
        phone.setNumber("1000000000"); // Default number
        contact.setPhones(List.of(phone));

        traveler.setContact(contact);

        // In a real app, you would add document information from user profile

        return List.of(traveler);
    }

    private AmadeusFlightOrderRequest.Remarks createRemarks() {
        AmadeusFlightOrderRequest.Remarks remarks = new AmadeusFlightOrderRequest.Remarks();
        AmadeusFlightOrderRequest.GeneralRemark generalRemark = new AmadeusFlightOrderRequest.GeneralRemark();
        generalRemark.setSubType("GENERAL_MISCELLANEOUS");
        generalRemark.setText("ONLINE BOOKING FROM MODULAR BOOKING SYSTEM");
        remarks.setGeneral(List.of(generalRemark));
        return remarks;
    }

    private AmadeusFlightOrderRequest.TicketingAgreement createTicketingAgreement() {
        AmadeusFlightOrderRequest.TicketingAgreement agreement = new AmadeusFlightOrderRequest.TicketingAgreement();
        agreement.setOption("DELAY_TO_CANCEL");
        agreement.setDelay("6D");
        return agreement;
    }

    private List<AmadeusFlightOrderRequest.Contact> createContacts() {
        AmadeusFlightOrderRequest.Contact contact = new AmadeusFlightOrderRequest.Contact();

        // Set addressee name
        AmadeusFlightOrderRequest.Name name = new AmadeusFlightOrderRequest.Name();
        name.setFirstName("MODULAR");
        name.setLastName("BOOKING");
        contact.setAddresseeName(name);

        contact.setCompanyName("MODULAR BOOKING SYSTEM");
        contact.setPurpose("STANDARD");

        // Set phone numbers
        AmadeusFlightOrderRequest.Phone landline = new AmadeusFlightOrderRequest.Phone();
        landline.setDeviceType("LANDLINE");
        landline.setCountryCallingCode("20");
        landline.setNumber("225551234");

        AmadeusFlightOrderRequest.Phone mobile = new AmadeusFlightOrderRequest.Phone();
        mobile.setDeviceType("MOBILE");
        mobile.setCountryCallingCode("20");
        mobile.setNumber("1009876543");

        contact.setPhones(List.of(landline, mobile));
        contact.setEmailAddress("support@modularbooking.com");

        // Set address
        AmadeusFlightOrderRequest.Address address = new AmadeusFlightOrderRequest.Address();
        address.setLines(List.of("123 Main Street"));
        address.setPostalCode("11511");
        address.setCityName("Cairo");
        address.setCountryCode("EG");
        contact.setAddress(address);

        return List.of(contact);
    }
}