package com.example.modular_booking_system.flight_booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmadeusFlightOrderRequest {
    private Data data;

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        private String type = "flight-order";
        private List<FlightOffer> flightOffers;
        private List<Traveler> travelers;
        private Remarks remarks;
        private TicketingAgreement ticketingAgreement;
        private List<Contact> contacts;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FlightOffer {
        private String type;
        private String id;
        private String source;
        private boolean instantTicketingRequired;
        private boolean nonHomogeneous;
        private boolean oneWay;
        private boolean isUpsellOffer;
        private String lastTicketingDate;
        private String lastTicketingDateTime;
        private int numberOfBookableSeats;
        private List<Itinerary> itineraries;
        private Price price;
        private PricingOptions pricingOptions;
        private List<String> validatingAirlineCodes;
        private List<TravelerPricing> travelerPricings;
    }


    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Itinerary {
        private String duration;
        private List<Segment> segments;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Segment {
        private Location departure;
        private Location arrival;
        private String carrierCode;
        private String number;
        private Aircraft aircraft;
        private Operating operating;
        private String duration;
        private String id;
        private int numberOfStops;
        private boolean blacklistedInEU;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Location {
        private String iataCode;
        private String terminal;
        private String at;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Aircraft {
        private String code;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Operating {
        private String carrierCode;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Price {
        private String currency;
        private String total;
        private String base;
        private List<Fee> fees;
        private String grandTotal;
        private List<AdditionalService> additionalServices;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Fee {
        private String amount;
        private String type;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdditionalService {
        private String amount;
        private String type;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PricingOptions {
        private List<String> fareType;
        private boolean includedCheckedBagsOnly;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TravelerPricing {
        private String travelerId;
        private String fareOption;
        private String travelerType;
        private Price price;
        private List<FareDetail> fareDetailsBySegment;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FareDetail {
        private String segmentId;
        private String cabin;
        private String fareBasis;
        private String brandedFare;
        private String brandedFareLabel;
//        private String @class;
        @JsonProperty("class")
        private String bookingClass;
        private Baggage includedCheckedBags;
        private Baggage includedCabinBags;
        private List<Amenity> amenities;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Baggage {
        private int quantity;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Amenity {
        private String description;
        private boolean isChargeable;
        private String amenityType;
        private AmenityProvider amenityProvider;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AmenityProvider {
        private String name;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Traveler {
        private String id;
        private String dateOfBirth;
        private Name name;
        private String gender;
        private TravelerContact contact;
        private List<Document> documents;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Name {
        private String firstName;
        private String lastName;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TravelerContact {
        private String emailAddress;
        private List<Phone> phones;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Phone {
        private String deviceType;
        private String countryCallingCode;
        private String number;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Document {
        private String documentType;
        private String birthPlace;
        private String issuanceLocation;
        private String issuanceDate;
        private String number;
        private String expiryDate;
        private String issuanceCountry;
        private String validityCountry;
        private String nationality;
        private boolean holder;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Remarks {
        private List<GeneralRemark> general;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GeneralRemark {
        private String subType;
        private String text;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TicketingAgreement {
        private String option;
        private String delay;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Contact {
        private Name addresseeName;
        private String companyName;
        private String purpose;
        private List<Phone> phones;
        private String emailAddress;
        private Address address;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address {
        private List<String> lines;
        private String postalCode;
        private String cityName;
        private String countryCode;
    }
}
