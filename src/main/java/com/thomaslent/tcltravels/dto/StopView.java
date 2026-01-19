package com.thomaslent.tcltravels.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record StopView(
    String airlineId,
    String airportId,
    Integer flightNumber,
    Integer stopNumber,
    OffsetDateTime arrivalTime,
    OffsetDateTime departureTime,
    LocalDate departureDate) {
}
