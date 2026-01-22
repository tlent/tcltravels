package com.thomaslent.tcltravels.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "auctions")
public class Auction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "class")
  private String seatingClass;

  private LocalDate date;

  @Column(name = "name_your_own_price", nullable = false)
  private BigDecimal nameYourOwnPrice;

  private Boolean accepted;

  @ManyToOne
  @JoinColumn(name = "customer_id", referencedColumnName = "id")
  private Customer customer;

  @ManyToOne
  @JoinColumn(name = "flight_id", referencedColumnName = "id")
  private Flight flight;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSeatingClass() {
    return seatingClass;
  }

  public void setSeatingClass(String seatingClass) {
    this.seatingClass = seatingClass;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public BigDecimal getNameYourOwnPrice() {
    return nameYourOwnPrice;
  }

  public void setNameYourOwnPrice(BigDecimal nameYourOwnPrice) {
    this.nameYourOwnPrice = nameYourOwnPrice;
  }

  public Boolean getAccepted() {
    return accepted;
  }

  public void setAccepted(Boolean accepted) {
    this.accepted = accepted;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public Flight getFlight() {
    return flight;
  }

  public void setFlight(Flight flight) {
    this.flight = flight;
  }

}
