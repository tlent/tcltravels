package com.thomaslent.tcltravels.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.thomaslent.tcltravels.entities.Fare;

public interface FareRepository extends JpaRepository<Fare, Long> {
  Optional<Fare> findByFlightAirline_IdAndFlightFlightNumberAndFareTypeAndSeatClass(
      String airlineId,
      Integer flightNumber,
      Integer fareType,
      String seatClass);
}
