package com.thomaslent.tcltravels.repositories;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thomaslent.tcltravels.entities.StopsAt;
import com.thomaslent.tcltravels.entities.StopsAtId;

public interface StopsAtRepository extends JpaRepository<StopsAt, StopsAtId> {
    List<StopsAt> findByIdFlightIdAirlineIdAndIdFlightIdFlightNumberOrderByIdStopNumber(
            String airlineId, Integer flightNumber);

    StopsAt findFirstByIdFlightIdAirlineIdAndIdFlightIdFlightNumberAndIdStopNumberOrderByDepartureTimeAsc(
            String airlineId, Integer flightNumber, Integer stopNumber);

    StopsAt findTopByIdFlightIdAirlineIdAndIdFlightIdFlightNumberOrderByIdStopNumberDesc(
            String airlineId, Integer flightNumber);

    boolean existsByIdFlightIdAirlineIdAndIdFlightIdFlightNumberAndIdStopNumberAndDepartureTimeBetween(
            String airlineId, Integer flightNumber, Integer stopNumber,
            OffsetDateTime start, OffsetDateTime end);

    @Query("""
            select s from StopsAt s
            where s.id.flightId.airlineId = :airlineId
            and s.airport.id = :airportId
            and s.departureTime >= :after
            """)
    List<StopsAt> findMatchingStopsNoBefore(
            @Param("airlineId") String airlineId,
            @Param("airportId") String airportId,
            @Param("after") OffsetDateTime after);

    @Query("""
            select s from StopsAt s
            where s.id.flightId.airlineId = :airlineId
            and s.airport.id = :airportId
            and s.departureTime >= :after
            and s.departureTime <= :before
            """)
    List<StopsAt> findMatchingStopsWithBefore(
            @Param("airlineId") String airlineId,
            @Param("airportId") String airportId,
            @Param("after") OffsetDateTime after,
            @Param("before") OffsetDateTime before);
}
