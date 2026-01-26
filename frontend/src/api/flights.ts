import api from './client';

export interface Airline {
  id: string;
  name: string;
}

export interface Airport {
  id: string;
  name: string;
  city: string;
}

export interface StopView {
  airlineId: string;
  airportId: string;
  flightNumber: number;
  stopNumber: number;
  arrivalTime: string;
  departureTime: string;
  departureDate: string;
}

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

export interface FlightFilterResult {
  airlines: Airline[];
  airports: Airport[];
  airlineFilter: string;
  airportFilter: string;
  afterFilter: string | null;
  beforeFilter: string | null;
  stops: StopView[];
}

export interface FlightsResponse {
  filter: FlightFilterResult;
  recommended: FlightView[];
  bestSelling: FlightView[];
}

export interface FlightSearchParams {
  airline?: string;
  airport?: string;
  after?: string; // ISO date format YYYY-MM-DD
  before?: string; // ISO date format YYYY-MM-DD
}

export const flightsApi = {
  search: async (params: FlightSearchParams = {}): Promise<FlightsResponse> => {
    const { data } = await api.get<FlightsResponse>('/flights', { params });
    return data;
  },

  getStops: async (airlineId: string, flightNumber: number): Promise<StopView[]> => {
    const { data } = await api.get<StopView[]>(`/flights/${airlineId}/${flightNumber}/stops`);
    return data;
  },
};
