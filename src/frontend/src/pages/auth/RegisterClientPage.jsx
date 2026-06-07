import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import MainLayout from '../../layouts/main/MainLayout';
import { RegisterClientForm } from '../../components/forms/RegisterClientForm';
import { Toast } from '../../components/common';

export const RegisterClientPage = () => {
  const navigate = useNavigate();
  const [successMsg, setSuccessMsg] = useState('');

  const handleRegisterSuccess = () => {
    setSuccessMsg('Inscription réussie ! Vérifiez votre email.');
    setTimeout(() => navigate('/login'), 3000);
  };

  return (
    <MainLayout>
      <div className="min-h-screen flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
        <div className="w-full max-w-2xl space-y-8">
          <div className="text-center">
            <h2 className="text-3xl font-bold text-black">Inscription Client</h2>
            <p className="mt-2 text-sm text-black/60">Créez votre compte pour réserver un véhicule</p>
          </div>

          {successMsg && (
            <Toast message={successMsg} type="success" />
          )}

          <RegisterClientForm onSuccess={handleRegisterSuccess} />

          <div className="text-center">
            <p className="text-sm text-black/60">
              Vous avez déjà un compte ?{' '}
              <Link to="/login" className="text-[#6C27D3] font-bold hover:underline">
                Se connecter
              </Link>
            </p>
          </div>
        </div>
      </div>
    </MainLayout>
  );
};
