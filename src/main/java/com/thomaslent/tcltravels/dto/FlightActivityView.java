package com.thomaslent.tcltravels.dto;

public record FlightActivityView(
    String airlineId,
    String airlineName,
    Integer flightNumber,
    Integer numberOfSeats,
    String daysOperating,
    String originId,
    String originCity,
    String destinationId,
    String destinationCity,
    Long reservationCount) {
}
