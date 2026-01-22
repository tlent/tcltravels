package com.thomaslent.tcltravels.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservation_passengers")
public class ReservationPassenger {
  @EmbeddedId
  private ReservationPassengerId id;

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
