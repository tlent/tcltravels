package com.thomaslent.tcltravels.dto;

public record FlightActivityRow(
    String airlineId,
    String airlineName,
    Integer flightNumber,
    Integer numberOfSeats,
    String daysOperating,
    Long reservationCount) {
}
