package com.thomaslent.tcltravels.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class FlightId implements Serializable {
  @Column(name = "airline_id", length = 2)
  private String airlineId;

  @Column(name = "flight_number")
  private Integer flightNumber;

  public String getAirlineId() {
    return airlineId;
  }

  public void setAirlineId(String airlineId) {
    this.airlineId = airlineId;
  }

  public Integer getFlightNumber() {
    return flightNumber;
  }

  public void setFlightNumber(Integer flightNumber) {
    this.flightNumber = flightNumber;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((airlineId == null) ? 0 : airlineId.hashCode());
    result = prime * result + ((flightNumber == null) ? 0 : flightNumber.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    FlightId other = (FlightId) obj;
    if (airlineId == null) {
      if (other.airlineId != null)
        return false;
    } else if (!airlineId.equals(other.airlineId))
      return false;
    if (flightNumber == null) {
      if (other.flightNumber != null)
        return false;
    } else if (!flightNumber.equals(other.flightNumber))
      return false;
    return true;
  }

}
