import api from './client';
import type {
  ReservationsResponse,
  CreateReservationRequest,
  PlaceBidRequest,
  BidResponse,
} from '../types';

// Re-export types for convenience
export type {
  ReservationsResponse,
  CreateReservationRequest,
  PlaceBidRequest,
  BidResponse,
};

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
