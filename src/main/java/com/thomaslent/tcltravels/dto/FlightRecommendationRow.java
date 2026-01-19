package com.thomaslent.tcltravels.dto;

public interface FlightRecommendationRow {
  String getAirlineId();

  Integer getFlightNumber();

  Integer getNumberOfSeats();

  String getDaysOperating();

  String getOriginId();

  String getOriginCity();

  String getDestinationId();

  String getDestinationCity();
}
