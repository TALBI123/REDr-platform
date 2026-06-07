import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import MainLayout from '../../layouts/main/MainLayout';
import { LoginForm } from '../../components/forms/LoginForm';
import { Toast } from '../../components/common';

export const LoginPage = () => {
  const navigate = useNavigate();
  const [successMsg, setSuccessMsg] = useState('');

  const handleLoginSuccess = () => {
    setSuccessMsg('Connexion réussie !');
    setTimeout(() => navigate('/'), 2000);
  };

  return (
    <MainLayout>
      <div className="min-h-screen flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
        <div className="w-full max-w-md space-y-8">
          <div className="text-center">
            <h2 className="text-3xl font-bold text-black">Connexion</h2>
            <p className="mt-2 text-sm text-black/60">Connectez-vous à votre compte</p>
          </div>

          {successMsg && (
            <Toast message={successMsg} type="success" />
          )}

          <LoginForm onSuccess={handleLoginSuccess} />

          <div className="text-center space-y-2">
            <p className="text-sm text-black/60">
              Pas de compte ?{' '}
              <Link to="/register/client" className="text-[#6C27D3] font-bold hover:underline">
                Inscrivez-vous
              </Link>
            </p>
            <p className="text-sm text-black/60">
              Gérant d'agence ?{' '}
              <Link to="/register/agency" className="text-[#6C27D3] font-bold hover:underline">
                Créer une agence
              </Link>
            </p>
          </div>
        </div>
      </div>
    </MainLayout>
  );
};
