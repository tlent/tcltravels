import React, { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { usersApi } from '../api/users';
import { LoadingSpinner } from '../components/common/LoadingSpinner';
import { ErrorMessage } from '../components/common/ErrorMessage';
import { updateProfileSchema, type UpdateProfileFormData } from '../lib/validations';

export const Account: React.FC = () => {
  const [editMode, setEditMode] = useState(false);
  const queryClient = useQueryClient();

  const { data: profile, isLoading, error } = useQuery({
    queryKey: ['profile'],
    queryFn: usersApi.getMe,
  });

  const {
    register,
    handleSubmit,
    formState: { errors: formErrors },
    reset,
    setError: setFormError,
  } = useForm<UpdateProfileFormData>({
    resolver: zodResolver(updateProfileSchema),
  });

  const updateMutation = useMutation({
    mutationFn: usersApi.updateMe,
    onSuccess: (data) => {
      queryClient.setQueryData(['profile'], data);
      reset({ ...data, currentPassword: '' });
      setEditMode(false);
    },
    onError: (err: any) => {
      setFormError('root', {
        message: err.response?.data?.error || 'Failed to update profile',
      });
    },
  });

  useEffect(() => {
    if (profile) {
      reset({ ...profile, currentPassword: '' });
    }
  }, [profile, reset]);

  const onSubmit = async (data: UpdateProfileFormData) => {
    const updateRequest = {
      firstName: data.firstName,
      lastName: data.lastName,
      address: data.address,
      city: data.city,
      state: data.state.toUpperCase(),
      zipcode: data.zipcode,
      telephone: data.telephone,
      email: profile?.email || '',
      password: data.currentPassword,
    };
    updateMutation.mutate(updateRequest);
  };

  const handleCancel = () => {
    if (profile) {
      reset({ ...profile, currentPassword: '' });
    }
    setEditMode(false);
  };

  if (isLoading) {
    return <LoadingSpinner />;
  }

  return (
    <div className="container mx-auto px-4 py-8 max-w-2xl">
      <div className="bg-white rounded-lg shadow p-6">
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-3xl font-bold text-gray-900">My Account</h1>
          {!editMode && (
            <button
              onClick={() => setEditMode(true)}
              className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
            >
              Edit Profile
            </button>
          )}
        </div>

        {error && <ErrorMessage message={String(error)} />}
        {formErrors.root && <ErrorMessage message={formErrors.root.message || ''} />}

        <form onSubmit={handleSubmit(onSubmit)} className="mt-6">
          <div className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">First Name</label>
                {editMode ? (
                  <>
                    <input
                      {...register('firstName')}
                      type="text"
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                    />
                    {formErrors.firstName && (
                      <p className="mt-1 text-sm text-red-600">{formErrors.firstName.message}</p>
                    )}
                  </>
                ) : (
                  <p className="mt-1 text-gray-900">{profile?.firstName}</p>
                )}
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700">Last Name</label>
                {editMode ? (
                  <>
                    <input
                      {...register('lastName')}
                      type="text"
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                    />
                    {formErrors.lastName && (
                      <p className="mt-1 text-sm text-red-600">{formErrors.lastName.message}</p>
                    )}
                  </>
                ) : (
                  <p className="mt-1 text-gray-900">{profile?.lastName}</p>
                )}
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">Email</label>
              <p className="mt-1 text-gray-900">{profile?.email}</p>
              <p className="mt-1 text-xs text-gray-500">Email cannot be changed</p>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">Address</label>
              {editMode ? (
                <>
                  <input
                    {...register('address')}
                    type="text"
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                  />
                  {formErrors.address && (
                    <p className="mt-1 text-sm text-red-600">{formErrors.address.message}</p>
                  )}
                </>
              ) : (
                <p className="mt-1 text-gray-900">{profile?.address}</p>
              )}
            </div>

            <div className="grid grid-cols-3 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">City</label>
                {editMode ? (
                  <>
                    <input
                      {...register('city')}
                      type="text"
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                    />
                    {formErrors.city && (
                      <p className="mt-1 text-sm text-red-600">{formErrors.city.message}</p>
                    )}
                  </>
                ) : (
                  <p className="mt-1 text-gray-900">{profile?.city}</p>
                )}
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700">State</label>
                {editMode ? (
                  <>
                    <input
                      {...register('state')}
                      type="text"
                      maxLength={2}
                      placeholder="NY"
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                    />
                    {formErrors.state && (
                      <p className="mt-1 text-sm text-red-600">{formErrors.state.message}</p>
                    )}
                  </>
                ) : (
                  <p className="mt-1 text-gray-900">{profile?.state}</p>
                )}
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700">ZIP Code</label>
                {editMode ? (
                  <>
                    <input
                      {...register('zipcode')}
                      type="text"
                      maxLength={5}
                      placeholder="12345"
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                    />
                    {formErrors.zipcode && (
                      <p className="mt-1 text-sm text-red-600">{formErrors.zipcode.message}</p>
                    )}
                  </>
                ) : (
                  <p className="mt-1 text-gray-900">{profile?.zipcode}</p>
                )}
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">Telephone</label>
              {editMode ? (
                <>
                  <input
                    {...register('telephone')}
                    type="tel"
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                  />
                  {formErrors.telephone && (
                    <p className="mt-1 text-sm text-red-600">{formErrors.telephone.message}</p>
                  )}
                </>
              ) : (
                <p className="mt-1 text-gray-900">{profile?.telephone}</p>
              )}
            </div>

            {editMode && (
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Current Password *
                </label>
                <input
                  {...register('currentPassword')}
                  type="password"
                  placeholder="Enter current password to confirm changes"
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                />
                {formErrors.currentPassword && (
                  <p className="mt-1 text-sm text-red-600">{formErrors.currentPassword.message}</p>
                )}
                <p className="mt-1 text-xs text-gray-500">
                  Required to verify your identity before making changes
                </p>
              </div>
            )}
          </div>

          {editMode && (
            <div className="mt-6 flex gap-3">
              <button
                type="submit"
                disabled={updateMutation.isPending}
                className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:bg-gray-400 disabled:cursor-not-allowed"
              >
                {updateMutation.isPending ? 'Saving...' : 'Save Changes'}
              </button>
              <button
                type="button"
                onClick={handleCancel}
                className="px-4 py-2 bg-gray-200 text-gray-700 rounded hover:bg-gray-300"
              >
                Cancel
              </button>
            </div>
          )}
        </form>
      </div>
    </div>
  );
};
