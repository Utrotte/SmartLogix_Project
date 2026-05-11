/**
 * Tipos relacionados con Pedidos (ms-pedidos-smartlogix)
 */

export interface DireccionEntrega {
  idDireccion?: number
  calle: string
  numero: string
  comuna: string
  ciudad: string
  region: string
  codigoPostal: string
  referencia?: string
}

export interface DetallePedido {
  idDetalle?: number
  idProductoRef: number
  codigoSkuRef?: string
  nombreProductoSnapshot?: string
  cantidad: number
  precioUnitario: number
  subtotal?: number
  estadoDetalle?: string
}

export interface ClienteRequest {
  nombre: string
  apellido: string
  correo: string
  telefono: string
  documento: string
}

export interface Pedido {
  idPedido?: number
  cliente: ClienteRequest
  canalOrigen?: string
  descuentoTotal?: number
  observacion?: string
  detalles: DetallePedido[]
  direccionEntrega: DireccionEntrega
  totalBruto?: number
  totalNeto?: number
}

export interface PedidoListaItem {
  idPedido: number
  codigoPedido: string
  idCliente: number
  nombreCliente?: string
  estadoActual: string
  fechaCreacion: string
  totalNeto: number
}

export interface PedidoResponse {
  idPedido: number
  idCliente: number
  nombreCliente: string
  codigoPedido: string
  fechaCreacion: string
  estadoActual: string
  canalOrigen: string
  totalBruto: number
  descuentoTotal: number
  totalNeto: number
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
