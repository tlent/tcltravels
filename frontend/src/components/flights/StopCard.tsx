import React from 'react';
import { useNavigate } from 'react-router-dom';
import type { StopView } from '../../api/flights';

interface StopCardProps {
  stop: StopView;
}

export const StopCard: React.FC<StopCardProps> = ({ stop }) => {
  const navigate = useNavigate();

  const handleBook = () => {
    navigate(
      `/reservations/new?airlineId=${stop.airlineId}&flightNumber=${stop.flightNumber}&departureDate=${stop.departureDate}`
    );
  };

  const handleBid = () => {
    navigate(
      `/reservations/bid?airlineId=${stop.airlineId}&flightNumber=${stop.flightNumber}&departureDate=${stop.departureDate}`
    );
  };

  return (
    <div className="bg-white border border-gray-200 rounded-lg p-4 hover:shadow-md transition-shadow">
      <div className="flex justify-between items-start mb-3">
        <div>
          <h3 className="text-lg font-semibold text-gray-900">
            {stop.airlineId} {stop.flightNumber}
          </h3>
          <p className="text-sm text-gray-600">Stop #{stop.stopNumber}</p>
        </div>
        <div className="text-right">
          <p className="text-xs text-gray-500">{stop.airportId}</p>
        </div>
      </div>

      <div className="mb-4 space-y-2">
        <div className="flex justify-between text-sm">
          <span className="text-gray-600">Departure:</span>
          <span className="font-medium text-gray-900">
            {new Date(stop.departureDate + 'T' + stop.departureTime).toLocaleString('en-US', {
              month: 'short',
              day: 'numeric',
              hour: 'numeric',
              minute: '2-digit',
            })}
          </span>
        </div>
        {stop.arrivalTime && (
          <div className="flex justify-between text-sm">
            <span className="text-gray-600">Arrival:</span>
            <span className="font-medium text-gray-900">
              {new Date(stop.departureDate + 'T' + stop.arrivalTime).toLocaleString('en-US', {
                month: 'short',
                day: 'numeric',
                hour: 'numeric',
                minute: '2-digit',
              })}
            </span>
          </div>
        )}
      </div>

      <div className="flex gap-2">
        <button
          onClick={handleBook}
          className="flex-1 px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 text-sm font-medium"
        >
          Book
        </button>
        <button
          onClick={handleBid}
          className="flex-1 px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 text-sm font-medium"
        >
          Bid
        </button>
      </div>
    </div>
  );
};
