package com.thomaslent.tcltravels.controllers;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.thomaslent.tcltravels.dto.AuctionView;
import com.thomaslent.tcltravels.dto.LegView;
import com.thomaslent.tcltravels.dto.ReservationLegRow;
import com.thomaslent.tcltravels.dto.ReservationView;
import com.thomaslent.tcltravels.entities.Auction;
import com.thomaslent.tcltravels.entities.Reservation;
import com.thomaslent.tcltravels.repositories.AuctionRepository;
import com.thomaslent.tcltravels.repositories.LegRepository;
import com.thomaslent.tcltravels.repositories.ReservationRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class ReservationsController {
    private ReservationRepository reservationRepository;
    private LegRepository legRepository;
    private AuctionRepository auctionRepository;

    public ReservationsController(ReservationRepository reservationRepository, LegRepository legRepository,
            AuctionRepository auctionRepository) {
        this.reservationRepository = reservationRepository;
        this.legRepository = legRepository;
        this.auctionRepository = auctionRepository;
    }

    @GetMapping("/reservations")
    public String getReservations(Model model, HttpSession session) {
        Long accountNumber = (Long) session.getAttribute("c_accountNumber");
        List<Reservation> currentReservations = reservationRepository.findCurrentReservations(accountNumber);
        List<Long> currentReservationNumbers = currentReservations.stream()
                .map(reservation -> reservation.getReservationNumber()).collect(Collectors.toList());
        List<ReservationLegRow> currentLegRows = currentReservationNumbers.isEmpty() ? List.of()
                : legRepository.getReservationLegs(currentReservationNumbers);
        Map<Long, List<LegView>> currentLegViewsByReservation = new HashMap<>();
        for (ReservationLegRow row : currentLegRows) {
            OffsetDateTime departureTime = OffsetDateTime.ofInstant(row.getDepartureTime(), ZoneId.systemDefault());
            OffsetDateTime arrivalTime = OffsetDateTime.ofInstant(row.getArrivalTime(), ZoneId.systemDefault());
            currentLegViewsByReservation.computeIfAbsent(row.getReservationNumber(), key -> new ArrayList<>())
                    .add(new LegView(row.getAirlineId(), row.getFlightNumber(), row.getOriginAirportId(),
                            row.getOriginName(),
                            row.getOriginCity(), row.getDestinationAirportId(), row.getDestinationName(),
                            row.getDestinationCity(),
                            departureTime, arrivalTime));
        }
        List<ReservationView> currentReservationViews = currentReservations.stream()
                .map(reservation -> new ReservationView(reservation.getReservationNumber(),
                        reservation.getCustomer().getAccountNumber(), reservation.getReservationDate(),
                        reservation.getBookingFee(),
                        reservation.getTotalFare(),
                        currentLegViewsByReservation.getOrDefault(reservation.getReservationNumber(), List.of())))
                .collect(Collectors.toList());
        model.addAttribute("customer_current", currentReservationViews);

        List<Reservation> pastReservations = reservationRepository.findPastReservations(accountNumber);
        List<Long> pastReservationNumbers = pastReservations.stream()
                .map(reservation -> reservation.getReservationNumber()).collect(Collectors.toList());
        List<ReservationLegRow> pastLegRows = pastReservationNumbers.isEmpty() ? List.of()
                : legRepository.getReservationLegs(pastReservationNumbers);
        Map<Long, List<LegView>> pastLegViewsByReservation = new HashMap<>();
        for (ReservationLegRow row : pastLegRows) {
            OffsetDateTime departureTime = OffsetDateTime.ofInstant(row.getDepartureTime(), ZoneId.systemDefault());
            OffsetDateTime arrivalTime = OffsetDateTime.ofInstant(row.getArrivalTime(), ZoneId.systemDefault());
            pastLegViewsByReservation.computeIfAbsent(row.getReservationNumber(), key -> new ArrayList<>())
                    .add(new LegView(row.getAirlineId(), row.getFlightNumber(), row.getOriginAirportId(),
                            row.getOriginName(),
                            row.getOriginCity(), row.getDestinationAirportId(), row.getDestinationName(),
                            row.getDestinationCity(),
                            departureTime, arrivalTime));
        }
        List<ReservationView> pastReservationViews = pastReservations.stream()
                .map(reservation -> new ReservationView(reservation.getReservationNumber(),
                        reservation.getCustomer().getAccountNumber(), reservation.getReservationDate(),
                        reservation.getBookingFee(),
                        reservation.getTotalFare(),
                        pastLegViewsByReservation.getOrDefault(reservation.getReservationNumber(), List.of())))
                .collect(Collectors.toList());
        model.addAttribute("customer_history", pastReservationViews);

        List<Auction> auctions = auctionRepository.findByCustomer_AccountNumberOrderByDateDesc(accountNumber);
        List<AuctionView> auctionViews = auctions.stream()
                .map(auction -> new AuctionView(auction.getAirlineId(), auction.getFlightNumber(),
                        auction.getSeatingClass(),
                        auction.getDate(), auction.getNameYourOwnPrice(), auction.getAccepted()))
                .collect(Collectors.toList());
        model.addAttribute("customer_auction_history", auctionViews);

        return "reservations";
    }
}
