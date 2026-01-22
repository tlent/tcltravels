package com.thomaslent.tcltravels.services;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.thomaslent.tcltravels.dto.FlightFilterResult;
import com.thomaslent.tcltravels.dto.StopView;
import com.thomaslent.tcltravels.entities.Airline;
import com.thomaslent.tcltravels.entities.Airport;
import com.thomaslent.tcltravels.entities.StopsAt;
import com.thomaslent.tcltravels.repositories.AirlineRepository;
import com.thomaslent.tcltravels.repositories.AirportRepository;
import com.thomaslent.tcltravels.repositories.StopsAtRepository;

@Service
public class FlightFilterService {
  private AirlineRepository airlineRepository;
  private AirportRepository airportRepository;
  private StopsAtRepository stopsAtRepository;

  public FlightFilterService(AirlineRepository airlineRepository, AirportRepository airportRepository,
      StopsAtRepository stopsAtRepository) {
    this.airlineRepository = airlineRepository;
    this.airportRepository = airportRepository;
    this.stopsAtRepository = stopsAtRepository;
  }

  public FlightFilterResult getFilterResult(String airlineFilter, String airportFilter, LocalDate afterLocal,
      LocalDate beforeLocal) {
    List<Airline> airlines = airlineRepository.findAll(Sort.by("name"));
    List<Airport> airports = airportRepository.findAll(Sort.by("name"));

    if (airlineFilter == null && !airlines.isEmpty()) {
      airlineFilter = airlines.get(0).getId();
    }
    if (airportFilter == null && !airports.isEmpty()) {
      airportFilter = airports.get(0).getId();
    }
    if (afterLocal == null) {
      afterLocal = LocalDate.now();
    }
    OffsetDateTime afterOffset = afterLocal.atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime();

    List<StopsAt> stops;
    if (beforeLocal == null) {
      stops = stopsAtRepository.findByFlightAirline_IdAndAirport_IdAndDepartureTimeGreaterThanEqual(
          airlineFilter, airportFilter, afterOffset);
    } else {
      OffsetDateTime beforeOffset = beforeLocal.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toOffsetDateTime();
      stops = stopsAtRepository.findByFlightAirline_IdAndAirport_IdAndDepartureTimeBetween(
          airlineFilter, airportFilter, afterOffset, beforeOffset);
    }

    List<StopView> stopViews = stops.stream().map(stop -> new StopView(
        stop.getFlight().getAirline().getId(), stop.getAirport().getId(),
        stop.getFlight().getFlightNumber(), stop.getStopNumber(), stop.getArrivalTime(),
        stop.getDepartureTime(), stop.getDepartureTime().toLocalDate())).collect(Collectors.toList());

    return new FlightFilterResult(
        airlines, airports, airlineFilter, airportFilter, afterLocal, beforeLocal, stopViews);
  }
}
