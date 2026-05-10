import React, { createContext, useContext, useState, useEffect } from 'react'
import { authService } from '@/services'
import type { UsuarioSesion, LoginRequest } from '@/types'

interface AuthContextType {
  usuario: UsuarioSesion | null
  token: string | null
  login: (correo: string, password: string) => Promise<void>
  logout: () => Promise<void>
  isAuthenticated: boolean
  isLoading: boolean
  error: string | null
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [usuario, setUsuario] = useState<UsuarioSesion | null>(null)
  const [token, setToken] = useState<string | null>(null)
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  // Cargar usuario y token del localStorage al iniciar
  useEffect(() => {
    const storedToken = localStorage.getItem('sessionToken')
    const storedUsuario = localStorage.getItem('usuario')
    if (storedToken && storedUsuario) {
      setToken(storedToken)
      setUsuario(JSON.parse(storedUsuario))
    }
  }, [])

  const login = async (correo: string, password: string) => {
    setIsLoading(true)
    setError(null)
    try {
      const credentials: LoginRequest = { correo, password }
      const response = await authService.login(credentials)
      
      // Guardar token y usuario en localStorage
      localStorage.setItem('sessionToken', response.tokenReferencia)
      const usuarioData: UsuarioSesion = {
        idUsuario: response.idUsuario,
        nombre: response.nombre,
        correo: response.correo,
        roles: response.roles,
        fechaExpiracion: response.fechaExpiracion,
      }
      localStorage.setItem('usuario', JSON.stringify(usuarioData))
      
      // Actualizar estado
      setToken(response.tokenReferencia)
      setUsuario(usuarioData)
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : 'Error al iniciar sesión'
      setError(errorMessage)
      throw err
    } finally {
      setIsLoading(false)
    }
  }

  const logout = async () => {
    setIsLoading(true)
    try {
      await authService.logout()
    } catch (err) {
      console.error('Error al cerrar sesión:', err)
    } finally {
      setUsuario(null)
      setToken(null)
      localStorage.removeItem('sessionToken')
      localStorage.removeItem('usuario')
      setIsLoading(false)
    }
  }

  return (
    <AuthContext.Provider
      value={{
        usuario,
        token,
        login,
        logout,
        isAuthenticated: !!token,
        isLoading,
        error,
      }}
    >
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth debe ser usado dentro de AuthProvider')
  }
  return context
}
