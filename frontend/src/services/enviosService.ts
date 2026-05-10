/**
 * Servicio de Envíos
 * Todas las llamadas van al BFF en http://localhost:8080
 */

import apiClient from './apiClient'
import type {
  EnvioResponse,
  EnvioListaItem,
  SeguimientoEnvio,
  Transportista,
  CrearEnvioRequest,
  CambiarEstadoEnvioRequest,
  AsignarTransportistaRequest,
} from '@/types'

export const enviosService = {
  /**
   * Obtiene el listado de todos los envíos
   */
  async listarEnvios(): Promise<EnvioListaItem[]> {
    const response = await apiClient.get<EnvioListaItem[]>('/api/bff/envios')
    return response.data
  },

  /**
   * Obtiene los detalles completos de un envío específico
   */
  async obtenerEnvio(idEnvio: string): Promise<EnvioResponse> {
    const response = await apiClient.get<EnvioResponse>(`/api/bff/envios/${idEnvio}`)
    return response.data
  },

  /**
   * Obtiene el envío asociado a un pedido
   */
  async obtenerEnvioPorPedido(idPedidoRef: string): Promise<EnvioResponse> {
    const response = await apiClient.get<EnvioResponse>(
      `/api/bff/envios/pedido/${idPedidoRef}`
    )
    return response.data
  },

  /**
   * Crea un nuevo envío para un pedido
   */
  async crearEnvio(envio: CrearEnvioRequest): Promise<EnvioResponse> {
    const response = await apiClient.post<EnvioResponse>('/api/bff/envios', envio)
    return response.data
  },

  /**
   * Cambia el estado de un envío
   */
  async cambiarEstado(
    idEnvio: string,
    cambio: CambiarEstadoEnvioRequest
  ): Promise<EnvioResponse> {
    const response = await apiClient.patch<EnvioResponse>(
      `/api/bff/envios/${idEnvio}/estado`,
      cambio
    )
    return response.data
  },

  /**
   * Asigna un transportista a un envío
   */
  async asignarTransportista(
    idEnvio: string,
    asignacion: AsignarTransportistaRequest
  ): Promise<EnvioResponse> {
    const response = await apiClient.patch<EnvioResponse>(
      `/api/bff/envios/${idEnvio}/transportista`,
      asignacion
    )
    return response.data
  },

  /**
   * Obtiene el historial de seguimiento de un envío
   */
  async obtenerSeguimiento(idEnvio: string): Promise<SeguimientoEnvio[]> {
    const response = await apiClient.get<SeguimientoEnvio[]>(
      `/api/bff/envios/${idEnvio}/seguimiento`
    )
    return response.data
  },

  /**
   * Obtiene el listado de transportistas disponibles
   */
  async listarTransportistas(): Promise<Transportista[]> {
    const response = await apiClient.get<Transportista[]>(
      '/api/bff/envios/transportistas'
    )
    return response.data
  },
}
