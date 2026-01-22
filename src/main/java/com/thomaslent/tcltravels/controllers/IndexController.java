package com.thomaslent.tcltravels.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.thomaslent.tcltravels.services.FlightFilterService;
import com.thomaslent.tcltravels.services.FlightsService;
import com.thomaslent.tcltravels.services.ReservationsService;
import com.thomaslent.tcltravels.security.UserPrincipal;

@Controller
@RequestMapping
public class IndexController {
  private FlightFilterService flightFilterService;
  private FlightsService flightsService;
  private ReservationsService reservationsService;

  public IndexController(FlightFilterService flightFilterService, FlightsService flightsService,
      ReservationsService reservationsService) {
    this.flightFilterService = flightFilterService;
    this.flightsService = flightsService;
    this.reservationsService = reservationsService;
  }

  @GetMapping("/")
  @PreAuthorize("hasRole('CUSTOMER')")
  public String index(Model model, @AuthenticationPrincipal UserPrincipal principal) {
    Long accountNumber = principal.getAccountNumber();
    model.addAttribute("filter", flightFilterService.getFilterResult(null, null, null, null));
    model.addAttribute("recommended_flights", flightsService.getRecommendedFlights(accountNumber));
    model.addAttribute("current_reservations", reservationsService.getCurrentReservations(accountNumber));
    return "index";
  }

  @GetMapping("/help")
  @PreAuthorize("isAuthenticated()")
  public String help() {
    return "help";
  }
}
