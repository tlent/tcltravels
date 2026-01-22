package com.thomaslent.tcltravels.dto;

import java.time.OffsetDateTime;

public record FlightStopView(
    String airlineId,
    Integer flightNumber,
    Integer stopNumber,
    String airportId,
    String airportName,
    String city,
    OffsetDateTime arrivalTime,
    OffsetDateTime departureTime) {
}
