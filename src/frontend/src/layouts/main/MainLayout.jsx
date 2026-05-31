import Navbar from '../../components/navbar/Navbar'
import Footer from '../../components/footer/Footer'

function MainLayout({ children }) {
  return (
    <div className="min-h-screen bg-white">
      <Navbar />
      <main className="px-2 sm:px-4">
        {children}
      </main>
      <Footer />
    </div>
  )
}

export default MainLayout
