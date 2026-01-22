package com.thomaslent.tcltravels.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thomaslent.tcltravels.entities.Fare;
import com.thomaslent.tcltravels.entities.FareId;

public interface FareRepository extends JpaRepository<Fare, FareId> {
  Optional<Fare> findByIdAirlineIdAndIdFlightNumberAndIdFareTypeAndIdSeatClass(
      String airlineId, Integer flightNumber, Integer fareType, String seatClass);
}
