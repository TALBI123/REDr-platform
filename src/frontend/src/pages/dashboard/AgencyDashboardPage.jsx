import { useEffect, useState } from 'react';
import MainLayout from '../../layouts/main/MainLayout';
import { vehicleService } from '../../services/vehicleService';
import { useAuth } from '../../hooks/useAuth';
import { Loader } from '../../components/common';

export const AgencyDashboardPage = () => {
  const { user } = useAuth();
  const [vehicles, setVehicles] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchVehicles();
  }, []);

  const fetchVehicles = async () => {
    try {
      setLoading(true);
      // Note: vous devrez récupérer l'agencyId depuis le user
      // const response = await vehicleService.getAgencyVehicles(user.agencyId);
      // setVehicles(Array.isArray(response) ? response : response.data || []);
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
            <h1 className="text-4xl font-bold text-black">Tableau de bord agence</h1>
            <p className="text-black/60 mt-2">Gérez votre flotte de véhicules</p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-8">
            <div className="bg-white rounded-2xl border border-black/10 p-6">
              <p className="text-black/60 text-sm">Véhicules</p>
              <p className="text-3xl font-bold text-black">{vehicles.length}</p>
            </div>
            <div className="bg-white rounded-2xl border border-black/10 p-6">
              <p className="text-black/60 text-sm">Disponibles</p>
              <p className="text-3xl font-bold text-green-600">
                {vehicles.filter(v => v.status === 'Available').length}
              </p>
            </div>
            <div className="bg-white rounded-2xl border border-black/10 p-6">
              <p className="text-black/60 text-sm">En maintenance</p>
              <p className="text-3xl font-bold text-orange-600">
                {vehicles.filter(v => v.status === 'Maintenance').length}
              </p>
            </div>
          </div>

          <div className="bg-white rounded-3xl border border-black/10 p-6">
            <h2 className="text-2xl font-bold text-black mb-6">Ma flotte</h2>

            {vehicles.length === 0 ? (
              <div className="text-center py-12">
                <p className="text-black/60 mb-4">Aucun véhicule pour le moment</p>
                <button className="h-11 px-6 rounded-full bg-[#6C27D3] text-white font-bold uppercase tracking-[0.2em]">
                  Ajouter un véhicule
                </button>
              </div>
            ) : (
              <div className="space-y-4">
                {vehicles.map((vehicle) => (
                  <div key={vehicle.id} className="border border-black/10 rounded-2xl p-4 flex items-center justify-between">
                    <div>
                      <p className="font-bold text-black">{vehicle.brand} {vehicle.mode}</p>
                      <p className="text-sm text-black/60">{vehicle.year} • {vehicle.seatCapacity} places</p>
                    </div>
                    <div className="text-right">
                      <p className="font-bold text-[#6C27D3]">{vehicle.dailyPrice}€/jour</p>
                      <p className="text-sm text-black/60">{vehicle.status}</p>
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
