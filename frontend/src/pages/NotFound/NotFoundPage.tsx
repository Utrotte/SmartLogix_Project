import { Link } from 'react-router-dom'

export default function NotFoundPage() {
  return (
    <div style={{
      display: 'flex',
      flexDirection: 'column',
      justifyContent: 'center',
      alignItems: 'center',
      height: '100vh',
      textAlign: 'center',
    }}>
      <h1 style={{ fontSize: '48px', marginBottom: '20px' }}>404</h1>
      <p style={{ fontSize: '18px', marginBottom: '20px' }}>
        La página que buscas no existe.
      </p>
      <Link
        to="/dashboard"
        style={{
          backgroundColor: 'var(--primary)',
          color: 'white',
          padding: '10px 20px',
          borderRadius: '4px',
        }}
      >
        Volver al Dashboard
      </Link>
    </div>
  )
}
