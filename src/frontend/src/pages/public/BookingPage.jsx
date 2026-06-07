import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import MainLayout from '../../layouts/main/MainLayout';
import { Toast, Loader } from '../../components/common';
import { bookingService } from '../../services/bookingService';

export const BookingPage = () => {
  const { carId } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState({
    startDate: '',
    endDate: '',
    pickupLocation: '',
    returnLocation: '',
  });
  const [message, setMessage] = useState('');
  const [messageType, setMessageType] = useState('info');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      await bookingService.createBooking({
        ...formData,
        vehicleId: carId,
      });
      setMessageType('success');
      setMessage('Réservation confirmée !');
      setTimeout(() => navigate('/dashboard/client'), 2000);
    } catch (error) {
      setMessageType('error');
      setMessage(error.response?.data?.message || 'Erreur de réservation');
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <MainLayout><Loader /></MainLayout>;

  return (
    <MainLayout>
      <div className="min-h-screen flex items-center justify-center py-12 px-4">
        <div className="w-full max-w-2xl space-y-8">
          <div className="text-center">
            <h2 className="text-3xl font-bold text-black">Réservation du véhicule</h2>
            <p className="mt-2 text-sm text-black/60">Complétez les détails de votre réservation</p>
          </div>

          {message && (
            <Toast message={message} type={messageType} />
          )}

          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="bg-white rounded-3xl border border-black/10 p-6 space-y-4">
              <h3 className="text-lg font-bold text-black">Dates et lieux</h3>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label className="text-xs font-semibold uppercase tracking-[0.2em] text-black/60">Date de prise en charge</label>
                  <input
                    type="date"
                    name="startDate"
                    value={formData.startDate}
                    onChange={handleChange}
                    className="h-11 w-full rounded-full border border-black/10 bg-white px-4 text-sm text-black mt-2"
                    required
                  />
                </div>

                <div>
                  <label className="text-xs font-semibold uppercase tracking-[0.2em] text-black/60">Date de retour</label>
                  <input
                    type="date"
                    name="endDate"
                    value={formData.endDate}
                    onChange={handleChange}
                    className="h-11 w-full rounded-full border border-black/10 bg-white px-4 text-sm text-black mt-2"
                    required
                  />
                </div>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label className="text-xs font-semibold uppercase tracking-[0.2em] text-black/60">Lieu de prise en charge</label>
                  <select
                    name="pickupLocation"
                    value={formData.pickupLocation}
                    onChange={handleChange}
                    className="h-11 w-full rounded-full border border-black/10 bg-white px-4 text-sm text-black mt-2"
                    required
                  >
                    <option value="">Sélectionner</option>
                    <option value="Rabat">Rabat - Aéroport</option>
                    <option value="Casablanca">Casablanca - Mohammed V</option>
                    <option value="Marrakech">Marrakech - Menara</option>
                  </select>
                </div>

                <div>
                  <label className="text-xs font-semibold uppercase tracking-[0.2em] text-black/60">Lieu de retour</label>
                  <select
                    name="returnLocation"
                    value={formData.returnLocation}
                    onChange={handleChange}
                    className="h-11 w-full rounded-full border border-black/10 bg-white px-4 text-sm text-black mt-2"
                    required
                  >
                    <option value="">Sélectionner</option>
                    <option value="Rabat">Rabat - Aéroport</option>
                    <option value="Casablanca">Casablanca - Mohammed V</option>
                    <option value="Marrakech">Marrakech - Menara</option>
                  </select>
                </div>
              </div>
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full h-12 px-5 rounded-full bg-[#6C27D3] text-white text-sm font-bold uppercase tracking-[0.2em] hover:bg-[#5a1fa8] disabled:opacity-50"
            >
              {loading ? 'Traitement...' : 'Confirmer la réservation'}
            </button>
          </form>
        </div>
      </div>
    </MainLayout>
  );
};
