import { Link } from 'react-router-dom';

function Footer() {
	return (
		<footer className="mt-16 rounded-t-[24px] bg-black text-white sm:mx-4">
			<div className="mx-auto w-[min(100%,1200px)] px-6 pt-12 pb-10">
				<div className="grid gap-10 lg:grid-cols-[1fr_1fr_1fr_1fr_1.4fr]">
					<div className="space-y-4">
						<h3 className="text-xs font-semibold uppercase tracking-[0.24em] text-white/50">Fleet</h3>
						<ul className="space-y-2 text-sm text-white/70">
							<li><Link className="hover:text-[#8DCF30]" to="/cars">All Vehicles</Link></li>
							<li><a className="hover:text-[#8DCF30]" href="#fleet">SUV</a></li>
							<li><a className="hover:text-[#8DCF30]" href="#fleet">Sedan</a></li>
							<li><a className="hover:text-[#8DCF30]" href="#fleet">Electric</a></li>
						</ul>
					</div>

					<div className="space-y-4">
						<h3 className="text-xs font-semibold uppercase tracking-[0.24em] text-white/50">Agences</h3>
						<ul className="space-y-2 text-sm text-white/70">
							<li><Link className="hover:text-[#8DCF30]" to="/agencies">All Agencies</Link></li>
							<li><a className="hover:text-[#8DCF30]" href="#agencies">Find Near You</a></li>
							<li><a className="hover:text-[#8DCF30]" href="#agencies">Partner With Us</a></li>
							<li><a className="hover:text-[#8DCF30]" href="#agencies">Locations</a></li>
						</ul>
					</div>

					<div className="space-y-4">
						<h3 className="text-xs font-semibold uppercase tracking-[0.24em] text-white/50">Support</h3>
						<ul className="space-y-2 text-sm text-white/70">
							<li><a className="hover:text-[#8DCF30]" href="#support">Help Center</a></li>
							<li><a className="hover:text-[#8DCF30]" href="#support">Driver Policy</a></li>
							<li><a className="hover:text-[#8DCF30]" href="#support">Insurance</a></li>
							<li><a className="hover:text-[#8DCF30]" href="#support">Contact</a></li>
						</ul>
					</div>

					<div className="space-y-4">
						<h3 className="text-xs font-semibold uppercase tracking-[0.24em] text-white/50">Company</h3>
						<ul className="space-y-2 text-sm text-white/70">
							<li><a className="hover:text-[#8DCF30]" href="#about">About</a></li>
							<li><Link className="hover:text-[#8DCF30]" to="/deals">Deals</Link></li>
							<li><a className="hover:text-[#8DCF30]" href="#stories">Stories</a></li>
							<li><a className="hover:text-[#8DCF30]" href="#careers">Careers</a></li>
						</ul>
					</div>
					<div className="space-y-4">
						<div className="text-2xl font-extrabold tracking-tight">
							Rent<span className="text-[#8DCF30]">car</span><span className="text-sm align-top -translate-y-1">®</span>
						</div>
						<p className="text-sm text-white/60">
							Premium car rental for business and leisure. Book in seconds.
						</p>
						<div className="flex flex-wrap gap-2">
							<a className="inline-flex items-center rounded-full border border-white/20 px-3 py-2 text-[11px] font-semibold uppercase tracking-[0.2em] text-white hover:border-[#8DCF30]" href="#fleet">
								View Fleet
							</a>
							<a className="inline-flex items-center rounded-full bg-[#8DCF30] px-3 py-2 text-[11px] font-bold uppercase tracking-[0.2em] text-black hover:bg-white" href="#deals">
								Book Now
							</a>
						</div>
					</div>
				</div>

				<div className="mt-10 space-y-3">
					<h4 className="text-xs font-semibold uppercase tracking-[0.24em] text-white/50">Payment methods</h4>
					<div className="flex flex-wrap gap-2">
						{['AMEX','Apple Pay','Bancontact','BLIK','G Pay','Klarna','Mastercard','Maestro','PayPal','Shop','UnionPay','VISA'].map((item) => (
							<span key={item} className="rounded-md border border-white/20 px-2 py-1 text-[10px] uppercase tracking-wide text-white/70">
								{item}
							</span>
						))}
					</div>
				</div>
			</div>

			<div className="mt-12 border-t border-white/10">
				<div className="mx-auto w-[min(100%,1200px)] px-6 py-6 flex flex-col gap-4 text-xs text-white/60 sm:flex-row sm:items-center sm:justify-between">
					<div className="flex items-center gap-4">
						<a className="hover:text-[#8DCF30]" href="#terms">Terms</a>
						<a className="hover:text-[#8DCF30]" href="#privacy">Privacy</a>
						<a className="hover:text-[#8DCF30]" href="#cookies">Cookies</a>
					</div>
					<div className="text-white/40">© 2026 Redr. All rights reserved.</div>
				</div>
			</div>

			<div className="mx-auto w-[min(100%,1200px)] px-6 pb-12">
				<div className="text-[clamp(3rem,18vw,10rem)] font-black leading-[0.85] tracking-tight text-white/90">
					Red<span className="text-[#8DCF30]">r</span>
					<span className="align-top text-[0.3em]">®</span>
				</div>
			</div>
		</footer>
	)
}

export default Footer
