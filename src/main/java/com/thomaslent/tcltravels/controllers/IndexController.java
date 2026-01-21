package com.thomaslent.tcltravels.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thomaslent.tcltravels.services.FlightFilterService;
import com.thomaslent.tcltravels.services.FlightsService;
import com.thomaslent.tcltravels.services.ReservationsService;

import jakarta.servlet.http.HttpSession;

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
  public String index(Model model, HttpSession session) {
    if (session.getAttribute("p_id") == null) {
      return "redirect:/login";
    }
    String role = (String) session.getAttribute("role");
    if (!"Customer".equals(role)) {
      return "redirect:/admin/";
    }

    Long accountNumber = (Long) session.getAttribute("c_accountNumber");
    model.addAttribute("filter", flightFilterService.getFilterResult(null, null, null, null));
    model.addAttribute("recommended_flights", flightsService.getRecommendedFlights(accountNumber));
    model.addAttribute("current_reservations", reservationsService.getCurrentReservations(accountNumber));
    return "index";
  }

  @GetMapping("/help")
  public String help(HttpSession session) {
    if (session.getAttribute("p_id") == null) {
      return "redirect:/login";
    }
    return "help";
  }
}
