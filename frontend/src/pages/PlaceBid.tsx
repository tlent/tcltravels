import React, { useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { reservationsApi } from '../api/reservations';
import type { PlaceBidRequest } from '../api/reservations';
import { ErrorMessage } from '../components/common/ErrorMessage';

export const PlaceBid: React.FC = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  const airlineId = searchParams.get('airlineId') || '';
  const flightNumber = parseInt(searchParams.get('flightNumber') || '0');

  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [formData, setFormData] = useState({
    departureDate: '',
    flightClass: 'Economy',
    bid: '',
    food: '',
    other: '',
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setSubmitting(true);

    try {
      const request: PlaceBidRequest = {
        airlineId,
        flightNumber,
        departureDate: formData.departureDate,
        flightClass: formData.flightClass,
        bid: parseFloat(formData.bid),
        food: formData.food,
        other: formData.other,
      };

      const response = await reservationsApi.placeBid(request);

      if (response.accepted) {
        setSuccess(response.message);
        setTimeout(() => navigate('/reservations'), 2000);
      } else if (response.rejected) {
        setError(response.message);
      } else {
        setSuccess('Bid submitted successfully');
      }
    } catch (err: any) {
      setError(err.response?.data?.error || 'Failed to submit bid');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="container mx-auto px-4 py-8 max-w-2xl">
      <h1 className="text-3xl font-bold text-gray-900 mb-6">Place Bid</h1>

      <div className="bg-white rounded-lg shadow p-6">
        <div className="mb-6">
          <h2 className="text-xl font-semibold text-gray-900">
            Flight {airlineId} {flightNumber}
          </h2>
          <p className="text-sm text-gray-600 mt-1">
            Name your own price for this flight!
          </p>
        </div>

        {error && <ErrorMessage message={error} />}
        {success && (
          <div className="bg-green-50 border border-green-200 text-green-700 px-4 py-3 rounded mb-4">
            <p className="font-medium">Success!</p>
            <p className="text-sm">{success}</p>
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Departure Date
              </label>
              <input
                type="date"
                value={formData.departureDate}
                onChange={(e) =>
                  setFormData((prev) => ({ ...prev, departureDate: e.target.value }))
                }
                className="w-full px-3 py-2 border border-gray-300 rounded-md"
                required
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Class
              </label>
              <select
                value={formData.flightClass}
                onChange={(e) =>
                  setFormData((prev) => ({ ...prev, flightClass: e.target.value }))
                }
                className="w-full px-3 py-2 border border-gray-300 rounded-md"
                required
              >
                <option value="Economy">Economy</option>
                <option value="Business">Business</option>
                <option value="First">First Class</option>
              </select>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Your Bid Amount ($)
              </label>
              <input
                type="number"
                step="0.01"
                min="0.01"
                value={formData.bid}
                onChange={(e) =>
                  setFormData((prev) => ({ ...prev, bid: e.target.value }))
                }
                className="w-full px-3 py-2 border border-gray-300 rounded-md"
                required
                placeholder="0.00"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Meal Preference (optional)
              </label>
              <input
                type="text"
                value={formData.food}
                onChange={(e) =>
                  setFormData((prev) => ({ ...prev, food: e.target.value }))
                }
                className="w-full px-3 py-2 border border-gray-300 rounded-md"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Special Requests (optional)
              </label>
              <input
                type="text"
                value={formData.other}
                onChange={(e) =>
                  setFormData((prev) => ({ ...prev, other: e.target.value }))
                }
                className="w-full px-3 py-2 border border-gray-300 rounded-md"
              />
            </div>
          </div>

          <div className="flex gap-4 mt-6">
            <button
              type="submit"
              disabled={submitting}
              className="px-6 py-2 bg-green-600 text-white rounded hover:bg-green-700 disabled:bg-gray-400"
            >
              {submitting ? 'Submitting...' : 'Submit Bid'}
            </button>
            <button
              type="button"
              onClick={() => navigate('/flights')}
              className="px-6 py-2 bg-gray-300 text-gray-700 rounded hover:bg-gray-400"
            >
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
