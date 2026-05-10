/**
 * Servicio de Autenticación
 * Todas las llamadas van al BFF en http://localhost:8080
 */

import apiClient from './apiClient'
import type { LoginRequest, LoginResponse, UsuarioSesion } from '@/types'

export const authService = {
  /**
   * Autentica un usuario con correo y contraseña
   */
  async login(credentials: LoginRequest): Promise<LoginResponse> {
    const response = await apiClient.post<LoginResponse>('/api/auth/login', credentials)
    return response.data
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
