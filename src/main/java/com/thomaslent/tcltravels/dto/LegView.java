package com.thomaslent.tcltravels.dto;

import java.time.OffsetDateTime;

public record LegView(
    String airlineId,
    Integer flightNumber,
    String originAirportId,
    String originName,
    String originCity,
    String destinationAirportId,
    String destinationName,
    String destinationCity,
    OffsetDateTime departureTime,
    OffsetDateTime arrivalTime) {
}
