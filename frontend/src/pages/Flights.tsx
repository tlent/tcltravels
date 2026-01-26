import React, { useState, useEffect } from 'react';
import { flightsApi } from '../api/flights';
import type { FlightsResponse, FlightSearchParams } from '../api/flights';
import { FlightFilter } from '../components/flights/FlightFilter';
import { FlightCard } from '../components/flights/FlightCard';
import { LoadingSpinner } from '../components/common/LoadingSpinner';
import { ErrorMessage } from '../components/common/ErrorMessage';

export const Flights: React.FC = () => {
  const [data, setData] = useState<FlightsResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    loadFlights({});
  }, []);

  const loadFlights = async (params: FlightSearchParams) => {
    try {
      setLoading(true);
      setError('');
      const result = await flightsApi.search(params);
      setData(result);
    } catch (err: any) {
      setError(err.response?.data?.error || 'Failed to load flights');
    } finally {
      setLoading(false);
    }
  };

  if (loading && !data) {
    return <LoadingSpinner />;
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold text-gray-900 mb-6">Search Flights</h1>

      {data && data.filter && (
        <FlightFilter
          airlines={data.filter.airlines}
          airports={data.filter.airports}
          onSearch={loadFlights}
          initialValues={{
            airline: data.filter.airlineFilter,
            airport: data.filter.airportFilter,
            after: data.filter.afterFilter || undefined,
            before: data.filter.beforeFilter || undefined,
          }}
        />
      )}

      {error && <ErrorMessage message={error} />}

      {loading && <LoadingSpinner />}

      {data && data.filter && !loading && (
        <>
          {/* Matching Flights */}
          {data.filter.stops && data.filter.stops.length > 0 && (
            <div className="mb-8">
              <h2 className="text-2xl font-bold text-gray-900 mb-4">
                Matching Flights ({data.filter.stops.length})
              </h2>
              <div className="bg-white rounded-lg shadow overflow-hidden">
                <div className="overflow-x-auto">
                  <table className="w-full">
                    <thead className="bg-gray-50">
                      <tr>
                        <th className="px-4 py-3 text-left text-sm font-medium text-gray-700">
                          Flight
                        </th>
                        <th className="px-4 py-3 text-left text-sm font-medium text-gray-700">
                          Airport
                        </th>
                        <th className="px-4 py-3 text-left text-sm font-medium text-gray-700">
                          Departure
                        </th>
                        <th className="px-4 py-3 text-left text-sm font-medium text-gray-700">
                          Arrival
                        </th>
                      </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-200">
                      {data.filter.stops.map((stop, idx) => (
                        <tr key={idx} className="hover:bg-gray-50">
                          <td className="px-4 py-3">
                            {stop.airlineId} {stop.flightNumber}
                          </td>
                          <td className="px-4 py-3">{stop.airportId}</td>
                          <td className="px-4 py-3">
                            {new Date(stop.departureTime).toLocaleString()}
                          </td>
                          <td className="px-4 py-3">
                            {stop.arrivalTime
                              ? new Date(stop.arrivalTime).toLocaleString()
                              : 'N/A'}
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          )}

          {/* Recommended Flights */}
          {data.recommended && data.recommended.length > 0 && (
            <div className="mb-8">
              <h2 className="text-2xl font-bold text-gray-900 mb-4">
                Recommended for You
              </h2>
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                {data.recommended.map((flight, idx) => (
                  <FlightCard key={idx} flight={flight} />
                ))}
              </div>
            </div>
          )}

          {/* Best-Selling Flights */}
          {data.bestSelling && data.bestSelling.length > 0 && (
            <div>
              <h2 className="text-2xl font-bold text-gray-900 mb-4">
                Best-Selling Flights
              </h2>
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                {data.bestSelling.map((flight, idx) => (
                  <FlightCard key={idx} flight={flight} />
                ))}
              </div>
            </div>
          )}
        </>
      )}
    </div>
  );
};
