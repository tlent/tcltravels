package com.thomaslent.tcltravels.services;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.thomaslent.tcltravels.dto.LegView;
import com.thomaslent.tcltravels.dto.ReservationLegRow;
import com.thomaslent.tcltravels.dto.ReservationView;
import com.thomaslent.tcltravels.entities.Reservation;
import com.thomaslent.tcltravels.repositories.LegRepository;
import com.thomaslent.tcltravels.repositories.ReservationRepository;

@Service
public class ReservationsService {
  private ReservationRepository reservationRepository;
  private LegRepository legRepository;

  public ReservationsService(ReservationRepository reservationRepository, LegRepository legRepository) {
    this.reservationRepository = reservationRepository;
    this.legRepository = legRepository;
  }

  public List<ReservationView> getCurrentReservations(Long accountNumber) {
    List<Reservation> currentReservations = reservationRepository.findCurrentReservations(accountNumber);
    return buildReservationViews(currentReservations);
  }

  public List<ReservationView> getPastReservations(Long accountNumber) {
    List<Reservation> pastReservations = reservationRepository.findPastReservations(accountNumber);
    return buildReservationViews(pastReservations);
  }

  private List<ReservationView> buildReservationViews(List<Reservation> reservations) {
    List<Long> reservationNumbers = reservations.stream()
        .map(reservation -> reservation.getReservationNumber()).collect(Collectors.toList());
    List<ReservationLegRow> legRows = reservationNumbers.isEmpty() ? List.of()
        : legRepository.getReservationLegs(reservationNumbers);
    Map<Long, List<LegView>> legViewsByReservation = new HashMap<>();
    for (ReservationLegRow row : legRows) {
      OffsetDateTime departureTime = OffsetDateTime.ofInstant(row.getDepartureTime(), ZoneId.systemDefault());
      OffsetDateTime arrivalTime = OffsetDateTime.ofInstant(row.getArrivalTime(), ZoneId.systemDefault());
      legViewsByReservation.computeIfAbsent(row.getReservationNumber(), key -> new ArrayList<>())
          .add(new LegView(row.getAirlineId(), row.getFlightNumber(), row.getOriginAirportId(),
              row.getOriginName(),
              row.getOriginCity(), row.getDestinationAirportId(), row.getDestinationName(),
              row.getDestinationCity(),
              departureTime, arrivalTime));
    }
    return reservations.stream()
        .map(reservation -> new ReservationView(reservation.getReservationNumber(),
            reservation.getCustomer().getAccountNumber(), reservation.getReservationDate(),
            reservation.getBookingFee(),
            reservation.getTotalFare(),
            legViewsByReservation.getOrDefault(reservation.getReservationNumber(), List.of())))
        .collect(Collectors.toList());
  }
}
