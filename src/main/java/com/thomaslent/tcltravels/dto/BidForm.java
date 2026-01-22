package com.thomaslent.tcltravels.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BidForm {
  @NotBlank(message = "Airline is required.")
  private String airlineId;

  @NotNull(message = "Flight number is required.")
  private Integer flightNumber;

  @NotNull(message = "You must enter a departure date.")
  @DateTimeFormat(iso = ISO.DATE)
  private LocalDate departureDate;

  @NotBlank(message = "Class of flight is required.")
  private String flightClass;

  private String food;

  private String other;

  @NotNull(message = "You must enter a bid value.")
  @DecimalMin(value = "0.01", message = "Bid must be greater than 0.")
  private BigDecimal bid;

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

  public LocalDate getDepartureDate() {
    return departureDate;
  }

  public void setDepartureDate(LocalDate departureDate) {
    this.departureDate = departureDate;
  }

  public String getFlightClass() {
    return flightClass;
  }

  public void setFlightClass(String flightClass) {
    this.flightClass = flightClass;
  }

  public String getFood() {
    return food;
  }

  public void setFood(String food) {
    this.food = food;
  }

  public String getOther() {
    return other;
  }

  public void setOther(String other) {
    this.other = other;
  }

  public BigDecimal getBid() {
    return bid;
  }

  public void setBid(BigDecimal bid) {
    this.bid = bid;
  }
}
