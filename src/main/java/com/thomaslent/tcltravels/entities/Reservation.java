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
  private Long id;

  @Column(name = "reservation_date")
  private OffsetDateTime reservationDate;

  @Column(name = "booking_fee")
  private BigDecimal bookingFee;

  @Column(name = "total_fare")
  private BigDecimal totalFare;

  @ManyToOne
  @JoinColumn(name = "employee_id", referencedColumnName = "id")
  private Employee employee;

  @ManyToOne
  @JoinColumn(name = "customer_id", referencedColumnName = "id")
  private Customer customer;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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
