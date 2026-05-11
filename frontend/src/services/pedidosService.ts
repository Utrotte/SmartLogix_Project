/**
 * Servicio de Pedidos
 * Todas las llamadas van al BFF en http://localhost:8080
 */

import apiClient from './apiClient'
import type {
  Pedido,
  PedidoListaItem,
  PedidoResponse,
  CambiarEstadoPedidoRequest,
} from '@/types'

export const pedidosService = {
  /**
   * Obtiene el listado de todos los pedidos
   */
  async listarPedidos(): Promise<PedidoListaItem[]> {
    const response = await apiClient.get<PedidoListaItem[]>('/api/bff/pedidos')
    return response.data
  },

  /**
   * Obtiene los detalles completos de un pedido específico
   */
  async obtenerPedido(idPedido: number): Promise<PedidoResponse> {
    const response = await apiClient.get<PedidoResponse>(`/api/bff/pedidos/${idPedido}`)
    return response.data
  },

  /**
   * Obtiene todos los pedidos de un cliente específico
   */
  async obtenerPedidosPorCliente(idCliente: number): Promise<PedidoListaItem[]> {
    const response = await apiClient.get<PedidoListaItem[]>(
      `/api/bff/pedidos/cliente/${idCliente}`
    )
    return response.data
  },

  /**
   * Crea un nuevo pedido
   */
  async crearPedido(pedido: Pedido): Promise<PedidoResponse> {
    const response = await apiClient.post<PedidoResponse>('/api/bff/pedidos', pedido)
    return response.data
  },

  /**
   * Cambia el estado de un pedido
   */
  async cambiarEstadoPedido(
    idPedido: number,
    cambio: CambiarEstadoPedidoRequest
  ): Promise<PedidoResponse> {
    console.log("Frontend -> PUT cambiar estado:", { idPedido, request: cambio });
    const response = await apiClient.put<PedidoResponse>(
      `/api/bff/pedidos/${idPedido}/estado`,
      cambio
    )
    return response.data
  },

  /**
   * Elimina un pedido
   */
  async eliminarPedido(idPedido: number): Promise<void> {
    await apiClient.delete(`/api/bff/pedidos/${idPedido}`)
  },
}
