package com.thomaslent.tcltravels.services;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.thomaslent.tcltravels.dto.FlightRecommendationRow;
import com.thomaslent.tcltravels.dto.FlightView;
import com.thomaslent.tcltravels.repositories.FlightRepository;

@Service
public class FlightsService {
  private FlightRepository flightRepository;

  public FlightsService(FlightRepository flightRepository) {
    this.flightRepository = flightRepository;
  }

  public List<FlightView> getRecommendedFlights(Long customerId) {
    List<FlightRecommendationRow> flightRecommendationRows = flightRepository.findRecommendedFlights(customerId);
    return flightRecommendationRows.stream().map(row -> new FlightView(
        row.getAirlineId(), row.getFlightNumber(), row.getNumberOfSeats(), formatDaysOperating(row.getDaysOperating()),
        row.getOriginId(), row.getOriginCity(), row.getDestinationId(), row.getDestinationCity()))
        .collect(Collectors.toList());
  }

  public List<FlightView> getBestSellingFlights() {
    List<FlightRecommendationRow> bestSellingFlightsRows = flightRepository.findBestSellingFlights();
    return bestSellingFlightsRows.stream().map(row -> new FlightView(
        row.getAirlineId(), row.getFlightNumber(), row.getNumberOfSeats(), formatDaysOperating(row.getDaysOperating()),
        row.getOriginId(), row.getOriginCity(), row.getDestinationId(), row.getDestinationCity()))
        .collect(Collectors.toList());
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
