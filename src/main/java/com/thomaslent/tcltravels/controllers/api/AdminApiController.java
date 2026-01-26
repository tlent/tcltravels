package com.thomaslent.tcltravels.controllers.api;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thomaslent.tcltravels.dto.AdminEmployeeView;
import com.thomaslent.tcltravels.dto.AirportOptionView;
import com.thomaslent.tcltravels.dto.CustomerOptionView;
import com.thomaslent.tcltravels.dto.FlightActivityView;
import com.thomaslent.tcltravels.dto.FlightOptionView;
import com.thomaslent.tcltravels.dto.FlightStopView;
import com.thomaslent.tcltravels.dto.FlightSummaryView;
import com.thomaslent.tcltravels.dto.SalesCityReservationView;
import com.thomaslent.tcltravels.dto.SalesCustomerReservationView;
import com.thomaslent.tcltravels.dto.SalesReservationView;
import com.thomaslent.tcltravels.dto.TopRevenueView;
import com.thomaslent.tcltravels.repositories.ReservationRepository;
import com.thomaslent.tcltravels.services.AdminEmployeeService;
import com.thomaslent.tcltravels.services.AdminFlightsService;
import com.thomaslent.tcltravels.services.AdminReportingService;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasAnyRole('MANAGER', 'EMPLOYEE')")
public class AdminApiController {

  private final AdminReportingService reportingService;
  private final AdminFlightsService flightsService;
  private final AdminEmployeeService employeeService;
  private final ReservationRepository reservationRepository;

  public AdminApiController(
      AdminReportingService reportingService,
      AdminFlightsService flightsService,
      AdminEmployeeService employeeService,
      ReservationRepository reservationRepository) {
    this.reportingService = reportingService;
    this.flightsService = flightsService;
    this.employeeService = employeeService;
    this.reservationRepository = reservationRepository;
  }

  @GetMapping("/dashboard")
  public ResponseEntity<Map<String, Object>> getDashboard(
      @RequestParam(required = false) Integer month,
      @RequestParam(required = false) Integer year) {

    int currentYear = LocalDate.now().getYear();
    int currentMonth = LocalDate.now().getMonthValue();

    if (year == null) {
      year = currentYear;
    }
    if (month == null) {
      month = currentMonth;
    }

    TopRevenueView topCustomer = reportingService.getTopCustomer();
    TopRevenueView topEmployee = reportingService.getTopEmployee();
    List<SalesReservationView> monthlySales = reportingService.getMonthlySales(month, year);
    BigDecimal monthTotal = reportingService.getMonthlySalesTotal(month, year);
    List<AdminEmployeeView> employees = employeeService.getEmployees();

    // Month/year options for selector
    List<Integer> months = IntStream.rangeClosed(1, 12).boxed().toList();
    List<Integer> years = IntStream.rangeClosed(currentYear - 5, currentYear + 1).boxed().toList();

    Map<String, Object> response = new HashMap<>();
    response.put("topCustomer", topCustomer);
    response.put("topEmployee", topEmployee);
    response.put("monthlySales", monthlySales);
    response.put("monthTotal", monthTotal);
    response.put("employees", employees);
    response.put("months", months);
    response.put("years", years);
    response.put("selectedMonth", month);
    response.put("selectedYear", year);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/sales")
  public ResponseEntity<Map<String, Object>> getSales(
      @RequestParam(required = false) Integer month,
      @RequestParam(required = false) Integer year,
      @RequestParam(required = false) String flight,
      @RequestParam(required = false) Long customer,
      @RequestParam(required = false) String city) {

    Map<String, Object> response = new HashMap<>();

    // Monthly sales
    if (month != null && year != null) {
      List<SalesReservationView> monthSales = reportingService.getMonthlySales(month, year);
      BigDecimal monthTotal = reportingService.getMonthlySalesTotal(month, year);
      response.put("monthSales", monthSales);
      response.put("monthTotal", monthTotal);
    }

    // Flight sales
    if (flight != null && !flight.isEmpty()) {
      String[] parts = flight.split("-");
      if (parts.length == 2) {
        String airlineId = parts[0];
        Integer flightNumber = Integer.parseInt(parts[1]);
        List<SalesReservationView> flightSales = reportingService.getSalesByFlight(airlineId, flightNumber);
        BigDecimal flightTotal = reportingService.getSalesTotalByFlight(airlineId, flightNumber);
        response.put("flightSales", flightSales);
        response.put("flightTotal", flightTotal);
      }
    }

    // Customer sales
    if (customer != null) {
      List<SalesCustomerReservationView> customerSales = reportingService.getSalesByCustomer(customer);
      BigDecimal customerTotal = reportingService.getSalesTotalByCustomer(customer);
      response.put("customerSales", customerSales);
      response.put("customerTotal", customerTotal);
    }

    // City sales
    if (city != null && !city.isEmpty()) {
      List<SalesCityReservationView> citySales = reportingService.getSalesByCity(city);
      response.put("citySales", citySales);
    }

    // Filter options
    List<FlightOptionView> flightOptions = reportingService.getFlightOptions();
    List<CustomerOptionView> customerOptions = reportingService.getCustomerOptions();
    List<String> cityOptions = reportingService.getCityOptions();

    response.put("flightOptions", flightOptions);
    response.put("customerOptions", customerOptions);
    response.put("cityOptions", cityOptions);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/reservations")
  public ResponseEntity<Map<String, Object>> getReservations(
      @RequestParam(required = false) String flight,
      @RequestParam(required = false) Long customer) {

    Map<String, Object> response = new HashMap<>();

    // Flight reservations
    if (flight != null && !flight.isEmpty()) {
      String[] parts = flight.split("-");
      if (parts.length == 2) {
        String airlineId = parts[0];
        Integer flightNumber = Integer.parseInt(parts[1]);
        List<SalesReservationView> flightReservations = reportingService.getSalesByFlight(airlineId, flightNumber);
        response.put("flightReservations", flightReservations);
      }
    }

    // Customer reservations
    if (customer != null) {
      List<SalesCustomerReservationView> customerReservations = reportingService.getSalesByCustomer(customer);
      response.put("customerReservations", customerReservations);
    }

    // Filter options
    List<FlightOptionView> flightOptions = reportingService.getFlightOptions();
    List<CustomerOptionView> customerOptions = reportingService.getCustomerOptions();

    response.put("flightOptions", flightOptions);
    response.put("customerOptions", customerOptions);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/flights")
  public ResponseEntity<Map<String, Object>> getFlights(
      @RequestParam(required = false) String flight,
      @RequestParam(required = false) String airport) {

    Map<String, Object> response = new HashMap<>();

    // Most active flights
    List<FlightActivityView> mostActiveFlights = flightsService.getMostActiveFlights();
    response.put("mostActiveFlights", mostActiveFlights);

    // All flights
    List<FlightSummaryView> allFlights = flightsService.getAllFlights();
    response.put("allFlights", allFlights);

    // Flight details
    if (flight != null && !flight.isEmpty()) {
      String[] parts = flight.split("-");
      if (parts.length == 2) {
        String airlineId = parts[0];
        Integer flightNumber = Integer.parseInt(parts[1]);
        List<FlightStopView> flightStops = flightsService.getFlightStops(airlineId, flightNumber);
        response.put("flightStops", flightStops);
      }
    }

    // Airport flights
    if (airport != null && !airport.isEmpty()) {
      List<FlightSummaryView> airportFlights = flightsService.getFlightsForAirport(airport);
      response.put("airportFlights", airportFlights);
    }

    // Filter options
    List<FlightOptionView> flightOptions = reportingService.getFlightOptions();
    List<AirportOptionView> airportOptions = flightsService.getAirportOptions();

    response.put("flightOptions", flightOptions);
    response.put("airportOptions", airportOptions);

    return ResponseEntity.ok(response);
  }
}
