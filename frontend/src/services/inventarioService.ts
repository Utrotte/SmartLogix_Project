/**
 * Servicio de Inventario
 * Todas las llamadas van al BFF en http://localhost:8080
 */

import apiClient from './apiClient'
import type {
  Producto,
  Bodega,
  Existencia,
  ExistenciaResponse,
  AjustarStockRequest,
  ReservaInventario,
} from '@/types'

export const inventarioService = {
  /**
   * Obtiene el listado de todos los productos
   */
  async listarProductos(): Promise<Producto[]> {
    const response = await apiClient.get<Producto[]>('/api/bff/inventario/productos')
    return response.data
  },

  /**
   * Obtiene el listado de todas las bodegas
   */
  async listarBodegas(): Promise<Bodega[]> {
    const response = await apiClient.get<Bodega[]>('/api/bff/inventario/bodegas')
    return response.data
  },

  /**
   * Obtiene las existencias de un producto en todas las bodegas
   */
  async obtenerExistenciasPorProducto(idProducto: number): Promise<Existencia[]> {
    const response = await apiClient.get<Existencia[]>(
      `/api/bff/inventario/existencias/producto/${idProducto}`
    )
    return response.data
  },

  /**
   * Obtiene las existencias de una bodega específica
   */
  async obtenerExistenciasPorBodega(idBodega: number): Promise<Existencia[]> {
    const response = await apiClient.get<Existencia[]>(
      `/api/bff/inventario/existencias/bodega/${idBodega}`
    )
    return response.data
  },

  /**
   * Obtiene la existencia de un producto en una bodega específica
   */
  async obtenerExistencia(
    idProducto: number,
    idBodega: number
  ): Promise<ExistenciaResponse> {
    const response = await apiClient.get<ExistenciaResponse>(
      `/api/bff/inventario/existencias/producto/${idProducto}/bodega/${idBodega}`
    )
    return response.data
  },

  /**
   * Ajusta el stock de una existencia
   */
  async ajustarStock(
    idExistencia: number,
    ajuste: AjustarStockRequest
  ): Promise<ExistenciaResponse> {
    const response = await apiClient.put<ExistenciaResponse>(
      `/api/bff/inventario/existencias/${idExistencia}/ajustar-stock`,
      ajuste
    )
    return response.data
  },

  /**
   * Obtiene el listado de reservas activas
   */
  async listarReservas(): Promise<ReservaInventario[]> {
    const response = await apiClient.get<ReservaInventario[]>(
      '/api/bff/inventario/reservas'
    )
    return response.data
  },
}
