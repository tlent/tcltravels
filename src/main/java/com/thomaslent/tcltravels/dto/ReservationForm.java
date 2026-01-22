package com.thomaslent.tcltravels.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReservationForm {
  @NotBlank(message = "Airline is required.")
  private String airlineId;

  @NotNull(message = "Flight number is required.")
  private Integer flightNumber;

  @NotNull(message = "Origin is required.")
  private Integer origin;

  @NotNull(message = "Destination is required.")
  private Integer destination;

  @NotNull(message = "Passenger count is required.")
  @Min(value = 1, message = "Passenger count must be at least 1.")
  @Max(value = 5, message = "Passenger count must be at most 5.")
  private Integer passengerCount;

  private String other;

  @NotNull(message = "You must enter a departure date.")
  @DateTimeFormat(iso = ISO.DATE)
  private LocalDate departureDate;

  private String first1;
  private String last1;
  private String class1;
  private String food1;

  private String first2;
  private String last2;
  private String class2;
  private String food2;

  private String first3;
  private String last3;
  private String class3;
  private String food3;

  private String first4;
  private String last4;
  private String class4;
  private String food4;

  private String first5;
  private String last5;
  private String class5;
  private String food5;

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

  public Integer getOrigin() {
    return origin;
  }

  public void setOrigin(Integer origin) {
    this.origin = origin;
  }

  public Integer getDestination() {
    return destination;
  }

  public void setDestination(Integer destination) {
    this.destination = destination;
  }

  public Integer getPassengerCount() {
    return passengerCount;
  }

  public void setPassengerCount(Integer passengerCount) {
    this.passengerCount = passengerCount;
  }

  public String getOther() {
    return other;
  }

  public void setOther(String other) {
    this.other = other;
  }

  public LocalDate getDepartureDate() {
    return departureDate;
  }

  public void setDepartureDate(LocalDate departureDate) {
    this.departureDate = departureDate;
  }

  public String getFirst1() {
    return first1;
  }

  public void setFirst1(String first1) {
    this.first1 = first1;
  }

  public String getLast1() {
    return last1;
  }

  public void setLast1(String last1) {
    this.last1 = last1;
  }

  public String getClass1() {
    return class1;
  }

  public void setClass1(String class1) {
    this.class1 = class1;
  }

  public String getFood1() {
    return food1;
  }

  public void setFood1(String food1) {
    this.food1 = food1;
  }

  public String getFirst2() {
    return first2;
  }

  public void setFirst2(String first2) {
    this.first2 = first2;
  }

  public String getLast2() {
    return last2;
  }

  public void setLast2(String last2) {
    this.last2 = last2;
  }

  public String getClass2() {
    return class2;
  }

  public void setClass2(String class2) {
    this.class2 = class2;
  }

  public String getFood2() {
    return food2;
  }

  public void setFood2(String food2) {
    this.food2 = food2;
  }

  public String getFirst3() {
    return first3;
  }

  public void setFirst3(String first3) {
    this.first3 = first3;
  }

  public String getLast3() {
    return last3;
  }

  public void setLast3(String last3) {
    this.last3 = last3;
  }

  public String getClass3() {
    return class3;
  }

  public void setClass3(String class3) {
    this.class3 = class3;
  }

  public String getFood3() {
    return food3;
  }

  public void setFood3(String food3) {
    this.food3 = food3;
  }

  public String getFirst4() {
    return first4;
  }

  public void setFirst4(String first4) {
    this.first4 = first4;
  }

  public String getLast4() {
    return last4;
  }

  public void setLast4(String last4) {
    this.last4 = last4;
  }

  public String getClass4() {
    return class4;
  }

  public void setClass4(String class4) {
    this.class4 = class4;
  }

  public String getFood4() {
    return food4;
  }

  public void setFood4(String food4) {
    this.food4 = food4;
  }

  public String getFirst5() {
    return first5;
  }

  public void setFirst5(String first5) {
    this.first5 = first5;
  }

  public String getLast5() {
    return last5;
  }

  public void setLast5(String last5) {
    this.last5 = last5;
  }

  public String getClass5() {
    return class5;
  }

  public void setClass5(String class5) {
    this.class5 = class5;
  }

  public String getFood5() {
    return food5;
  }

  public void setFood5(String food5) {
    this.food5 = food5;
  }

  public String getFirstName(int index) {
    switch (index) {
      case 1:
        return first1;
      case 2:
        return first2;
      case 3:
        return first3;
      case 4:
        return first4;
      case 5:
        return first5;
      default:
        return null;
    }
  }

  public String getLastName(int index) {
    switch (index) {
      case 1:
        return last1;
      case 2:
        return last2;
      case 3:
        return last3;
      case 4:
        return last4;
      case 5:
        return last5;
      default:
        return null;
    }
  }

  public String getFlightClass(int index) {
    switch (index) {
      case 1:
        return class1;
      case 2:
        return class2;
      case 3:
        return class3;
      case 4:
        return class4;
      case 5:
        return class5;
      default:
        return null;
    }
  }

  public String getFood(int index) {
    switch (index) {
      case 1:
        return food1;
      case 2:
        return food2;
      case 3:
        return food3;
      case 4:
        return food4;
      case 5:
        return food5;
      default:
        return null;
    }
  }
}
