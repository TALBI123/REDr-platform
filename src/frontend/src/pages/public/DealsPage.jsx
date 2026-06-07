import { Link } from 'react-router-dom';
import MainLayout from '../../layouts/main/MainLayout';

export const DealsPage = () => (
  <MainLayout>
    <div className="mx-auto max-w-5xl px-4 py-12">
      <h1 className="text-4xl font-bold text-black">Deals</h1>
      <div className="mt-8 rounded-3xl border border-black/10 bg-white p-6">
        <p className="text-black/70">Retrouvez les vehicules avec promotion active.</p>
        <Link
          to="/cars?promotionActive=true"
          className="mt-5 inline-flex h-11 items-center rounded-full bg-[#8DCF30] px-5 text-sm font-bold uppercase tracking-[0.18em] text-black hover:bg-[#6C27D3] hover:text-white"
        >
          Voir les deals
        </Link>
      </div>
    </div>
  </MainLayout>
);
