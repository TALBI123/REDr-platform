import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import './App.css';

// Pages
import PageHome from './pages/client/PageHome';
import { LoginPage } from './pages/auth/LoginPage';
import { RegisterClientPage } from './pages/auth/RegisterClientPage';
import { RegisterAgencyPage } from './pages/auth/RegisterAgencyPage';
import { CarsPage } from './pages/public/CarsPage';
import { CarDetailPage } from './pages/public/CarDetailPage';
import { BookingPage } from './pages/public/BookingPage';
import AgenciesPage from './pages/public/AgenciesPage';
import AgencyDetailPage from './pages/public/AgencyDetailPage';
import { LocationsPage } from './pages/public/LocationsPage';
import { DealsPage } from './pages/public/DealsPage';
import { SupportPage } from './pages/public/SupportPage';
import { ProfilePage } from './pages/profile/ProfilePage';
import { ClientDashboardPage } from './pages/dashboard/ClientDashboardPage';
import { AgencyDashboardPage } from './pages/dashboard/AgencyDashboardPage';
import { AdminDashboardPage } from './pages/dashboard/AdminDashboardPage';
import { ProtectedRoute } from './components/common/ProtectedRoute';

function App() {
  return (
    <Router>
      <Routes>
        {/* Routes publiques */}
        <Route path="/" element={<PageHome />} />
        <Route path="/cars" element={<CarsPage />} />
        <Route path="/cars/:carId" element={<CarDetailPage />} />
        <Route path="/booking/:carId" element={<BookingPage />} />
        <Route path="/agencies" element={<AgenciesPage />} />
        <Route path="/agencies/:agencyId" element={<AgencyDetailPage />} />
        <Route path="/locations" element={<LocationsPage />} />
        <Route path="/deals" element={<DealsPage />} />
        <Route path="/support" element={<SupportPage />} />

        {/* Routes d'authentification */}
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register/client" element={<RegisterClientPage />} />
        <Route path="/register/agency" element={<RegisterAgencyPage />} />

        <Route
          path="/profile"
          element={
            <ProtectedRoute>
              <ProfilePage />
            </ProtectedRoute>
          }
        />

        {/* Routes protégées - Dashboard Client */}
        <Route
          path="/dashboard/client"
          element={
            <ProtectedRoute requiredRole="CLIENT">
              <ClientDashboardPage />
            </ProtectedRoute>
          }
        />

        {/* Routes protégées - Dashboard Agence */}
        <Route
          path="/dashboard/agency"
          element={
            <ProtectedRoute requiredRole="AGENCY_MANAGER">
              <AgencyDashboardPage />
            </ProtectedRoute>
          }
        />

        {/* Routes protégées - Dashboard Admin */}
        <Route
          path="/dashboard/admin"
          element={
            <ProtectedRoute requiredRole="SUPER_ADMIN">
              <AdminDashboardPage />
            </ProtectedRoute>
          }
        />

        {/* Route par défaut */}
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </Router>
  );
}

export default App;
