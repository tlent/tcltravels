// Shared domain types for TCL Travels

// ============================================================================
// Reservations & Flights
// ============================================================================

export interface LegView {
  airlineId: string;
  flightNumber: number;
  originAirportId: string;
  originName: string;
  originCity: string;
  destinationAirportId: string;
  destinationName: string;
  destinationCity: string;
  departureTime: string;
  arrivalTime: string;
}

export interface ReservationView {
  id: number;
  customerId: number;
  reservationDate: string;
  bookingFee: number;
  totalFare: number;
  legs: LegView[];
}

export interface AuctionView {
  airlineId: string;
  flightNumber: number;
  seatingClass: string;
  date: string;
  nameYourOwnPrice: number;
  accepted: boolean;
}

export interface PassengerInput {
  firstName: string;
  lastName: string;
  seatClass: string;
  meal?: string;
}

// ============================================================================
// Admin - Sales & Revenue
// ============================================================================

export interface TopRevenueView {
  id: number;
  name: string;
  totalRevenue: number;
}

export interface SalesReservationView {
  id: number;
  customerName: string;
  reservationDate: string;
  bookingFee: number;
  totalFare: number;
}

// Individual reservation by customer (matches SalesCustomerReservationView)
export interface SalesCustomerReservationView {
  reservationNumber: number;
  airlineId: string;
  flightNumber: number;
  reservationDate: string;
  bookingFee: number;
  totalFare: number;
}

// Individual reservation by city (matches SalesCityReservationView)
export interface SalesCityReservationView {
  reservationNumber: number;
  airlineId: string;
  flightNumber: number;
  reservationDate: string;
  bookingFee: number;
  totalFare: number;
}

// Legacy aggregate views (if needed by frontend - may not match backend)
export interface CustomerSalesView {
  id: number;
  name: string;
  email: string;
  totalSpent: number;
  reservationCount: number;
}

export interface CitySalesView {
  city: string;
  state: string;
  totalRevenue: number;
  customerCount: number;
  reservationCount?: number; // Frontend expects this
}

// ============================================================================
// Admin - Flights
// ============================================================================

// Matches FlightView from backend
export interface FlightView {
  airlineId: string;
  flightNumber: number;
  numberOfSeats: number;
  daysOperatingLabel: string;
  originId: string;
  originCity: string;
  destinationId: string;
  destinationCity: string;
}

// Matches FlightActivityView from backend (for most active flights)
export interface FlightActivityView {
  airlineId: string;
  airlineName: string;
  flightNumber: number;
  numberOfSeats: number;
  daysOperating: string;
  originId: string;
  originCity: string;
  destinationId: string;
  destinationCity: string;
  reservationCount: number;
}

// Matches FlightSummaryView from backend (for all flights list)
export interface FlightSummaryView {
  airlineId: string;
  airlineName: string;
  flightNumber: number;
  numberOfSeats: number;
  daysOperating: string;
  originId: string;
  originCity: string;
  destinationId: string;
  destinationCity: string;
}

// Legacy - may not be used
export interface ActiveFlightView {
  airlineId: string;
  flightNumber: number;
  numberOfSeats: number;
  departureDays: string;
  mealType: string;
  totalReservations: number;
  totalRevenue: number;
}

export interface FlightStopView {
  stopNumber: number;
  airportId: string;
  airportName: string;
  city: string;
  departureTime: string;
  arrivalTime: string | null;
}

export interface AirportFlightView {
  airlineId: string;
  flightNumber: number;
  stopNumber: number;
  departureTime: string;
  arrivalTime: string | null;
}

// ============================================================================
// Admin - Employees
// ============================================================================

export interface EmployeeView {
  id: number;
  personId: number;
  firstName: string;
  lastName: string;
  email: string;
  address: string;
  city: string;
  state: string;
  zipcode: string;
  telephone: string;
  ssn: string;
  startDate: string;
  hourlyRate: string;
  manager: boolean;
}

// ============================================================================
// Admin - Filter Options
// ============================================================================

export interface FlightOption {
  airlineId: string;
  flightNumber: number;
  label: string;
}

// Matches CustomerOptionView from backend
export interface CustomerOption {
  customerId: number;
  firstName: string;
  lastName: string;
}

export interface EmployeeOption {
  id: number;
  name: string;
}

export interface AirportOptionView {
  id: string;
  name: string;
}

// ============================================================================
// Admin API Response Types
// ============================================================================

export interface DashboardResponse {
  topCustomer: TopRevenueView | null;
  topEmployee: TopRevenueView | null;
  monthlySales: SalesReservationView[];
  monthTotal: number;
  employees: EmployeeOption[];
  months: number[];
  years: number[];
  selectedMonth: number;
  selectedYear: number;
}

export interface SalesResponse {
  monthSales?: SalesReservationView[];
  monthTotal?: number;
  flightSales?: SalesReservationView[];
  flightTotal?: number;
  customerSales?: SalesCustomerReservationView[]; // Individual reservations, not aggregates
  customerTotal?: number;
  citySales?: SalesCityReservationView[]; // Individual reservations, not aggregates
  flightOptions: FlightOption[];
  customerOptions: CustomerOption[];
  cityOptions: string[];
}

export interface AdminFlightsResponse {
  mostActiveFlights: FlightActivityView[];
  allFlights: FlightSummaryView[];
  flightStops?: FlightStopView[];
  airportFlights?: AirportFlightView[];
  flightOptions: FlightOption[];
  airportOptions: AirportOptionView[];
}

export interface AdminReservationsResponse {
  flightReservations?: SalesReservationView[];
  customerReservations?: SalesCustomerReservationView[]; // Individual reservations, not ReservationView
  flightOptions: FlightOption[];
  customerOptions: CustomerOption[];
}

// ============================================================================
// Customer API Response Types
// ============================================================================

export interface ReservationsResponse {
  current: ReservationView[];
  past: ReservationView[];
  auctions: AuctionView[];
}

export interface CreateReservationRequest {
  airlineId: string;
  flightNumber: number;
  origin: number;
  destination: number;
  passengerCount: number;
  departureDate: string;
  other?: string;
  passengers: PassengerInput[];
}

export interface PlaceBidRequest {
  airlineId: string;
  flightNumber: number;
  departureDate: string;
  flightClass: string;
  bid: number;
  food?: string;
  other?: string;
}

export interface BidResponse {
  accepted: boolean;
  rejected: boolean;
  message: string;
}
