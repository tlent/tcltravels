package com.thomaslent.tcltravels.controllers;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.thomaslent.tcltravels.dto.StopView;
import com.thomaslent.tcltravels.entities.Airline;
import com.thomaslent.tcltravels.entities.Airport;
import com.thomaslent.tcltravels.entities.StopsAt;
import com.thomaslent.tcltravels.repositories.AirlineRepository;
import com.thomaslent.tcltravels.repositories.AirportRepository;
import com.thomaslent.tcltravels.repositories.StopsAtRepository;

@Controller
@RequestMapping("/flights")
public class FlightsController {
  private AirlineRepository airlineRepository;
  private AirportRepository airportRepository;
  private StopsAtRepository stopsAtRepository;

  public FlightsController(AirlineRepository airlineRepository, AirportRepository airportRepository,
      StopsAtRepository stopsAtRepository) {
    this.airlineRepository = airlineRepository;
    this.airportRepository = airportRepository;
    this.stopsAtRepository = stopsAtRepository;
  }

  @GetMapping
  public String getFlights(@RequestParam(name = "ff_airline", required = false) String airlineFilter,
      @RequestParam(name = "ff_airport", required = false) String airportFilter,
      @RequestParam(name = "ff_after", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate afterLocal,
      @RequestParam(name = "ff_before", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate beforeLocal,
      Model model) {
    List<Airline> airlines = airlineRepository.findAll(Sort.by("name"));
    List<Airport> airports = airportRepository.findAll(Sort.by("name"));

    if (airlineFilter == null) {
      airlineFilter = airlines.get(0).getId();
    }
    if (airportFilter == null) {
      airportFilter = airports.get(0).getId();
    }
    if (afterLocal == null) {
      afterLocal = LocalDate.now();
    }
    OffsetDateTime afterOffset = afterLocal.atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime();

    List<StopsAt> stops;
    if (beforeLocal == null) {
      stops = stopsAtRepository.findMatchingStopsNoBefore(airlineFilter, airportFilter, afterOffset);
    } else {
      OffsetDateTime beforeOffset = beforeLocal.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toOffsetDateTime();
      stops = stopsAtRepository.findMatchingStopsWithBefore(airlineFilter, airportFilter, afterOffset, beforeOffset);
    }

    List<StopView> stopViews = stops.stream().map(stop -> new StopView(
        stop.getId().getFlightId().getAirlineId(), stop.getAirport().getId(),
        stop.getId().getFlightId().getFlightNumber(), stop.getId().getStopNumber(), stop.getArrivalTime(),
        stop.getDepartureTime(), stop.getDepartureTime().toLocalDate())).collect(Collectors.toList());

    model.addAttribute("airlines", airlines);
    model.addAttribute("airports", airports);
    model.addAttribute("selectedAirlineId", airlineFilter);
    model.addAttribute("selectedAirportId", airportFilter);
    model.addAttribute("after", afterLocal);
    model.addAttribute("before", beforeLocal);
    model.addAttribute("matchingStops", stopViews);

    return "flights";
  }
}
