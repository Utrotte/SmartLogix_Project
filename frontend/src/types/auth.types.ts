/**
 * Tipos relacionados con autenticación y sesión
 */

export interface LoginRequest {
  correo: string
  password: string
}

export interface LoginResponse {
  idUsuario: number
  nombre: string
  correo: string
  roles: string[]
  tokenReferencia: string
  fechaExpiracion: string
  mensaje: string
}

export interface UsuarioSesion {
  idUsuario: number
  nombre: string
  correo: string
  roles: string[]
  fechaExpiracion: string
}

export interface ApiResponse<T = unknown> {
  success: boolean
  message: string
  data: T | null
}
