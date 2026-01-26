import api from './client';

export interface UserProfile {
  firstName: string;
  lastName: string;
  address: string;
  city: string;
  state: string;
  zipcode: string;
  telephone: string;
  email: string;
}

export interface UpdateProfileRequest extends UserProfile {
  password: string; // Current password for verification
}

export const usersApi = {
  getMe: async (): Promise<UserProfile> => {
    const { data } = await api.get<UserProfile>('/users/me');
    return data;
  },

  updateMe: async (profile: UpdateProfileRequest): Promise<UserProfile> => {
    const { data } = await api.put<UserProfile>('/users/me', profile);
    return data;
  },
};
