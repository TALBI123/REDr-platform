import MainLayout from '../../layouts/main/MainLayout';

export const LocationsPage = () => (
  <MainLayout>
    <div className="mx-auto max-w-5xl px-4 py-12">
      <h1 className="text-4xl font-bold text-black">Locations</h1>
      <div className="mt-8 grid gap-4 sm:grid-cols-2">
        {['Rabat', 'Casablanca', 'Marrakech', 'Tanger'].map((city) => (
          <div key={city} className="rounded-2xl border border-black/10 bg-white p-6">
            <h2 className="text-xl font-bold text-black">{city}</h2>
            <p className="mt-2 text-sm text-black/60">Agences et vehicules disponibles.</p>
          </div>
        ))}
      </div>
    </div>
  </MainLayout>
);
