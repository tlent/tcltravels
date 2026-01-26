package com.thomaslent.tcltravels.controllers.api;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thomaslent.tcltravels.dto.FlightFilterResult;
import com.thomaslent.tcltravels.dto.FlightView;
import com.thomaslent.tcltravels.dto.StopView;
import com.thomaslent.tcltravels.entities.StopsAt;
import com.thomaslent.tcltravels.repositories.StopsAtRepository;
import com.thomaslent.tcltravels.security.UserPrincipal;
import com.thomaslent.tcltravels.services.FlightFilterService;
import com.thomaslent.tcltravels.services.FlightsService;

@RestController
@RequestMapping("/api/v1/flights")
@PreAuthorize("hasRole('CUSTOMER')")
public class FlightsApiController {

  private final FlightFilterService flightFilterService;
  private final FlightsService flightsService;
  private final StopsAtRepository stopsAtRepository;

  public FlightsApiController(
      FlightFilterService flightFilterService,
      FlightsService flightsService,
      StopsAtRepository stopsAtRepository) {
    this.flightFilterService = flightFilterService;
    this.flightsService = flightsService;
    this.stopsAtRepository = stopsAtRepository;
  }

  @GetMapping
  public ResponseEntity<Map<String, Object>> getFlights(
      @RequestParam(required = false) String airline,
      @RequestParam(required = false) String airport,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate after,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate before,
      Authentication authentication) {

    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

    // Get filter results
    FlightFilterResult filterResult = flightFilterService.getFilterResult(
        airline, airport, after, before);

    // Get recommended flights for this customer
    List<FlightView> recommended = flightsService.getRecommendedFlights(
        userPrincipal.getCustomerId());

    // Get best-selling flights
    List<FlightView> bestSelling = flightsService.getBestSellingFlights();

    Map<String, Object> response = new HashMap<>();
    response.put("filter", filterResult);
    response.put("recommended", recommended);
    response.put("bestSelling", bestSelling);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/{airlineId}/{flightNumber}/stops")
  public ResponseEntity<List<StopView>> getFlightStops(
      @PathVariable String airlineId,
      @PathVariable Integer flightNumber) {

    List<StopsAt> stops = stopsAtRepository
        .findByFlight_Airline_IdAndFlight_FlightNumberOrderByStopNumber(airlineId, flightNumber);

    List<StopView> stopViews = stops.stream()
        .map(stop -> new StopView(
            stop.getFlight().getAirline().getId(),
            stop.getAirport().getId(),
            stop.getFlight().getFlightNumber(),
            stop.getStopNumber(),
            stop.getArrivalTime(),
            stop.getDepartureTime(),
            stop.getDepartureTime().toLocalDate()))
        .collect(Collectors.toList());

    return ResponseEntity.ok(stopViews);
  }
}
