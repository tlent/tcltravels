package com.thomaslent.tcltravels.dto;

import java.time.Instant;

public interface ReservationLegRow {
  Long getReservationNumber();

  String getAirlineId();

  Integer getFlightNumber();

  String getOriginAirportId();

  String getOriginName();

  String getOriginCity();

  String getDestinationAirportId();

  String getDestinationName();

  String getDestinationCity();

  Instant getDepartureTime();

  Instant getArrivalTime();
}
