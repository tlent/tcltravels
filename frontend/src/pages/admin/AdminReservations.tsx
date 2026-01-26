import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { adminApi } from '../../api/admin';
import { LoadingSpinner } from '../../components/common/LoadingSpinner';
import { ErrorMessage } from '../../components/common/ErrorMessage';

export const AdminReservations: React.FC = () => {
  const [selectedFlight, setSelectedFlight] = useState<string>('');
  const [selectedCustomer, setSelectedCustomer] = useState<number | undefined>();
  const [expandedReservations, setExpandedReservations] = useState<Set<number>>(new Set());

  const { data, isLoading, error } = useQuery({
    queryKey: ['adminReservations', selectedFlight, selectedCustomer],
    queryFn: () =>
      adminApi.getReservations({
        flight: selectedFlight || undefined,
        customer: selectedCustomer,
      }),
  });

  const toggleReservation = (id: number) => {
    setExpandedReservations((prev) => {
      const newSet = new Set(prev);
      if (newSet.has(id)) {
        newSet.delete(id);
      } else {
        newSet.add(id);
      }
      return newSet;
    });
  };

  if (isLoading) {
    return <LoadingSpinner />;
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold text-gray-900 mb-6">Reservation Management</h1>

      {error && <ErrorMessage message={String(error)} />}

      {data && (
        <>
          {/* Filters */}
          <div className="bg-white rounded-lg shadow p-6 mb-8">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">Filters</h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
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
            </div>
          </div>

          {/* Flight Reservations */}
          {data.flightReservations && data.flightReservations.length > 0 && (
            <div className="bg-white rounded-lg shadow p-6 mb-8">
              <h3 className="text-lg font-semibold text-gray-900 mb-4">Reservations by Flight</h3>
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Reservation #</th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Customer</th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Date</th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Booking Fee</th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Total Fare</th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Actions</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-gray-200">
                    {data.flightReservations.map((reservation) => (
                      <React.Fragment key={reservation.id}>
                        <tr>
                          <td className="px-4 py-2">{reservation.id}</td>
                          <td className="px-4 py-2">{reservation.customerName}</td>
                          <td className="px-4 py-2">{new Date(reservation.reservationDate).toLocaleDateString()}</td>
                          <td className="px-4 py-2">${reservation.bookingFee.toFixed(2)}</td>
                          <td className="px-4 py-2">${reservation.totalFare.toFixed(2)}</td>
                          <td className="px-4 py-2">
                            <button
                              onClick={() => toggleReservation(reservation.id)}
                              className="text-blue-600 hover:text-blue-800"
                            >
                              {expandedReservations.has(reservation.id) ? 'Hide Details' : 'Show Details'}
                            </button>
                          </td>
                        </tr>
                        {expandedReservations.has(reservation.id) && (
                          <tr>
                            <td colSpan={6} className="px-4 py-2 bg-gray-50">
                              <div className="text-sm">
                                <p className="font-semibold mb-2">Reservation Details</p>
                                <p>Booking Fee: ${reservation.bookingFee.toFixed(2)}</p>
                                <p>Total Fare: ${reservation.totalFare.toFixed(2)}</p>
                              </div>
                            </td>
                          </tr>
                        )}
                      </React.Fragment>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {/* Customer Reservations */}
          {data.customerReservations && data.customerReservations.length > 0 && (
            <div className="bg-white rounded-lg shadow p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-4">Reservations by Customer</h3>
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Customer</th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Reservation Count</th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Total Spent</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-gray-200">
                    {data.customerReservations.map((customer: any) => (
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
        </>
      )}
    </div>
  );
};
