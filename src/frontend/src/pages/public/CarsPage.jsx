import { useEffect, useState } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import MainLayout from '../../layouts/main/MainLayout';
import CarCard from '../../components/card/CarCard';
import { vehicleService } from '../../services/vehicleService';
import { Loader } from '../../components/common';

const PAGE_SIZE = 6;

export const extractCars = (response) => {
  const data = response?.data ?? response;

  if (Array.isArray(data)) return data;
  if (Array.isArray(data?.content)) return data.content;
  if (Array.isArray(data?.data)) return data.data;
  if (Array.isArray(data?.items)) return data.items;
  if (Array.isArray(data?.vehicles)) return data.vehicles;

  return [];
};

const extractPageInfo = (response) => {
  const data = response?.data ?? response;

  return {
    page: data?.number ?? data?.page ?? 0,
    totalPages: data?.totalPages ?? data?.total_pages ?? 1,
    totalElements: data?.totalElements ?? data?.total ?? extractCars(response).length,
  };
};

const getErrorMessage = (error) =>
  error.response?.data?.message ||
  error.response?.data?.error ||
  error.message ||
  'Erreur lors du chargement des vehicules';

const cleanParams = (params) =>
  Object.fromEntries(
    Object.entries(params).filter(([, value]) => value !== '' && value !== null && value !== undefined)
  );

export const CarsPage = () => {
  const [searchParams] = useSearchParams();
  const agencyId = searchParams.get('agencyId') || '';
  const [cars, setCars] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState(null);
  const [filters, setFilters] = useState({
    brand: '',
    minDailyPrice: '',
    maxDailyPrice: '',
    transmissionType: '',
    seatCapacity: '',
    conditionStatus: '',
  });
  const [page, setPage] = useState(0);
  const [pageInfo, setPageInfo] = useState({
    page: 0,
    totalPages: 1,
    totalElements: 0,
  });

  useEffect(() => {
    const fetchCars = async () => {
      try {
        setLoading(true);
        setErrorMessage(null);
        const response = await vehicleService.getPublicVehicles(
          cleanParams({ ...filters, agencyId, page, size: PAGE_SIZE })
        );
        setCars(extractCars(response));
        setPageInfo(extractPageInfo(response));
      } catch (error) {
        console.error('Erreur lors du chargement des vehicules:', error);
        setCars([]);
        setPageInfo({ page: 0, totalPages: 1, totalElements: 0 });
        setErrorMessage(getErrorMessage(error));
      } finally {
        setLoading(false);
      }
    };

    fetchCars();
  }, [filters, agencyId, page]);

  const handleFilterChange = (name, value) => {
    setPage(0);
    setFilters((prev) => ({ ...prev, [name]: value }));
  };

  const resetFilters = () => {
    setPage(0);
    setFilters({
      brand: '',
      minDailyPrice: '',
      maxDailyPrice: '',
      transmissionType: '',
      seatCapacity: '',
      conditionStatus: '',
    });
  };

  const goToPage = (nextPage) => {
    const lastPage = Math.max(pageInfo.totalPages - 1, 0);
    setPage(Math.min(Math.max(nextPage, 0), lastPage));
  };

  return (
    <MainLayout>
      <div className="py-12">
        <div className="mx-auto max-w-7xl px-4">
          <div className="mb-8">
            <h1 className="text-4xl font-bold text-black">Notre flotte</h1>
            <p className="mt-2 text-sm text-black/60">
              {pageInfo.totalElements} vehicules trouves{agencyId ? ' pour cette agence' : ''}
            </p>
          </div>

          {errorMessage && (
            <div className="mb-6 rounded-xl bg-red-100 p-4 text-red-700">
              {errorMessage}
            </div>
          )}

          <div className="grid gap-6 lg:grid-cols-[260px_1fr]">
            <aside className="h-fit rounded-2xl border border-black/10 bg-white p-5">
              <div className="mb-5 flex items-center justify-between">
                <h2 className="text-sm font-bold uppercase tracking-[0.18em] text-black">Filtres</h2>
                <button type="button" onClick={resetFilters} className="text-xs font-semibold text-[#6C27D3] hover:underline">
                  Reset
                </button>
              </div>

              <div className="space-y-4">
                <input
                  type="text"
                  value={filters.brand}
                  placeholder="Marque"
                  className="h-11 w-full rounded-full border border-black/10 bg-white px-4 text-sm text-black"
                  onChange={(e) => handleFilterChange('brand', e.target.value)}
                />
                <div className="grid grid-cols-2 gap-3">
                  <input
                    type="number"
                    value={filters.minDailyPrice}
                    placeholder="Prix min"
                    className="h-11 w-full rounded-full border border-black/10 bg-white px-4 text-sm text-black"
                    onChange={(e) => handleFilterChange('minDailyPrice', e.target.value)}
                  />
                  <input
                    type="number"
                    value={filters.maxDailyPrice}
                    placeholder="Prix max"
                    className="h-11 w-full rounded-full border border-black/10 bg-white px-4 text-sm text-black"
                    onChange={(e) => handleFilterChange('maxDailyPrice', e.target.value)}
                  />
                </div>
                <select
                  value={filters.transmissionType}
                  className="h-11 w-full rounded-full border border-black/10 bg-white px-4 text-sm text-black"
                  onChange={(e) => handleFilterChange('transmissionType', e.target.value)}
                >
                  <option value="">Transmission</option>
                  <option value="MANUAL">Manuel</option>
                  <option value="AUTOMATIC">Automatique</option>
                </select>
                <select
                  value={filters.seatCapacity}
                  className="h-11 w-full rounded-full border border-black/10 bg-white px-4 text-sm text-black"
                  onChange={(e) => handleFilterChange('seatCapacity', e.target.value)}
                >
                  <option value="">Sieges</option>
                  <option value="2">2 sieges</option>
                  <option value="4">4 sieges</option>
                  <option value="5">5 sieges</option>
                  <option value="7">7 sieges</option>
                </select>
                <select
                  value={filters.conditionStatus}
                  className="h-11 w-full rounded-full border border-black/10 bg-white px-4 text-sm text-black"
                  onChange={(e) => handleFilterChange('conditionStatus', e.target.value)}
                >
                  <option value="">Etat</option>
                  <option value="NEW">New</option>
                  <option value="GOOD">Good</option>
                  <option value="DAMAGED">Damaged</option>
                </select>
              </div>
            </aside>

            <section>
              {loading ? (
                <Loader />
              ) : cars.length === 0 ? (
                <div className="rounded-2xl border border-black/10 bg-white py-16 text-center">
                  <p className="text-lg text-black/60">Aucun vehicule disponible</p>
                </div>
              ) : (
                <>
                  <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 xl:grid-cols-3">
                    {cars.map((car) => (
                      <Link key={car.id} to={`/cars/${car.id}`}>
                        <CarCard car={car} />
                      </Link>
                    ))}
                  </div>

                  <div className="mt-8 flex items-center justify-center gap-3">
                    <button
                      type="button"
                      onClick={() => goToPage(page - 1)}
                      disabled={page <= 0}
                      className="h-10 rounded-full border border-black/10 px-4 text-sm font-semibold text-black disabled:opacity-40"
                    >
                      Prev
                    </button>
                    <span className="text-sm font-semibold text-black/70">
                      Page {page + 1} / {Math.max(pageInfo.totalPages, 1)}
                    </span>
                    <button
                      type="button"
                      onClick={() => goToPage(page + 1)}
                      disabled={page >= pageInfo.totalPages - 1}
                      className="h-10 rounded-full border border-black/10 px-4 text-sm font-semibold text-black disabled:opacity-40"
                    >
                      Next
                    </button>
                  </div>
                </>
              )}
            </section>
          </div>
        </div>
      </div>
    </MainLayout>
  );
};
