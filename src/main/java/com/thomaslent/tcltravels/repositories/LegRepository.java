package com.thomaslent.tcltravels.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thomaslent.tcltravels.dto.FlightActivityRow;
import com.thomaslent.tcltravels.dto.ReservationLegRow;
import com.thomaslent.tcltravels.entities.Leg;
import com.thomaslent.tcltravels.entities.LegId;

public interface LegRepository extends JpaRepository<Leg, LegId> {
  Leg findFirstByReservationReservationNumberOrderByIdLegNumberAsc(Long reservationNumber);

  List<Leg> findByReservationReservationNumberInAndIdLegNumber(
      List<Long> reservationNumbers, Integer legNumber);

  @Query("""
      select new com.thomaslent.tcltravels.dto.FlightActivityRow(
        f.id.airlineId,
        f.airline.name,
        f.id.flightNumber,
        f.numberOfSeats,
        f.daysOperating,
        count(distinct l.reservation.reservationNumber)
      )
      from Leg l
      join l.flight f
      group by f.id.airlineId, f.airline.name, f.id.flightNumber, f.numberOfSeats, f.daysOperating
      order by count(distinct l.reservation.reservationNumber) desc
      """)
  List<FlightActivityRow> findMostActiveFlights(Pageable pageable);

  @Query(value = """
        SELECT
          l.reservation_number AS reservationNumber,
          l.airline_id AS airlineId,
          l.flight_number AS flightNumber,
          os.airport_id AS originAirportId,
          o.name AS originName,
          o.city AS originCity,
          ds.airport_id AS destinationAirportId,
          d.name AS destinationName,
          d.city AS destinationCity,
          os.departure_time AS departureTime,
          ds.arrival_time AS arrivalTime
        FROM legs l
        JOIN stops_at os
          ON os.airline_id = l.airline_id
          AND os.flight_number = l.flight_number
          AND os.stop_number = l.from_stop_number
        JOIN airports o ON o.id = os.airport_id
        JOIN stops_at ds
          ON ds.airline_id = l.airline_id
          AND ds.flight_number = l.flight_number
          AND ds.stop_number = l.from_stop_number + 1
        JOIN airports d ON d.id = ds.airport_id
        WHERE l.reservation_number IN :reservationNumbers
        ORDER BY l.reservation_number, l.leg_number
      """, nativeQuery = true)
  public List<ReservationLegRow> getReservationLegs(@Param("reservationNumbers") List<Long> reservationNumbers);
}
