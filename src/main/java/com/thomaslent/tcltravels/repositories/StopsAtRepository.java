package com.thomaslent.tcltravels.repositories;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thomaslent.tcltravels.entities.StopsAt;

public interface StopsAtRepository extends JpaRepository<StopsAt, Long> {
    List<StopsAt> findByFlight_Airline_IdAndFlight_FlightNumberOrderByStopNumber(
            String airlineId, Integer flightNumber);

    StopsAt findFirstByFlight_Airline_IdAndFlight_FlightNumberAndStopNumberOrderByDepartureTimeAsc(
            String airlineId, Integer flightNumber, Integer stopNumber);

    StopsAt findTopByFlight_Airline_IdAndFlight_FlightNumberOrderByStopNumberDesc(
            String airlineId, Integer flightNumber);

    boolean existsByFlight_Airline_IdAndFlight_FlightNumberAndStopNumberAndDepartureTimeBetween(
            String airlineId, Integer flightNumber, Integer stopNumber,
            OffsetDateTime start, OffsetDateTime end);

    List<StopsAt> findByFlight_Airline_IdAndAirport_IdAndDepartureTimeGreaterThanEqual(
            String airlineId,
            String airportId,
            OffsetDateTime after);

    List<StopsAt> findByFlight_Airline_IdAndAirport_IdAndDepartureTimeBetween(
            String airlineId,
            String airportId,
            OffsetDateTime after,
            OffsetDateTime before);
}
