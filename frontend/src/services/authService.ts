/**
 * Servicio de Autenticación
 * Todas las llamadas van al BFF en http://localhost:8080
 */

import apiClient from './apiClient'
import type { LoginRequest, LoginResponse, UsuarioSesion } from '@/types'

const unwrapData = <T>(responseData: any): T => {
  if (responseData && typeof responseData === 'object' && 'data' in responseData) {
    return responseData.data as T
  }
  return responseData as T
}

export const authService = {
  /**
   * Autentica un usuario con correo y contraseña
   */
  async login(credentials: LoginRequest): Promise<LoginResponse> {
    const response = await apiClient.post<any>('/api/auth/login', credentials)
    const data = unwrapData<LoginResponse>(response.data)
    console.log("Login response normalizado:", data)
    return data
  },

  /**
   * Obtiene la información del usuario actual
   */
  async getCurrentUser(): Promise<UsuarioSesion> {
    const response = await apiClient.get<UsuarioSesion>('/api/auth/me')
    return response.data
  },

  /**
   * Cierra la sesión actual
   */
  async logout(): Promise<void> {
    await apiClient.post('/api/auth/logout')
  },
}
