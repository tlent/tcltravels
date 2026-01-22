package com.thomaslent.tcltravels.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thomaslent.tcltravels.dto.CustomerOnFlightView;
import com.thomaslent.tcltravels.entities.ReservationPassenger;
import com.thomaslent.tcltravels.entities.ReservationPassengerId;

public interface ReservationPassengerRepository extends JpaRepository<ReservationPassenger, ReservationPassengerId> {
  @Query("""
      select count(rp)
      from ReservationPassenger rp
      join rp.reservation r
      join Leg l on l.reservation = r
      where l.flight.id.airlineId = :airlineId
      and l.flight.id.flightNumber = :flightNumber
      """)
  long countSeatsForFlight(@Param("airlineId") String airlineId,
      @Param("flightNumber") Integer flightNumber);

  @Query("""
      select distinct new com.thomaslent.tcltravels.dto.CustomerOnFlightView(
        r.reservationNumber,
        c.accountNumber,
        p.passengerName,
        rp.seatClass,
        (
          select count(rp2)
          from ReservationPassenger rp2
          where rp2.id.reservationNumber = r.reservationNumber
        ),
        r.reservationDate
      )
      from ReservationPassenger rp
      join rp.reservation r
      join rp.passenger p
      join p.customer c
      join Leg l on l.reservation = r
      where l.flight.id.airlineId = :airlineId
      and l.flight.id.flightNumber = :flightNumber
      order by r.reservationDate desc
      """)
  List<CustomerOnFlightView> findCustomersOnFlight(
      @Param("airlineId") String airlineId,
      @Param("flightNumber") Integer flightNumber);
}
