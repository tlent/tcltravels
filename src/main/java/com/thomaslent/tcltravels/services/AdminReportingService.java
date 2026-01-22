package com.thomaslent.tcltravels.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.thomaslent.tcltravels.dto.CustomerOptionView;
import com.thomaslent.tcltravels.dto.FlightOptionView;
import com.thomaslent.tcltravels.dto.SalesCityReservationView;
import com.thomaslent.tcltravels.dto.SalesCustomerReservationView;
import com.thomaslent.tcltravels.dto.SalesReservationView;
import com.thomaslent.tcltravels.dto.TopRevenueView;
import com.thomaslent.tcltravels.entities.Customer;
import com.thomaslent.tcltravels.entities.Leg;
import com.thomaslent.tcltravels.entities.Reservation;
import com.thomaslent.tcltravels.entities.Flight;
import com.thomaslent.tcltravels.repositories.AirportRepository;
import com.thomaslent.tcltravels.repositories.CustomerRepository;
import com.thomaslent.tcltravels.repositories.FlightRepository;
import com.thomaslent.tcltravels.repositories.LegRepository;
import com.thomaslent.tcltravels.repositories.ReservationRepository;

@Service
public class AdminReportingService {
  private ReservationRepository reservationRepository;
  private FlightRepository flightRepository;
  private CustomerRepository customerRepository;
  private AirportRepository airportRepository;
  private LegRepository legRepository;

  public AdminReportingService(ReservationRepository reservationRepository, FlightRepository flightRepository,
      CustomerRepository customerRepository, AirportRepository airportRepository, LegRepository legRepository) {
    this.reservationRepository = reservationRepository;
    this.flightRepository = flightRepository;
    this.customerRepository = customerRepository;
    this.airportRepository = airportRepository;
    this.legRepository = legRepository;
  }

  public TopRevenueView getTopCustomer() {
    List<TopRevenueView> results = reservationRepository.findTopCustomers(PageRequest.of(0, 1));
    return results.isEmpty() ? null : results.get(0);
  }

  public TopRevenueView getTopEmployee() {
    List<TopRevenueView> results = reservationRepository.findTopEmployees(PageRequest.of(0, 1));
    return results.isEmpty() ? null : results.get(0);
  }

  public List<SalesReservationView> getMonthlySales(int month, int year) {
    OffsetDateTime start = monthStart(month, year);
    OffsetDateTime end = monthEnd(month, year);
    return reservationRepository.findByReservationDateBetweenOrderByTotalFareDesc(start, end)
        .stream()
        .map(this::toSalesReservationView)
        .collect(Collectors.toList());
  }

  public BigDecimal getMonthlySalesTotal(int month, int year) {
    OffsetDateTime start = monthStart(month, year);
    OffsetDateTime end = monthEnd(month, year);
    BigDecimal total = reservationRepository.sumTotalFareByReservationDateBetween(start, end);
    return total == null ? BigDecimal.ZERO : total;
  }

  public List<FlightOptionView> getFlightOptions() {
    return flightRepository.findAllByOrderByAirline_IdAscFlightNumberAsc()
        .stream()
        .map(this::toFlightOption)
        .collect(Collectors.toList());
  }

  public List<CustomerOptionView> getCustomerOptions() {
    return customerRepository.findAllByOrderByAccountPersonLastNameAscAccountPersonFirstNameAsc()
        .stream()
        .map(this::toCustomerOption)
        .collect(Collectors.toList());
  }

  public List<String> getCityOptions() {
    return airportRepository.findDistinctCityByOrderByCityAsc();
  }

  public List<SalesReservationView> getSalesByFlight(String airlineId, Integer flightNumber) {
    return reservationRepository.findByFlight(airlineId, flightNumber)
        .stream()
        .map(this::toSalesReservationView)
        .collect(Collectors.toList());
  }

  public BigDecimal getSalesTotalByFlight(String airlineId, Integer flightNumber) {
    BigDecimal total = reservationRepository.sumTotalFareByFlight(airlineId, flightNumber);
    return total == null ? BigDecimal.ZERO : total;
  }

  public List<SalesCustomerReservationView> getSalesByCustomer(Long customerId) {
    List<Reservation> reservations =
        reservationRepository.findByCustomerIdOrderByTotalFareDesc(customerId);
    if (reservations.isEmpty()) {
      return List.of();
    }
    List<Long> reservationNumbers = reservations.stream()
        .map(Reservation::getId)
        .collect(Collectors.toList());
    Map<Long, Leg> firstLegs = legRepository
        .findByReservationIdInAndLegNumber(reservationNumbers, 1)
        .stream()
        .collect(Collectors.toMap(l -> l.getReservation().getId(), l -> l));

    return reservations.stream()
        .map(reservation -> toCustomerReservationView(reservation, firstLegs.get(reservation.getId())))
        .collect(Collectors.toList());
  }

  public BigDecimal getSalesTotalByCustomer(Long customerId) {
    BigDecimal total = reservationRepository.sumTotalFareByCustomerId(customerId);
    return total == null ? BigDecimal.ZERO : total;
  }

  public List<SalesCityReservationView> getSalesByCity(String city) {
    return reservationRepository.findReservationsByDestinationCity(city);
  }

  private SalesReservationView toSalesReservationView(Reservation reservation) {
    String customerName = "";
    if (reservation.getCustomer() != null
        && reservation.getCustomer().getAccount() != null
        && reservation.getCustomer().getAccount().getPerson() != null) {
      customerName = reservation.getCustomer().getAccount().getPerson().getFirstName() + " "
          + reservation.getCustomer().getAccount().getPerson().getLastName();
    }
    return new SalesReservationView(
        reservation.getId(),
        customerName,
        reservation.getReservationDate(),
        reservation.getBookingFee(),
        reservation.getTotalFare());
  }

  private SalesCustomerReservationView toCustomerReservationView(Reservation reservation, Leg firstLeg) {
    String airlineId = null;
    Integer flightNumber = null;
    if (firstLeg != null && firstLeg.getFlight() != null && firstLeg.getFlight().getAirline() != null) {
      airlineId = firstLeg.getFlight().getAirline().getId();
      flightNumber = firstLeg.getFlight().getFlightNumber();
    }
    return new SalesCustomerReservationView(
        reservation.getId(),
        airlineId,
        flightNumber,
        reservation.getReservationDate(),
        reservation.getBookingFee(),
        reservation.getTotalFare());
  }

  private FlightOptionView toFlightOption(Flight flight) {
    return new FlightOptionView(flight.getAirline().getId(), flight.getFlightNumber());
  }

  private CustomerOptionView toCustomerOption(Customer customer) {
    String firstName = customer.getAccount() != null && customer.getAccount().getPerson() != null
        ? customer.getAccount().getPerson().getFirstName()
        : "";
    String lastName = customer.getAccount() != null && customer.getAccount().getPerson() != null
        ? customer.getAccount().getPerson().getLastName()
        : "";
    return new CustomerOptionView(customer.getId(), firstName, lastName);
  }

  private OffsetDateTime monthStart(int month, int year) {
    return LocalDate.of(year, month, 1).atStartOfDay().atOffset(ZoneOffset.UTC);
  }

  private OffsetDateTime monthEnd(int month, int year) {
    return LocalDate.of(year, month, 1).plusMonths(1).atStartOfDay().minusNanos(1).atOffset(ZoneOffset.UTC);
  }
}
