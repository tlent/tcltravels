import React, { useState, useEffect } from 'react';
import { adminApi } from '../../api/admin';
import type { SalesResponse } from '../../api/admin';
import { LoadingSpinner } from '../../components/common/LoadingSpinner';
import { ErrorMessage } from '../../components/common/ErrorMessage';

export const Sales: React.FC = () => {
  const [data, setData] = useState<SalesResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const [selectedMonth, setSelectedMonth] = useState<number>(new Date().getMonth() + 1);
  const [selectedYear, setSelectedYear] = useState<number>(new Date().getFullYear());
  const [selectedFlight, setSelectedFlight] = useState<string>('');
  const [selectedCustomer, setSelectedCustomer] = useState<number | undefined>();
  const [selectedCity, setSelectedCity] = useState<string>('');

  useEffect(() => {
    loadSales();
  }, [selectedMonth, selectedYear, selectedFlight, selectedCustomer, selectedCity]);

  const loadSales = async () => {
    try {
      setLoading(true);
      const result = await adminApi.getSales({
        month: selectedMonth,
        year: selectedYear,
        flight: selectedFlight || undefined,
        customer: selectedCustomer,
        city: selectedCity || undefined,
      });
      setData(result);
    } catch (err: any) {
      setError(err.response?.data?.error || 'Failed to load sales data');
    } finally {
      setLoading(false);
    }
  };

  if (loading && !data) {
    return <LoadingSpinner />;
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold text-gray-900 mb-6">Sales Reports</h1>

      {error && <ErrorMessage message={error} />}

      {data && (
        <>
          {/* Filters */}
          <div className="bg-white rounded-lg shadow p-6 mb-8">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">Filters</h3>
            <div className="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-5 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Month</label>
                <select
                  value={selectedMonth}
                  onChange={(e) => setSelectedMonth(parseInt(e.target.value))}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md"
                >
                  {[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12].map((m) => (
                    <option key={m} value={m}>
                      {new Date(2000, m - 1).toLocaleString('default', { month: 'long' })}
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Year</label>
                <select
                  value={selectedYear}
                  onChange={(e) => setSelectedYear(parseInt(e.target.value))}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md"
                >
                  {[2024, 2025, 2026].map((y) => (
                    <option key={y} value={y}>
                      {y}
                    </option>
                  ))}
                </select>
              </div>

              {data.flightOptions && data.flightOptions.length > 0 && (
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Flight</label>
                  <select
                    value={selectedFlight}
                    onChange={(e) => setSelectedFlight(e.target.value)}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md"
                  >
                    <option value="">All Flights</option>
                    {data.flightOptions.map((flight: any) => (
                      <option key={`${flight.airlineId}-${flight.flightNumber}`} value={`${flight.airlineId}-${flight.flightNumber}`}>
                        {flight.airlineId}-{flight.flightNumber}
                      </option>
                    ))}
                  </select>
                </div>
              )}

              {data.customerOptions && data.customerOptions.length > 0 && (
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Customer</label>
                  <select
                    value={selectedCustomer || ''}
                    onChange={(e) => setSelectedCustomer(e.target.value ? parseInt(e.target.value) : undefined)}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md"
                  >
                    <option value="">All Customers</option>
                    {data.customerOptions.map((customer: any) => (
                      <option key={customer.customerId} value={customer.customerId}>
                        {customer.firstName} {customer.lastName}
                      </option>
                    ))}
                  </select>
                </div>
              )}

              {data.cityOptions && data.cityOptions.length > 0 && (
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">City</label>
                  <select
                    value={selectedCity}
                    onChange={(e) => setSelectedCity(e.target.value)}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md"
                  >
                    <option value="">All Cities</option>
                    {data.cityOptions.map((city: string) => (
                      <option key={city} value={city}>
                        {city}
                      </option>
                    ))}
                  </select>
                </div>
              )}
            </div>
          </div>

          {/* Monthly Sales */}
          {data.monthSales && data.monthSales.length > 0 && (
            <div className="bg-white rounded-lg shadow p-6 mb-8">
              <h3 className="text-lg font-semibold text-gray-900 mb-4">Monthly Sales</h3>
              <p className="text-xl font-bold mb-4">Total: ${data.monthTotal?.toFixed(2)}</p>
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Reservation #</th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Customer</th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Date</th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Booking Fee</th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Total Fare</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-gray-200">
                    {data.monthSales.map((sale) => (
                      <tr key={sale.id}>
                        <td className="px-4 py-2">{sale.id}</td>
                        <td className="px-4 py-2">{sale.customerName}</td>
                        <td className="px-4 py-2">{new Date(sale.reservationDate).toLocaleDateString()}</td>
                        <td className="px-4 py-2">${sale.bookingFee.toFixed(2)}</td>
                        <td className="px-4 py-2">${sale.totalFare.toFixed(2)}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {/* Flight Sales */}
          {data.flightSales && data.flightSales.length > 0 && (
            <div className="bg-white rounded-lg shadow p-6 mb-8">
              <h3 className="text-lg font-semibold text-gray-900 mb-4">Sales by Flight</h3>
              <p className="text-xl font-bold mb-4">Total: ${data.flightTotal?.toFixed(2)}</p>
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Reservation #</th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Customer</th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Date</th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Booking Fee</th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Total Fare</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-gray-200">
                    {data.flightSales.map((sale) => (
                      <tr key={sale.id}>
                        <td className="px-4 py-2">{sale.id}</td>
                        <td className="px-4 py-2">{sale.customerName}</td>
                        <td className="px-4 py-2">{new Date(sale.reservationDate).toLocaleDateString()}</td>
                        <td className="px-4 py-2">${sale.bookingFee.toFixed(2)}</td>
                        <td className="px-4 py-2">${sale.totalFare.toFixed(2)}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {/* Customer Sales */}
          {data.customerSales && data.customerSales.length > 0 && (
            <div className="bg-white rounded-lg shadow p-6 mb-8">
              <h3 className="text-lg font-semibold text-gray-900 mb-4">Sales by Customer</h3>
              <p className="text-xl font-bold mb-4">Total: ${data.customerTotal?.toFixed(2)}</p>
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Customer</th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Reservations</th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Total Spent</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-gray-200">
                    {data.customerSales.map((customer: any) => (
                      <tr key={customer.customerId}>
                        <td className="px-4 py-2">{customer.firstName} {customer.lastName}</td>
                        <td className="px-4 py-2">{customer.reservationCount}</td>
                        <td className="px-4 py-2">${customer.totalSpent?.toFixed(2)}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {/* City Sales */}
          {data.citySales && data.citySales.length > 0 && (
            <div className="bg-white rounded-lg shadow p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-4">Sales by City</h3>
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">City</th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Reservations</th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Total Revenue</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-gray-200">
                    {data.citySales.map((city: any) => (
                      <tr key={city.city}>
                        <td className="px-4 py-2">{city.city}</td>
                        <td className="px-4 py-2">{city.reservationCount}</td>
                        <td className="px-4 py-2">${city.totalRevenue?.toFixed(2)}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}
        </>
      )}
    </div>
  );
};
