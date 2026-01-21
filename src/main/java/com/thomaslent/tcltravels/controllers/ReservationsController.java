package com.thomaslent.tcltravels.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.thomaslent.tcltravels.dto.ReservationView;
import com.thomaslent.tcltravels.services.AuctionsService;
import com.thomaslent.tcltravels.services.ReservationsService;

import jakarta.servlet.http.HttpSession;

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
}
