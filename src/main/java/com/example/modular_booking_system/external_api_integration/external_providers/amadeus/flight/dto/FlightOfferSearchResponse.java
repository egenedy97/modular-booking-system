package com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightOfferSearchResponse {
    private Meta meta;
    private List<FlightOffer> data;
    private Dictionaries dictionaries;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Meta {
        private Integer count;
        private Links links;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Links {
        private String self;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FlightOffer {
        private String type;
        private String id;
        private String source;
        private Boolean instantTicketingRequired;
        private Boolean nonHomogeneous;
        private Boolean oneWay;
        private Boolean isUpsellOffer;
        private String lastTicketingDate;
        private String lastTicketingDateTime;
        private Integer numberOfBookableSeats;
        private List<Itinerary> itineraries;
        private Price price;
        private PricingOptions pricingOptions;
        private List<String> validatingAirlineCodes;
        private List<TravelerPricing> travelerPricings;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Itinerary {
        private String duration;
        private List<Segment> segments;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Segment {
        private FlightEndpoint departure;
        private FlightEndpoint arrival;
        private String carrierCode;
        private String number;
        private Aircraft aircraft;
        private Operating operating;
        private String duration;
        private String id;
        private Integer numberOfStops;
        private Boolean blacklistedInEU;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FlightEndpoint {
        private String iataCode;
        private String terminal;
        private String at; // ISO 8601 datetime format
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Aircraft {
        private String code;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Operating {
        private String carrierCode;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Price {
        private String currency;
        private String total;
        private String base;
        private List<Fee> fees;
        private String grandTotal;
        private List<AdditionalService> additionalServices;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Fee {
        private String amount;
        private String type;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AdditionalService {
        private String amount;
        private String type;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PricingOptions {
        private List<String> fareType;
        private Boolean includedCheckedBagsOnly;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TravelerPricing {
        private String travelerId;
        private String fareOption;
        private String travelerType;
        private TravelerPrice price;
        private List<FareDetailsBySegment> fareDetailsBySegment;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TravelerPrice {
        private String currency;
        private String total;
        private String base;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FareDetailsBySegment {
        private String segmentId;
        private String cabin;
        private String fareBasis;
        private String brandedFare;
        private String brandedFareLabel;
        @JsonProperty("class")
        private String fareClass; // 'class' is a reserved keyword in Java
        private BaggageAllowance includedCheckedBags;
        private BaggageAllowance includedCabinBags;
        private List<Amenity> amenities;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BaggageAllowance {
        private Integer quantity;
        private Integer weight;
        private String weightUnit;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Amenity {
        private String description;
        private Boolean isChargeable;
        private String amenityType;
        private AmenityProvider amenityProvider;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AmenityProvider {
        private String name;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Dictionaries {
        private Map<String, Location> locations;
        private Map<String, String> aircraft;
        private Map<String, String> currencies;
        private Map<String, String> carriers;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Location {
        private String cityCode;
        private String countryCode;
    }
}