import { useEffect, useState } from 'react';
import MainLayout from '../../layouts/main/MainLayout';
import { agencyService } from '../../services/agencyService';
import { Loader } from '../../components/common';

export const AdminDashboardPage = () => {
  const [pendingAgencies, setPendingAgencies] = useState([]);
  const [allAgencies, setAllAgencies] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchAgencies();
  }, []);

  const fetchAgencies = async () => {
    try {
      setLoading(true);
      const pending = await agencyService.getPendingAgencies();
      const all = await agencyService.getAdminAgencies?.();
      
      setPendingAgencies(Array.isArray(pending) ? pending : pending.data || []);
      setAllAgencies(Array.isArray(all) ? all : all?.data || []);
    } catch (error) {
      console.error('Erreur:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleApprove = async (agencyId) => {
    try {
      await agencyService.approveAgency(agencyId, 'Approuvé');
      fetchAgencies();
    } catch (error) {
      console.error('Erreur:', error);
    }
  };

  const handleReject = async (agencyId) => {
    try {
      await agencyService.rejectAgency(agencyId, 'Rejeté');
      fetchAgencies();
    } catch (error) {
      console.error('Erreur:', error);
    }
  };

  if (loading) return <MainLayout><Loader /></MainLayout>;

  return (
    <MainLayout>
      <div className="py-12">
        <div className="max-w-6xl mx-auto px-4">
          <div className="mb-8">
            <h1 className="text-4xl font-bold text-black">Tableau de bord admin</h1>
            <p className="text-black/60 mt-2">Gérez les agences et l'activité</p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-8">
            <div className="bg-white rounded-2xl border border-black/10 p-6">
              <p className="text-black/60 text-sm">Total agences</p>
              <p className="text-3xl font-bold text-black">{allAgencies.length}</p>
            </div>
            <div className="bg-white rounded-2xl border border-black/10 p-6">
              <p className="text-black/60 text-sm">En attente</p>
              <p className="text-3xl font-bold text-orange-600">{pendingAgencies.length}</p>
            </div>
            <div className="bg-white rounded-2xl border border-black/10 p-6">
              <p className="text-black/60 text-sm">Approuvées</p>
              <p className="text-3xl font-bold text-green-600">
                {allAgencies.filter(a => a.status === 'APPROVED').length}
              </p>
            </div>
          </div>

          {/* Agences en attente */}
          {pendingAgencies.length > 0 && (
            <div className="bg-white rounded-3xl border border-black/10 p-6 mb-8">
              <h2 className="text-2xl font-bold text-black mb-6">Demandes en attente</h2>
              <div className="space-y-4">
                {pendingAgencies.map((agency) => (
                  <div key={agency.id} className="border border-black/10 rounded-2xl p-4">
                    <div className="flex items-center justify-between mb-4">
                      <div>
                        <p className="font-bold text-black">{agency.agencyName}</p>
                        <p className="text-sm text-black/60">{agency.agencyCity} • {agency.agencyEmail}</p>
                      </div>
                      <p className="text-sm font-semibold text-orange-600">EN ATTENTE</p>
                    </div>
                    <div className="flex gap-2">
                      <button
                        onClick={() => handleApprove(agency.id)}
                        className="flex-1 h-10 rounded-full bg-green-500 text-white font-bold text-sm uppercase hover:bg-green-600"
                      >
                        Approuver
                      </button>
                      <button
                        onClick={() => handleReject(agency.id)}
                        className="flex-1 h-10 rounded-full bg-red-500 text-white font-bold text-sm uppercase hover:bg-red-600"
                      >
                        Rejeter
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* Toutes les agences */}
          <div className="bg-white rounded-3xl border border-black/10 p-6">
            <h2 className="text-2xl font-bold text-black mb-6">Toutes les agences</h2>

            {allAgencies.length === 0 ? (
              <div className="text-center py-12">
                <p className="text-black/60">Aucune agence</p>
              </div>
            ) : (
              <div className="space-y-4">
                {allAgencies.map((agency) => (
                  <div key={agency.id} className="border border-black/10 rounded-2xl p-4 flex items-center justify-between">
                    <div>
                      <p className="font-bold text-black">{agency.agencyName}</p>
                      <p className="text-sm text-black/60">{agency.agencyCity} • {agency.agencyPhone}</p>
                    </div>
                    <div className="text-right">
                      <p className="text-sm font-semibold">{agency.status}</p>
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
