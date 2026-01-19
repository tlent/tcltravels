package com.thomaslent.tcltravels.controllers;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.thomaslent.tcltravels.dto.FlightRecommendationRow;
import com.thomaslent.tcltravels.dto.FlightView;
import com.thomaslent.tcltravels.dto.StopView;
import com.thomaslent.tcltravels.entities.Airline;
import com.thomaslent.tcltravels.entities.Airport;
import com.thomaslent.tcltravels.entities.StopsAt;
import com.thomaslent.tcltravels.repositories.AirlineRepository;
import com.thomaslent.tcltravels.repositories.AirportRepository;
import com.thomaslent.tcltravels.repositories.FlightRepository;
import com.thomaslent.tcltravels.repositories.StopsAtRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/flights")
public class FlightsController {
  private AirlineRepository airlineRepository;
  private AirportRepository airportRepository;
  private StopsAtRepository stopsAtRepository;
  private FlightRepository flightRepository;

  public FlightsController(AirlineRepository airlineRepository, AirportRepository airportRepository,
      StopsAtRepository stopsAtRepository, FlightRepository flightRepository) {
    this.airlineRepository = airlineRepository;
    this.airportRepository = airportRepository;
    this.stopsAtRepository = stopsAtRepository;
    this.flightRepository = flightRepository;
  }

  @GetMapping
  public String getFlights(@RequestParam(name = "ff_airline", required = false) String airlineFilter,
      @RequestParam(name = "ff_airport", required = false) String airportFilter,
      @RequestParam(name = "ff_after", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate afterLocal,
      @RequestParam(name = "ff_before", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate beforeLocal,
      Model model, HttpSession session) {
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

    Long accountNumber = (Long) session.getAttribute("c_accountNumber");
    List<FlightRecommendationRow> flightRecommendationRows = flightRepository.findRecommendedFlights(accountNumber);
    List<FlightView> flightViews = flightRecommendationRows.stream().map(row -> new FlightView(
        row.getAirlineId(), row.getFlightNumber(), row.getNumberOfSeats(), formatDaysOperating(row.getDaysOperating()),
        row.getOriginId(), row.getOriginCity(), row.getDestinationId(), row.getDestinationCity()))
        .collect(Collectors.toList());

    model.addAttribute("flights_info", flightViews);

    List<FlightRecommendationRow> bestSellingFlightsRows = flightRepository.findBestSellingFlights();
    List<FlightView> bestSellingFlightViews = bestSellingFlightsRows.stream().map(row -> new FlightView(
        row.getAirlineId(), row.getFlightNumber(), row.getNumberOfSeats(), formatDaysOperating(row.getDaysOperating()),
        row.getOriginId(), row.getOriginCity(), row.getDestinationId(), row.getDestinationCity()))
        .collect(Collectors.toList());

    model.addAttribute("best_flights", bestSellingFlightViews);

    return "flights";
  }

  private static final String[] DAY_LABELS = { "Mo", "Tu", "We", "Th", "Fr", "Sa", "Su" };

  private String formatDaysOperating(String daysOperating) {
    if ("1111111".equals(daysOperating))
      return "Every Day";
    if ("0000000".equals(daysOperating))
      return "Not Active";
    if (daysOperating == null || daysOperating.length() != 7)
      return "";

    StringJoiner joiner = new StringJoiner("-");
    for (int i = 0; i < 7; i++) {
      if (daysOperating.charAt(i) == '1') {
        joiner.add(DAY_LABELS[i]);
      }
    }
    return joiner.toString();
  }

}
