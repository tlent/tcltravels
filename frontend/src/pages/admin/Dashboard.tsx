import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { useAuth } from '../../context/AuthContext';
import { adminApi } from '../../api/admin';
import { LoadingSpinner } from '../../components/common/LoadingSpinner';
import { ErrorMessage } from '../../components/common/ErrorMessage';

export const Dashboard: React.FC = () => {
  const { user } = useAuth();
  const [selectedMonth, setSelectedMonth] = useState<number>(new Date().getMonth() + 1);
  const [selectedYear, setSelectedYear] = useState<number>(new Date().getFullYear());

  const { data, isLoading, error } = useQuery({
    queryKey: ['dashboard', selectedMonth, selectedYear],
    queryFn: () => adminApi.getDashboard(selectedMonth, selectedYear),
  });

  if (isLoading) {
    return <LoadingSpinner />;
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold text-gray-900 mb-6">Admin Dashboard</h1>

      {error && <ErrorMessage message={String(error)} />}

      {data && (
        <>
          {/* Summary Cards */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
            {data.topCustomer && (
              <div className="bg-white rounded-lg shadow p-6">
                <h3 className="text-lg font-semibold text-gray-900 mb-2">Top Customer</h3>
                <p className="text-2xl font-bold text-blue-600">{data.topCustomer.name}</p>
                <p className="text-gray-600">${data.topCustomer.totalRevenue.toFixed(2)} revenue</p>
              </div>
            )}

            {data.topEmployee && (
              <div className="bg-white rounded-lg shadow p-6">
                <h3 className="text-lg font-semibold text-gray-900 mb-2">Top Employee</h3>
                <p className="text-2xl font-bold text-green-600">{data.topEmployee.name}</p>
                <p className="text-gray-600">${data.topEmployee.totalRevenue.toFixed(2)} revenue</p>
              </div>
            )}
          </div>

          {/* Month/Year Selector */}
          <div className="bg-white rounded-lg shadow p-6 mb-8">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">Monthly Sales</h3>
            <div className="flex gap-4 mb-4">
              <select
                value={selectedMonth}
                onChange={(e) => setSelectedMonth(parseInt(e.target.value))}
                className="px-3 py-2 border border-gray-300 rounded-md"
              >
                {data.months.map((m) => (
                  <option key={m} value={m}>
                    {new Date(2000, m - 1).toLocaleString('default', { month: 'long' })}
                  </option>
                ))}
              </select>

              <select
                value={selectedYear}
                onChange={(e) => setSelectedYear(parseInt(e.target.value))}
                className="px-3 py-2 border border-gray-300 rounded-md"
              >
                {data.years.map((y) => (
                  <option key={y} value={y}>
                    {y}
                  </option>
                ))}
              </select>
            </div>

            <p className="text-xl font-bold mb-4">
              Total: ${data.monthTotal.toFixed(2)}
            </p>

            {data.monthlySales.length > 0 ? (
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">
                        Reservation #
                      </th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">
                        Customer
                      </th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">
                        Date
                      </th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">
                        Total Fare
                      </th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-gray-200">
                    {data.monthlySales.map((sale) => (
                      <tr key={sale.id}>
                        <td className="px-4 py-2">{sale.id}</td>
                        <td className="px-4 py-2">{sale.customerName}</td>
                        <td className="px-4 py-2">
                          {new Date(sale.reservationDate).toLocaleDateString()}
                        </td>
                        <td className="px-4 py-2">${sale.totalFare.toFixed(2)}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            ) : (
              <p className="text-gray-600">No sales for this month</p>
            )}
          </div>

          {/* Employees (MANAGER only) */}
          {user?.role === 'ROLE_MANAGER' && data.employees && data.employees.length > 0 && (
            <div className="bg-white rounded-lg shadow p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-4">
                Employees ({data.employees.length})
              </h3>
              <p className="text-gray-600">
                View and manage employees in the Employees section
              </p>
            </div>
          )}
        </>
      )}
    </div>
  );
};
