package com.thomaslent.tcltravels.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.thomaslent.tcltravels.entities.AdvancePurchaseDiscount;

public interface AdvancePurchaseDiscountRepository
    extends JpaRepository<AdvancePurchaseDiscount, Long> {
  Optional<AdvancePurchaseDiscount> findTopByAirline_IdAndDaysLessThanEqualOrderByDaysDesc(
      String airlineId,
      Integer days);
}
