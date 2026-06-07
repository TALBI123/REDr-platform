import { useEffect, useRef, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../../hooks/useAuth'

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
  const [profileOpen, setProfileOpen] = useState(false)
  const lastScrollY = useRef(0)
  const { isAuthenticated, user, logout } = useAuth()
  const navigate = useNavigate()

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

  const handleLogout = async () => {
    try {
      await logout()
      setOpen(false)
      navigate('/')
    } catch (error) {
      console.error('Logout error:', error)
    }
  }

  const handleProfileClick = async () => {
    setProfileOpen((current) => !current)
  }

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

        <Link to="/" className="flex items-center gap-2 sm:gap-3 hover:opacity-80">
          <div className="text-xl sm:text-2xl font-extrabold tracking-tight text-[#8DCF30]">
            Red<span className="text-[#7230CF]">r</span>
            <span className="text-sm align-top -translate-y-1">®</span>
          </div>
          <span className="hidden sm:inline-flex items-center rounded-full bg-[#8DCF30] px-3 py-1 text-[11px] font-bold uppercase tracking-[0.18em] text-black">
            Premium Fleet
          </span>
        </Link>

        <div className="hidden lg:flex items-center gap-8 text-sm uppercase tracking-[0.2em]">
          <Link to="/cars" className="text-black font-semibold hover:text-[#6C27D3]">Fleet</Link>
          <Link to="/locations" className="text-black font-semibold hover:text-[#6C27D3]">Locations</Link>
          <Link to="/deals" className="text-black font-semibold hover:text-[#6C27D3]">Deals</Link>
          <Link to="/support" className="text-black font-semibold hover:text-[#6C27D3]">Support</Link>
        </div>

        <div className="relative flex items-center gap-2">
          <button className="hidden sm:inline-flex items-center justify-center w-10 h-10 rounded-full text-black hover:bg-black/5" type="button" aria-label="Help">
            <IconHelp />
          </button>
          {isAuthenticated ? (
            <button
              type="button"
              onClick={handleProfileClick}
              className="inline-flex items-center justify-center w-10 h-10 rounded-full bg-[#8DCF30] text-black hover:bg-[#6C27D3] hover:text-white transition-colors"
              aria-label="Account"
            >
              <IconUser />
            </button>
          ) : (
            <Link
              to="/login"
              className="inline-flex items-center justify-center h-11 px-5 rounded-full bg-[#8DCF30] text-black text-sm font-bold uppercase tracking-[0.22em] hover:bg-[#6C27D3] hover:text-white transition-colors"
            >
              Login
            </Link>
          )}

          {isAuthenticated && profileOpen && (
            <div className="absolute right-0 top-14 z-[120] w-56 rounded-2xl border border-black/10 bg-white p-2 text-black shadow-[0_18px_40px_rgba(0,0,0,0.18)]">
              <Link
                to="/profile"
                onClick={() => setProfileOpen(false)}
                className="block rounded-xl px-4 py-3 text-sm font-semibold hover:bg-black/5"
              >
                Mon profil
              </Link>
              <button
                type="button"
                onClick={handleLogout}
                className="block w-full rounded-xl px-4 py-3 text-left text-sm font-semibold text-red-600 hover:bg-red-50"
              >
                Logout
              </button>
            </div>
          )}
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
            <Link to="/" onClick={() => setOpen(false)} className="text-2xl font-extrabold tracking-tight">
              Rent<span className="text-[#8DCF30]">car</span>®
            </Link>
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
                <Link to="/cars" onClick={() => setOpen(false)} className="hover:text-white/80">Fleet</Link>                <Link to="/agencies" onClick={() => setOpen(false)} className="hover:text-white/80">Agences</Link>                <Link to="/locations" onClick={() => setOpen(false)} className="hover:text-white/80">Locations</Link>
                <Link to="/deals" onClick={() => setOpen(false)} className="hover:text-white/80">Deals</Link>
                <Link to="/support" onClick={() => setOpen(false)} className="hover:text-white/80">Support</Link>
              </div>
            </div>
            <div className="rounded-3xl border border-white/10 bg-white/5 p-6">
              <p className="text-xs uppercase tracking-[0.3em] text-white/60">Account</p>
              <div className="mt-6 space-y-3">
                {isAuthenticated ? (
                  <>
                    <p className="text-lg font-semibold text-white">{user?.email}</p>
                    <Link
                      to={user?.role === 'CLIENT' ? '/dashboard/client' : user?.role === 'AGENCY_MANAGER' ? '/dashboard/agency' : '/dashboard/admin'}
                      className="block w-full text-center rounded-full bg-[#6C27D3] py-3 text-sm font-bold uppercase tracking-[0.22em] text-white hover:bg-[#5a1fa8]"
                      onClick={() => setOpen(false)}
                    >
                      Dashboard
                    </Link>
                    <button
                      onClick={handleLogout}
                      className="block w-full text-center rounded-full border border-white/30 py-3 text-sm font-semibold uppercase tracking-[0.2em] text-white hover:border-red-500 hover:text-red-400"
                    >
                      Logout
                    </button>
                  </>
                ) : (
                  <>
                    <p className="text-lg font-semibold text-white">Connect to your account</p>
                    <Link
                      to="/login"
                      className="block w-full text-center rounded-full bg-[#6C27D3] py-3 text-sm font-bold uppercase tracking-[0.22em] text-white hover:bg-[#5a1fa8]"
                      onClick={() => setOpen(false)}
                    >
                      Login
                    </Link>
                    <Link
                      to="/register/client"
                      className="block w-full text-center rounded-full border border-white/30 py-3 text-sm font-semibold uppercase tracking-[0.2em] text-white hover:border-[#6C27D3]"
                      onClick={() => setOpen(false)}
                    >
                      Register
                    </Link>
                  </>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  )
}

export default Navbar
