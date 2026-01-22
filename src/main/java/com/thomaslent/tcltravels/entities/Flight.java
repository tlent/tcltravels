package com.thomaslent.tcltravels.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.List;

import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "flights")
public class Flight {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "airline_id", referencedColumnName = "id")
  private Airline airline;

  @Column(name = "flight_number", nullable = false)
  private Integer flightNumber;

  @Column(name = "number_of_seats", nullable = false)
  private Integer numberOfSeats;

  @Column(name = "days_operating", nullable = false)
  private String daysOperating;

  @Column(name = "min_length_stay")
  private Integer minLengthStay;

  @Column(name = "max_length_stay")
  private Integer maxLengthStay;

  @ManyToOne
  @JoinColumn(name = "origin_airport_id", referencedColumnName = "id")
  private Airport originAirport;

  @ManyToOne
  @JoinColumn(name = "destination_airport_id", referencedColumnName = "id")
  private Airport destinationAirport;

  @OneToMany(mappedBy = "flight")
  private List<StopsAt> stopsAt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Airline getAirline() {
    return airline;
  }

  public void setAirline(Airline airline) {
    this.airline = airline;
  }

  public Integer getFlightNumber() {
    return flightNumber;
  }

  public void setFlightNumber(Integer flightNumber) {
    this.flightNumber = flightNumber;
  }

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

  public Airport getOriginAirport() {
    return originAirport;
  }

  public void setOriginAirport(Airport originAirport) {
    this.originAirport = originAirport;
  }

  public Airport getDestinationAirport() {
    return destinationAirport;
  }

  public void setDestinationAirport(Airport destinationAirport) {
    this.destinationAirport = destinationAirport;
  }

  public List<StopsAt> getStopsAt() {
    return stopsAt;
  }

  public void setStopsAt(List<StopsAt> stopsAt) {
    this.stopsAt = stopsAt;
  }
}
