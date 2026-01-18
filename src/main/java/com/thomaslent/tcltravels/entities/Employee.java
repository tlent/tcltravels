package com.thomaslent.tcltravels.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "employees")
public class Employee {
  @Id
  @Column(nullable = false)
  private Integer ssn;

  @Column(name = "is_manager")
  private Boolean isManager;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "hourly_rate")
  private BigDecimal hourlyRate;

  @OneToOne
  @JoinColumn(name = "id", referencedColumnName = "id")
  private Person person;

  public boolean isManager() {
    return isManager;
  }
}
