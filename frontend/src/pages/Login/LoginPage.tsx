import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '@/context/AuthContext'

export default function LoginPage() {
  const [correo, setCorreo] = useState('')
  const [password, setPassword] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)
  const navigate = useNavigate()
  const { login, error } = useAuth()

  const handleLogin = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault()
    setIsSubmitting(true)
    try {
      await login(correo, password)
      navigate('/dashboard')
    } catch (err) {
      console.error('Error en login:', err)
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <div style={{
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      height: '100vh',
      backgroundColor: 'var(--neutral-50)',
    }}>
      <div style={{
        backgroundColor: 'white',
        padding: '40px',
        borderRadius: '8px',
        boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
        width: '100%',
        maxWidth: '400px',
      }}>
        <h1 style={{ marginBottom: '30px', color: 'var(--primary)' }}>SmartLogix</h1>
        
        {error && (
          <div style={{
            backgroundColor: 'var(--danger)',
            color: 'white',
            padding: '10px',
            borderRadius: '4px',
            marginBottom: '20px',
          }}>
            {error}
          </div>
        )}
        
        <form onSubmit={handleLogin}>
          <div style={{ marginBottom: '15px' }}>
            <label htmlFor="correo" style={{ display: 'block', marginBottom: '5px' }}>
              Correo:
            </label>
            <input
              id="correo"
              type="email"
              placeholder="usuario@example.com"
              value={correo}
              onChange={(e) => setCorreo(e.target.value)}
              style={{ width: '100%', padding: '8px', boxSizing: 'border-box' }}
              required
              disabled={isSubmitting}
            />
          </div>
          <div style={{ marginBottom: '20px' }}>
            <label htmlFor="password" style={{ display: 'block', marginBottom: '5px' }}>
              Contraseña:
            </label>
            <input
              id="password"
              type="password"
              placeholder="••••••••"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              style={{ width: '100%', padding: '8px', boxSizing: 'border-box' }}
              required
              disabled={isSubmitting}
            />
          </div>
          <button
            type="submit"
            style={{
              width: '100%',
              backgroundColor: isSubmitting ? 'var(--neutral-500)' : 'var(--primary)',
              color: 'white',
              padding: '10px',
              fontSize: '16px',
              cursor: isSubmitting ? 'not-allowed' : 'pointer',
              borderRadius: '4px',
              border: 'none',
            }}
            disabled={isSubmitting}
          >
            {isSubmitting ? 'Ingresando...' : 'Ingresar'}
          </button>
        </form>
      </div>
    </div>
  )
}
