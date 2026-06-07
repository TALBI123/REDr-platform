import { useState } from 'react';
import { useAuth } from '../../hooks/useAuth';

export const RegisterClientForm = ({ onSuccess }) => {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    confirmPassword: '',
    firstName: '',
    lastName: '',
    phone: '',
  });
  const [localError, setLocalError] = useState(null);
  const { registerClient, loading, error } = useAuth();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setLocalError(null);
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (formData.password !== formData.confirmPassword) {
      setLocalError('Les mots de passe ne correspondent pas');
      return;
    }

    const { confirmPassword, ...registerData } = formData;

    try {
      await registerClient(registerData);
      if (onSuccess) onSuccess();
    } catch (err) {
      console.error('Register error:', err);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      {(localError || error) && (
        <div className="p-3 bg-red-100 text-red-700 rounded">
          {localError || error}
        </div>
      )}

      <div className="grid grid-cols-2 gap-4">
        <input
          type="email"
          name="email"
          value={formData.email}
          onChange={handleChange}
          placeholder="Email"
          className="h-11 rounded-full border border-black/10 bg-white px-4 text-sm text-black"
          autoComplete="email"
          required
        />
        <input
          type="tel"
          name="phone"
          value={formData.phone}
          onChange={handleChange}
          placeholder="Telephone"
          className="h-11 rounded-full border border-black/10 bg-white px-4 text-sm text-black"
          autoComplete="tel"
          required
        />
        <input
          type="text"
          name="firstName"
          value={formData.firstName}
          onChange={handleChange}
          placeholder="Prenom"
          className="h-11 rounded-full border border-black/10 bg-white px-4 text-sm text-black"
          autoComplete="given-name"
          required
        />
        <input
          type="text"
          name="lastName"
          value={formData.lastName}
          onChange={handleChange}
          placeholder="Nom"
          className="h-11 rounded-full border border-black/10 bg-white px-4 text-sm text-black"
          autoComplete="family-name"
          required
        />
        <input
          type="password"
          name="password"
          value={formData.password}
          onChange={handleChange}
          placeholder="Mot de passe"
          className="h-11 rounded-full border border-black/10 bg-white px-4 text-sm text-black"
          autoComplete="new-password"
          minLength={8}
          required
        />
        <input
          type="password"
          name="confirmPassword"
          value={formData.confirmPassword}
          onChange={handleChange}
          placeholder="Confirmer le mot de passe"
          className="h-11 rounded-full border border-black/10 bg-white px-4 text-sm text-black"
          autoComplete="new-password"
          minLength={8}
          required
        />
      </div>

      <button
        type="submit"
        disabled={loading}
        className="w-full h-11 px-5 rounded-full bg-[#6C27D3] text-white text-sm font-bold uppercase tracking-[0.2em] hover:bg-[#5a1fa8] disabled:opacity-50"
      >
        {loading ? 'Inscription...' : "S'inscrire"}
      </button>
    </form>
  );
};
