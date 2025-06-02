package com.example.modular_booking_system.external_api_integration.amadeus.flight.search.service;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.example.modular_booking_system.external_api_integration.amadeus.flight.search.exception.AmadeusErrorHandler;
import com.example.modular_booking_system.external_api_integration.amadeus.flight.search.dto.FlightOffer;
import com.example.modular_booking_system.external_api_integration.amadeus.flight.search.exception.FlightSearchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AmadeusFlightSearchService {

    private final Amadeus amadeus;

    @Autowired
    public AmadeusFlightSearchService(Amadeus amadeus) {
        this.amadeus = amadeus;
    }

    public List<FlightOffer> searchFlights(String originLocationCode,
                                           String destinationLocationCode,
                                           LocalDate departureDate,
                                           LocalDate returnDate,
                                           Integer adults,
                                           Integer max) {
        try {
            FlightOfferSearch[] amadeusFlightOffers = amadeus.shopping.flightOffersSearch.get(buildSearchParams(
                    originLocationCode, destinationLocationCode, departureDate, returnDate, adults, max));

            if (amadeusFlightOffers == null || amadeusFlightOffers.length == 0) {
                throw new FlightSearchException("No flights found for the given criteria");
            }

            return FlightOfferMapper.mapToFlightOffer(amadeusFlightOffers);

        } catch (ResponseException e) {
            throw AmadeusErrorHandler.handleError(e);
        } catch (Exception e) {
            throw new FlightSearchException("Unexpected error while searching flights", e);
        }
    }

    private static final String DEFAULT_CURRENCY = "USD";
    private static final String DEFAULT_TRAVEL_CLASS = "ECONOMY";


    private Params buildSearchParams(String originLocationCode, String destinationLocationCode,
                                     LocalDate departureDate, LocalDate returnDate,
                                     Integer adults, Integer max) {
        Params params = Params.with("originLocationCode", originLocationCode)
                .and("destinationLocationCode", destinationLocationCode)
                .and("departureDate", departureDate.toString())
                .and("adults", adults)
                .and("max", max)
                .and("currencyCode", DEFAULT_CURRENCY)
                .and("nonStop", "false")
                .and("travelClass", DEFAULT_TRAVEL_CLASS);

        if (returnDate != null) {
            params.and("returnDate", returnDate.toString());
        }

        return params;
    }


}
