import api from './client';

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

export interface DashboardResponse {
  topCustomer: TopRevenueView | null;
  topEmployee: TopRevenueView | null;
  monthlySales: SalesReservationView[];
  monthTotal: number;
  employees: any[];
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
  customerSales?: any[];
  customerTotal?: number;
  citySales?: any[];
  flightOptions: any[];
  customerOptions: any[];
  cityOptions: string[];
}

export interface FlightsResponse {
  mostActiveFlights: any[];
  allFlights: any[];
  flightStops?: any[];
  airportFlights?: any[];
  flightOptions: any[];
  airportOptions: any[];
}

export interface ReservationsResponse {
  flightReservations?: SalesReservationView[];
  customerReservations?: any[];
  flightOptions: any[];
  customerOptions: any[];
}

export const adminApi = {
  getDashboard: async (month?: number, year?: number): Promise<DashboardResponse> => {
    const params: any = {};
    if (month) params.month = month;
    if (year) params.year = year;
    const { data } = await api.get<DashboardResponse>('/admin/dashboard', { params });
    return data;
  },

  getSales: async (params: {
    month?: number;
    year?: number;
    flight?: string;
    customer?: number;
    city?: string;
  }): Promise<SalesResponse> => {
    const { data } = await api.get<SalesResponse>('/admin/sales', { params });
    return data;
  },

  getFlights: async (params: {
    flight?: string;
    airport?: string;
  }): Promise<FlightsResponse> => {
    const { data } = await api.get<FlightsResponse>('/admin/flights', { params });
    return data;
  },

  getReservations: async (params: {
    flight?: string;
    customer?: number;
  }): Promise<ReservationsResponse> => {
    const { data } = await api.get<ReservationsResponse>('/admin/reservations', { params });
    return data;
  },
};
