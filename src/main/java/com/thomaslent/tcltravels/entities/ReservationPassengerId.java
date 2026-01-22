package com.thomaslent.tcltravels.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ReservationPassengerId implements Serializable {
  @Column(name = "reservation_number")
  private Long reservationNumber;

  @Column(name = "passenger_id")
  private Long passengerId;

  @Column(name = "account_number")
  private Long accountNumber;

  public Long getReservationNumber() {
    return reservationNumber;
  }

  public void setReservationNumber(Long reservationNumber) {
    this.reservationNumber = reservationNumber;
  }

  public Long getPassengerId() {
    return passengerId;
  }

  public void setPassengerId(Long passengerId) {
    this.passengerId = passengerId;
  }

  public Long getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(Long accountNumber) {
    this.accountNumber = accountNumber;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((reservationNumber == null) ? 0 : reservationNumber.hashCode());
    result = prime * result + ((passengerId == null) ? 0 : passengerId.hashCode());
    result = prime * result + ((accountNumber == null) ? 0 : accountNumber.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ReservationPassengerId other = (ReservationPassengerId) obj;
    if (reservationNumber == null) {
      if (other.reservationNumber != null)
        return false;
    } else if (!reservationNumber.equals(other.reservationNumber))
      return false;
    if (passengerId == null) {
      if (other.passengerId != null)
        return false;
    } else if (!passengerId.equals(other.passengerId))
      return false;
    if (accountNumber == null) {
      if (other.accountNumber != null)
        return false;
    } else if (!accountNumber.equals(other.accountNumber))
      return false;
    return true;
  }
}
