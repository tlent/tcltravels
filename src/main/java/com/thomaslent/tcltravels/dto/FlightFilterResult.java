package com.thomaslent.tcltravels.dto;

import java.time.LocalDate;
import java.util.List;

import com.thomaslent.tcltravels.entities.Airline;
import com.thomaslent.tcltravels.entities.Airport;

public record FlightFilterResult(
    List<Airline> airlines,
    List<Airport> airports,
    String selectedAirlineId,
    String selectedAirportId,
    LocalDate after,
    LocalDate before,
    List<StopView> matchingStops) {
}
