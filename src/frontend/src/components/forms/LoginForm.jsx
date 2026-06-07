import { useState } from 'react';
import { useAuth } from '../../hooks/useAuth';

export const LoginForm = ({ onSuccess }) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const { login, loading, error } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await login(email, password);
      if (onSuccess) onSuccess();
    } catch (err) {
      console.error('Login error:', err);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      {error && <div className="p-3 bg-red-100 text-red-700 rounded">{error}</div>}

      <div>
        <label className="text-xs font-semibold uppercase tracking-[0.2em] text-black/60">Email</label>
        <input
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          className="h-11 w-full rounded-full border border-black/10 bg-white px-4 text-sm text-black mt-2"
          placeholder="votre@email.com"
          required
        />
      </div>

      <div>
        <label className="text-xs font-semibold uppercase tracking-[0.2em] text-black/60">Mot de passe</label>
        <input
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          className="h-11 w-full rounded-full border border-black/10 bg-white px-4 text-sm text-black mt-2"
          placeholder="••••••••"
          required
        />
      </div>

      <button
        type="submit"
        disabled={loading}
        className="w-full h-11 px-5 rounded-full bg-[#6C27D3] text-white text-sm font-bold uppercase tracking-[0.2em] hover:bg-[#5a1fa8] disabled:opacity-50"
      >
        {loading ? 'Connexion...' : 'Se connecter'}
      </button>
    </form>
  );
};
