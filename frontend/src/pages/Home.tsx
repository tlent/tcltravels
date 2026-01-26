import React from 'react';
import { useAuth } from '../context/AuthContext';

export const Home: React.FC = () => {
  const { user } = useAuth();

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="bg-white rounded-lg shadow p-6">
        <h1 className="text-3xl font-bold text-gray-900 mb-4">
          Welcome, {user?.firstName}!
        </h1>
        <p className="text-gray-600">
          You are logged in as a {user?.roleLabel}.
        </p>
      </div>
    </div>
  );
};
