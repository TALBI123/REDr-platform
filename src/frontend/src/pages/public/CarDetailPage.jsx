import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import MainLayout from '../../layouts/main/MainLayout';
import { Loader } from '../../components/common';
import { agencyService } from '../../services/agencyService';
import { vehicleService } from '../../services/vehicleService';

const extractResource = (response) => response?.data?.data || response?.data || response;

const extractList = (response) => {
  const data = response?.data ?? response;

  if (Array.isArray(data)) return data;
  if (Array.isArray(data?.content)) return data.content;
  if (Array.isArray(data?.data)) return data.data;
  if (Array.isArray(data?.items)) return data.items;
  if (Array.isArray(data?.photos)) return data.photos;

  return [];
};

export const CarDetailPage = () => {
  const { carId } = useParams();
  const [car, setCar] = useState(null);
  const [agency, setAgency] = useState(null);
  const [photos, setPhotos] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchCarDetail = async () => {
      try {
        setLoading(true);
        const carResponse = await vehicleService.getPublicVehicleDetail(carId);
        const carData = extractResource(carResponse);
        setCar(carData);

        const photosResponse = await vehicleService.getPublicVehiclePhotos(carId);
        setPhotos(extractList(photosResponse));

        if (carData?.agencyId) {
          const agencyResponse = await agencyService.getAgencyDetail(carData.agencyId);
          setAgency(extractResource(agencyResponse));
        } else {
          setAgency(null);
        }
      } catch (error) {
        console.error('Erreur:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchCarDetail();
  }, [carId]);

  if (loading) return <MainLayout><Loader /></MainLayout>;
  if (!car) {
    return (
      <MainLayout>
        <div className="py-12 text-center">Vehicule non trouve</div>
      </MainLayout>
    );
  }

  const title = car.name || [car.brand, car.mode].filter(Boolean).join(' ');
  const photoUrls = photos.length > 0
    ? photos.map((photo) => (typeof photo === 'string' ? photo : photo.secureUrl || photo.url)).filter(Boolean)
    : car.photoUrls || [];
  const mainPhoto = car.image || photoUrls[0] || 'https://via.placeholder.com/800x600?text=Car';

  return (
    <MainLayout>
      <div className="py-12">
        <div className="mx-auto max-w-5xl px-4">
          <div className="mb-8 grid grid-cols-1 gap-6 md:grid-cols-[1.4fr_1fr]">
            <img
              src={mainPhoto}
              alt={title}
              className="h-96 w-full rounded-3xl object-cover"
            />
            <div className="grid grid-cols-2 gap-4 md:grid-cols-1">
              {photoUrls.slice(1, 5).map((photo, idx) => (
                <img
                  key={photo || idx}
                  src={photo}
                  alt={`Photo ${idx + 1}`}
                  className="h-20 w-full rounded-2xl object-cover"
                />
              ))}
            </div>
          </div>

          <div className="grid grid-cols-1 gap-6 md:grid-cols-3">
            <div className="space-y-6 md:col-span-2">
              <div>
                <h1 className="text-4xl font-bold text-black">{title}</h1>
                <p className="mt-2 text-black/60">{car.currentStatus || car.conditionStatus}</p>
              </div>

              <div className="space-y-4 rounded-3xl border border-black/10 bg-white p-6">
                <h2 className="text-xl font-bold">Caracteristiques</h2>
                <div className="grid grid-cols-2 gap-4 text-sm">
                  <div>
                    <p className="text-black/60">Annee</p>
                    <p className="font-bold text-black">{car.year}</p>
                  </div>
                  <div>
                    <p className="text-black/60">Transmission</p>
                    <p className="font-bold text-black">{car.transmissionType}</p>
                  </div>
                  <div>
                    <p className="text-black/60">Carburant</p>
                    <p className="font-bold text-black">{car.fuelTypes?.join(', ') || 'N/A'}</p>
                  </div>
                  <div>
                    <p className="text-black/60">Sieges</p>
                    <p className="font-bold text-black">{car.seatCapacity}</p>
                  </div>
                  <div>
                    <p className="text-black/60">Kilometrage</p>
                    <p className="font-bold text-black">{car.mileage?.toLocaleString()} km</p>
                  </div>
                  <div>
                    <p className="text-black/60">Etat</p>
                    <p className="font-bold text-black">{car.conditionStatus}</p>
                  </div>
                </div>
              </div>

              {car.description && (
                <div className="rounded-3xl border border-black/10 bg-white p-6">
                  <h2 className="mb-4 text-xl font-bold">Description</h2>
                  <p className="text-black/80">{car.description}</p>
                </div>
              )}

              {agency && (
                <div className="rounded-3xl border border-black/10 bg-white p-6">
                  <div className="flex items-start gap-4">
                    {agency.logoUrl && (
                      <img
                        src={agency.logoUrl}
                        alt={agency.name}
                        className="h-14 w-14 rounded-2xl object-cover"
                      />
                    )}
                    <div className="min-w-0 flex-1">
                      <p className="text-sm text-black/60">Agence</p>
                      <h2 className="text-xl font-bold text-black">{agency.name}</h2>
                      <p className="mt-1 text-sm text-black/70">
                        {[agency.city, agency.rating ? `${agency.rating}/5` : null].filter(Boolean).join(' - ')}
                      </p>
                      {agency.description && (
                        <p className="mt-3 text-sm text-black/70">{agency.description}</p>
                      )}
                      <Link
                        to={`/cars?agencyId=${agency.id}`}
                        className="mt-4 inline-flex h-10 items-center justify-center rounded-full bg-black px-4 text-xs font-bold uppercase tracking-[0.18em] text-white hover:bg-[#6C27D3]"
                      >
                        Voir ses voitures
                      </Link>
                    </div>
                  </div>
                </div>
              )}
            </div>

            <div className="h-fit space-y-6 rounded-3xl border border-black/10 bg-white p-6">
              <div>
                <p className="text-sm text-black/60">Prix par jour</p>
                <p className="text-4xl font-bold text-[#6C27D3]">{car.dailyPrice}€</p>
              </div>

              {car.promotionActive && (
                <div className="rounded-2xl border border-[#93D52B] bg-[#93D52B]/20 p-4">
                  <p className="text-sm text-black/60">Promotion active</p>
                  <p className="font-bold text-[#93D52B]">{car.promotionRate}% de reduction</p>
                </div>
              )}

              <div className="space-y-2">
                <p className="text-sm text-black/60">Statut</p>
                <p className="font-bold text-black">{car.currentStatus || car.status || 'Disponible'}</p>
              </div>

              <Link
                to={`/booking/${car.id}`}
                className="flex h-12 w-full items-center justify-center rounded-full bg-[#6C27D3] font-bold uppercase tracking-[0.2em] text-white hover:bg-[#5a1fa8]"
              >
                Reserver maintenant
              </Link>
            </div>
          </div>
        </div>
      </div>
    </MainLayout>
  );
};
