import { useEffect, useState } from 'react';
import MainLayout from '../../layouts/main/MainLayout';
import { bookingService } from '../../services/bookingService';
import { useAuth } from '../../hooks/useAuth';
import { Loader } from '../../components/common';

export const ClientDashboardPage = () => {
  const { user } = useAuth();
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchBookings();
  }, []);

  const fetchBookings = async () => {
    try {
      setLoading(true);
      const response = await bookingService.getMyBookings();
      setBookings(Array.isArray(response) ? response : response.data || []);
    } catch (error) {
      console.error('Erreur:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <MainLayout><Loader /></MainLayout>;

  return (
    <MainLayout>
      <div className="py-12">
        <div className="max-w-5xl mx-auto px-4">
          <div className="mb-8">
            <h1 className="text-4xl font-bold text-black">Mon tableau de bord</h1>
            <p className="text-black/60 mt-2">Bienvenue, {user?.email}</p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-8">
            <div className="bg-white rounded-2xl border border-black/10 p-6">
              <p className="text-black/60 text-sm">Réservations</p>
              <p className="text-3xl font-bold text-black">{bookings.length}</p>
            </div>
            <div className="bg-white rounded-2xl border border-black/10 p-6">
              <p className="text-black/60 text-sm">En cours</p>
              <p className="text-3xl font-bold text-[#6C27D3]">
                {bookings.filter(b => b.status === 'ACTIVE').length}
              </p>
            </div>
            <div className="bg-white rounded-2xl border border-black/10 p-6">
              <p className="text-black/60 text-sm">Complétées</p>
              <p className="text-3xl font-bold text-green-600">
                {bookings.filter(b => b.status === 'COMPLETED').length}
              </p>
            </div>
            <div className="bg-white rounded-2xl border border-black/10 p-6">
              <p className="text-black/60 text-sm">Annulées</p>
              <p className="text-3xl font-bold text-red-600">
                {bookings.filter(b => b.status === 'CANCELLED').length}
              </p>
            </div>
          </div>

          <div className="bg-white rounded-3xl border border-black/10 p-6">
            <h2 className="text-2xl font-bold text-black mb-6">Mes réservations</h2>

            {bookings.length === 0 ? (
              <div className="text-center py-12">
                <p className="text-black/60">Aucune réservation pour le moment</p>
              </div>
            ) : (
              <div className="space-y-4">
                {bookings.map((booking) => (
                  <div key={booking.id} className="border border-black/10 rounded-2xl p-4 flex items-center justify-between">
                    <div>
                      <p className="font-bold text-black">{booking.vehicleName || 'Véhicule'}</p>
                      <p className="text-sm text-black/60">
                        {new Date(booking.startDate).toLocaleDateString()} au {new Date(booking.endDate).toLocaleDateString()}
                      </p>
                    </div>
                    <div className="text-right">
                      <p className="font-bold text-[#6C27D3]">{booking.totalPrice}€</p>
                      <p className="text-sm text-black/60">{booking.status}</p>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      </div>
    </MainLayout>
  );
};
