package com.example.modular_booking_system.flight_search.service;

import com.example.modular_booking_system.flight_search.model.*;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class FlightOfferMapper {

    public static FlightSearchResult mapFlightOffers(JsonNode response) {
        FlightSearchResult result = new FlightSearchResult();
        result.setRawFlightOffer(response);

        JsonNode dataArray = response.get("data");
        if (dataArray != null && !dataArray.isEmpty()) {
            result.setFlightOffers(mapJsonToFlightOffer(dataArray));
        }

        return result;
    }

    private static List<FlightOffer> mapJsonToFlightOffer(JsonNode dataArray) {
        List<FlightOffer> flightOffers = new ArrayList<>();
        if (dataArray == null || dataArray.isEmpty()) return flightOffers;

        for (JsonNode flightNode : dataArray) {
            FlightOffer flightOffer = new FlightOffer();
            flightOffer.setId(flightNode.get("id").asText());
            flightOffer.setType(flightNode.get("type").asText());
            flightOffer.setValidatingAirlineCodes(flightNode.get("validatingAirlineCodes").get(0).asText());
            flightOffer.setNumberOfBookableSeats(flightNode.get("numberOfBookableSeats").asInt());

            // Map price
            JsonNode priceNode = flightNode.get("price");
            if (priceNode != null) {
                Price price = new Price();
                price.setCurrency(priceNode.get("currency").asText());
                price.setBase(priceNode.get("base").asText());
                price.setTotal(priceNode.get("total").asText());
                price.setGrandTotal(priceNode.get("grandTotal").asText());

                JsonNode feesNode = priceNode.get("fees");
                if (feesNode != null && !feesNode.isEmpty()) {
                    price.setFees(mapJsonFees(feesNode));
                }
                flightOffer.setPrice(price);
            }

            // Map itineraries
            JsonNode itinerariesNode = flightNode.get("itineraries");
            if (itinerariesNode != null && !itinerariesNode.isEmpty()) {
                flightOffer.setItineraries(mapJsonItineraries(itinerariesNode));
            }

            flightOffers.add(flightOffer);
        }
        return flightOffers;
    }

    private static List<Fee> mapJsonFees(JsonNode feesNode) {
        List<Fee> feeList = new ArrayList<>();
        for (JsonNode feeNode : feesNode) {
            Fee fee = new Fee();
            fee.setAmount(feeNode.get("amount").asText());
            fee.setType(feeNode.get("type").asText());
            feeList.add(fee);
        }
        return feeList;
    }

    private static List<Itinerary> mapJsonItineraries(JsonNode itinerariesNode) {
        List<Itinerary> itineraries = new ArrayList<>();
        for (JsonNode itineraryNode : itinerariesNode) {
            Itinerary itinerary = new Itinerary();
            itinerary.setDuration(itineraryNode.get("duration").asText());
            itinerary.setSegments(mapJsonSegments(itineraryNode.get("segments")));
            itineraries.add(itinerary);
        }
        return itineraries;
    }

    private static List<Segment> mapJsonSegments(JsonNode segmentsNode) {
        List<Segment> segments = new ArrayList<>();
        for (JsonNode segmentNode : segmentsNode) {
            Segment segment = new Segment();
            segment.setCarrierCode(segmentNode.get("carrierCode").asText());
            segment.setNumber(segmentNode.get("number").asText());
            segment.setDuration(segmentNode.get("duration").asText());
            segment.setAircraft(segmentNode.get("aircraft").get("code").asText());

            // Map departure
            JsonNode departureNode = segmentNode.get("departure");
            AirportInfo departureInfo = new AirportInfo();
            departureInfo.setIataCode(departureNode.get("iataCode").asText());
            departureInfo.setTerminal(departureNode.has("terminal") ? departureNode.get("terminal").asText() : null);
            departureInfo.setAt(departureNode.get("at").asText());
            segment.setDepartureInfo(departureInfo);
            segment.setDeparture(departureInfo.getIataCode());

            // Map arrival
            JsonNode arrivalNode = segmentNode.get("arrival");
            AirportInfo arrivalInfo = new AirportInfo();
            arrivalInfo.setIataCode(arrivalNode.get("iataCode").asText());
            arrivalInfo.setTerminal(arrivalNode.has("terminal") ? arrivalNode.get("terminal").asText() : null);
            arrivalInfo.setAt(arrivalNode.get("at").asText());
            segment.setArrivalInfo(arrivalInfo);
            segment.setArrival(arrivalInfo.getIataCode());

            segments.add(segment);
        }
        return segments;
    }
}