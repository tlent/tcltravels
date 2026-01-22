package com.thomaslent.tcltravels.entities;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "fares")
public class Fare {
  @EmbeddedId
  private FareId id;

  @Column(name = "fare", nullable = false)
  private BigDecimal fare;

  public FareId getId() {
    return id;
  }

  public void setId(FareId id) {
    this.id = id;
  }

  public BigDecimal getFare() {
    return fare;
  }

  public void setFare(BigDecimal fare) {
    this.fare = fare;
  }
}
