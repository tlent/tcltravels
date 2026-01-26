import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

export const Header: React.FC = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const isCustomer = user?.role === 'ROLE_CUSTOMER';
  const isEmployee = user?.role === 'ROLE_EMPLOYEE' || user?.role === 'ROLE_MANAGER';

  return (
    <header className="bg-blue-600 text-white shadow">
      <div className="container mx-auto px-4">
        <div className="flex items-center justify-between h-16">
          <div className="flex items-center space-x-8">
            <Link to="/" className="text-xl font-bold">
              TCL Travels
            </Link>

            <nav className="flex space-x-4">
              {isCustomer && (
                <>
                  <Link to="/" className="hover:text-blue-200">
                    Home
                  </Link>
                  <Link to="/flights" className="hover:text-blue-200">
                    Flights
                  </Link>
                  <Link to="/reservations" className="hover:text-blue-200">
                    Reservations
                  </Link>
                  <Link to="/account" className="hover:text-blue-200">
                    Account
                  </Link>
                </>
              )}

              {isEmployee && (
                <>
                  <Link to="/admin" className="hover:text-blue-200">
                    Dashboard
                  </Link>
                  <Link to="/admin/sales" className="hover:text-blue-200">
                    Sales
                  </Link>
                  <Link to="/admin/flights" className="hover:text-blue-200">
                    Flights
                  </Link>
                  <Link to="/admin/reservations" className="hover:text-blue-200">
                    Reservations
                  </Link>
                  {user?.role === 'ROLE_MANAGER' && (
                    <Link to="/admin/employees" className="hover:text-blue-200">
                      Employees
                    </Link>
                  )}
                </>
              )}
            </nav>
          </div>

          <div className="flex items-center space-x-4">
            <span className="text-sm">
              {user?.firstName} {user?.lastName} ({user?.roleLabel})
            </span>
            <button
              onClick={handleLogout}
              className="bg-blue-700 hover:bg-blue-800 px-4 py-2 rounded"
            >
              Logout
            </button>
          </div>
        </div>
      </div>
    </header>
  );
};
