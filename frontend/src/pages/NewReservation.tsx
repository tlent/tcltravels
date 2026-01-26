import React, { useState, useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { flightsApi } from '../api/flights';
import { reservationsApi } from '../api/reservations';
import type { CreateReservationRequest } from '../api/reservations';
import { LoadingSpinner } from '../components/common/LoadingSpinner';
import { ErrorMessage } from '../components/common/ErrorMessage';

export const NewReservation: React.FC = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  const airlineId = searchParams.get('airlineId') || '';
  const flightNumber = parseInt(searchParams.get('flightNumber') || '0');

  const [stops, setStops] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');
  const [passengerCount, setPassengerCount] = useState(1);
  const [formData, setFormData] = useState({
    origin: 0,
    destination: 0,
    departureDate: '',
    other: '',
    passengers: [{ firstName: '', lastName: '', seatClass: 'Economy', meal: '' }],
  });

  useEffect(() => {
    loadStops();
  }, []);

  const loadStops = async () => {
    try {
      const data = await flightsApi.getStops(airlineId, flightNumber);
      setStops(data);
      if (data.length > 0) {
        setFormData((prev) => ({
          ...prev,
          origin: 1,
          destination: data.length,
          departureDate: data[0].departureDate,
        }));
      }
    } catch (err: any) {
      setError('Failed to load flight information');
    } finally {
      setLoading(false);
    }
  };

  const handlePassengerCountChange = (count: number) => {
    setPassengerCount(count);
    const passengers = Array.from({ length: count }, (_, i) => formData.passengers[i] || {
      firstName: '',
      lastName: '',
      seatClass: 'Economy',
      meal: '',
    });
    setFormData((prev) => ({ ...prev, passengers }));
  };

  const handlePassengerChange = (index: number, field: string, value: string) => {
    const updatedPassengers = [...formData.passengers];
    updatedPassengers[index] = { ...updatedPassengers[index], [field]: value };
    setFormData((prev) => ({ ...prev, passengers: updatedPassengers }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setSubmitting(true);

    try {
      const request: CreateReservationRequest = {
        airlineId,
        flightNumber,
        origin: formData.origin,
        destination: formData.destination,
        passengerCount,
        departureDate: formData.departureDate,
        other: formData.other,
      };

      // Map passengers to the backend format (first1, last1, class1, food1, etc.)
      formData.passengers.forEach((p, i) => {
        const idx = i + 1;
        (request as any)[`first${idx}`] = p.firstName;
        (request as any)[`last${idx}`] = p.lastName;
        (request as any)[`class${idx}`] = p.seatClass;
        (request as any)[`food${idx}`] = p.meal;
      });

      await reservationsApi.create(request);
      navigate('/reservations');
    } catch (err: any) {
      setError(err.response?.data?.error || 'Failed to create reservation');
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) {
    return <LoadingSpinner />;
  }

  return (
    <div className="container mx-auto px-4 py-8 max-w-4xl">
      <h1 className="text-3xl font-bold text-gray-900 mb-6">New Reservation</h1>

      <div className="bg-white rounded-lg shadow p-6">
        <div className="mb-6">
          <h2 className="text-xl font-semibold text-gray-900">
            Flight {airlineId} {flightNumber}
          </h2>
        </div>

        {error && <ErrorMessage message={error} />}

        <form onSubmit={handleSubmit}>
          {/* Origin/Destination */}
          <div className="grid grid-cols-2 gap-4 mb-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Origin Stop
              </label>
              <select
                value={formData.origin}
                onChange={(e) =>
                  setFormData((prev) => ({ ...prev, origin: parseInt(e.target.value) }))
                }
                className="w-full px-3 py-2 border border-gray-300 rounded-md"
                required
              >
                {stops.map((stop) => (
                  <option key={stop.stopNumber} value={stop.stopNumber}>
                    Stop {stop.stopNumber}: {stop.airportId}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Destination Stop
              </label>
              <select
                value={formData.destination}
                onChange={(e) =>
                  setFormData((prev) => ({ ...prev, destination: parseInt(e.target.value) }))
                }
                className="w-full px-3 py-2 border border-gray-300 rounded-md"
                required
              >
                {stops.map((stop) => (
                  <option key={stop.stopNumber} value={stop.stopNumber}>
                    Stop {stop.stopNumber}: {stop.airportId}
                  </option>
                ))}
              </select>
            </div>
          </div>

          {/* Departure Date */}
          <div className="mb-4">
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

          {/* Passenger Count */}
          <div className="mb-6">
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Number of Passengers (1-5)
            </label>
            <input
              type="number"
              min="1"
              max="5"
              value={passengerCount}
              onChange={(e) => handlePassengerCountChange(parseInt(e.target.value))}
              className="w-full px-3 py-2 border border-gray-300 rounded-md"
              required
            />
          </div>

          {/* Passenger Details */}
          <div className="space-y-4 mb-6">
            <h3 className="text-lg font-semibold text-gray-900">Passenger Details</h3>
            {formData.passengers.map((passenger, idx) => (
              <div key={idx} className="p-4 border border-gray-200 rounded-lg">
                <h4 className="font-medium text-gray-900 mb-3">Passenger {idx + 1}</h4>
                <div className="grid grid-cols-2 gap-4">
                  <input
                    type="text"
                    placeholder="First Name"
                    value={passenger.firstName}
                    onChange={(e) => handlePassengerChange(idx, 'firstName', e.target.value)}
                    className="px-3 py-2 border border-gray-300 rounded-md"
                    required
                  />
                  <input
                    type="text"
                    placeholder="Last Name"
                    value={passenger.lastName}
                    onChange={(e) => handlePassengerChange(idx, 'lastName', e.target.value)}
                    className="px-3 py-2 border border-gray-300 rounded-md"
                    required
                  />
                  <select
                    value={passenger.seatClass}
                    onChange={(e) => handlePassengerChange(idx, 'seatClass', e.target.value)}
                    className="px-3 py-2 border border-gray-300 rounded-md"
                  >
                    <option value="Economy">Economy</option>
                    <option value="Business">Business</option>
                    <option value="First">First Class</option>
                  </select>
                  <input
                    type="text"
                    placeholder="Meal Preference"
                    value={passenger.meal}
                    onChange={(e) => handlePassengerChange(idx, 'meal', e.target.value)}
                    className="px-3 py-2 border border-gray-300 rounded-md"
                  />
                </div>
              </div>
            ))}
          </div>

          {/* Submit */}
          <div className="flex gap-4">
            <button
              type="submit"
              disabled={submitting}
              className="px-6 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:bg-gray-400"
            >
              {submitting ? 'Creating...' : 'Create Reservation'}
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
