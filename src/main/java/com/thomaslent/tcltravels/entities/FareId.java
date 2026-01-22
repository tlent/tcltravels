package com.thomaslent.tcltravels.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class FareId implements Serializable {
  @Column(name = "airline_id", length = 2)
  private String airlineId;

  @Column(name = "flight_number")
  private Integer flightNumber;

  @Column(name = "fare_type")
  private Integer fareType;

  @Column(name = "class")
  private String seatClass;

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

  public Integer getFareType() {
    return fareType;
  }

  public void setFareType(Integer fareType) {
    this.fareType = fareType;
  }

  public String getSeatClass() {
    return seatClass;
  }

  public void setSeatClass(String seatClass) {
    this.seatClass = seatClass;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((airlineId == null) ? 0 : airlineId.hashCode());
    result = prime * result + ((flightNumber == null) ? 0 : flightNumber.hashCode());
    result = prime * result + ((fareType == null) ? 0 : fareType.hashCode());
    result = prime * result + ((seatClass == null) ? 0 : seatClass.hashCode());
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
    FareId other = (FareId) obj;
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
    if (fareType == null) {
      if (other.fareType != null)
        return false;
    } else if (!fareType.equals(other.fareType))
      return false;
    if (seatClass == null) {
      if (other.seatClass != null)
        return false;
    } else if (!seatClass.equals(other.seatClass))
      return false;
    return true;
  }
}
