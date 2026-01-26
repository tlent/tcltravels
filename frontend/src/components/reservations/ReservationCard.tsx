import React, { useState } from 'react';
import type { ReservationView } from '../../api/reservations';

interface ReservationCardProps {
  reservation: ReservationView;
}

export const ReservationCard: React.FC<ReservationCardProps> = ({ reservation }) => {
  const [expanded, setExpanded] = useState(false);

  return (
    <div className="bg-white border border-gray-200 rounded-lg p-4 mb-4">
      <div className="flex justify-between items-start">
        <div>
          <h3 className="text-lg font-semibold text-gray-900">
            Reservation #{reservation.id}
          </h3>
          <p className="text-sm text-gray-600">
            {new Date(reservation.reservationDate).toLocaleDateString()}
          </p>
        </div>
        <div className="text-right">
          <p className="text-lg font-bold text-gray-900">
            ${reservation.totalFare.toFixed(2)}
          </p>
          <p className="text-sm text-gray-600">
            + ${reservation.bookingFee.toFixed(2)} fee
          </p>
        </div>
      </div>

      <button
        onClick={() => setExpanded(!expanded)}
        className="mt-4 text-blue-600 hover:text-blue-700 text-sm font-medium"
      >
        {expanded ? 'Hide' : 'Show'} Itinerary
      </button>

      {expanded && (
        <div className="mt-4 border-t pt-4">
          <h4 className="font-semibold text-gray-900 mb-2">Flight Legs:</h4>
          {reservation.legs.map((leg, idx) => (
            <div key={idx} className="mb-3 p-3 bg-gray-50 rounded">
              <div className="flex justify-between items-center">
                <div>
                  <p className="font-medium">
                    {leg.airlineId} {leg.flightNumber}
                  </p>
                  <p className="text-sm text-gray-600">
                    {leg.originName} ({leg.originAirportId}) → {leg.destinationName} (
                    {leg.destinationAirportId})
                  </p>
                </div>
                <div className="text-right text-sm">
                  <p>{new Date(leg.departureTime).toLocaleString()}</p>
                  <p className="text-gray-600">
                    to {new Date(leg.arrivalTime).toLocaleString()}
                  </p>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
