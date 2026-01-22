package com.thomaslent.tcltravels.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thomaslent.tcltravels.entities.ReservationPassenger;
import com.thomaslent.tcltravels.entities.ReservationPassengerId;

public interface ReservationPassengerRepository
    extends JpaRepository<ReservationPassenger, ReservationPassengerId> {
  @Query("""
      select count(rp)
      from ReservationPassenger rp
      join Leg l on l.id.reservationNumber = rp.id.reservationNumber
      where l.flight.id.airlineId = :airlineId
        and l.flight.id.flightNumber = :flightNumber
      """)
  long countSeatsForFlight(@Param("airlineId") String airlineId, @Param("flightNumber") Integer flightNumber);
}
