import { useEffect, useRef, useState } from 'react'

function IconSearch() {
  return (
    <svg viewBox="0 0 24 24" aria-hidden="true" className="w-5 h-5">
      <path d="M10.5 4a6.5 6.5 0 1 0 4.11 11.54l4.42 4.42 1.41-1.41-4.42-4.42A6.5 6.5 0 0 0 10.5 4Zm0 2a4.5 4.5 0 1 1 0 9 4.5 4.5 0 0 1 0-9Z" fill="currentColor" />
    </svg>
  )
}

function IconUser() {
  return (
    <svg viewBox="0 0 24 24" aria-hidden="true" className="w-5 h-5">
      <path d="M12 12a4.5 4.5 0 1 0-4.5-4.5A4.5 4.5 0 0 0 12 12Zm0 2c-4.42 0-8 2.21-8 4.94V21h16v-2.06C20 16.21 16.42 14 12 14Z" fill="currentColor" />
    </svg>
  )
}

function IconHelp() {
  return (
    <svg viewBox="0 0 24 24" aria-hidden="true" className="w-5 h-5">
      <path d="M12 2a10 10 0 1 0 0 20 10 10 0 0 0 0-20Zm0 15a1.25 1.25 0 1 1 0 2.5A1.25 1.25 0 0 1 12 17Zm1.2-3.8c-.78.38-1.2.77-1.2 1.8v.4h-1.8v-.6c0-1.36.7-2.1 1.8-2.62.75-.35 1.2-.7 1.2-1.4 0-.82-.66-1.4-1.6-1.4-1 0-1.66.52-1.9 1.5l-1.72-.52c.38-1.64 1.82-2.58 3.62-2.58 2.12 0 3.6 1.16 3.6 3 0 1.54-.98 2.28-2 2.82Z" fill="currentColor" />
    </svg>
  )
}

function Navbar() {
  const [open, setOpen] = useState(false)
  const [hidden, setHidden] = useState(false)
  const lastScrollY = useRef(0)

  useEffect(() => {
    const handleScroll = () => {
      const currentY = window.scrollY
      const delta = currentY - lastScrollY.current

      if (currentY < 12) {
        setHidden(false)
      } else if (delta > 8) {
        setHidden(true)
      } else if (delta < -8) {
        setHidden(false)
      }

      lastScrollY.current = currentY
    }

    lastScrollY.current = window.scrollY
    window.addEventListener('scroll', handleScroll, { passive: true })
    return () => window.removeEventListener('scroll', handleScroll)
  }, [])

  useEffect(() => {
    document.body.style.overflow = open ? 'hidden' : ''
    return () => {
      document.body.style.overflow = ''
    }
  }, [open])

  return (
    <>
      <header
        className={`sticky top-4 z-[100] mx-auto w-[calc(100%-20px)] lg:w-[min(100%,1100px)] px-3 py-2 sm:px-5 sm:py-3 rounded-2xl bg-white/95 border border-black/10 shadow-[0_14px_32px_rgba(17,17,17,0.14)] backdrop-blur-md flex items-center justify-between gap-2 sm:gap-3 transition-[transform,opacity] duration-300 ease-out will-change-transform ${
          hidden ? '-translate-y-24 opacity-0 pointer-events-none' : 'translate-y-0 opacity-100'
        }`}
      >
        <div className="flex items-center gap-2">
          <button
            className="inline-flex items-center justify-center w-10 h-10 rounded-xl text-black hover:bg-black/5"
            aria-label="Toggle menu"
            aria-expanded={open}
            onClick={() => setOpen(true)}
          >
            <svg className="w-5 h-5" viewBox="0 0 24 24" fill="none" aria-hidden="true">
              <path d="M4 6h16M4 12h16M4 18h16" stroke="currentColor" strokeWidth="2" strokeLinecap="round" />
            </svg>
          </button>

          <button className="inline-flex items-center justify-center w-10 h-10 rounded-full text-black hover:bg-black/5" type="button" aria-label="Search">
            <IconSearch />
          </button>
        </div>

        <div className="flex items-center gap-2 sm:gap-3">
          <div className="text-xl sm:text-2xl font-extrabold tracking-tight text-[#8DCF30]">
            Red<span className="text-[#7230CF]">r</span>
            <span className="text-sm align-top -translate-y-1">®</span>
          </div>
          <span className="hidden sm:inline-flex items-center rounded-full bg-[#8DCF30] px-3 py-1 text-[11px] font-bold uppercase tracking-[0.18em] text-black">
            Premium Fleet
          </span>
        </div>

        <div className="hidden lg:flex items-center gap-8 text-sm uppercase tracking-[0.2em]">
          <a href="#fleet" className="text-black font-semibold hover:text-[#7230CF]">Fleet</a>
          <a href="#locations" className="text-black font-semibold hover:text-[#7230CF]">Locations</a>
          <a href="#deals" className="text-black font-semibold hover:text-[#7230CF]">Deals</a>
          <a href="#support" className="text-black font-semibold hover:text-[#7230CF]">Support</a>
        </div>

        <div className="flex items-center gap-2">
          <button className="hidden sm:inline-flex items-center justify-center w-10 h-10 rounded-full text-black hover:bg-black/5" type="button" aria-label="Account">
            <IconUser />
          </button>
          <button className="hidden sm:inline-flex items-center justify-center w-10 h-10 rounded-full text-black hover:bg-black/5" type="button" aria-label="Help">
            <IconHelp />
          </button>
          <button
            className="sm:hidden inline-flex items-center justify-center h-9 px-3 rounded-full bg-[#8DCF30] text-black text-[11px] font-bold uppercase tracking-[0.2em]"
            type="button"
          >
            Book
          </button>
          <button
            className="hidden sm:inline-flex items-center justify-center h-11 px-5 rounded-full bg-[#8DCF30] text-black text-sm font-bold uppercase tracking-[0.22em] hover:bg-[#7230CF] hover:text-white transition-colors"
            type="button"
          >
            Book Now
          </button>
        </div>
      </header>

      <div
        className={`fixed inset-0 z-40 bg-gradient-to-b from-black via-[#1c1030] to-[#0c0c0c] text-white transition-transform duration-500 ease-out ${
          open ? 'translate-y-0' : 'translate-y-full'
        }`}
        aria-hidden={!open}
      >
        <div className="min-h-full w-full px-6 py-8 sm:px-12 sm:py-12">
          <div className="flex items-center justify-between">
            <div className="text-2xl font-extrabold tracking-tight">
              Rent<span className="text-[#8DCF30]">car</span>®
            </div>
            <button
              className="inline-flex items-center justify-center w-11 h-11 rounded-full border border-white/20 hover:bg-white/10"
              type="button"
              aria-label="Close menu"
              onClick={() => setOpen(false)}
            >
              <svg viewBox="0 0 24 24" className="w-5 h-5" aria-hidden="true">
                <path d="M6 6l12 12M18 6l-12 12" stroke="currentColor" strokeWidth="2" strokeLinecap="round" />
              </svg>
            </button>
          </div>

          <div className="mt-10 grid gap-10 lg:grid-cols-[1.2fr_1fr]">
            <div>
              <p className="text-xs uppercase tracking-[0.3em] text-white/60">Explore</p>
              <div className="mt-6 grid gap-4 text-3xl sm:text-4xl font-semibold">
                <a href="#fleet" onClick={() => setOpen(false)} className="hover:text-white/80">Fleet</a>
                <a href="#locations" onClick={() => setOpen(false)} className="hover:text-white/80">Locations</a>
                <a href="#deals" onClick={() => setOpen(false)} className="hover:text-white/80">Deals</a>
                <a href="#support" onClick={() => setOpen(false)} className="hover:text-white/80">Support</a>
              </div>
            </div>
            <div className="rounded-3xl border border-white/10 bg-white/5 p-6">
              <p className="text-xs uppercase tracking-[0.3em] text-white/60">Quick Booking</p>
              <p className="mt-3 text-lg font-semibold text-white">Reserve your car in 60 seconds.</p>
              <button
                className="mt-6 inline-flex w-full items-center justify-center rounded-full bg-[#8DCF30] py-3 text-sm font-bold uppercase tracking-[0.22em] text-black hover:bg-white"
                type="button"
                onClick={() => setOpen(false)}
              >
                Book Now
              </button>
              <button
                className="mt-3 inline-flex w-full items-center justify-center rounded-full border border-white/30 py-3 text-sm font-semibold uppercase tracking-[0.2em] text-white hover:border-[#7230CF]"
                type="button"
                onClick={() => setOpen(false)}
              >
                See Deals
              </button>
              <div className="mt-6 grid grid-cols-2 gap-3 text-xs uppercase tracking-[0.2em]">
                <button
                  className="inline-flex items-center justify-center rounded-full border border-white/20 py-2 text-white/90 hover:border-[#8DCF30]"
                  type="button"
                  onClick={() => setOpen(false)}
                >
                  Account
                </button>
                <button
                  className="inline-flex items-center justify-center rounded-full border border-white/20 py-2 text-white/90 hover:border-[#8DCF30]"
                  type="button"
                  onClick={() => setOpen(false)}
                >
                  Help
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  )
}

export default Navbar