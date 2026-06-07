import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import MainLayout from '../../layouts/main/MainLayout';
import { agencyService } from '../../services/agencyService';
import { Loader } from '../../components/common';

const AgenciesPage = () => {
  const [agencies, setAgencies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchAgencies();
  }, []);

  const fetchAgencies = async () => {
    try {
      setLoading(true);
      const response = await agencyService.getAgencies();
      const data = Array.isArray(response?.data) ? response.data : response;
      setAgencies(Array.isArray(data) ? data : []);
      setError(null);
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur lors du chargement des agences');
      setAgencies([]);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <Loader />;

  return (
    <MainLayout>
      <div className="min-h-screen bg-white">
        <div className="mx-auto max-w-7xl px-6 py-12 sm:px-12 sm:py-16 lg:px-8">
          <div className="mb-12">
            <h1 className="mb-4 text-4xl font-bold tracking-tight text-black sm:text-5xl">
              Nos Agences
            </h1>
            <p className="text-lg text-black/60">
              Découvrez nos agences partenaires et explorez leurs véhicules disponibles
            </p>
          </div>

          {error && (
            <div className="mb-8 rounded-lg bg-red-50 p-4 text-red-800">
              {error}
            </div>
          )}

          {agencies.length === 0 ? (
            <div className="text-center py-12">
              <p className="text-lg text-black/60">Aucune agence disponible</p>
            </div>
          ) : (
            <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
              {agencies.map((agency) => (
                <Link
                  key={agency.id}
                  to={`/agencies/${agency.id}`}
                  className="group rounded-2xl border border-black/10 bg-white p-6 shadow-sm transition-all duration-300 hover:shadow-lg hover:border-[#6C27D3]"
                >
                  <div className="flex flex-col h-full">
                    <h2 className="text-xl font-bold text-black group-hover:text-[#6C27D3] transition-colors">
                      {agency.name}
                    </h2>

                    <div className="mt-4 space-y-2 flex-grow">
                      {agency.city && (
                        <p className="text-sm text-black/60">
                          <span className="font-semibold">Ville:</span> {agency.city}
                        </p>
                      )}
                      {agency.address && (
                        <p className="text-sm text-black/60">
                          <span className="font-semibold">Adresse:</span> {agency.address}
                        </p>
                      )}
                      {agency.contactNumber && (
                        <p className="text-sm text-black/60">
                          <span className="font-semibold">Téléphone:</span> {agency.contactNumber}
                        </p>
                      )}
                      {agency.email && (
                        <p className="text-sm text-black/60">
                          <span className="font-semibold">Email:</span> {agency.email}
                        </p>
                      )}
                      {agency.description && (
                        <p className="text-sm text-black/60 mt-3">
                          {agency.description.length > 100
                            ? `${agency.description.substring(0, 100)}...`
                            : agency.description}
                        </p>
                      )}
                    </div>

                    <div className="mt-6 inline-block">
                      <span className="inline-flex items-center rounded-full bg-[#6C27D3] px-4 py-2 text-sm font-semibold text-white group-hover:bg-[#5a1fa8] transition-colors">
                        Voir les véhicules →
                      </span>
                    </div>
                  </div>
                </Link>
              ))}
            </div>
          )}
        </div>
      </div>
    </MainLayout>
  );
};

export default AgenciesPage;
