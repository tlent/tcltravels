package com.thomaslent.tcltravels.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegistrationDto {
  @NotBlank(message = "First name is required")
  @Pattern(regexp = "^[a-zA-Z]+$", message = "First name must contain only letters")
  private String firstName;

  @NotBlank(message = "Last name is required")
  @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name must contain only letters")
  private String lastName;

  @Email(message = "Please enter a valid email")
  @NotBlank(message = "Email is required")
  private String email;

  @Size(min = 6, message = "Password must be at least 6 characters")
  private String password;

  @Pattern(regexp = "^\\d{5}$", message = "ZIP must be exactly 5 digits")
  private String zipcode;

  @Pattern(regexp = "^\\d{16}$", message = "Credit card must be 16 digits")
  private String creditCard;

  @NotBlank(message = "Address is required")
  private String address;

  @NotBlank(message = "City is required")
  private String city;

  @NotBlank(message = "State is required")
  @Size(min = 2, max = 2, message = "State must be exactly 2 characters (e.g. NY)")
  private String state;

  @Pattern(regexp = "^\\d{10,11}$", message = "Telephone must be 10 or 11 digits")
  private String telephone;

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

  public String getCreditCard() {
    return creditCard;
  }

  public void setCreditCard(String creditCard) {
    this.creditCard = creditCard;
  }

  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
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
