package com.thomaslent.tcltravels.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer_preferences")
public class CustomerPreference {
  @EmbeddedId
  private CustomerPreferenceId id;

  public CustomerPreferenceId getId() {
    return id;
  }

  public void setId(CustomerPreferenceId id) {
    this.id = id;
  }
}
