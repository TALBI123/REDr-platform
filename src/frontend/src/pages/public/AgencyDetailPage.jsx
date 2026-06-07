import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import MainLayout from '../../layouts/main/MainLayout';
import CarCard from '../../components/card/CarCard';
import { agencyService } from '../../services/agencyService';
import { vehicleService } from '../../services/vehicleService';
import { Loader } from '../../components/common';

const AgencyDetailPage = () => {
  const { agencyId } = useParams();
  const [agency, setAgency] = useState(null);
  const [vehicles, setVehicles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchAgencyAndVehicles();
  }, [agencyId]);

  const fetchAgencyAndVehicles = async () => {
    try {
      setLoading(true);
      setError(null);

      // Récupérer les détails de l'agence
      const agencyResponse = await agencyService.getAgencyDetail(agencyId);
      const agencyData = agencyResponse?.data || agencyResponse;
      setAgency(agencyData);

      // Récupérer les véhicules de l'agence
      const vehiclesResponse = await vehicleService.getAgencyVehicles(agencyId);
      const vehiclesData = Array.isArray(vehiclesResponse?.data)
        ? vehiclesResponse.data
        : Array.isArray(vehiclesResponse)
        ? vehiclesResponse
        : [];
      setVehicles(vehiclesData);
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur lors du chargement des données');
      setAgency(null);
      setVehicles([]);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <Loader />;

  if (error || !agency) {
    return (
      <MainLayout>
        <div className="min-h-screen bg-white">
          <div className="mx-auto max-w-7xl px-6 py-12 sm:px-12">
            <div className="text-center">
              <h1 className="text-2xl font-bold text-black mb-4">Agence non trouvée</h1>
              <p className="text-black/60 mb-6">{error || 'L\'agence demandée n\'existe pas'}</p>
              <Link
                to="/agencies"
                className="inline-flex items-center rounded-full bg-[#6C27D3] px-6 py-3 text-sm font-semibold text-white hover:bg-[#5a1fa8] transition-colors"
              >
                ← Retour aux agences
              </Link>
            </div>
          </div>
        </div>
      </MainLayout>
    );
  }

  return (
    <MainLayout>
      <div className="min-h-screen bg-white">
        <div className="mx-auto max-w-7xl px-6 py-12 sm:px-12 sm:py-16 lg:px-8">
          {/* Bouton retour */}
          <Link
            to="/agencies"
            className="mb-8 inline-flex items-center text-[#6C27D3] hover:text-[#5a1fa8] transition-colors"
          >
            ← Retour aux agences
          </Link>

          {/* En-tête agence */}
          <div className="mb-12 rounded-2xl border border-black/10 bg-gradient-to-r from-[#6C27D3]/5 to-transparent p-8">
            <h1 className="mb-4 text-4xl font-bold text-black sm:text-5xl">
              {agency.name}
            </h1>

            <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
              {agency.city && (
                <div>
                  <p className="text-sm font-semibold text-black/60">Ville</p>
                  <p className="text-lg text-black">{agency.city}</p>
                </div>
              )}
              {agency.address && (
                <div>
                  <p className="text-sm font-semibold text-black/60">Adresse</p>
                  <p className="text-lg text-black">{agency.address}</p>
                </div>
              )}
              {agency.contactNumber && (
                <div>
                  <p className="text-sm font-semibold text-black/60">Téléphone</p>
                  <p className="text-lg text-black">{agency.contactNumber}</p>
                </div>
              )}
              {agency.email && (
                <div>
                  <p className="text-sm font-semibold text-black/60">Email</p>
                  <p className="text-lg text-black">{agency.email}</p>
                </div>
              )}
            </div>

            {agency.description && (
              <p className="mt-6 text-black/70">
                {agency.description}
              </p>
            )}
          </div>

          {/* Véhicules */}
          <div>
            <h2 className="mb-8 text-3xl font-bold text-black">
              Véhicules disponibles ({vehicles.length})
            </h2>

            {vehicles.length === 0 ? (
              <div className="text-center py-12 rounded-2xl border border-black/10 bg-white">
                <p className="text-lg text-black/60">Aucun véhicule disponible pour cette agence</p>
              </div>
            ) : (
              <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
                {vehicles.map((car) => (
                  <CarCard key={car.id} car={car} />
                ))}
              </div>
            )}
          </div>
        </div>
      </div>
    </MainLayout>
  );
};

export default AgencyDetailPage;
