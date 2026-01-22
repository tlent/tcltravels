package com.thomaslent.tcltravels.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.thomaslent.tcltravels.dto.BidForm;
import com.thomaslent.tcltravels.dto.ReservationView;
import com.thomaslent.tcltravels.dto.ReservationForm;
import com.thomaslent.tcltravels.dto.StopView;
import com.thomaslent.tcltravels.services.AuctionsService;
import com.thomaslent.tcltravels.services.ReservationsService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
public class ReservationsController {
    private ReservationsService reservationsService;
    private AuctionsService auctionsService;

    public ReservationsController(ReservationsService reservationsService, AuctionsService auctionsService) {
        this.reservationsService = reservationsService;
        this.auctionsService = auctionsService;
    }

    @GetMapping("/reservations")
    public String getReservations(Model model, HttpSession session) {
        if (session.getAttribute("p_id") == null) {
            return "redirect:/login";
        }
        String role = (String) session.getAttribute("role");
        if (!"Customer".equals(role)) {
            return "redirect:/admin/reservations";
        }

        Long accountNumber = (Long) session.getAttribute("c_accountNumber");
        List<ReservationView> currentReservationViews = reservationsService.getCurrentReservations(accountNumber);
        model.addAttribute("current_reservations", currentReservationViews);

        List<ReservationView> pastReservationViews = reservationsService.getPastReservations(accountNumber);
        model.addAttribute("past_reservations", pastReservationViews);

        model.addAttribute("auction_history", auctionsService.getAuctionHistory(accountNumber));

        return "reservations/index";
    }

    @GetMapping("/reservations/new")
    public String createReservation(@RequestParam String airlineId,
        @RequestParam Integer flightNumber,
        @RequestParam(name = "departureDate", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate departureDate,
        Model model, HttpSession session) {
        if (session.getAttribute("p_id") == null) {
            return "redirect:/login";
        }
        String role = (String) session.getAttribute("role");
        if (!"Customer".equals(role)) {
            return "redirect:/admin/reservations";
        }

        ReservationForm reservationForm = new ReservationForm();
        reservationForm.setAirlineId(airlineId);
        reservationForm.setFlightNumber(flightNumber);
        reservationForm.setPassengerCount(1);

        if (departureDate == null) {
            departureDate = reservationsService.getDefaultDepartureDate(airlineId, flightNumber);
        }
        reservationForm.setDepartureDate(departureDate);

        List<StopView> stops = reservationsService.getStopsForFlight(airlineId, flightNumber);
        loadStopLists(model, stops);

        model.addAttribute("reservationForm", reservationForm);
        return "reservations/new";
    }

    @GetMapping("/reservations/bid")
    public String placeBid(@RequestParam String airlineId,
        @RequestParam Integer flightNumber,
        @RequestParam(name = "departureDate", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate departureDate,
        Model model, HttpSession session) {
        if (session.getAttribute("p_id") == null) {
            return "redirect:/login";
        }
        String role = (String) session.getAttribute("role");
        if (!"Customer".equals(role)) {
            return "redirect:/admin/reservations";
        }

        BidForm bidForm = new BidForm();
        bidForm.setAirlineId(airlineId);
        bidForm.setFlightNumber(flightNumber);
        if (departureDate == null) {
            departureDate = reservationsService.getDefaultDepartureDate(airlineId, flightNumber);
        }
        bidForm.setDepartureDate(departureDate);
        bidForm.setFlightClass("First");

        model.addAttribute("bidForm", bidForm);
        return "reservations/bid";
    }

    @PostMapping("/reservations")
    public String submitReservation(@Valid @ModelAttribute("reservationForm") ReservationForm reservationForm,
        BindingResult bindingResult, Model model, HttpSession session) {
        if (session.getAttribute("p_id") == null) {
            return "redirect:/login";
        }
        String role = (String) session.getAttribute("role");
        if (!"Customer".equals(role)) {
            return "redirect:/admin/reservations";
        }
        if (bindingResult.hasErrors()) {
            reloadReservationForm(model, reservationForm);
            return "reservations/new";
        }

        Long accountNumber = (Long) session.getAttribute("c_accountNumber");
        reservationsService.createReservation(reservationForm, accountNumber)
            .ifPresent(error -> bindingResult.reject("reservationForm", error));

        if (bindingResult.hasErrors()) {
            reloadReservationForm(model, reservationForm);
            return "reservations/new";
        }

        return "redirect:/reservations";
    }

    @PostMapping("/reservations/bid")
    public String submitBid(@Valid @ModelAttribute("bidForm") BidForm bidForm, BindingResult bindingResult,
        Model model, HttpSession session) {
        if (session.getAttribute("p_id") == null) {
            return "redirect:/login";
        }
        String role = (String) session.getAttribute("role");
        if (!"Customer".equals(role)) {
            return "redirect:/admin/reservations";
        }
        if (bindingResult.hasErrors()) {
            return "reservations/bid";
        }

        Long accountNumber = (Long) session.getAttribute("c_accountNumber");
        Long personId = (Long) session.getAttribute("p_id");
        ReservationsService.BidResult result = reservationsService.submitBid(bidForm, accountNumber, personId);
        if (result.error() != null) {
            bindingResult.reject("bidForm", result.error());
            return "reservations/bid";
        }
        if (result.rejected()) {
            model.addAttribute("bidRejected", true);
            return "reservations/bid";
        }
        return "redirect:/reservations";
    }

    private void reloadReservationForm(Model model, ReservationForm reservationForm) {
        List<StopView> stops = reservationsService.getStopsForFlight(
            reservationForm.getAirlineId(), reservationForm.getFlightNumber());
        loadStopLists(model, stops);
        model.addAttribute("reservationForm", reservationForm);
    }

    private void loadStopLists(Model model, List<StopView> stops) {
        List<StopView> originStops = stops.size() > 1 ? stops.subList(0, stops.size() - 1) : List.of();
        List<StopView> destinationStops = stops.size() > 1 ? stops.subList(1, stops.size()) : List.of();
        model.addAttribute("originStops", originStops);
        model.addAttribute("destinationStops", destinationStops);
    }

}
