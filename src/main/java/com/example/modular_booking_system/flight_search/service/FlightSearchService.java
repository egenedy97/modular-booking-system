package com.example.modular_booking_system.flight_search.service;

import com.example.modular_booking_system.external_api_integration.aggregator.flight.service.FlightSearchAggregatorService;
import com.example.modular_booking_system.flight_search.model.FlightSearchResult;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class FlightSearchService {

    private FlightSearchAggregatorService flightSearchAggregatorService;

    public FlightSearchService(FlightSearchAggregatorService flightSearchAggregatorService) {
        this.flightSearchAggregatorService = flightSearchAggregatorService;
    }

    @Cacheable(value = "flightSearchCache",
            key = "#origin + '_' + #destination + '_' + #departureDate + '_' + #returnDate + '_' + #adults + '_' + #max",
            unless = "#result == null")
    public FlightSearchResult searchFlightsForGuestUser(String guestUserId,
                                                        String origin,
                                                        String destination,
                                                        LocalDate departureDate,
                                                        LocalDate returnDate,
                                                        Integer adults,
                                                        Integer max) {
        JsonNode result = flightSearchAggregatorService.searchAllProviders(origin, destination, departureDate, returnDate, adults, max);
        return FlightOfferMapper.mapFlightOffers(result);
    }
}
