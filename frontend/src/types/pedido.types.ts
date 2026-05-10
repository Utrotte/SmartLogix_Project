/**
 * Tipos relacionados con Pedidos (ms-pedidos-smartlogix)
 */

export interface DireccionEntrega {
  idDireccion?: number
  calle: string
  ciudad: string
  region: string
  codigoPostal: string
  instruccionesEspeciales?: string
}

export interface DetallePedido {
  idDetalle?: number
  idProducto: number
  nombreProducto?: string
  cantidad: number
  precioUnitario: number
  subtotal?: number
}

export interface Pedido {
  idPedido?: number
  idCliente: number
  nombreCliente?: string
  numeroReferencia?: string
  estadoActual?: string
  fechaCreacion?: string
  fechaConfirmacion?: string
  montoTotal: number
  observacion?: string
  detalles: DetallePedido[]
  direccionEntrega: DireccionEntrega
}

export interface PedidoListaItem {
  idPedido: number
  numeroReferencia: string
  idCliente: number
  nombreCliente?: string
  estadoActual: string
  fechaCreacion: string
  montoTotal: number
}

export interface PedidoResponse {
  idPedido: number
  numeroReferencia: string
  idCliente: number
  estadoActual: string
  fechaCreacion: string
  fechaConfirmacion?: string
  montoTotal: number
  observacion?: string
  detalles: DetallePedido[]
  direccionEntrega: DireccionEntrega
}

export interface CambiarEstadoPedidoRequest {
  nuevoEstado: string
  usuarioResponsable: string
  observacion: string
}

export type EstadoPedido = 
  | 'PENDIENTE_CONFIRMACION'
  | 'CONFIRMADO'
  | 'EN_PREPARACION'
  | 'LISTO_PARA_ENVIO'
  | 'CANCELADO'
