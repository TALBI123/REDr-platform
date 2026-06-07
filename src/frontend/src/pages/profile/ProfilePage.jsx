import { useEffect, useState } from 'react';
import MainLayout from '../../layouts/main/MainLayout';
import { Loader } from '../../components/common';
import { useAuth } from '../../hooks/useAuth';
import { authService } from '../../services/authService';

export const ProfilePage = () => {
  const { user } = useAuth();
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        setLoading(true);
        setError(null);
        const response = await authService.getProfile(user?.role);
        setProfile(response.data);
      } catch (err) {
        setError(err.response?.data?.message || err.message || 'Profile not available');
      } finally {
        setLoading(false);
      }
    };

    if (user?.role) fetchProfile();
  }, [user?.role]);

  if (loading) return <MainLayout><Loader /></MainLayout>;

  return (
    <MainLayout>
      <div className="mx-auto max-w-4xl px-4 py-12">
        <h1 className="text-4xl font-bold text-black">Mon profil</h1>
        <p className="mt-2 text-sm text-black/60">Informations de votre compte</p>

        {error ? (
          <div className="mt-8 rounded-2xl bg-red-100 p-4 text-red-700">{error}</div>
        ) : (
          <div className="mt-8 rounded-3xl border border-black/10 bg-white p-6">
            <div className="grid gap-5 sm:grid-cols-2">
              <Info label="Nom" value={[profile?.firstName, profile?.lastName].filter(Boolean).join(' ')} />
              <Info label="Email" value={profile?.email || user?.email} />
              <Info label="Role" value={profile?.role || user?.role} />
              <Info label="Statut" value={profile?.accountStatus} />
              <Info label="Telephone" value={profile?.phone} />
              <Info label="Agence" value={profile?.agencyName} />
              <Info label="Niveau admin" value={profile?.adminLevel} />
              <Info label="Date inscription" value={profile?.inscriptionDate} />
            </div>
          </div>
        )}
      </div>
    </MainLayout>
  );
};

const Info = ({ label, value }) => (
  <div className="rounded-2xl border border-black/10 p-4">
    <p className="text-xs font-bold uppercase tracking-[0.18em] text-black/50">{label}</p>
    <p className="mt-2 font-semibold text-black">{value || '-'}</p>
  </div>
);
