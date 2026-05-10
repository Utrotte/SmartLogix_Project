import Navbar from './Navbar'
import Sidebar from './Sidebar'

interface MainLayoutProps {
  children: React.ReactNode
}

export default function MainLayout({ children }: MainLayoutProps) {
  return (
    <div style={{ display: 'flex', height: '100vh', backgroundColor: 'var(--neutral-50)' }}>
      {/* Sidebar */}
      <Sidebar />
      
      {/* Main content area */}
      <div style={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
        {/* Navbar */}
        <Navbar />
        
        {/* Page content */}
        <main style={{
          flex: 1,
          overflow: 'auto',
          padding: '20px 30px',
        }}>
          {children}
        </main>
      </div>
    </div>
  )
}
