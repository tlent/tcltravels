import React from 'react';
import { useNavigate } from 'react-router-dom';
import type { FlightView } from '../../api/flights';

interface FlightCardProps {
  flight: FlightView;
}

export const FlightCard: React.FC<FlightCardProps> = ({ flight }) => {
  const navigate = useNavigate();

  const handleBook = () => {
    navigate(
      `/reservations/new?airlineId=${flight.airlineId}&flightNumber=${flight.flightNumber}`
    );
  };

  const handleBid = () => {
    navigate(
      `/reservations/bid?airlineId=${flight.airlineId}&flightNumber=${flight.flightNumber}`
    );
  };

  return (
    <div className="bg-white border border-gray-200 rounded-lg p-4 hover:shadow-md transition-shadow">
      <div className="flex justify-between items-start mb-3">
        <div>
          <h3 className="text-lg font-semibold text-gray-900">
            {flight.airlineId} {flight.flightNumber}
          </h3>
          <p className="text-sm text-gray-600">{flight.daysOperatingLabel}</p>
        </div>
        <div className="text-right">
          <p className="text-sm text-gray-600">{flight.numberOfSeats} seats</p>
        </div>
      </div>

      <div className="flex items-center gap-2 mb-4">
        <div className="flex-1">
          <p className="text-sm font-medium text-gray-700">{flight.originId}</p>
          <p className="text-xs text-gray-500">{flight.originCity}</p>
        </div>
        <div className="text-gray-400">→</div>
        <div className="flex-1 text-right">
          <p className="text-sm font-medium text-gray-700">{flight.destinationId}</p>
          <p className="text-xs text-gray-500">{flight.destinationCity}</p>
        </div>
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
