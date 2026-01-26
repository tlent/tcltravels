import React, { useState } from 'react';
import type { Airline, Airport, FlightSearchParams } from '../../api/flights';

interface FlightFilterProps {
  airlines: Airline[];
  airports: Airport[];
  onSearch: (params: FlightSearchParams) => void;
  initialValues?: FlightSearchParams;
}

export const FlightFilter: React.FC<FlightFilterProps> = ({
  airlines,
  airports,
  onSearch,
  initialValues = {},
}) => {
  const [airline, setAirline] = useState(initialValues.airline || '');
  const [airport, setAirport] = useState(initialValues.airport || '');
  const [after, setAfter] = useState(initialValues.after || '');
  const [before, setBefore] = useState(initialValues.before || '');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSearch({ airline, airport, after, before });
  };

  return (
    <div className="bg-white rounded-lg shadow p-6 mb-6">
      <h2 className="text-xl font-bold text-gray-900 mb-4">Search Flights</h2>
      <form onSubmit={handleSubmit}>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Airline
            </label>
            <select
              value={airline}
              onChange={(e) => setAirline(e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-md"
            >
              <option value="">All Airlines</option>
              {airlines.map((a) => (
                <option key={a.id} value={a.id}>
                  {a.name}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Airport
            </label>
            <select
              value={airport}
              onChange={(e) => setAirport(e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-md"
            >
              <option value="">All Airports</option>
              {airports.map((a) => (
                <option key={a.id} value={a.id}>
                  {a.name} ({a.city})
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              After Date
            </label>
            <input
              type="date"
              value={after}
              onChange={(e) => setAfter(e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-md"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Before Date
            </label>
            <input
              type="date"
              value={before}
              onChange={(e) => setBefore(e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-md"
            />
          </div>
        </div>

        <div className="mt-4">
          <button
            type="submit"
            className="px-6 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 font-medium"
          >
            Search
          </button>
        </div>
      </form>
    </div>
  );
};
