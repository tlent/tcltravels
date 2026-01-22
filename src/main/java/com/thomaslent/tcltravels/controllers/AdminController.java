package com.thomaslent.tcltravels.controllers;

import java.math.BigDecimal;
import java.time.Month;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.access.prepost.PreAuthorize;

import com.thomaslent.tcltravels.dto.AirportOptionView;
import com.thomaslent.tcltravels.dto.CustomerOnFlightView;
import com.thomaslent.tcltravels.dto.CustomerOptionView;
import com.thomaslent.tcltravels.dto.FlightActivityView;
import com.thomaslent.tcltravels.dto.FlightOptionView;
import com.thomaslent.tcltravels.dto.FlightSummaryView;
import com.thomaslent.tcltravels.dto.SalesCityReservationView;
import com.thomaslent.tcltravels.dto.SalesCustomerReservationView;
import com.thomaslent.tcltravels.dto.SalesReservationView;
import com.thomaslent.tcltravels.dto.TopRevenueView;
import com.thomaslent.tcltravels.services.AdminEmployeeService;
import com.thomaslent.tcltravels.services.AdminFlightsService;
import com.thomaslent.tcltravels.services.AdminReportingService;
import com.thomaslent.tcltravels.repositories.ReservationPassengerRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {
  private AdminReportingService adminReportingService;
  private AdminEmployeeService adminEmployeeService;
  private AdminFlightsService adminFlightsService;
  private ReservationPassengerRepository reservationPassengerRepository;

  public AdminController(AdminReportingService adminReportingService, AdminEmployeeService adminEmployeeService,
      AdminFlightsService adminFlightsService, ReservationPassengerRepository reservationPassengerRepository) {
    this.adminReportingService = adminReportingService;
    this.adminEmployeeService = adminEmployeeService;
    this.adminFlightsService = adminFlightsService;
    this.reservationPassengerRepository = reservationPassengerRepository;
  }

  @GetMapping({ "", "/" })
  @PreAuthorize("hasAnyRole('MANAGER','EMPLOYEE')")
  public String getAdminDashboard(
      @RequestParam(name = "month", required = false) Integer month,
      @RequestParam(name = "year", required = false) Integer year,
      Model model) {
    int selectedMonth = month == null ? 1 : month;
    int selectedYear = year == null ? 2011 : year;

    TopRevenueView topCustomer = adminReportingService.getTopCustomer();
    TopRevenueView topEmployee = adminReportingService.getTopEmployee();
    List<SalesReservationView> monthSales = adminReportingService.getMonthlySales(selectedMonth, selectedYear);
    BigDecimal monthTotal = adminReportingService.getMonthlySalesTotal(selectedMonth, selectedYear);

    model.addAttribute("topCustomer", topCustomer);
    model.addAttribute("topEmployee", topEmployee);
    model.addAttribute("employees", adminEmployeeService.getEmployees());
    model.addAttribute("monthSales", monthSales);
    model.addAttribute("monthTotal", monthTotal);
    model.addAttribute("selectedMonth", selectedMonth);
    model.addAttribute("selectedYear", selectedYear);
    model.addAttribute("months", Month.values());
    model.addAttribute("years", List.of(2011, 2012, 2013, 2014, 2015));
    return "admin/index";
  }

  @GetMapping("/sales")
  @PreAuthorize("hasAnyRole('MANAGER','EMPLOYEE')")
  public String getSalesReport(
      @RequestParam(name = "month", required = false) Integer month,
      @RequestParam(name = "year", required = false) Integer year,
      @RequestParam(name = "flight", required = false) String flightKey,
      @RequestParam(name = "customer", required = false) Long customerId,
      @RequestParam(name = "city", required = false) String city,
      Model model) {
    int selectedMonth = month == null ? 1 : month;
    int selectedYear = year == null ? 2011 : year;

    List<FlightOptionView> flightOptions = adminReportingService.getFlightOptions();
    String selectedFlightKey = selectFlightKey(flightKey, flightOptions);
    FlightOptionView selectedFlight = parseFlightKey(selectedFlightKey);

    List<CustomerOptionView> customerOptions = adminReportingService.getCustomerOptions();
    Long selectedCustomer = selectCustomerId(customerId, customerOptions);
    String selectedCustomerName = customerOptions.stream()
        .filter(option -> option.customerId().equals(selectedCustomer))
        .map(option -> option.firstName() + " " + option.lastName())
        .findFirst()
        .orElse("");

    List<String> cityOptions = adminReportingService.getCityOptions();
    String selectedCity = selectCity(city, cityOptions);

    model.addAttribute("topCustomer", adminReportingService.getTopCustomer());
    model.addAttribute("topEmployee", adminReportingService.getTopEmployee());

    model.addAttribute("months", Month.values());
    model.addAttribute("years", List.of(2011, 2012, 2013, 2014, 2015));
    model.addAttribute("selectedMonth", selectedMonth);
    model.addAttribute("selectedYear", selectedYear);
    model.addAttribute("monthSales", adminReportingService.getMonthlySales(selectedMonth, selectedYear));
    model.addAttribute("monthTotal", adminReportingService.getMonthlySalesTotal(selectedMonth, selectedYear));

    model.addAttribute("flightOptions", flightOptions);
    model.addAttribute("selectedFlightKey", selectedFlightKey);
    model.addAttribute("flightSales", adminReportingService.getSalesByFlight(
        selectedFlight.airlineId(), selectedFlight.flightNumber()));
    model.addAttribute("flightTotal", adminReportingService.getSalesTotalByFlight(
        selectedFlight.airlineId(), selectedFlight.flightNumber()));

    model.addAttribute("customerOptions", customerOptions);
    model.addAttribute("selectedCustomer", selectedCustomer);
    model.addAttribute("customerSales", adminReportingService.getSalesByCustomer(selectedCustomer));
    model.addAttribute("customerTotal", adminReportingService.getSalesTotalByCustomer(selectedCustomer));
    model.addAttribute("selectedCustomerName", selectedCustomerName);

    model.addAttribute("cityOptions", cityOptions);
    model.addAttribute("selectedCity", selectedCity);
    List<SalesCityReservationView> citySales = adminReportingService.getSalesByCity(selectedCity);
    BigDecimal cityTotal = citySales.stream()
        .map(SalesCityReservationView::totalFare)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    model.addAttribute("citySales", citySales);
    model.addAttribute("cityTotal", cityTotal);

    return "admin/sales";
  }

  @GetMapping("/reservations")
  @PreAuthorize("hasAnyRole('MANAGER','EMPLOYEE')")
  public String getReservations(
      @RequestParam(name = "flight", required = false) String flightKey,
      @RequestParam(name = "customer", required = false) Long customerId,
      Model model) {
    List<FlightOptionView> flightOptions = adminReportingService.getFlightOptions();
    String selectedFlightKey = selectFlightKey(flightKey, flightOptions);
    FlightOptionView selectedFlight = parseFlightKey(selectedFlightKey);

    List<CustomerOptionView> customerOptions = adminReportingService.getCustomerOptions();
    Long selectedCustomer = selectCustomerId(customerId, customerOptions);
    String selectedCustomerName = customerOptions.stream()
        .filter(option -> option.customerId().equals(selectedCustomer))
        .map(option -> option.firstName() + " " + option.lastName())
        .findFirst()
        .orElse("");

    List<SalesReservationView> flightReservations = adminReportingService.getSalesByFlight(
        selectedFlight.airlineId(), selectedFlight.flightNumber());

    List<SalesCustomerReservationView> customerReservations = adminReportingService.getSalesByCustomer(
        selectedCustomer);

    model.addAttribute("flightOptions", flightOptions);
    model.addAttribute("selectedFlightKey", selectedFlightKey);
    model.addAttribute("flightReservations", flightReservations);

    model.addAttribute("customerOptions", customerOptions);
    model.addAttribute("selectedCustomer", selectedCustomer);
    model.addAttribute("customerReservations", customerReservations);
    model.addAttribute("selectedCustomerName", selectedCustomerName);

    return "admin/reservations";
  }

  @GetMapping("/flights")
  @PreAuthorize("hasAnyRole('MANAGER','EMPLOYEE')")
  public String getFlights(
      @RequestParam(name = "flight", required = false) String flightKey,
      @RequestParam(name = "airport", required = false) String airportId,
      Model model) {
    List<FlightActivityView> mostActiveFlights = adminFlightsService.getMostActiveFlights();
    List<FlightSummaryView> allFlights = adminFlightsService.getAllFlights();
    model.addAttribute("mostActiveFlights", mostActiveFlights);
    model.addAttribute("allFlights", allFlights);
    model.addAttribute("flightStops", adminFlightsService.getAllFlightStops(allFlights));

    List<FlightOptionView> flightOptions = adminReportingService.getFlightOptions();
    String selectedFlightKey = selectFlightKey(flightKey, flightOptions);
    FlightOptionView selectedFlight = parseFlightKey(selectedFlightKey);
    List<CustomerOnFlightView> customersOnFlight = reservationPassengerRepository
        .findCustomersOnFlight(selectedFlight.airlineId(), selectedFlight.flightNumber());

    List<AirportOptionView> airportOptions = adminFlightsService.getAirportOptions();
    String selectedAirport = selectAirportId(airportId, airportOptions);
    String selectedAirportName = airportOptions.stream()
        .filter(option -> option.id().equals(selectedAirport))
        .map(AirportOptionView::name)
        .findFirst()
        .orElse("");
    List<FlightSummaryView> airportFlights = adminFlightsService.getFlightsForAirport(selectedAirport);

    model.addAttribute("flightOptions", flightOptions);
    model.addAttribute("selectedFlightKey", selectedFlightKey);
    model.addAttribute("customersOnFlight", customersOnFlight);

    model.addAttribute("airportOptions", airportOptions);
    model.addAttribute("selectedAirport", selectedAirport);
    model.addAttribute("selectedAirportName", selectedAirportName);
    model.addAttribute("airportFlights", airportFlights);

    return "admin/flights";
  }

  private String selectFlightKey(String flightKey, List<FlightOptionView> options) {
    if (flightKey != null && flightKey.length() > 2) {
      return flightKey;
    }
    if (options.isEmpty()) {
      return "";
    }
    FlightOptionView first = options.get(0);
    return first.airlineId() + first.flightNumber();
  }

  private FlightOptionView parseFlightKey(String flightKey) {
    if (flightKey == null || flightKey.length() < 3) {
      return new FlightOptionView("", 0);
    }
    String airlineId = flightKey.substring(0, 2);
    Integer flightNumber = Integer.parseInt(flightKey.substring(2));
    return new FlightOptionView(airlineId, flightNumber);
  }

  private Long selectCustomerId(Long customerId, List<CustomerOptionView> options) {
    if (customerId != null) {
      return customerId;
    }
    if (options.isEmpty()) {
      return 0L;
    }
    return options.get(0).customerId();
  }

  private String selectCity(String city, List<String> options) {
    if (city != null && !city.isBlank()) {
      return city;
    }
    if (options.isEmpty()) {
      return "";
    }
    return options.get(0);
  }

  private String selectAirportId(String airportId, List<AirportOptionView> options) {
    if (airportId != null && !airportId.isBlank()) {
      return airportId;
    }
    if (options.isEmpty()) {
      return "";
    }
    return options.get(0).id();
  }
}
