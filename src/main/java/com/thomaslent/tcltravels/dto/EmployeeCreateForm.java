package com.thomaslent.tcltravels.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class EmployeeCreateForm {
  @NotBlank(message = "First name is required")
  @Pattern(regexp = "^[^\\d]{2,}$", message = "First name must be at least 2 letters")
  private String firstName;

  @NotBlank(message = "Last name is required")
  @Pattern(regexp = "^[^\\d]{2,}$", message = "Last name must be at least 2 letters")
  private String lastName;

  @NotBlank(message = "Address is required")
  @Size(min = 6)
  private String address;

  @NotBlank(message = "City is required")
  @Size(min = 3)
  private String city;

  @NotBlank(message = "State is required")
  @Size(min = 2, max = 2, message = "State must be exactly 2 characters (e.g. NY)")
  private String state;

  @Pattern(regexp = "^\\d{5}$", message = "ZIP must be exactly 5 digits")
  private String zipcode;

  @Pattern(regexp = "^\\d{10,11}$", message = "Telephone must be 10 or 11 digits")
  private String telephone;

  @Pattern(regexp = "^\\d{9}$", message = "SSN must be 9 digits")
  private String ssn;

  @Pattern(regexp = "^(0[1-9]|1[0-2])/(0[1-9]|[12]\\d|3[01])/\\d{4}$",
      message = "Start date must be MM/DD/YYYY")
  private String startDate;

  @Pattern(regexp = "^[1-9]\\d*(\\.\\d\\d)$", message = "Hourly rate must be a number with two decimals")
  private String hourlyRate;

  private boolean isManager;

  @Email(message = "Please enter a valid email")
  @NotBlank(message = "Email is required")
  private String email;

  @NotBlank(message = "Password is required")
  @Size(min = 6, message = "Password must be at least 6 characters")
  private String password;

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

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getZipcode() {
    return zipcode;
  }

  public void setZipcode(String zipcode) {
    this.zipcode = zipcode;
  }

  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  public String getSsn() {
    return ssn;
  }

  public void setSsn(String ssn) {
    this.ssn = ssn;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getHourlyRate() {
    return hourlyRate;
  }

  public void setHourlyRate(String hourlyRate) {
    this.hourlyRate = hourlyRate;
  }

  public boolean getIsManager() {
    return isManager;
  }

  public void setIsManager(boolean isManager) {
    this.isManager = isManager;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
