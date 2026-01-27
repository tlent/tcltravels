package com.thomaslent.tcltravels.dto;

import jakarta.validation.constraints.NotBlank;

public class PassengerInfo {
  @NotBlank(message = "First name is required.")
  private String firstName;

  @NotBlank(message = "Last name is required.")
  private String lastName;

  @NotBlank(message = "Seat class is required.")
  private String seatClass;

  private String meal;

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getSeatClass() {
    return seatClass;
  }

  public void setSeatClass(String seatClass) {
    this.seatClass = seatClass;
  }

  public String getMeal() {
    return meal;
  }

  public void setMeal(String meal) {
    this.meal = meal;
  }
}
