package com.thomaslent.tcltravels.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.thomaslent.tcltravels.dto.FlightFilterResult;
import com.thomaslent.tcltravels.dto.FlightView;
import com.thomaslent.tcltravels.security.UserPrincipal;
import com.thomaslent.tcltravels.services.FlightFilterService;
import com.thomaslent.tcltravels.services.FlightsService;

@Controller
@RequestMapping("/flights")
public class FlightsController {
  private FlightsService flightsService;
  private FlightFilterService flightFilterService;

  public FlightsController(FlightsService flightsService, FlightFilterService flightFilterService) {
    this.flightsService = flightsService;
    this.flightFilterService = flightFilterService;
  }

  @GetMapping
  @PreAuthorize("hasRole('CUSTOMER')")
  public String getFlights(@RequestParam(name = "ff_airline", required = false) String airlineFilter,
      @RequestParam(name = "ff_airport", required = false) String airportFilter,
      @RequestParam(name = "ff_after", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate afterLocal,
      @RequestParam(name = "ff_before", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate beforeLocal,
      Model model, @AuthenticationPrincipal UserPrincipal principal) {
    FlightFilterResult filterResult = flightFilterService.getFilterResult(
        airlineFilter, airportFilter, afterLocal, beforeLocal);
    model.addAttribute("filter", filterResult);

    Long customerId = principal.getCustomerId();
    List<FlightView> recommendedFlights = flightsService.getRecommendedFlights(customerId);
    model.addAttribute("recommended_flights", recommendedFlights);

    List<FlightView> bestSellingFlights = flightsService.getBestSellingFlights();
    model.addAttribute("best_flights", bestSellingFlights);

    return "flights/index";
  }

}
