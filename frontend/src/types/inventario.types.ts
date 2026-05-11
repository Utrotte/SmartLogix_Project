/**
 * Tipos relacionados con Inventario (ms-inventario)
 */

export interface Bodega {
  idBodega: number
  nombre: string
  ubicacion?: string
  capacidadMaxima?: number
  descripcion?: string
}

export interface Producto {
  idProducto: number
  nombre: string
  idCategoria: number
  nombreCategoria?: string
  descripcion?: string
  precioUnitario?: number
  precioReferencia?: number
  sku?: string
  codigoSku?: string
  marca?: string
  activo?: boolean
}

export interface CategoriaProducto {
  idCategoria: number
  nombre: string
  descripcion?: string
}

export interface Existencia {
  idExistencia: number
  idProducto: number
  idBodega: number
  nombreProducto?: string
  codigoSkuProducto?: string
  nombreBodega?: string
  // Campos que devuelve el backend (ms_inventario → ExistenciaResponse)
  stockActual: number
  stockReservado: number
  stockDisponible: number
  stockMinimo: number
  fechaActualizacion?: string
  // Aliases de compatibilidad (algunos componentes aún los usan)
  cantidadDisponible?: number
  cantidadReservada?: number
  cantidadMínima?: number
}

export interface CrearExistenciaRequest {
  idProducto: number
  idBodega: number
  cantidadDisponible: number
  cantidadMínima: number
}

export interface AjustarStockRequest {
  ajuste: number
  observacion: string
}

export interface MovimientoInventario {
  idMovimiento: number
  idProducto: number
  idBodega: number
  tipo: 'ENTRADA' | 'SALIDA' | 'AJUSTE'
  cantidad: number
  fecha: string
  observacion?: string
}

export interface ReservaInventarioDetalle {
  idProducto: number
  cantidad: number
}

export interface ReservaInventario {
  idReserva?: number
  idPedidoRef: string
  estado: 'ACTIVA' | 'COMPLETADA' | 'CANCELADA'
  fechaCreacion?: string
  detalles: ReservaInventarioDetalle[]
}

export interface ExistenciaResponse {
  idExistencia: number
  idProducto: number
  idBodega: number
  codigoSkuProducto?: string
  nombreProducto: string
  nombreBodega: string
  stockActual: number
  stockReservado: number
  stockDisponible: number
  stockMinimo: number
  fechaActualizacion?: string
}
