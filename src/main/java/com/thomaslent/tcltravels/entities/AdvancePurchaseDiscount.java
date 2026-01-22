package com.thomaslent.tcltravels.entities;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "advance_purchase_discounts")
public class AdvancePurchaseDiscount {
  @EmbeddedId
  private AdvancePurchaseDiscountId id;

  @Column(name = "discount_rate")
  private BigDecimal discountRate;

  public AdvancePurchaseDiscountId getId() {
    return id;
  }

  public void setId(AdvancePurchaseDiscountId id) {
    this.id = id;
  }

  public BigDecimal getDiscountRate() {
    return discountRate;
  }

  public void setDiscountRate(BigDecimal discountRate) {
    this.discountRate = discountRate;
  }
}
