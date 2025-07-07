package com.example.modular_booking_system.external_api_integration.external_providers.amadeus.flight.search.service;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CityCodeService {

    private final Amadeus amadeus;

    public String getCityCode(String cityName) {
        try {
            Location[] locations = amadeus.referenceData.locations.get(Params
                    .with("keyword", cityName) // Use the city name as a keyword to search for locations
                    .and("subType", "CITY")); // Specify that we want city locations

            if (locations != null && locations.length > 0) {
                return locations[0].getIataCode();
            }
            throw new RuntimeException("City code not found for: " + cityName);
        } catch (ResponseException e) {
            throw new RuntimeException("Error getting city code for: " + cityName, e);
        }
    }
}
