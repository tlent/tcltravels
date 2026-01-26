import api from './client';

export interface AdminEmployeeView {
  personId: number;
  firstName: string;
  lastName: string;
  telephone: string;
  ssn: number;
  isManager: boolean;
  hourlyRate: number;
}

export interface EmployeeEditForm {
  firstName: string;
  lastName: string;
  address: string;
  city: string;
  state: string;
  zipcode: string;
  telephone: string;
  ssn: string;
  startDate: string;
  hourlyRate: string;
  isManager: boolean;
}

export interface EmployeeCreateForm extends EmployeeEditForm {
  email: string;
  password: string;
}

export const employeesApi = {
  getAll: async (): Promise<AdminEmployeeView[]> => {
    const { data } = await api.get<AdminEmployeeView[]>('/admin/employees');
    return data;
  },

  get: async (personId: number): Promise<EmployeeEditForm> => {
    const { data } = await api.get<EmployeeEditForm>(`/admin/employees/${personId}`);
    return data;
  },

  create: async (employee: EmployeeCreateForm): Promise<{ message: string }> => {
    const { data } = await api.post<{ message: string }>('/admin/employees', employee);
    return data;
  },

  update: async (personId: number, employee: EmployeeEditForm): Promise<{ message: string }> => {
    const { data } = await api.put<{ message: string }>(`/admin/employees/${personId}`, employee);
    return data;
  },

  delete: async (personId: number): Promise<{ message: string }> => {
    const { data } = await api.delete<{ message: string }>(`/admin/employees/${personId}`);
    return data;
  },
};
