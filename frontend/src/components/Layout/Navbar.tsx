import { useNavigate } from 'react-router-dom'
import { useAuth } from '@/context/AuthContext'

export default function Navbar() {
  const navigate = useNavigate()
  const { usuario, logout, isLoading } = useAuth()

  const handleLogout = async () => {
    await logout()
    navigate('/login')
  }

  return (
    <nav style={{
      backgroundColor: 'white',
      borderBottom: '1px solid var(--neutral-200)',
      padding: '15px 30px',
      display: 'flex',
      justifyContent: 'space-between',
      alignItems: 'center',
      boxShadow: '0 1px 3px rgba(0, 0, 0, 0.1)',
    }}>
      {/* Brand/Logo */}
      <div style={{
        fontSize: '18px',
        fontWeight: 700,
        color: 'var(--primary)',
      }}>
        SmartLogix
      </div>

      {/* Right section with user info and logout */}
      <div style={{
        display: 'flex',
        alignItems: 'center',
        gap: '20px',
      }}>
        <div style={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'flex-end',
        }}>
          <span style={{
            fontSize: '14px',
            fontWeight: 600,
            color: 'var(--neutral-900)',
          }}>
            {usuario?.nombre || 'Usuario'}
          </span>
          <span style={{
            fontSize: '12px',
            color: 'var(--neutral-500)',
          }}>
            {usuario?.correo || 'usuario@example.com'}
          </span>
        </div>
        
        <button
          onClick={handleLogout}
          disabled={isLoading}
          style={{
            backgroundColor: isLoading ? 'var(--neutral-400)' : 'var(--danger)',
            color: 'white',
            padding: '8px 16px',
            cursor: isLoading ? 'not-allowed' : 'pointer',
            borderRadius: '4px',
            border: 'none',
            fontSize: '14px',
            fontWeight: 500,
            transition: 'background-color 0.2s',
          }}
        >
          {isLoading ? 'Cerrando...' : 'Logout'}
        </button>
      </div>
    </nav>
  )
}
