import React, { useState, useEffect } from 'react';
import { reservationsApi } from '../api/reservations';
import type { ReservationsResponse } from '../api/reservations';
import { ReservationCard } from '../components/reservations/ReservationCard';
import { LoadingSpinner } from '../components/common/LoadingSpinner';
import { ErrorMessage } from '../components/common/ErrorMessage';

export const Reservations: React.FC = () => {
  const [data, setData] = useState<ReservationsResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [activeTab, setActiveTab] = useState<'current' | 'past'>('current');

  useEffect(() => {
    loadReservations();
  }, []);

  const loadReservations = async () => {
    try {
      setLoading(true);
      const result = await reservationsApi.getAll();
      setData(result);
    } catch (err: any) {
      setError(err.response?.data?.error || 'Failed to load reservations');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <LoadingSpinner />;
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold text-gray-900 mb-6">My Reservations</h1>

      {error && <ErrorMessage message={error} />}

      {data && (
        <>
          {/* Tabs */}
          <div className="border-b border-gray-200 mb-6">
            <div className="flex space-x-4">
              <button
                onClick={() => setActiveTab('current')}
                className={`px-4 py-2 font-medium border-b-2 transition-colors ${
                  activeTab === 'current'
                    ? 'border-blue-600 text-blue-600'
                    : 'border-transparent text-gray-600 hover:text-gray-900'
                }`}
              >
                Current ({data.current.length})
              </button>
              <button
                onClick={() => setActiveTab('past')}
                className={`px-4 py-2 font-medium border-b-2 transition-colors ${
                  activeTab === 'past'
                    ? 'border-blue-600 text-blue-600'
                    : 'border-transparent text-gray-600 hover:text-gray-900'
                }`}
              >
                Past ({data.past.length})
              </button>
            </div>
          </div>

          {/* Reservations */}
          <div className="mb-8">
            {activeTab === 'current' ? (
              data.current.length > 0 ? (
                data.current.map((res) => <ReservationCard key={res.id} reservation={res} />)
              ) : (
                <p className="text-gray-600">No current reservations</p>
              )
            ) : data.past.length > 0 ? (
              data.past.map((res) => <ReservationCard key={res.id} reservation={res} />)
            ) : (
              <p className="text-gray-600">No past reservations</p>
            )}
          </div>

          {/* Auction History */}
          {data.auctions.length > 0 && (
            <div>
              <h2 className="text-2xl font-bold text-gray-900 mb-4">Auction History</h2>
              <div className="bg-white rounded-lg shadow overflow-hidden">
                <table className="w-full">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-4 py-3 text-left text-sm font-medium text-gray-700">
                        Flight
                      </th>
                      <th className="px-4 py-3 text-left text-sm font-medium text-gray-700">
                        Class
                      </th>
                      <th className="px-4 py-3 text-left text-sm font-medium text-gray-700">
                        Bid Amount
                      </th>
                      <th className="px-4 py-3 text-left text-sm font-medium text-gray-700">
                        Date
                      </th>
                      <th className="px-4 py-3 text-left text-sm font-medium text-gray-700">
                        Status
                      </th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-gray-200">
                    {data.auctions.map((auction, idx) => (
                      <tr key={idx}>
                        <td className="px-4 py-3">
                          {auction.airlineId} {auction.flightNumber}
                        </td>
                        <td className="px-4 py-3">{auction.seatingClass}</td>
                        <td className="px-4 py-3">${auction.nameYourOwnPrice.toFixed(2)}</td>
                        <td className="px-4 py-3">
                          {new Date(auction.date).toLocaleDateString()}
                        </td>
                        <td className="px-4 py-3">
                          <span
                            className={`px-2 py-1 rounded text-sm ${
                              auction.accepted
                                ? 'bg-green-100 text-green-800'
                                : 'bg-red-100 text-red-800'
                            }`}
                          >
                            {auction.accepted ? 'Accepted' : 'Rejected'}
                          </span>
                        </td>
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
