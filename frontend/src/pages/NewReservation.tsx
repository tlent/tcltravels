import React, { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useForm, useFieldArray } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { flightsApi } from '../api/flights';
import { reservationsApi } from '../api/reservations';
import type { CreateReservationRequest } from '../api/reservations';
import { LoadingSpinner } from '../components/common/LoadingSpinner';
import { ErrorMessage } from '../components/common/ErrorMessage';
import { newReservationSchema, type NewReservationFormData } from '../lib/validations';

export const NewReservation: React.FC = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const airlineId = searchParams.get('airlineId') || '';
  const flightNumber = parseInt(searchParams.get('flightNumber') || '0');

  const {
    register,
    control,
    handleSubmit,
    formState: { errors },
    reset,
    setError: setFormError,
  } = useForm<NewReservationFormData>({
    resolver: zodResolver(newReservationSchema),
    defaultValues: {
      origin: 1,
      destination: 2,
      departureDate: '',
      other: '',
      passengers: [{ firstName: '', lastName: '', seatClass: 'Economy', meal: '' }],
    },
  });

  const { fields, append, remove } = useFieldArray({
    control,
    name: 'passengers',
  });

  const { data: stops, isLoading, error } = useQuery({
    queryKey: ['flightStops', airlineId, flightNumber],
    queryFn: () => flightsApi.getStops(airlineId, flightNumber),
  });

  useEffect(() => {
    if (stops && stops.length > 0) {
      reset({
        origin: 1,
        destination: stops.length,
        departureDate: stops[0].departureDate,
        other: '',
        passengers: [{ firstName: '', lastName: '', seatClass: 'Economy', meal: '' }],
      });
    }
  }, [stops, reset]);

  const createReservationMutation = useMutation({
    mutationFn: (request: CreateReservationRequest) => reservationsApi.create(request),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['reservations'] });
      navigate('/reservations');
    },
    onError: (err: any) => {
      setFormError('root', {
        message: err.response?.data?.error || 'Failed to create reservation',
      });
    },
  });

  const onSubmit = (data: NewReservationFormData) => {
    const request: CreateReservationRequest = {
      airlineId,
      flightNumber,
      origin: data.origin,
      destination: data.destination,
      passengerCount: data.passengers.length,
      departureDate: data.departureDate,
      other: data.other || '',
      passengers: data.passengers,
    };

    createReservationMutation.mutate(request);
  };

  const addPassenger = () => {
    if (fields.length < 5) {
      append({ firstName: '', lastName: '', seatClass: 'Economy', meal: '' });
    }
  };

  const removePassenger = (index: number) => {
    if (fields.length > 1) {
      remove(index);
    }
  };

  if (isLoading) {
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

        {error && <ErrorMessage message={String(error)} />}
        {errors.root && <ErrorMessage message={errors.root.message || ''} />}

        <form onSubmit={handleSubmit(onSubmit)}>
          {/* Origin/Destination */}
          <div className="grid grid-cols-2 gap-4 mb-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Origin Stop *
              </label>
              <select
                {...register('origin', { valueAsNumber: true })}
                className="w-full px-3 py-2 border border-gray-300 rounded-md"
              >
                {stops?.map((stop) => (
                  <option key={stop.stopNumber} value={stop.stopNumber}>
                    Stop {stop.stopNumber}: {stop.airportId}
                  </option>
                ))}
              </select>
              {errors.origin && (
                <p className="mt-1 text-sm text-red-600">{errors.origin.message}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Destination Stop *
              </label>
              <select
                {...register('destination', { valueAsNumber: true })}
                className="w-full px-3 py-2 border border-gray-300 rounded-md"
              >
                {stops?.map((stop) => (
                  <option key={stop.stopNumber} value={stop.stopNumber}>
                    Stop {stop.stopNumber}: {stop.airportId}
                  </option>
                ))}
              </select>
              {errors.destination && (
                <p className="mt-1 text-sm text-red-600">{errors.destination.message}</p>
              )}
            </div>
          </div>

          {/* Departure Date */}
          <div className="mb-4">
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Departure Date *
            </label>
            <input
              {...register('departureDate')}
              type="date"
              className="w-full px-3 py-2 border border-gray-300 rounded-md"
            />
            {errors.departureDate && (
              <p className="mt-1 text-sm text-red-600">{errors.departureDate.message}</p>
            )}
          </div>

          {/* Other/Special Requests */}
          <div className="mb-6">
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Special Requests (Optional)
            </label>
            <input
              {...register('other')}
              type="text"
              placeholder="Any special requests or notes"
              className="w-full px-3 py-2 border border-gray-300 rounded-md"
            />
          </div>

          {/* Passenger Details */}
          <div className="space-y-4 mb-6">
            <div className="flex justify-between items-center">
              <h3 className="text-lg font-semibold text-gray-900">
                Passengers ({fields.length}/5)
              </h3>
              {fields.length < 5 && (
                <button
                  type="button"
                  onClick={addPassenger}
                  className="px-4 py-2 bg-green-600 text-white text-sm rounded hover:bg-green-700"
                >
                  + Add Passenger
                </button>
              )}
            </div>

            {errors.passengers && typeof errors.passengers === 'object' && !Array.isArray(errors.passengers) && (
              <p className="text-sm text-red-600">{errors.passengers.message}</p>
            )}

            {fields.map((field, idx) => (
              <div key={field.id} className="p-4 border border-gray-200 rounded-lg">
                <div className="flex justify-between items-center mb-3">
                  <h4 className="font-medium text-gray-900">Passenger {idx + 1}</h4>
                  {fields.length > 1 && (
                    <button
                      type="button"
                      onClick={() => removePassenger(idx)}
                      className="text-red-600 hover:text-red-700 text-sm"
                    >
                      Remove
                    </button>
                  )}
                </div>
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <input
                      {...register(`passengers.${idx}.firstName`)}
                      type="text"
                      placeholder="First Name *"
                      className="px-3 py-2 border border-gray-300 rounded-md w-full"
                    />
                    {errors.passengers?.[idx]?.firstName && (
                      <p className="mt-1 text-sm text-red-600">
                        {errors.passengers[idx]?.firstName?.message}
                      </p>
                    )}
                  </div>
                  <div>
                    <input
                      {...register(`passengers.${idx}.lastName`)}
                      type="text"
                      placeholder="Last Name *"
                      className="px-3 py-2 border border-gray-300 rounded-md w-full"
                    />
                    {errors.passengers?.[idx]?.lastName && (
                      <p className="mt-1 text-sm text-red-600">
                        {errors.passengers[idx]?.lastName?.message}
                      </p>
                    )}
                  </div>
                  <div>
                    <select
                      {...register(`passengers.${idx}.seatClass`)}
                      className="px-3 py-2 border border-gray-300 rounded-md w-full"
                    >
                      <option value="Economy">Economy</option>
                      <option value="Business">Business</option>
                      <option value="First">First Class</option>
                    </select>
                    {errors.passengers?.[idx]?.seatClass && (
                      <p className="mt-1 text-sm text-red-600">
                        {errors.passengers[idx]?.seatClass?.message}
                      </p>
                    )}
                  </div>
                  <div>
                    <input
                      {...register(`passengers.${idx}.meal`)}
                      type="text"
                      placeholder="Meal Preference (Optional)"
                      className="px-3 py-2 border border-gray-300 rounded-md w-full"
                    />
                  </div>
                </div>
              </div>
            ))}
          </div>

          {/* Submit */}
          <div className="flex gap-4">
            <button
              type="submit"
              disabled={createReservationMutation.isPending}
              className="px-6 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:bg-gray-400 disabled:cursor-not-allowed"
            >
              {createReservationMutation.isPending ? 'Creating...' : 'Create Reservation'}
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
