package com.thomaslent.tcltravels.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thomaslent.tcltravels.entities.AdvancePurchaseDiscount;
import com.thomaslent.tcltravels.entities.AdvancePurchaseDiscountId;

public interface AdvancePurchaseDiscountRepository
    extends JpaRepository<AdvancePurchaseDiscount, AdvancePurchaseDiscountId> {
  Optional<AdvancePurchaseDiscount> findTopByIdAirlineIdAndIdDaysLessThanEqualOrderByIdDaysDesc(
      String airlineId, Integer days);
}
