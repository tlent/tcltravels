import api from './client';
import type {
  DashboardResponse,
  SalesResponse,
  AdminFlightsResponse,
  AdminReservationsResponse,
} from '../types';

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
  }): Promise<AdminFlightsResponse> => {
    const { data } = await api.get<AdminFlightsResponse>('/admin/flights', { params });
    return data;
  },

  getReservations: async (params: {
    flight?: string;
    customer?: number;
  }): Promise<AdminReservationsResponse> => {
    const { data } = await api.get<AdminReservationsResponse>('/admin/reservations', { params });
    return data;
  },
};
