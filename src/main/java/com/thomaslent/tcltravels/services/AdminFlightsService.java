package com.thomaslent.tcltravels.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.thomaslent.tcltravels.dto.AirportOptionView;
import com.thomaslent.tcltravels.dto.FlightActivityRow;
import com.thomaslent.tcltravels.dto.FlightStopView;
import com.thomaslent.tcltravels.dto.FlightActivityView;
import com.thomaslent.tcltravels.dto.FlightSummaryView;
import com.thomaslent.tcltravels.entities.Airport;
import com.thomaslent.tcltravels.entities.Flight;
import com.thomaslent.tcltravels.entities.StopsAt;
import com.thomaslent.tcltravels.repositories.AirportRepository;
import com.thomaslent.tcltravels.repositories.FlightRepository;
import com.thomaslent.tcltravels.repositories.LegRepository;
import com.thomaslent.tcltravels.repositories.StopsAtRepository;

@Service
public class AdminFlightsService {
  private FlightRepository flightRepository;
  private StopsAtRepository stopsAtRepository;
  private AirportRepository airportRepository;
  private LegRepository legRepository;

  public AdminFlightsService(FlightRepository flightRepository, StopsAtRepository stopsAtRepository,
      AirportRepository airportRepository, LegRepository legRepository) {
    this.flightRepository = flightRepository;
    this.stopsAtRepository = stopsAtRepository;
    this.airportRepository = airportRepository;
    this.legRepository = legRepository;
  }

  public List<FlightActivityView> getMostActiveFlights() {
    List<FlightActivityRow> activityRows = legRepository.findMostActiveFlights(PageRequest.of(0, 3));
    return activityRows.stream()
        .map(row -> buildActivityWithStops(
            row.airlineId(), row.airlineName(), row.flightNumber(),
            row.numberOfSeats(), row.daysOperating(), row.reservationCount()))
        .collect(Collectors.toList());
  }

  public List<FlightSummaryView> getAllFlights() {
    return flightRepository.findAllByOrderByAirline_IdAscFlightNumberAsc()
        .stream()
        .map(this::buildSummary)
        .collect(Collectors.toList());
  }

  public List<FlightStopView> getFlightStops(String airlineId, Integer flightNumber) {
    List<StopsAt> stops = stopsAtRepository
        .findByFlightAirline_IdAndFlightFlightNumberOrderByStopNumber(airlineId, flightNumber);
    return stops.stream().map(this::toStopView).collect(Collectors.toList());
  }

  public Map<String, List<FlightStopView>> getAllFlightStops(List<FlightSummaryView> flights) {
    return flights.stream().collect(Collectors.toMap(
        flight -> flight.airlineId() + flight.flightNumber(),
        flight -> getFlightStops(flight.airlineId(), flight.flightNumber())));
  }

  public List<AirportOptionView> getAirportOptions() {
    return airportRepository.findAllByOrderByIdAsc()
        .stream()
        .map(this::toAirportOption)
        .collect(Collectors.toList());
  }

  public List<FlightSummaryView> getFlightsForAirport(String airportId) {
    return flightRepository.findDistinctByStopsAt_Airport_IdOrderByAirline_IdAscFlightNumberAsc(airportId)
        .stream()
        .map(this::buildSummary)
        .collect(Collectors.toList());
  }

  private FlightSummaryView buildSummary(Flight flight) {
    String airlineId = flight.getAirline().getId();
    Integer flightNumber = flight.getFlightNumber();
    String airlineName = flight.getAirline() != null ? flight.getAirline().getName() : "";

    String originId = flight.getOriginAirport() != null ? flight.getOriginAirport().getId() : "";
    String originCity = flight.getOriginAirport() != null ? flight.getOriginAirport().getCity() : "";
    String destinationId = flight.getDestinationAirport() != null ? flight.getDestinationAirport().getId() : "";
    String destinationCity = flight.getDestinationAirport() != null ? flight.getDestinationAirport().getCity() : "";

    return new FlightSummaryView(
        airlineId,
        airlineName,
        flightNumber,
        flight.getNumberOfSeats(),
        formatDaysOperating(flight.getDaysOperating()),
        originId,
        originCity,
        destinationId,
        destinationCity);
  }

  private FlightActivityView buildActivityWithStops(String airlineId, String airlineName, Integer flightNumber,
      Integer numberOfSeats, String daysOperating, Long reservationCount) {
    List<StopsAt> stops = stopsAtRepository
        .findByFlightAirline_IdAndFlightFlightNumberOrderByStopNumber(airlineId, flightNumber);
    StopsAt origin = stops.isEmpty() ? null : stops.get(0);
    StopsAt destination = stops.isEmpty() ? null : stops.get(stops.size() - 1);

    String originId = origin != null && origin.getAirport() != null ? origin.getAirport().getId() : "";
    String originCity = origin != null && origin.getAirport() != null ? origin.getAirport().getCity() : "";
    String destinationId = destination != null && destination.getAirport() != null ? destination.getAirport().getId()
        : "";
    String destinationCity = destination != null && destination.getAirport() != null
        ? destination.getAirport().getCity()
        : "";

    return new FlightActivityView(
        airlineId,
        airlineName,
        flightNumber,
        numberOfSeats,
        formatDaysOperating(daysOperating),
        originId,
        originCity,
        destinationId,
        destinationCity,
        reservationCount);
  }

  private FlightStopView toStopView(StopsAt stop) {
    String airportId = stop.getAirport() != null ? stop.getAirport().getId() : "";
    String airportName = stop.getAirport() != null ? stop.getAirport().getName() : "";
    String city = stop.getAirport() != null ? stop.getAirport().getCity() : "";
    return new FlightStopView(
        stop.getFlight().getAirline().getId(),
        stop.getFlight().getFlightNumber(),
        stop.getStopNumber(),
        airportId,
        airportName,
        city,
        stop.getArrivalTime(),
        stop.getDepartureTime());
  }

  private AirportOptionView toAirportOption(Airport airport) {
    return new AirportOptionView(airport.getId(), airport.getName());
  }

  private static final String[] DAY_LABELS = { "Mo", "Tu", "We", "Th", "Fr", "Sa", "Su" };

  private String formatDaysOperating(String daysOperating) {
    if ("1111111".equals(daysOperating)) {
      return "Every Day";
    }
    if ("0000000".equals(daysOperating)) {
      return "Not Active";
    }
    if (daysOperating == null || daysOperating.length() != 7) {
      return "";
    }

    List<String> days = new ArrayList<>();
    for (int i = 0; i < 7; i++) {
      if (daysOperating.charAt(i) == '1') {
        days.add(DAY_LABELS[i]);
      }
    }
    return String.join("-", days);
  }
}
