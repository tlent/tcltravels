import React, { useState, useEffect } from 'react';
import { adminApi } from '../../api/admin';
import type { FlightsResponse } from '../../api/admin';
import { LoadingSpinner } from '../../components/common/LoadingSpinner';
import { ErrorMessage } from '../../components/common/ErrorMessage';

export const AdminFlights: React.FC = () => {
  const [data, setData] = useState<FlightsResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const [selectedFlight, setSelectedFlight] = useState<string>('');
  const [selectedAirport, setSelectedAirport] = useState<string>('');

  useEffect(() => {
    loadFlights();
  }, [selectedFlight, selectedAirport]);

  const loadFlights = async () => {
    try {
      setLoading(true);
      const result = await adminApi.getFlights({
        flight: selectedFlight || undefined,
        airport: selectedAirport || undefined,
      });
      setData(result);
    } catch (err: any) {
      setError(err.response?.data?.error || 'Failed to load flights data');
    } finally {
      setLoading(false);
    }
  };

  if (loading && !data) {
    return <LoadingSpinner />;
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold text-gray-900 mb-6">Flight Management</h1>

      {error && <ErrorMessage message={error} />}

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

              {data.airportOptions && data.airportOptions.length > 0 && (
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Airport</label>
                  <select
                    value={selectedAirport}
                    onChange={(e) => setSelectedAirport(e.target.value)}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md"
                  >
                    <option value="">All Airports</option>
                    {data.airportOptions.map((airport: any) => (
                      <option key={airport.airportId} value={airport.airportId}>
                        {airport.airportId} - {airport.name}
                      </option>
                    ))}
                  </select>
                </div>
              )}
            </div>
          </div>

          {/* Most Active Flights */}
          {data.mostActiveFlights && data.mostActiveFlights.length > 0 && (
            <div className="bg-white rounded-lg shadow p-6 mb-8">
              <h3 className="text-lg font-semibold text-gray-900 mb-4">Most Active Flights</h3>
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Flight</th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Airline</th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Reservations</th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Total Revenue</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-gray-200">
                    {data.mostActiveFlights.map((flight: any) => (
                      <tr key={`${flight.airlineId}-${flight.flightNumber}`}>
                        <td className="px-4 py-2">{flight.airlineId}-{flight.flightNumber}</td>
                        <td className="px-4 py-2">{flight.airlineName}</td>
                        <td className="px-4 py-2">{flight.reservationCount}</td>
                        <td className="px-4 py-2">${flight.totalRevenue?.toFixed(2)}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {/* All Flights */}
          {data.allFlights && data.allFlights.length > 0 && (
            <div className="bg-white rounded-lg shadow p-6 mb-8">
              <h3 className="text-lg font-semibold text-gray-900 mb-4">All Flights</h3>
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Flight</th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Airline</th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Days of Week</th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Active</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-gray-200">
                    {data.allFlights.map((flight: any) => (
                      <tr key={`${flight.airlineId}-${flight.flightNumber}`}>
                        <td className="px-4 py-2">{flight.airlineId}-{flight.flightNumber}</td>
                        <td className="px-4 py-2">{flight.airlineName}</td>
                        <td className="px-4 py-2">{flight.daysOfWeek}</td>
                        <td className="px-4 py-2">
                          <span className={`px-2 py-1 rounded text-sm ${flight.active ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'}`}>
                            {flight.active ? 'Active' : 'Inactive'}
                          </span>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {/* Flight Stops */}
          {data.flightStops && data.flightStops.length > 0 && (
            <div className="bg-white rounded-lg shadow p-6 mb-8">
              <h3 className="text-lg font-semibold text-gray-900 mb-4">Flight Stops</h3>
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Stop #</th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Airport</th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Departure</th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Arrival</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-gray-200">
                    {data.flightStops.map((stop: any) => (
                      <tr key={stop.stopNumber}>
                        <td className="px-4 py-2">{stop.stopNumber}</td>
                        <td className="px-4 py-2">{stop.airportId} - {stop.airportName}</td>
                        <td className="px-4 py-2">{stop.departureTime || '-'}</td>
                        <td className="px-4 py-2">{stop.arrivalTime || '-'}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {/* Airport Flights */}
          {data.airportFlights && data.airportFlights.length > 0 && (
            <div className="bg-white rounded-lg shadow p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-4">Airport Activity</h3>
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Flight</th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Direction</th>
                      <th className="px-4 py-2 text-left text-sm font-medium text-gray-700">Time</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-gray-200">
                    {data.airportFlights.map((flight: any, index: number) => (
                      <tr key={index}>
                        <td className="px-4 py-2">{flight.airlineId}-{flight.flightNumber}</td>
                        <td className="px-4 py-2">
                          <span className={`px-2 py-1 rounded text-sm ${flight.direction === 'Departure' ? 'bg-blue-100 text-blue-800' : 'bg-green-100 text-green-800'}`}>
                            {flight.direction}
                          </span>
                        </td>
                        <td className="px-4 py-2">{flight.time}</td>
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
