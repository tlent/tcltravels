package com.thomaslent.tcltravels.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservation_passengers")
public class ReservationPassenger {
  @EmbeddedId
  private ReservationPassengerId id;

  @ManyToOne
  @JoinColumn(name = "reservation_number", referencedColumnName = "reservation_number", insertable = false, updatable = false)
  private Reservation reservation;

  @ManyToOne
  @JoinColumn(name = "passenger_id", referencedColumnName = "id", insertable = false, updatable = false)
  private Passenger passenger;

  @Column(name = "seat_number")
  private Integer seatNumber;

  @Column(name = "class")
  private String seatClass;

  @Column(name = "meal")
  private String meal;

  public ReservationPassengerId getId() {
    return id;
  }

  public void setId(ReservationPassengerId id) {
    this.id = id;
  }

  public Reservation getReservation() {
    return reservation;
  }

  public void setReservation(Reservation reservation) {
    this.reservation = reservation;
  }

  public Passenger getPassenger() {
    return passenger;
  }

  public void setPassenger(Passenger passenger) {
    this.passenger = passenger;
  }

  public Integer getSeatNumber() {
    return seatNumber;
  }

  public void setSeatNumber(Integer seatNumber) {
    this.seatNumber = seatNumber;
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
