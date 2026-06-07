import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import MainLayout from '../../layouts/main/MainLayout';
import { RegisterAgencyForm } from '../../components/forms/RegisterAgencyForm';
import { Toast } from '../../components/common';

export const RegisterAgencyPage = () => {
  const navigate = useNavigate();
  const [successMsg, setSuccessMsg] = useState('');

  const handleRegisterSuccess = () => {
    setSuccessMsg('Agence créée ! Vérifiez votre email et attendez l\'approbation.');
    setTimeout(() => navigate('/login'), 3000);
  };

  return (
    <MainLayout>
      <div className="min-h-screen flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
        <div className="w-full max-w-4xl space-y-8">
          <div className="text-center">
            <h2 className="text-3xl font-bold text-black">Créer une Agence</h2>
            <p className="mt-2 text-sm text-black/60">Inscrivez votre agence de location de véhicules</p>
          </div>

          {successMsg && (
            <Toast message={successMsg} type="success" />
          )}

          <RegisterAgencyForm onSuccess={handleRegisterSuccess} />

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
