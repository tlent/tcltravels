package com.thomaslent.tcltravels.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.thomaslent.tcltravels.dto.PassengerInfo;
import com.thomaslent.tcltravels.dto.ReservationForm;
import com.thomaslent.tcltravels.entities.Customer;
import com.thomaslent.tcltravels.entities.Employee;
import com.thomaslent.tcltravels.entities.Fare;
import com.thomaslent.tcltravels.entities.Flight;
import com.thomaslent.tcltravels.entities.Passenger;
import com.thomaslent.tcltravels.entities.Reservation;
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

@ExtendWith(MockitoExtension.class)
class ReservationsServiceTest {

  @Mock
  private ReservationRepository reservationRepository;

  @Mock
  private LegRepository legRepository;

  @Mock
  private StopsAtRepository stopsAtRepository;

  @Mock
  private PersonRepository personRepository;

  @Mock
  private CustomerRepository customerRepository;

  @Mock
  private EmployeeRepository employeeRepository;

  @Mock
  private FlightRepository flightRepository;

  @Mock
  private FareRepository fareRepository;

  @Mock
  private AdvancePurchaseDiscountRepository discountRepository;

  @Mock
  private PassengerRepository passengerRepository;

  @Mock
  private ReservationPassengerRepository reservationPassengerRepository;

  @Mock
  private CustomerPreferenceRepository customerPreferenceRepository;

  @Mock
  private AuctionRepository auctionRepository;

  @InjectMocks
  private ReservationsService reservationsService;

  private ReservationForm validReservationForm;
  private Flight mockFlight;
  private Fare mockFare;
  private Customer mockCustomer;
  private Reservation mockReservation;
  private Employee mockEmployee;
  private StopsAt mockStopsAt;

  @BeforeEach
  void setUp() {
    // Create valid reservation form
    validReservationForm = new ReservationForm();
    validReservationForm.setAirlineId("AA");
    validReservationForm.setFlightNumber(100);
    validReservationForm.setOrigin(1);
    validReservationForm.setDestination(5);
    validReservationForm.setPassengerCount(2);
    validReservationForm.setDepartureDate(LocalDate.now().plusDays(30));
    validReservationForm.setFirst1("John");
    validReservationForm.setLast1("Doe");
    validReservationForm.setClass1("Economy");
    validReservationForm.setFirst2("Jane");
    validReservationForm.setLast2("Smith");
    validReservationForm.setClass2("Economy");

    // Mock flight with 150 seats
    mockFlight = new Flight();
    mockFlight.setNumberOfSeats(150);

    // Mock fare
    mockFare = new Fare();
    mockFare.setFare(new BigDecimal("200.00"));

    // Mock customer
    mockCustomer = new Customer();
    mockCustomer.setId(1L);

    // Mock reservation
    mockReservation = new Reservation();
    mockReservation.setId(1L);

    // Mock employee
    mockEmployee = new Employee();
    mockEmployee.setId(1L);

    // Mock StopsAt
    mockStopsAt = new StopsAt();
    mockStopsAt.setDepartureTime(OffsetDateTime.now().plusDays(30));
  }

  @Test
  void createReservation_withInvalidOriginDestination_returnsError() {
    ReservationForm form = new ReservationForm();
    form.setOrigin(5);
    form.setDestination(3); // Destination before origin

    Optional<String> result = reservationsService.createReservation(form, 1L);

    assertThat(result).isPresent();
    assertThat(result.get()).isEqualTo("Invalid origin and destination.");
    verify(flightRepository, never()).findByAirline_IdAndFlightNumber(anyString(), anyInt());
  }

  @Test
  void createReservation_whenNoSeatsAvailable_returnsError() {
    // Setup: flight has 150 seats, 150 already booked
    setupValidReservationMocks();
    when(reservationPassengerRepository.countSeatsForFlight("AA", 100))
        .thenReturn(150L); // All seats taken

    Optional<String> result = reservationsService.createReservation(validReservationForm, 1L);

    assertThat(result).isPresent();
    assertThat(result.get()).contains("Not enough seats available");
    assertThat(result.get()).contains("Requested: 2");
    assertThat(result.get()).contains("Available: 0");
    verify(reservationRepository, never()).save(any(Reservation.class));
  }

  @Test
  void createReservation_withPartialSeatsAvailable_returnsError() {
    // Setup: flight has 150 seats, 149 already booked, requesting 2
    setupValidReservationMocks();
    when(reservationPassengerRepository.countSeatsForFlight("AA", 100))
        .thenReturn(149L); // Only 1 seat available

    Optional<String> result = reservationsService.createReservation(validReservationForm, 1L);

    assertThat(result).isPresent();
    assertThat(result.get()).contains("Not enough seats available");
    assertThat(result.get()).contains("Requested: 2");
    assertThat(result.get()).contains("Available: 1");
  }

  @Test
  void createReservation_withExactSeatsAvailable_succeeds() {
    // Setup: flight has 150 seats, 148 booked, requesting 2 (exactly available)
    setupValidReservationMocks();
    when(reservationPassengerRepository.countSeatsForFlight("AA", 100))
        .thenReturn(148L, 148L); // 2 seats available
    when(customerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer));
    when(employeeRepository.findById(1L)).thenReturn(Optional.of(mockEmployee));
    when(reservationRepository.save(any(Reservation.class))).thenReturn(mockReservation);
    when(passengerRepository.save(any(Passenger.class))).thenAnswer(inv -> inv.getArgument(0));

    Optional<String> result = reservationsService.createReservation(validReservationForm, 1L);

    assertThat(result).isEmpty(); // Success
    verify(reservationRepository).save(any(Reservation.class));
  }

  @Test
  void createReservation_withPlentyOfSeatsAvailable_succeeds() {
    // Setup: flight has 150 seats, 50 booked, requesting 2 (plenty available)
    setupValidReservationMocks();
    when(reservationPassengerRepository.countSeatsForFlight("AA", 100))
        .thenReturn(50L, 50L); // 100 seats available
    when(customerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer));
    when(employeeRepository.findById(1L)).thenReturn(Optional.of(mockEmployee));
    when(reservationRepository.save(any(Reservation.class))).thenReturn(mockReservation);
    when(passengerRepository.save(any(Passenger.class))).thenAnswer(inv -> inv.getArgument(0));

    Optional<String> result = reservationsService.createReservation(validReservationForm, 1L);

    assertThat(result).isEmpty(); // Success
  }

  @Test
  void createReservation_withSinglePassenger_calculatesSeatsCorrectly() {
    // Setup: requesting 1 seat when 149 are taken (1 available)
    validReservationForm.setPassengerCount(1);
    setupValidReservationMocks();
    when(reservationPassengerRepository.countSeatsForFlight("AA", 100))
        .thenReturn(149L, 149L); // 1 seat available
    when(customerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer));
    when(employeeRepository.findById(1L)).thenReturn(Optional.of(mockEmployee));
    when(reservationRepository.save(any(Reservation.class))).thenReturn(mockReservation);
    when(passengerRepository.save(any(Passenger.class))).thenAnswer(inv -> inv.getArgument(0));

    Optional<String> result = reservationsService.createReservation(validReservationForm, 1L);

    assertThat(result).isEmpty(); // Success - exactly 1 seat available
  }

  @Test
  void createReservation_withMaxPassengers_checksSeatCapacity() {
    // Setup: requesting 5 seats when only 4 are available
    validReservationForm.setPassengerCount(5);
    validReservationForm.setFirst3("Alice");
    validReservationForm.setLast3("Johnson");
    validReservationForm.setClass3("Economy");
    validReservationForm.setFirst4("Bob");
    validReservationForm.setLast4("Williams");
    validReservationForm.setClass4("Economy");
    validReservationForm.setFirst5("Carol");
    validReservationForm.setLast5("Brown");
    validReservationForm.setClass5("Economy");

    setupValidReservationMocks();
    when(reservationPassengerRepository.countSeatsForFlight("AA", 100))
        .thenReturn(146L); // Only 4 seats available

    Optional<String> result = reservationsService.createReservation(validReservationForm, 1L);

    assertThat(result).isPresent();
    assertThat(result.get()).contains("Not enough seats available");
    assertThat(result.get()).contains("Requested: 5");
    assertThat(result.get()).contains("Available: 4");
  }

  @Test
  void createReservation_withPassengerList_succeeds() {
    // Setup: Use the new structured passenger list format
    ReservationForm form = new ReservationForm();
    form.setAirlineId("AA");
    form.setFlightNumber(100);
    form.setOrigin(1);
    form.setDestination(5);
    form.setDepartureDate(LocalDate.now().plusDays(30));

    // Create passenger list
    PassengerInfo p1 = new PassengerInfo();
    p1.setFirstName("Alice");
    p1.setLastName("Anderson");
    p1.setSeatClass("Business");
    p1.setMeal("Vegetarian");

    PassengerInfo p2 = new PassengerInfo();
    p2.setFirstName("Bob");
    p2.setLastName("Baker");
    p2.setSeatClass("Economy");
    p2.setMeal("Regular");

    form.setPassengers(java.util.Arrays.asList(p1, p2));

    setupValidReservationMocks();
    when(reservationPassengerRepository.countSeatsForFlight("AA", 100))
        .thenReturn(50L, 50L); // Seats available
    when(customerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer));
    when(employeeRepository.findById(1L)).thenReturn(Optional.of(mockEmployee));
    when(reservationRepository.save(any(Reservation.class))).thenReturn(mockReservation);
    when(passengerRepository.save(any(Passenger.class))).thenAnswer(inv -> inv.getArgument(0));

    Optional<String> result = reservationsService.createReservation(form, 1L);

    assertThat(result).isEmpty(); // Success
    assertThat(form.getPassengerCount()).isEqualTo(2); // Auto-synced
    assertThat(form.getFirstName(1)).isEqualTo("Alice");
    assertThat(form.getLastName(1)).isEqualTo("Anderson");
    assertThat(form.getFlightClass(1)).isEqualTo("Business");
    assertThat(form.getFood(1)).isEqualTo("Vegetarian");
    assertThat(form.getFirstName(2)).isEqualTo("Bob");
    assertThat(form.getFlightClass(2)).isEqualTo("Economy");
  }

  /**
   * Helper method to setup common mocks for valid reservation scenarios
   */
  private void setupValidReservationMocks() {
    when(flightRepository.findByAirline_IdAndFlightNumber("AA", 100))
        .thenReturn(Optional.of(mockFlight));
    when(fareRepository.findByFlight_Airline_IdAndFlight_FlightNumberAndFareTypeAndSeatClass(
        eq("AA"), eq(100), anyInt(), anyString()))
        .thenReturn(Optional.of(mockFare));
    lenient().when(stopsAtRepository.findFirstByFlight_Airline_IdAndFlight_FlightNumberAndStopNumberOrderByDepartureTimeAsc(
        eq("AA"), eq(100), anyInt()))
        .thenReturn(mockStopsAt);
    lenient().when(stopsAtRepository.existsByFlight_Airline_IdAndFlight_FlightNumberAndStopNumberAndDepartureTimeBetween(
        eq("AA"), eq(100), anyInt(), any(OffsetDateTime.class), any(OffsetDateTime.class)))
        .thenReturn(true); // Flight schedule exists for departure date
  }
}
