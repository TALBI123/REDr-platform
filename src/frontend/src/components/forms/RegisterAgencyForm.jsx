import { useState } from 'react';
import { useAuth } from '../../hooks/useAuth';

export const RegisterAgencyForm = ({ onSuccess }) => {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    firstName: '',
    lastName: '',
    phone: '',
    licenceNumber: '',
    nationalId: '',
    agencyName: '',
    agencyCity: '',
    agencyPhone: '',
    agencyEmail: '',
    agencyAddress: '',
    agencyDescription: '',
  });
  const { registerAgency, loading, error } = useAuth();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await registerAgency(formData);
      if (onSuccess) onSuccess();
    } catch (err) {
      console.error('Register agency error:', err);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      {error && <div className="p-3 bg-red-100 text-red-700 rounded">{error}</div>}

      <div className="grid grid-cols-2 gap-4">
        <h3 className="col-span-2 text-sm font-bold">Informations Manager</h3>
        <input
          type="email"
          name="email"
          value={formData.email}
          onChange={handleChange}
          placeholder="Email"
          className="h-11 rounded-full border border-black/10 bg-white px-4 text-sm text-black"
          required
        />
        <input
          type="password"
          name="password"
          value={formData.password}
          onChange={handleChange}
          placeholder="Mot de passe"
          className="h-11 rounded-full border border-black/10 bg-white px-4 text-sm text-black"
          required
        />
        <input
          type="text"
          name="firstName"
          value={formData.firstName}
          onChange={handleChange}
          placeholder="Prénom"
          className="h-11 rounded-full border border-black/10 bg-white px-4 text-sm text-black"
          required
        />
        <input
          type="text"
          name="lastName"
          value={formData.lastName}
          onChange={handleChange}
          placeholder="Nom"
          className="h-11 rounded-full border border-black/10 bg-white px-4 text-sm text-black"
          required
        />
        <input
          type="tel"
          name="phone"
          value={formData.phone}
          onChange={handleChange}
          placeholder="Téléphone"
          className="h-11 rounded-full border border-black/10 bg-white px-4 text-sm text-black"
          required
        />
        <input
          type="text"
          name="licenceNumber"
          value={formData.licenceNumber}
          onChange={handleChange}
          placeholder="N° Permis"
          className="h-11 rounded-full border border-black/10 bg-white px-4 text-sm text-black"
          required
        />
        <input
          type="text"
          name="nationalId"
          value={formData.nationalId}
          onChange={handleChange}
          placeholder="CIN/Passeport"
          className="h-11 rounded-full border border-black/10 bg-white px-4 text-sm text-black col-span-2"
          required
        />

        <h3 className="col-span-2 text-sm font-bold mt-4">Informations Agence</h3>
        <input
          type="text"
          name="agencyName"
          value={formData.agencyName}
          onChange={handleChange}
          placeholder="Nom agence"
          className="h-11 rounded-full border border-black/10 bg-white px-4 text-sm text-black col-span-2"
          required
        />
        <input
          type="text"
          name="agencyCity"
          value={formData.agencyCity}
          onChange={handleChange}
          placeholder="Ville"
          className="h-11 rounded-full border border-black/10 bg-white px-4 text-sm text-black"
          required
        />
        <input
          type="text"
          name="agencyAddress"
          value={formData.agencyAddress}
          onChange={handleChange}
          placeholder="Adresse"
          className="h-11 rounded-full border border-black/10 bg-white px-4 text-sm text-black col-span-2"
          required
        />
        <input
          type="email"
          name="agencyEmail"
          value={formData.agencyEmail}
          onChange={handleChange}
          placeholder="Email agence"
          className="h-11 rounded-full border border-black/10 bg-white px-4 text-sm text-black"
          required
        />
        <input
          type="tel"
          name="agencyPhone"
          value={formData.agencyPhone}
          onChange={handleChange}
          placeholder="Téléphone agence"
          className="h-11 rounded-full border border-black/10 bg-white px-4 text-sm text-black"
          required
        />
        <textarea
          name="agencyDescription"
          value={formData.agencyDescription}
          onChange={handleChange}
          placeholder="Description"
          className="h-20 rounded-2xl border border-black/10 bg-white px-4 py-2 text-sm text-black col-span-2"
        />
      </div>

      <button
        type="submit"
        disabled={loading}
        className="w-full h-11 px-5 rounded-full bg-[#6C27D3] text-white text-sm font-bold uppercase tracking-[0.2em] hover:bg-[#5a1fa8] disabled:opacity-50"
      >
        {loading ? 'Inscription...' : 'Créer l\'agence'}
      </button>
    </form>
  );
};
