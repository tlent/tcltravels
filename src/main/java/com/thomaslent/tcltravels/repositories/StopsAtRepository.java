package com.thomaslent.tcltravels.repositories;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thomaslent.tcltravels.entities.StopsAt;

public interface StopsAtRepository extends JpaRepository<StopsAt, Long> {
    List<StopsAt> findByFlightAirline_IdAndFlightFlightNumberOrderByStopNumber(
            String airlineId, Integer flightNumber);

    StopsAt findFirstByFlightAirline_IdAndFlightFlightNumberAndStopNumberOrderByDepartureTimeAsc(
            String airlineId, Integer flightNumber, Integer stopNumber);

    StopsAt findTopByFlightAirline_IdAndFlightFlightNumberOrderByStopNumberDesc(
            String airlineId, Integer flightNumber);

    boolean existsByFlightAirline_IdAndFlightFlightNumberAndStopNumberAndDepartureTimeBetween(
            String airlineId, Integer flightNumber, Integer stopNumber,
            OffsetDateTime start, OffsetDateTime end);

    List<StopsAt> findByFlightAirline_IdAndAirport_IdAndDepartureTimeGreaterThanEqual(
            String airlineId,
            String airportId,
            OffsetDateTime after);

    List<StopsAt> findByFlightAirline_IdAndAirport_IdAndDepartureTimeBetween(
            String airlineId,
            String airportId,
            OffsetDateTime after,
            OffsetDateTime before);
}
