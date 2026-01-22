package com.thomaslent.tcltravels.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "legs")
public class Leg {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "reservation_id", referencedColumnName = "id")
  private Reservation reservation;

  @Column(name = "leg_number")
  private Integer legNumber;

  @ManyToOne
  @JoinColumn(name = "flight_id", referencedColumnName = "id")
  private Flight flight;

  @Column(name = "from_stop_number")
  private Integer fromStopNumber;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Reservation getReservation() {
    return reservation;
  }

  public void setReservation(Reservation reservation) {
    this.reservation = reservation;
  }

  public Integer getLegNumber() {
    return legNumber;
  }

  public void setLegNumber(Integer legNumber) {
    this.legNumber = legNumber;
  }

  public Flight getFlight() {
    return flight;
  }

  public void setFlight(Flight flight) {
    this.flight = flight;
  }

  public Integer getFromStopNumber() {
    return fromStopNumber;
  }

  public void setFromStopNumber(Integer fromStopNumber) {
    this.fromStopNumber = fromStopNumber;
  }
}
