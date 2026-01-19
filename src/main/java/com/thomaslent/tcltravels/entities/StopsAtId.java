package com.thomaslent.tcltravels.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

@Embeddable
public class StopsAtId implements Serializable {
  @Embedded
  private FlightId flightId;

  @Column(name = "stop_number")
  private Integer stopNumber;

  public FlightId getFlightId() {
    return flightId;
  }

  public void setFlightId(FlightId flightId) {
    this.flightId = flightId;
  }

  public Integer getStopNumber() {
    return stopNumber;
  }

  public void setStopNumber(Integer stopNumber) {
    this.stopNumber = stopNumber;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((flightId == null) ? 0 : flightId.hashCode());
    result = prime * result + ((stopNumber == null) ? 0 : stopNumber.hashCode());
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
    StopsAtId other = (StopsAtId) obj;
    if (flightId == null) {
      if (other.flightId != null)
        return false;
    } else if (!flightId.equals(other.flightId))
      return false;
    if (stopNumber == null) {
      if (other.stopNumber != null)
        return false;
    } else if (!stopNumber.equals(other.stopNumber))
      return false;
    return true;
  }

}
