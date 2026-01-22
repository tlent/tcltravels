package com.thomaslent.tcltravels.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thomaslent.tcltravels.dto.FlightActivityRow;
import com.thomaslent.tcltravels.dto.ReservationLegRow;
import com.thomaslent.tcltravels.entities.Leg;

public interface LegRepository extends JpaRepository<Leg, Long> {
  Leg findFirstByReservationIdOrderByLegNumberAsc(Long reservationId);

  List<Leg> findByReservationIdInAndLegNumber(
      List<Long> reservationIds, Integer legNumber);

  @Query("""
      select new com.thomaslent.tcltravels.dto.FlightActivityRow(
        f.airline.id,
        f.airline.name,
        f.flightNumber,
        f.numberOfSeats,
        f.daysOperating,
        count(distinct l.reservation.id)
      )
      from Leg l
      join l.flight f
      group by f.airline.id, f.airline.name, f.flightNumber, f.numberOfSeats, f.daysOperating
      order by count(distinct l.reservation.id) desc
      """)
  List<FlightActivityRow> findMostActiveFlights(Pageable pageable);

  @Query(value = """
        SELECT
          l.reservation_id AS reservationNumber,
          f.airline_id AS airlineId,
          f.flight_number AS flightNumber,
          os.airport_id AS originAirportId,
          o.name AS originName,
          o.city AS originCity,
          ds.airport_id AS destinationAirportId,
          d.name AS destinationName,
          d.city AS destinationCity,
          os.departure_time AS departureTime,
          ds.arrival_time AS arrivalTime
        FROM legs l
        JOIN flights f ON f.id = l.flight_id
        JOIN stops_at os
          ON os.flight_id = l.flight_id
          AND os.stop_number = l.from_stop_number
        JOIN airports o ON o.id = os.airport_id
        JOIN stops_at ds
          ON ds.flight_id = l.flight_id
          AND ds.stop_number = l.from_stop_number + 1
        JOIN airports d ON d.id = ds.airport_id
        WHERE l.reservation_id IN :reservationIds
        ORDER BY l.reservation_id, l.leg_number
      """, nativeQuery = true)
  public List<ReservationLegRow> getReservationLegs(@Param("reservationIds") List<Long> reservationIds);
}
