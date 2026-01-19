package com.thomaslent.tcltravels.dto;

public record FlightView(
    String airlineId,
    Integer flightNumber,
    Integer numberOfSeats,
    String daysOperatingLabel,
    String originId,
    String originCity,
    String destinationId,
    String destinationCity) {

}
