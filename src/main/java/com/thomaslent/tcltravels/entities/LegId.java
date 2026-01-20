package com.thomaslent.tcltravels.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class LegId implements Serializable {
  @Column(name = "reservation_number")
  private Long reservationNumber;

  @Column(name = "leg_number")
  private Integer legNumber;

  public Long getReservationNumber() {
    return reservationNumber;
  }

  public void setReservationNumber(Long reservationNumber) {
    this.reservationNumber = reservationNumber;
  }

  public Integer getLegNumber() {
    return legNumber;
  }

  public void setLegNumber(Integer legNumber) {
    this.legNumber = legNumber;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((reservationNumber == null) ? 0 : reservationNumber.hashCode());
    result = prime * result + ((legNumber == null) ? 0 : legNumber.hashCode());
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
    LegId other = (LegId) obj;
    if (reservationNumber == null) {
      if (other.reservationNumber != null)
        return false;
    } else if (!reservationNumber.equals(other.reservationNumber))
      return false;
    if (legNumber == null) {
      if (other.legNumber != null)
        return false;
    } else if (!legNumber.equals(other.legNumber))
      return false;
    return true;
  }

}
