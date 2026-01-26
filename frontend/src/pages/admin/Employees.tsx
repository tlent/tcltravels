import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { employeesApi } from '../../api/employees';
import { LoadingSpinner } from '../../components/common/LoadingSpinner';
import { ErrorMessage } from '../../components/common/ErrorMessage';

export const Employees: React.FC = () => {
  const queryClient = useQueryClient();
  const [mutationError, setMutationError] = useState('');

  const { data: employees, isLoading, error } = useQuery({
    queryKey: ['employees'],
    queryFn: () => employeesApi.getAll(),
  });

  const deleteMutation = useMutation({
    mutationFn: (personId: number) => employeesApi.delete(personId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['employees'] });
      setMutationError('');
    },
    onError: (err: any) => {
      setMutationError(err.response?.data?.error || 'Failed to delete employee');
    },
  });

  const handleDelete = (personId: number, name: string) => {
    if (!confirm(`Are you sure you want to delete ${name}?`)) {
      return;
    }
    deleteMutation.mutate(personId);
  };

  if (isLoading) {
    return <LoadingSpinner />;
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold text-gray-900 mb-6">Employee Management</h1>

      {error && <ErrorMessage message={String(error)} />}
      {mutationError && <ErrorMessage message={mutationError} />}

      <div className="bg-white rounded-lg shadow overflow-hidden">
        <table className="w-full">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-4 py-3 text-left text-sm font-medium text-gray-700">Name</th>
              <th className="px-4 py-3 text-left text-sm font-medium text-gray-700">Telephone</th>
              <th className="px-4 py-3 text-left text-sm font-medium text-gray-700">Role</th>
              <th className="px-4 py-3 text-left text-sm font-medium text-gray-700">Hourly Rate</th>
              <th className="px-4 py-3 text-left text-sm font-medium text-gray-700">Actions</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-200">
            {employees?.map((emp) => (
              <tr key={emp.personId}>
                <td className="px-4 py-3">
                  {emp.firstName} {emp.lastName}
                </td>
                <td className="px-4 py-3">{emp.telephone}</td>
                <td className="px-4 py-3">
                  <span
                    className={`px-2 py-1 rounded text-sm ${
                      emp.isManager
                        ? 'bg-purple-100 text-purple-800'
                        : 'bg-blue-100 text-blue-800'
                    }`}
                  >
                    {emp.isManager ? 'Manager' : 'Employee'}
                  </span>
                </td>
                <td className="px-4 py-3">${emp.hourlyRate.toFixed(2)}/hr</td>
                <td className="px-4 py-3">
                  <button
                    onClick={() => handleDelete(emp.personId, `${emp.firstName} ${emp.lastName}`)}
                    className="text-red-600 hover:text-red-800"
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};
