package com.thomaslent.tcltravels.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.thomaslent.tcltravels.dto.FlightRecommendationRow;
import com.thomaslent.tcltravels.entities.Flight;
import com.thomaslent.tcltravels.entities.FlightId;

public interface FlightRepository extends JpaRepository<Flight, FlightId> {
  @Query(value = """
      SELECT f.airline_id AS airlineId,
              f.flight_number AS flightNumber,
              f.number_of_seats AS numberOfSeats,
              f.days_operating AS daysOperating,
              o.id AS originId,
              o.city AS originCity,
              d.id AS destinationId,
              d.city AS destinationCity
      FROM flights f
      JOIN stops_at os
        ON os.airline_id = f.airline_id AND os.flight_number = f.flight_number
      JOIN airports o ON o.id = os.airport_id
      JOIN stops_at ds
        ON ds.airline_id = f.airline_id AND ds.flight_number = f.flight_number
      JOIN airports d ON d.id = ds.airport_id
      WHERE os.stop_number = 1
        AND ds.stop_number = (
          SELECT MAX(s2.stop_number)
          FROM stops_at s2
          WHERE s2.airline_id = f.airline_id AND s2.flight_number = f.flight_number
        )
        AND NOT EXISTS (
          SELECT 1
          FROM reservations r
          JOIN legs l ON l.reservation_number = r.reservation_number
          WHERE l.flight_number = f.flight_number
            AND l.airline_id = f.airline_id
            AND r.account_number = :accountNumber
        )
      ORDER BY (
        SELECT COUNT(DISTINCT l2.reservation_number)
        FROM legs l2
        WHERE l2.flight_number = f.flight_number
          AND l2.airline_id = f.airline_id
      ) DESC
      """, nativeQuery = true)
  List<FlightRecommendationRow> findRecommendedFlights(@Param("accountNumber") Long accountNumber);
}
