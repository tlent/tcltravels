import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { flightsApi } from '../api/flights';
import type { FlightSearchParams } from '../api/flights';
import { FlightFilter } from '../components/flights/FlightFilter';
import { FlightCard } from '../components/flights/FlightCard';
import { StopCard } from '../components/flights/StopCard';
import { LoadingSpinner } from '../components/common/LoadingSpinner';
import { ErrorMessage } from '../components/common/ErrorMessage';

export const Flights: React.FC = () => {
  const [searchParams, setSearchParams] = useState<FlightSearchParams>({});

  const { data, isLoading, error } = useQuery({
    queryKey: ['flights', searchParams],
    queryFn: () => flightsApi.search(searchParams),
  });

  const handleSearch = (params: FlightSearchParams) => {
    setSearchParams(params);
  };

  if (isLoading && !data) {
    return <LoadingSpinner />;
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold text-gray-900 mb-6">Search Flights</h1>

      {data && data.filter && (
        <FlightFilter
          airlines={data.filter.airlines}
          airports={data.filter.airports}
          onSearch={handleSearch}
          initialValues={{
            airline: data.filter.airlineFilter,
            airport: data.filter.airportFilter,
            after: data.filter.afterFilter || undefined,
            before: data.filter.beforeFilter || undefined,
          }}
        />
      )}

      {error && <ErrorMessage message={String(error)} />}

      {isLoading && <LoadingSpinner />}

      {data && data.filter && !isLoading && (
        <>
          {/* Matching Flights */}
          {data.filter.stops && data.filter.stops.length > 0 && (
            <div className="mt-8">
              <h2 className="text-2xl font-bold text-gray-900 mb-4">Matching Flights</h2>
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                {data.filter.stops.map((stop, idx) => (
                  <StopCard key={idx} stop={stop} />
                ))}
              </div>
            </div>
          )}

          {/* Recommended Flights */}
          {data.recommended && data.recommended.length > 0 && (
            <div className="mt-8">
              <h2 className="text-2xl font-bold text-gray-900 mb-4">Recommended Flights</h2>
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                {data.recommended.map((flight, idx) => (
                  <FlightCard key={idx} flight={flight} />
                ))}
              </div>
            </div>
          )}

          {/* Best-Selling Flights */}
          {data.bestSelling && data.bestSelling.length > 0 && (
            <div className="mt-8">
              <h2 className="text-2xl font-bold text-gray-900 mb-4">Best-Selling Flights</h2>
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                {data.bestSelling.map((flight, idx) => (
                  <FlightCard key={idx} flight={flight} />
                ))}
              </div>
            </div>
          )}

          {/* No Results */}
          {(!data.filter.stops || data.filter.stops.length === 0) &&
            (!data.recommended || data.recommended.length === 0) &&
            (!data.bestSelling || data.bestSelling.length === 0) && (
              <div className="mt-8 text-center text-gray-600">
                <p>No flights found matching your criteria.</p>
              </div>
            )}
        </>
      )}
    </div>
  );
};
