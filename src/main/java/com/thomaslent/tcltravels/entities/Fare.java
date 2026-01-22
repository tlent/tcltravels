package com.thomaslent.tcltravels.entities;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "fares")
public class Fare {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "flight_id", referencedColumnName = "id")
  private Flight flight;

  @Column(name = "fare_type", nullable = false)
  private Integer fareType;

  @Column(name = "class", nullable = false)
  private String seatClass;

  @Column(name = "fare", nullable = false)
  private BigDecimal fare;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Flight getFlight() {
    return flight;
  }

  public void setFlight(Flight flight) {
    this.flight = flight;
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

  public BigDecimal getFare() {
    return fare;
  }

  public void setFare(BigDecimal fare) {
    this.fare = fare;
  }
}
