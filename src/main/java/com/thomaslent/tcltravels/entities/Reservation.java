package com.thomaslent.tcltravels.entities;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservations")
public class Reservation {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "reservation_number")
  private Long reservationNumber;

  @Column(name = "reservation_date")
  private OffsetDateTime reservationDate;

  @Column(name = "booking_fee")
  private BigDecimal bookingFee;

  @Column(name = "total_fare")
  private BigDecimal totalFare;

  @ManyToOne
  @JoinColumn(name = "employee_ssn", referencedColumnName = "ssn")
  private Employee employee;

  @ManyToOne
  @JoinColumn(name = "account_number", referencedColumnName = "account_number")
  private Customer customer;

  public Long getReservationNumber() {
    return reservationNumber;
  }

  public void setReservationNumber(Long reservationNumber) {
    this.reservationNumber = reservationNumber;
  }

  public OffsetDateTime getReservationDate() {
    return reservationDate;
  }

  public void setReservationDate(OffsetDateTime reservationDate) {
    this.reservationDate = reservationDate;
  }

  public BigDecimal getBookingFee() {
    return bookingFee;
  }

  public void setBookingFee(BigDecimal bookingFee) {
    this.bookingFee = bookingFee;
  }

  public BigDecimal getTotalFare() {
    return totalFare;
  }

  public void setTotalFare(BigDecimal totalFare) {
    this.totalFare = totalFare;
  }

  public Employee getEmployee() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

}
