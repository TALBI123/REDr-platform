import MainLayout from '../../layouts/main/MainLayout'
import CarCard from '../../components/card/CarCard'
import heroImage from '../../data/img1_rentcar.jpg'

const cars = [
	{
		id: 1,
		name: 'Mercedes C-Class',
		category: 'Sedan',
		pricePerDay: 120,
		seats: 5,
		transmission: 'Auto',
		image: 'https://images.unsplash.com/photo-1503376780353-7e6692767b70?q=80&w=1200&auto=format&fit=crop',
	},
	{
		id: 2,
		name: 'Range Rover Evoque',
		category: 'SUV',
		pricePerDay: 160,
		seats: 5,
		transmission: 'Auto',
		image: 'https://images.unsplash.com/photo-1503376780353-7e6692767b70?q=80&w=1200&auto=format&fit=crop',
	},
	{
		id: 3,
		name: 'Tesla Model 3',
		category: 'Electric',
		pricePerDay: 140,
		seats: 5,
		transmission: 'Auto',
		image: 'https://images.unsplash.com/photo-1549924231-f129b911e442?q=80&w=1200&auto=format&fit=crop',
	},
]

function PageHome() {
	return (
		<MainLayout>
            <div className="border-x-gray-600 mb-16">
                <section className="relative mt-8 h-[420px] overflow-hidden rounded-3xl">
				<img src={heroImage} className="h-full w-full object-cover" alt="Car Rental" />
				<div className="absolute inset-0 bg-gradient-to-t from-black/70 via-black/10 to-transparent" />
			</section>
				<div className="absolute inset-x-0 bottom-10 px-4 z-40 sm:bottom-40 sm:px-6">
					<form className="mx-auto w-full max-w-5xl rounded-3xl border  border-black/10 bg-white/95 p-4 shadow-[0_18px_40px_rgba(0,0,0,0.18)] backdrop-blur">
						<div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-[1.6fr_1fr_1fr_1fr_1fr_auto]">
							<div className="space-y-2">
								<label className="text-xs font-semibold uppercase tracking-[0.2em] text-black/60">Lieu de depart</label>
								<select className="h-11 w-full rounded-full border border-black/10 bg-white px-4 text-sm text-black">
									<option>Rabat - Aeroport Rabat-Sale</option>
									<option>Casablanca - Mohammed V</option>
								</select>
							</div>
							<div className="space-y-2">
								<label className="text-xs font-semibold uppercase tracking-[0.2em] text-black/60">Date de depart</label>
								<input className="h-11 w-full rounded-full border border-black/10 bg-white px-4 text-sm text-black" type="date" />
							</div>
							<div className="space-y-2">
								<label className="text-xs font-semibold uppercase tracking-[0.2em] text-black/60">Heure de depart</label>
								<input className="h-11 w-full rounded-full border border-black/10 bg-white px-4 text-sm text-black" type="time" />
							</div>
							<div className="space-y-2">
								<label className="text-xs font-semibold uppercase tracking-[0.2em] text-black/60">Lieu de retour</label>
								<select className="h-11 w-full rounded-full border border-black/10 bg-white px-4 text-sm text-black">
									<option>Rabat - Aeroport Rabat-Sale</option>
									<option>Casablanca - Mohammed V</option>
								</select>
							</div>
							<div className="space-y-2">
								<label className="text-xs font-semibold uppercase tracking-[0.2em] text-black/60">Date de retour</label>
								<input className="h-11 w-full rounded-full border border-black/10 bg-white px-4 text-sm text-black" type="date" />
							</div>
							<button
								type="button"
								className="h-11 w-full rounded-full bg-[#7230CF] px-6 text-sm font-bold uppercase tracking-[0.2em] text-white hover:bg-[#8DCF30] hover:text-black"
							>
								Search
							</button>
						</div>
					</form>
				</div>
            </div>
			
			<section className="sm:mt-20 mt-40">
				<div className="mx-auto w-[min(100%,1200px)] px-4 py-12 sm:px-6 text-center">
					<p className="text-xs font-semibold uppercase tracking-[0.3em] text-black/60">Welcome to Rentcar</p>
					<h1 className="mt-3 text-3xl font-extrabold tracking-tight text-black sm:text-4xl">
						Your Journey Starts Here
					</h1>
					<p className="mx-auto mt-3 max-w-xl text-sm text-black/70">
						Premium car rentals for business and leisure. Book in seconds with transparent pricing and 24/7 support.
					</p>
				</div>
			</section>
			<section className="mx-auto w-[min(100%,1200px)] px-4 py-12 sm:px-6">
				<div className="flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
					<div>
						<p className="text-xs font-semibold uppercase tracking-[0.3em] text-black/60">Premium Rentals</p>
						<h1 className="mt-3 text-3xl font-extrabold tracking-tight text-black sm:text-4xl">
							Choose your next ride
						</h1>
						<p className="mt-3 max-w-xl text-sm text-black/70">
							Luxury, electric, and business class cars ready for pickup. Book in minutes with transparent pricing.
						</p>
					</div>
					<button
						className="inline-flex items-center justify-center rounded-full bg-[#8DCF30] px-5 py-3 text-xs font-bold uppercase tracking-[0.2em] text-black hover:bg-[#7230CF] hover:text-white"
						type="button"
					>
						View Deals
					</button>
				</div>

				<div className="mt-10 grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
					{cars.map((car) => (
						<CarCard key={car.id} car={car} />
					))}
				</div>
			</section>
		</MainLayout>
	)
}

export default PageHome
