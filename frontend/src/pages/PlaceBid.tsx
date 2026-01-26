import React, { useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { reservationsApi } from '../api/reservations';
import type { PlaceBidRequest } from '../api/reservations';
import { ErrorMessage } from '../components/common/ErrorMessage';
import { placeBidSchema, type PlaceBidFormData } from '../lib/validations';

export const PlaceBid: React.FC = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const airlineId = searchParams.get('airlineId') || '';
  const flightNumber = parseInt(searchParams.get('flightNumber') || '0');

  const [success, setSuccess] = useState('');

  const {
    register,
    handleSubmit,
    formState: { errors },
    setError,
  } = useForm<PlaceBidFormData>({
    resolver: zodResolver(placeBidSchema),
    defaultValues: {
      departureDate: '',
      flightClass: 'Economy',
      bid: 0,
      food: '',
      other: '',
    },
  });

  const placeBidMutation = useMutation({
    mutationFn: (request: PlaceBidRequest) => reservationsApi.placeBid(request),
    onSuccess: (response) => {
      queryClient.invalidateQueries({ queryKey: ['reservations'] });

      if (response.accepted) {
        setSuccess(response.message);
        setTimeout(() => navigate('/reservations'), 2000);
      } else if (response.rejected) {
        setError('root', { message: response.message });
      } else {
        setSuccess('Bid submitted successfully');
        setTimeout(() => navigate('/reservations'), 2000);
      }
    },
    onError: (err: any) => {
      setError('root', {
        message: err.response?.data?.error || 'Failed to submit bid',
      });
    },
  });

  const onSubmit = (data: PlaceBidFormData) => {
    setSuccess('');

    const request: PlaceBidRequest = {
      airlineId,
      flightNumber,
      departureDate: data.departureDate,
      flightClass: data.flightClass,
      bid: data.bid,
      food: data.food || '',
      other: data.other || '',
    };

    placeBidMutation.mutate(request);
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

        {errors.root && <ErrorMessage message={errors.root.message || ''} />}
        {success && (
          <div className="bg-green-50 border border-green-200 text-green-700 px-4 py-3 rounded mb-4">
            <p className="font-medium">Success!</p>
            <p className="text-sm">{success}</p>
          </div>
        )}

        <form onSubmit={handleSubmit(onSubmit)}>
          <div className="space-y-4">
            <div>
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

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Class *
              </label>
              <select
                {...register('flightClass')}
                className="w-full px-3 py-2 border border-gray-300 rounded-md"
              >
                <option value="Economy">Economy</option>
                <option value="Business">Business</option>
                <option value="First">First Class</option>
              </select>
              {errors.flightClass && (
                <p className="mt-1 text-sm text-red-600">{errors.flightClass.message}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Your Bid Amount ($) *
              </label>
              <input
                {...register('bid', { valueAsNumber: true })}
                type="number"
                step="0.01"
                min="0.01"
                placeholder="0.00"
                className="w-full px-3 py-2 border border-gray-300 rounded-md"
              />
              {errors.bid && (
                <p className="mt-1 text-sm text-red-600">{errors.bid.message}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Meal Preference (optional)
              </label>
              <input
                {...register('food')}
                type="text"
                className="w-full px-3 py-2 border border-gray-300 rounded-md"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Special Requests (optional)
              </label>
              <input
                {...register('other')}
                type="text"
                className="w-full px-3 py-2 border border-gray-300 rounded-md"
              />
            </div>
          </div>

          <div className="flex gap-4 mt-6">
            <button
              type="submit"
              disabled={placeBidMutation.isPending}
              className="px-6 py-2 bg-green-600 text-white rounded hover:bg-green-700 disabled:bg-gray-400 disabled:cursor-not-allowed"
            >
              {placeBidMutation.isPending ? 'Submitting...' : 'Submit Bid'}
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
