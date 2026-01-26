import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { AuthProvider } from './context/AuthContext';
import { Login } from './pages/Login';
import { Register } from './pages/Register';
import { Home } from './pages/Home';
import { Account } from './pages/Account';
import { Flights } from './pages/Flights';
import { Reservations } from './pages/Reservations';
import { NewReservation } from './pages/NewReservation';
import { PlaceBid } from './pages/PlaceBid';
import { Dashboard } from './pages/admin/Dashboard';
import { Employees } from './pages/admin/Employees';
import { Sales } from './pages/admin/Sales';
import { AdminFlights } from './pages/admin/AdminFlights';
import { AdminReservations } from './pages/admin/AdminReservations';
import { Layout } from './components/layout/Layout';
import { ProtectedRoute } from './components/layout/ProtectedRoute';

// Configure React Query client
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 1000 * 60 * 5, // 5 minutes
      retry: 1,
      refetchOnWindowFocus: false,
    },
    mutations: {
      retry: false,
    },
  },
});

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <BrowserRouter>
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />

            <Route element={<ProtectedRoute />}>
              <Route element={<Layout />}>
                <Route path="/" element={<Home />} />
                <Route path="/account" element={<Account />} />
                <Route path="/flights" element={<Flights />} />
                <Route path="/reservations" element={<Reservations />} />
                <Route path="/reservations/new" element={<NewReservation />} />
                <Route path="/reservations/bid" element={<PlaceBid />} />
                <Route path="/admin" element={<Dashboard />} />
                <Route path="/admin/sales" element={<Sales />} />
                <Route path="/admin/flights" element={<AdminFlights />} />
                <Route path="/admin/reservations" element={<AdminReservations />} />
                <Route path="/admin/employees" element={<Employees />} />
              </Route>
            </Route>

            <Route path="*" element={<Navigate to="/login" replace />} />
          </Routes>
        </BrowserRouter>
      </AuthProvider>
    </QueryClientProvider>
  );
}

export default App;
