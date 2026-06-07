import MainLayout from '../../layouts/main/MainLayout';

export const SupportPage = () => (
  <MainLayout>
    <div className="mx-auto max-w-4xl px-4 py-12">
      <h1 className="text-4xl font-bold text-black">Support</h1>
      <div className="mt-8 rounded-3xl border border-black/10 bg-white p-6">
        <p className="text-black/70">Besoin d'aide ? Contactez notre equipe support.</p>
        <div className="mt-5 space-y-2 text-sm text-black/70">
          <p>Email: support@rentcar.com</p>
          <p>Telephone: +212 600 000 000</p>
        </div>
      </div>
    </div>
  </MainLayout>
);
