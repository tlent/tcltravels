package com.thomaslent.tcltravels.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thomaslent.tcltravels.dto.BidForm;
import com.thomaslent.tcltravels.dto.LegView;
import com.thomaslent.tcltravels.dto.ReservationForm;
import com.thomaslent.tcltravels.dto.ReservationLegRow;
import com.thomaslent.tcltravels.dto.ReservationView;
import com.thomaslent.tcltravels.dto.StopView;
import com.thomaslent.tcltravels.entities.Auction;
import com.thomaslent.tcltravels.entities.Customer;
import com.thomaslent.tcltravels.entities.CustomerPreference;
import com.thomaslent.tcltravels.entities.CustomerPreferenceId;
import com.thomaslent.tcltravels.entities.Employee;
import com.thomaslent.tcltravels.entities.Fare;
import com.thomaslent.tcltravels.entities.Flight;
import com.thomaslent.tcltravels.entities.FlightId;
import com.thomaslent.tcltravels.entities.Leg;
import com.thomaslent.tcltravels.entities.LegId;
import com.thomaslent.tcltravels.entities.Passenger;
import com.thomaslent.tcltravels.entities.Person;
import com.thomaslent.tcltravels.entities.Reservation;
import com.thomaslent.tcltravels.entities.ReservationPassenger;
import com.thomaslent.tcltravels.entities.ReservationPassengerId;
import com.thomaslent.tcltravels.entities.StopsAt;
import com.thomaslent.tcltravels.repositories.AdvancePurchaseDiscountRepository;
import com.thomaslent.tcltravels.repositories.AuctionRepository;
import com.thomaslent.tcltravels.repositories.CustomerPreferenceRepository;
import com.thomaslent.tcltravels.repositories.CustomerRepository;
import com.thomaslent.tcltravels.repositories.EmployeeRepository;
import com.thomaslent.tcltravels.repositories.FareRepository;
import com.thomaslent.tcltravels.repositories.FlightRepository;
import com.thomaslent.tcltravels.repositories.LegRepository;
import com.thomaslent.tcltravels.repositories.PassengerRepository;
import com.thomaslent.tcltravels.repositories.PersonRepository;
import com.thomaslent.tcltravels.repositories.ReservationPassengerRepository;
import com.thomaslent.tcltravels.repositories.ReservationRepository;
import com.thomaslent.tcltravels.repositories.StopsAtRepository;

@Service
public class ReservationsService {
  private static final BigDecimal BOOKING_FEE = new BigDecimal("30.00");
  private static final int DEFAULT_EMPLOYEE_SSN = 123456789;
  private ReservationRepository reservationRepository;
  private LegRepository legRepository;
  private StopsAtRepository stopsAtRepository;
  private PersonRepository personRepository;
  private CustomerRepository customerRepository;
  private EmployeeRepository employeeRepository;
  private FlightRepository flightRepository;
  private FareRepository fareRepository;
  private AdvancePurchaseDiscountRepository discountRepository;
  private PassengerRepository passengerRepository;
  private ReservationPassengerRepository reservationPassengerRepository;
  private CustomerPreferenceRepository customerPreferenceRepository;
  private AuctionRepository auctionRepository;

  public ReservationsService(ReservationRepository reservationRepository, LegRepository legRepository,
      StopsAtRepository stopsAtRepository, PersonRepository personRepository, CustomerRepository customerRepository,
      EmployeeRepository employeeRepository, FlightRepository flightRepository, FareRepository fareRepository,
      AdvancePurchaseDiscountRepository discountRepository, PassengerRepository passengerRepository,
      ReservationPassengerRepository reservationPassengerRepository,
      CustomerPreferenceRepository customerPreferenceRepository, AuctionRepository auctionRepository) {
    this.reservationRepository = reservationRepository;
    this.legRepository = legRepository;
    this.stopsAtRepository = stopsAtRepository;
    this.personRepository = personRepository;
    this.customerRepository = customerRepository;
    this.employeeRepository = employeeRepository;
    this.flightRepository = flightRepository;
    this.fareRepository = fareRepository;
    this.discountRepository = discountRepository;
    this.passengerRepository = passengerRepository;
    this.reservationPassengerRepository = reservationPassengerRepository;
    this.customerPreferenceRepository = customerPreferenceRepository;
    this.auctionRepository = auctionRepository;
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

  public List<StopView> getStopsForFlight(String airlineId, Integer flightNumber) {
    return stopsAtRepository.findByIdFlightIdAirlineIdAndIdFlightIdFlightNumberOrderByIdStopNumber(
        airlineId, flightNumber).stream()
        .map(stop -> new StopView(
            stop.getId().getFlightId().getAirlineId(),
            stop.getAirport().getId(),
            stop.getId().getFlightId().getFlightNumber(),
            stop.getId().getStopNumber(),
            stop.getArrivalTime(),
            stop.getDepartureTime(),
            stop.getDepartureTime() == null ? null : stop.getDepartureTime().toLocalDate()))
        .collect(Collectors.toList());
  }

  public LocalDate getDefaultDepartureDate(String airlineId, Integer flightNumber) {
    OffsetDateTime departure = null;
    if (airlineId != null && flightNumber != null) {
      var stop = stopsAtRepository
          .findFirstByIdFlightIdAirlineIdAndIdFlightIdFlightNumberAndIdStopNumberOrderByDepartureTimeAsc(
              airlineId, flightNumber, 1);
      if (stop != null) {
        departure = stop.getDepartureTime();
      }
    }
    return departure == null ? null : departure.toLocalDate();
  }

  @Transactional
  public Optional<String> createReservation(ReservationForm form, Long accountNumber) {
    if (form.getOrigin() != null && form.getDestination() != null && form.getOrigin() >= form.getDestination()) {
      return Optional.of("Invalid origin and destination.");
    }
    Optional<String> dateError = validateDepartureDate(form.getAirlineId(), form.getFlightNumber(),
        form.getOrigin(), form.getDepartureDate());
    if (dateError.isPresent()) {
      return dateError;
    }

    BigDecimal totalCost = calculateFareTotal(form);
    if (totalCost == null) {
      return Optional.of("Fare information is unavailable for this flight.");
    }

    Optional<Reservation> reservationOpt = createReservationRecord(accountNumber, totalCost);
    if (reservationOpt.isEmpty()) {
      return Optional.of("Reservation could not be created.");
    }
    Reservation reservation = reservationOpt.get();
    Optional<Flight> flightOpt = loadFlight(form.getAirlineId(), form.getFlightNumber());
    if (flightOpt.isEmpty()) {
      return Optional.of("Flight information is unavailable.");
    }
    Flight flight = flightOpt.get();
    insertLegs(reservation, flight, form.getOrigin(), form.getDestination());

    int seatNumber = (int) reservationPassengerRepository
        .countSeatsForFlight(form.getAirlineId(), form.getFlightNumber()) + 1;
    if (form.getOther() != null && !form.getOther().isBlank()
        && !customerPreferenceRepository.existsByIdAccountNumberAndIdPreference(accountNumber, form.getOther())) {
      CustomerPreferenceId preferenceId = new CustomerPreferenceId();
      preferenceId.setAccountNumber(accountNumber);
      preferenceId.setPreference(form.getOther());
      CustomerPreference preference = new CustomerPreference();
      preference.setId(preferenceId);
      customerPreferenceRepository.save(preference);
    }

    Customer customer = customerRepository.findById(accountNumber).orElse(null);
    if (customer == null) {
      return Optional.of("Account information is missing.");
    }

    for (int i = 1; i <= form.getPassengerCount(); i++) {
      String firstName = Optional.ofNullable(form.getFirstName(i)).orElse("");
      String lastName = Optional.ofNullable(form.getLastName(i)).orElse("");
      String passengerName = (firstName + " " + lastName).trim();
      Passenger passenger = new Passenger();
      passenger.setCustomer(customer);
      passenger.setPassengerName(passengerName);
      passenger = passengerRepository.save(passenger);

      ReservationPassengerId id = new ReservationPassengerId();
      id.setReservationNumber(reservation.getReservationNumber());
      id.setPassengerId(passenger.getId());
      id.setAccountNumber(accountNumber);
      ReservationPassenger reservationPassenger = new ReservationPassenger();
      reservationPassenger.setId(id);
      reservationPassenger.setSeatNumber(seatNumber);
      reservationPassenger.setSeatClass(form.getFlightClass(i));
      reservationPassenger.setMeal(form.getFood(i));
      reservationPassengerRepository.save(reservationPassenger);
      seatNumber++;
    }

    return Optional.empty();
  }

  @Transactional
  public BidResult submitBid(BidForm form, Long accountNumber, Long personId) {
    Optional<String> dateError = validateDepartureDate(form.getAirlineId(), form.getFlightNumber(),
        1, form.getDepartureDate());
    if (dateError.isPresent()) {
      return new BidResult(false, false, dateError.get());
    }

    Fare hiddenFare = fareRepository.findByIdAirlineIdAndIdFlightNumberAndIdFareTypeAndIdSeatClass(
        form.getAirlineId(), form.getFlightNumber(), 2, form.getFlightClass()).orElse(null);
    if (hiddenFare == null) {
      return new BidResult(false, false, "Fare information is unavailable for this flight.");
    }

    BigDecimal discountRate = getDiscountRate(form.getAirlineId(), form.getDepartureDate());
    BigDecimal fare = hiddenFare.getFare().multiply(BigDecimal.ONE.subtract(discountRate))
        .setScale(2, RoundingMode.HALF_UP);

    if (form.getBid().compareTo(fare) <= 0) {
      if (!createAuction(accountNumber, form, false)) {
        return new BidResult(false, false, "Auction could not be saved.");
      }
      return new BidResult(false, true, null);
    }

    if (!createAuction(accountNumber, form, true)) {
      return new BidResult(false, false, "Auction could not be saved.");
    }

    StopsAt lastStop = stopsAtRepository
        .findTopByIdFlightIdAirlineIdAndIdFlightIdFlightNumberOrderByIdStopNumberDesc(
            form.getAirlineId(), form.getFlightNumber());
    if (lastStop == null) {
      return new BidResult(false, false, "Flight stops could not be loaded.");
    }

    Person person = personRepository.findById(personId).orElse(null);
    if (person == null) {
      return new BidResult(false, false, "Account information is missing.");
    }

    Optional<Reservation> reservationOpt = createReservationRecord(accountNumber, form.getBid());
    if (reservationOpt.isEmpty()) {
      return new BidResult(false, false, "Reservation could not be created.");
    }
    Reservation reservation = reservationOpt.get();
    Optional<Flight> flightOpt = loadFlight(form.getAirlineId(), form.getFlightNumber());
    if (flightOpt.isEmpty()) {
      return new BidResult(false, false, "Flight information is unavailable.");
    }
    Flight flight = flightOpt.get();
    insertLegs(reservation, flight, 1, lastStop.getId().getStopNumber());

    if (form.getOther() != null && !form.getOther().isBlank()
        && !customerPreferenceRepository.existsByIdAccountNumberAndIdPreference(accountNumber, form.getOther())) {
      CustomerPreferenceId preferenceId = new CustomerPreferenceId();
      preferenceId.setAccountNumber(accountNumber);
      preferenceId.setPreference(form.getOther());
      CustomerPreference preference = new CustomerPreference();
      preference.setId(preferenceId);
      customerPreferenceRepository.save(preference);
    }

    Customer customer = customerRepository.findById(accountNumber).orElse(null);
    if (customer == null) {
      return new BidResult(false, false, "Account information is missing.");
    }

    int seatNumber = (int) reservationPassengerRepository
        .countSeatsForFlight(form.getAirlineId(), form.getFlightNumber()) + 1;
    String passengerName = String.format("%s %s", person.getFirstName(), person.getLastName()).trim();
    Passenger passenger = new Passenger();
    passenger.setCustomer(customer);
    passenger.setPassengerName(passengerName);
    passenger = passengerRepository.save(passenger);

    ReservationPassengerId id = new ReservationPassengerId();
    id.setReservationNumber(reservation.getReservationNumber());
    id.setPassengerId(passenger.getId());
    id.setAccountNumber(accountNumber);
    ReservationPassenger reservationPassenger = new ReservationPassenger();
    reservationPassenger.setId(id);
    reservationPassenger.setSeatNumber(seatNumber);
    reservationPassenger.setSeatClass(form.getFlightClass());
    reservationPassenger.setMeal(form.getFood());
    reservationPassengerRepository.save(reservationPassenger);

    return new BidResult(true, false, null);
  }

  private Optional<String> validateDepartureDate(String airlineId, Integer flightNumber, Integer originStop,
      LocalDate departureDate) {
    if (departureDate == null) {
      return Optional.of("You must enter a departure date.");
    }
    if (departureDate.isBefore(LocalDate.now())) {
      return Optional.of("The entered departure date has already passed.");
    }
    if (originStop == null || !isFlightAvailableOnDate(airlineId, flightNumber, originStop, departureDate)) {
      return Optional.of(String.format("%s flight number %d is not available on %s.",
          airlineId, flightNumber, departureDate));
    }
    return Optional.empty();
  }

  private BigDecimal calculateFareTotal(ReservationForm form) {
    BigDecimal totalFares = BigDecimal.ZERO;
    for (int i = 1; i <= form.getPassengerCount(); i++) {
      Fare fare = fareRepository.findByIdAirlineIdAndIdFlightNumberAndIdFareTypeAndIdSeatClass(
          form.getAirlineId(), form.getFlightNumber(), 0, form.getFlightClass(i)).orElse(null);
      if (fare == null) {
        return null;
      }
      totalFares = totalFares.add(fare.getFare());
    }

    BigDecimal discountRate = getDiscountRate(form.getAirlineId(), form.getDepartureDate());
    return totalFares.multiply(BigDecimal.ONE.subtract(discountRate))
        .setScale(2, RoundingMode.HALF_UP);
  }

  private BigDecimal getDiscountRate(String airlineId, LocalDate departureDate) {
    long days = ChronoUnit.DAYS.between(LocalDate.now(), departureDate);
    var discount = discountRepository
        .findTopByIdAirlineIdAndIdDaysLessThanEqualOrderByIdDaysDesc(airlineId, (int) days)
        .orElse(null);
    if (discount == null || discount.getDiscountRate() == null) {
      return BigDecimal.ZERO;
    }
    return discount.getDiscountRate().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
  }

  private boolean isFlightAvailableOnDate(String airlineId, Integer flightNumber, Integer originStop,
      LocalDate departureDate) {
    OffsetDateTime start = departureDate.atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime();
    OffsetDateTime end = departureDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toOffsetDateTime();
    return stopsAtRepository.existsByIdFlightIdAirlineIdAndIdFlightIdFlightNumberAndIdStopNumberAndDepartureTimeBetween(
        airlineId, flightNumber, originStop, start, end);
  }

  private Optional<Reservation> createReservationRecord(Long accountNumber, BigDecimal totalFare) {
    Customer customer = customerRepository.findById(accountNumber).orElse(null);
    Employee employee = employeeRepository.findById(DEFAULT_EMPLOYEE_SSN).orElse(null);
    if (customer == null || employee == null) {
      return Optional.empty();
    }
    Reservation reservation = new Reservation();
    reservation.setReservationDate(OffsetDateTime.now());
    reservation.setBookingFee(BOOKING_FEE);
    reservation.setTotalFare(totalFare);
    reservation.setEmployee(employee);
    reservation.setCustomer(customer);
    return Optional.of(reservationRepository.save(reservation));
  }

  private Optional<Flight> loadFlight(String airlineId, Integer flightNumber) {
    FlightId id = new FlightId();
    id.setAirlineId(airlineId);
    id.setFlightNumber(flightNumber);
    return flightRepository.findById(id);
  }

  private void insertLegs(Reservation reservation, Flight flight, Integer originStop, Integer destinationStop) {
    int legNumber = 1;
    for (int stop = originStop; stop < destinationStop; stop++) {
      Leg leg = new Leg();
      LegId legId = new LegId();
      legId.setReservationNumber(reservation.getReservationNumber());
      legId.setLegNumber(legNumber);
      leg.setId(legId);
      leg.setReservation(reservation);
      leg.setFlight(flight);
      leg.setFromStopNumber(stop);
      legRepository.save(leg);
      legNumber++;
    }
  }

  private boolean createAuction(Long accountNumber, BidForm form, boolean accepted) {
    Customer customer = customerRepository.findById(accountNumber).orElse(null);
    if (customer == null) {
      return false;
    }
    Auction auction = new Auction();
    auction.setCustomer(customer);
    auction.setAirlineId(form.getAirlineId());
    auction.setFlightNumber(form.getFlightNumber());
    auction.setSeatingClass(form.getFlightClass());
    auction.setDate(LocalDate.now());
    auction.setNameYourOwnPrice(form.getBid());
    auction.setAccepted(accepted);
    auctionRepository.save(auction);
    return true;
  }

  public record BidResult(boolean accepted, boolean rejected, String error) {
  }
}
