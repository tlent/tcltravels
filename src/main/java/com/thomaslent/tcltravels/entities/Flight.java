package com.thomaslent.tcltravels.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "flights")
public class Flight {
  @EmbeddedId
  private FlightId id;

  @ManyToOne
  @MapsId("airlineId")
  @JoinColumn(name = "airline_id", referencedColumnName = "id")
  private Airline airline;

  @Column(name = "number_of_seats", nullable = false)
  private Integer numberOfSeats;

  @Column(name = "days_operating", nullable = false)
  private String daysOperating;

  @Column(name = "min_length_stay")
  private Integer minLengthStay;

  @Column(name = "max_length_stay")
  private Integer maxLengthStay;

  public Integer getNumberOfSeats() {
    return numberOfSeats;
  }

  public void setNumberOfSeats(Integer numberOfSeats) {
    this.numberOfSeats = numberOfSeats;
  }

  public String getDaysOperating() {
    return daysOperating;
  }

  public void setDaysOperating(String daysOperating) {
    this.daysOperating = daysOperating;
  }

  public Integer getMinLengthStay() {
    return minLengthStay;
  }

  public void setMinLengthStay(Integer minLengthStay) {
    this.minLengthStay = minLengthStay;
  }

  public Integer getMaxLengthStay() {
    return maxLengthStay;
  }

  public void setMaxLengthStay(Integer maxLengthStay) {
    this.maxLengthStay = maxLengthStay;
  }

  public Airline getAirline() {
    return airline;
  }

  public void setAirline(Airline airline) {
    this.airline = airline;
  }

  public FlightId getId() {
    return id;
  }

  public void setId(FlightId id) {
    this.id = id;
  }

}
