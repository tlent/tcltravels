import api from './client';

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

export interface ReservationsResponse {
  current: ReservationView[];
  past: ReservationView[];
  auctions: AuctionView[];
}

export interface PassengerInput {
  firstName: string;
  lastName: string;
  seatClass: string;
  meal: string;
}

export interface CreateReservationRequest {
  airlineId: string;
  flightNumber: number;
  origin: number;
  destination: number;
  passengerCount: number;
  departureDate: string; // ISO date format YYYY-MM-DD
  other?: string;
  first1?: string;
  last1?: string;
  class1?: string;
  food1?: string;
  first2?: string;
  last2?: string;
  class2?: string;
  food2?: string;
  first3?: string;
  last3?: string;
  class3?: string;
  food3?: string;
  first4?: string;
  last4?: string;
  class4?: string;
  food4?: string;
  first5?: string;
  last5?: string;
  class5?: string;
  food5?: string;
}

export interface PlaceBidRequest {
  airlineId: string;
  flightNumber: number;
  departureDate: string; // ISO date format YYYY-MM-DD
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

export const reservationsApi = {
  getAll: async (status?: 'current' | 'past'): Promise<ReservationsResponse> => {
    const params = status ? { status } : {};
    const { data } = await api.get<ReservationsResponse>('/reservations', { params });
    return data;
  },

  create: async (reservation: CreateReservationRequest): Promise<{ message: string }> => {
    const { data } = await api.post<{ message: string }>('/reservations', reservation);
    return data;
  },

  placeBid: async (bid: PlaceBidRequest): Promise<BidResponse> => {
    const { data } = await api.post<BidResponse>('/reservations/bid', bid);
    return data;
  },
};
