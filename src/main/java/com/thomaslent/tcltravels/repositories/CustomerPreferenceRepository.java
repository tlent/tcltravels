package com.thomaslent.tcltravels.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thomaslent.tcltravels.entities.CustomerPreference;
import com.thomaslent.tcltravels.entities.CustomerPreferenceId;

public interface CustomerPreferenceRepository extends JpaRepository<CustomerPreference, CustomerPreferenceId> {
  boolean existsByIdAccountNumberAndIdPreference(Long accountNumber, String preference);
}
