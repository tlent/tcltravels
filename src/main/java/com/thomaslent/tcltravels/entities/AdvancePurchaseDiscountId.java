package com.thomaslent.tcltravels.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class AdvancePurchaseDiscountId implements Serializable {
  @Column(name = "airline_id", length = 2)
  private String airlineId;

  @Column(name = "days")
  private Integer days;

  public String getAirlineId() {
    return airlineId;
  }

  public void setAirlineId(String airlineId) {
    this.airlineId = airlineId;
  }

  public Integer getDays() {
    return days;
  }

  public void setDays(Integer days) {
    this.days = days;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((airlineId == null) ? 0 : airlineId.hashCode());
    result = prime * result + ((days == null) ? 0 : days.hashCode());
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
    AdvancePurchaseDiscountId other = (AdvancePurchaseDiscountId) obj;
    if (airlineId == null) {
      if (other.airlineId != null)
        return false;
    } else if (!airlineId.equals(other.airlineId))
      return false;
    if (days == null) {
      if (other.days != null)
        return false;
    } else if (!days.equals(other.days))
      return false;
    return true;
  }
}
