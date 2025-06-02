package com.example.modular_booking_system.external_api_integration.amadeus.flight.search.service;

import com.amadeus.resources.FlightOfferSearch;
import com.example.modular_booking_system.external_api_integration.amadeus.flight.search.dto.*;

import java.util.ArrayList;
import java.util.List;

public class FlightOfferMapper {

    public static List<FlightOffer> mapToFlightOffer(FlightOfferSearch[] flightOfferSearches) {
        List<FlightOffer> flightOffers = new ArrayList<>();
        if (flightOfferSearches == null) return flightOffers;

        for (FlightOfferSearch flightOffer : flightOfferSearches) {
            FlightOffer flightOfferTemp = new FlightOffer();
            flightOfferTemp.setId(flightOffer.getId());
            flightOfferTemp.setType(flightOffer.getType());
            flightOfferTemp.setValidatingAirlineCodes(flightOffer.getValidatingAirlineCodes()[0]);
            flightOfferTemp.setNumberOfBookableSeats(flightOffer.getNumberOfBookableSeats());

            // Map price
            if (flightOffer.getPrice() != null) {
                Price price = new Price();
                price.setCurrency(flightOffer.getPrice().getCurrency());
                price.setBase(flightOffer.getPrice().getBase());
                price.setTotal(flightOffer.getPrice().getTotal());
                price.setGrandTotal(flightOffer.getPrice().getGrandTotal());

                if (flightOffer.getPrice().getFees() != null) {
                    price.setFees(mapFees(flightOffer.getPrice().getFees()));
                }
                flightOfferTemp.setPrice(price);
            }

            // Map itineraries
            if (flightOffer.getItineraries() != null) {
                flightOfferTemp.setItineraries(mapItineraries(flightOffer.getItineraries()));
            }

            flightOffers.add(flightOfferTemp);
        }
        return flightOffers;
    }

    private static List<Fee> mapFees(com.amadeus.resources.FlightOfferSearch.Fee[] fees) {
        List<Fee> feeList = new ArrayList<>();
        for (com.amadeus.resources.FlightOfferSearch.Fee fee : fees) {
            Fee feeTemp = new Fee();
            feeTemp.setAmount(fee.getAmount());
            feeTemp.setType(fee.getType());
            feeList.add(feeTemp);
        }
        return feeList;
    }

    private static List<Itinerary> mapItineraries(com.amadeus.resources.FlightOfferSearch.Itinerary[] itineraries) {
        List<Itinerary> itineraryTemp = new ArrayList<>();
        for (com.amadeus.resources.FlightOfferSearch.Itinerary itinerary : itineraries) {
            Itinerary itineraryDto = new Itinerary();
            itineraryDto.setDuration(itinerary.getDuration());
            itineraryDto.setSegments(mapSegments(itinerary.getSegments()));
            itineraryTemp.add(itineraryDto);
        }
        return itineraryTemp;
    }

    private static List<Segment> mapSegments(com.amadeus.resources.FlightOfferSearch.SearchSegment[] segments) {
        List<Segment> segmentsList = new ArrayList<>();
        for (com.amadeus.resources.FlightOfferSearch.SearchSegment segment : segments) {
            Segment segmentTemp = new Segment();
            segmentTemp.setCarrierCode(segment.getCarrierCode());
            segmentTemp.setNumber(segment.getNumber());
            segmentTemp.setDuration(segment.getDuration());
            segmentTemp.setAircraft(segment.getAircraft().getCode());

            // Map departure
            AirportInfo departureInfo = new AirportInfo();
            departureInfo.setIataCode(segment.getDeparture().getIataCode());
            departureInfo.setTerminal(segment.getDeparture().getTerminal());
            departureInfo.setAt(segment.getDeparture().getAt());
            segmentTemp.setDepartureInfo(departureInfo);

            segmentTemp.setDeparture(departureInfo.getIataCode());

            // Map arrival
            AirportInfo arrivalInfo = new AirportInfo();
            arrivalInfo.setIataCode(segment.getArrival().getIataCode());
            arrivalInfo.setTerminal(segment.getArrival().getTerminal());
            arrivalInfo.setAt(segment.getArrival().getAt());
            segmentTemp.setArrivalInfo(arrivalInfo);

            segmentTemp.setArrival(arrivalInfo.getIataCode());

            segmentsList.add(segmentTemp);
        }
        return segmentsList;
    }
}