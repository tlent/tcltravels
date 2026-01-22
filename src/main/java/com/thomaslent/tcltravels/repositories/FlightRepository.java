package com.thomaslent.tcltravels.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thomaslent.tcltravels.dto.FlightRecommendationRow;
import com.thomaslent.tcltravels.entities.Flight;

public interface FlightRepository extends JpaRepository<Flight, Long> {
  List<Flight> findAllByOrderByAirline_IdAscFlightNumberAsc();

  List<Flight> findDistinctByStopsAt_Airport_IdOrderByAirline_IdAscFlightNumberAsc(
      String airportId);

  Optional<Flight> findByAirline_IdAndFlightNumber(String airlineId, Integer flightNumber);

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
      JOIN airports o ON o.id = f.origin_airport_id
      JOIN airports d ON d.id = f.destination_airport_id
      WHERE NOT EXISTS (
        SELECT 1
        FROM reservations r
        JOIN legs l ON l.reservation_id = r.id
        WHERE l.flight_id = f.id
          AND r.customer_id = :customerId
      )
      ORDER BY (
        SELECT COUNT(DISTINCT l2.reservation_id)
        FROM legs l2
        WHERE l2.flight_id = f.id
      ) DESC
      """, nativeQuery = true)
  List<FlightRecommendationRow> findRecommendedFlights(@Param("customerId") Long customerId);

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
        JOIN airports o ON o.id = f.origin_airport_id
        JOIN airports d ON d.id = f.destination_airport_id
        ORDER BY (
          SELECT COUNT(DISTINCT l2.reservation_id)
          FROM legs l2
          WHERE l2.flight_id = f.id
        ) DESC
      """, nativeQuery = true)
  List<FlightRecommendationRow> findBestSellingFlights();
}
