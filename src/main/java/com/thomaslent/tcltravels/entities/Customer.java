package com.thomaslent.tcltravels.entities;

import java.time.OffsetDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "account_number")
  private Long accountNumber;

  @Column(name = "credit_card_number", nullable = false, length = 16)
  private String creditCardNumber;

  @Column(nullable = false)
  private String email;

  @Column(name = "creation_date")
  private OffsetDateTime creationDate;

  private Integer rating;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "id", referencedColumnName = "id")
  private Person person;

  @PrePersist
  protected void onCreate() {
    this.creationDate = OffsetDateTime.now();
  }

  public Long getAccountNumber() {
    return accountNumber;
  }
}
