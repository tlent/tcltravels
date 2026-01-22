package com.thomaslent.tcltravels.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.thomaslent.tcltravels.dto.AirportOptionView;
import com.thomaslent.tcltravels.dto.CustomerOptionView;
import com.thomaslent.tcltravels.dto.FlightOptionView;

@Service
public class AdminSelectionService {
  public String selectFlightKey(String flightKey, List<FlightOptionView> options) {
    if (flightKey != null && flightKey.length() > 2) {
      return flightKey;
    }
    if (options.isEmpty()) {
      return "";
    }
    FlightOptionView first = options.get(0);
    return first.airlineId() + first.flightNumber();
  }

  public FlightOptionView parseFlightKey(String flightKey) {
    if (flightKey == null || flightKey.length() < 3) {
      return new FlightOptionView("", 0);
    }
    String airlineId = flightKey.substring(0, 2);
    Integer flightNumber = Integer.parseInt(flightKey.substring(2));
    return new FlightOptionView(airlineId, flightNumber);
  }

  public Long selectCustomerId(Long customerId, List<CustomerOptionView> options) {
    if (customerId != null) {
      return customerId;
    }
    if (options.isEmpty()) {
      return 0L;
    }
    return options.get(0).customerId();
  }

  public String selectCustomerName(Long customerId, List<CustomerOptionView> options) {
    return options.stream()
        .filter(option -> option.customerId().equals(customerId))
        .map(option -> option.firstName() + " " + option.lastName())
        .findFirst()
        .orElse("");
  }

  public String selectCity(String city, List<String> options) {
    if (city != null && !city.isBlank()) {
      return city;
    }
    if (options.isEmpty()) {
      return "";
    }
    return options.get(0);
  }

  public String selectAirportId(String airportId, List<AirportOptionView> options) {
    if (airportId != null && !airportId.isBlank()) {
      return airportId;
    }
    if (options.isEmpty()) {
      return "";
    }
    return options.get(0).id();
  }
}
