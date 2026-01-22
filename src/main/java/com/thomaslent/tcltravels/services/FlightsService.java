package com.thomaslent.tcltravels.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.thomaslent.tcltravels.dto.FlightRecommendationRow;
import com.thomaslent.tcltravels.dto.FlightView;
import com.thomaslent.tcltravels.repositories.FlightRepository;

@Service
public class FlightsService {
  private FlightRepository flightRepository;
  private FlightScheduleService flightScheduleService;

  public FlightsService(FlightRepository flightRepository, FlightScheduleService flightScheduleService) {
    this.flightRepository = flightRepository;
    this.flightScheduleService = flightScheduleService;
  }

  public List<FlightView> getRecommendedFlights(Long customerId) {
    List<FlightRecommendationRow> flightRecommendationRows = flightRepository.findRecommendedFlights(customerId);
    return flightRecommendationRows.stream().map(row -> new FlightView(
        row.getAirlineId(), row.getFlightNumber(), row.getNumberOfSeats(),
        flightScheduleService.formatDaysOperating(row.getDaysOperating()),
        row.getOriginId(), row.getOriginCity(), row.getDestinationId(), row.getDestinationCity()))
        .collect(Collectors.toList());
  }

  public List<FlightView> getBestSellingFlights() {
    List<FlightRecommendationRow> bestSellingFlightsRows = flightRepository.findBestSellingFlights();
    return bestSellingFlightsRows.stream().map(row -> new FlightView(
        row.getAirlineId(), row.getFlightNumber(), row.getNumberOfSeats(),
        flightScheduleService.formatDaysOperating(row.getDaysOperating()),
        row.getOriginId(), row.getOriginCity(), row.getDestinationId(), row.getDestinationCity()))
        .collect(Collectors.toList());
  }
}
