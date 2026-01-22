package com.thomaslent.tcltravels.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thomaslent.tcltravels.entities.CustomerPreference;

public interface CustomerPreferenceRepository extends JpaRepository<CustomerPreference, Long> {
  boolean existsByCustomerIdAndPreference(Long customerId, String preference);
}
