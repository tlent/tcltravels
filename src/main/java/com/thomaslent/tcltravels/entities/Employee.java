package com.thomaslent.tcltravels.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "employees")
public class Employee {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "account_id", referencedColumnName = "id")
  private User account;

  @Column(nullable = false)
  private Integer ssn;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "hourly_rate")
  private BigDecimal hourlyRate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getAccount() {
    return account;
  }

  public void setAccount(User account) {
    this.account = account;
  }

  public Integer getSsn() {
    return ssn;
  }

  public void setSsn(Integer ssn) {
    this.ssn = ssn;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public BigDecimal getHourlyRate() {
    return hourlyRate;
  }

  public void setHourlyRate(BigDecimal hourlyRate) {
    this.hourlyRate = hourlyRate;
  }

}
