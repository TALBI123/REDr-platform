function CarCard({ car }) {
  return (
    <article className="rounded-3xl border border-black/10 bg-white p-4 shadow-[0_12px_24px_rgba(0,0,0,0.08)]">
      <div className="aspect-[4/3] w-full overflow-hidden rounded-2xl bg-black/5">
        <img
          src={car.image}
          alt={car.name}
          className="h-full w-full object-cover"
          loading="lazy"
        />
      </div>
      <div className="mt-4 flex items-center justify-between">
        <h3 className="text-lg font-bold text-black">{car.name}</h3>
        <span className="rounded-full bg-[#8DCF30] px-3 py-1 text-xs font-bold uppercase tracking-[0.2em] text-black">
          {car.category}
        </span>
      </div>
      <div className="mt-2 text-sm text-black/70">
        {car.transmission} • {car.seats} seats
      </div>
      <div className="mt-4 flex items-center justify-between">
        <p className="text-xl font-extrabold text-[#7230CF]">
          ${car.pricePerDay}
          <span className="text-sm font-semibold text-black/60">/day</span>
        </p>
        <button className="rounded-full bg-black px-4 py-2 text-xs font-bold uppercase tracking-[0.2em] text-white hover:bg-[#7230CF]">
          Book
        </button>
      </div>
    </article>
  )
}

export default CarCard
