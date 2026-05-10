/**
 * Tipos relacionados con Envíos (ms-envios)
 */

export interface DireccionEnvio {
  calle: string
  ciudad: string
  region: string
  codigoPostal: string
  instrucciones?: string
}

export interface PaqueteEnvio {
  peso: number
  dimensiones: string
  contenido: string
}

export interface Transportista {
  idTransportista: number
  nombre: string
  telefono?: string
  email?: string
  vehiculo?: string
  disponible?: boolean
}

export interface SeguimientoEnvio {
  idSeguimiento: number
  idEnvio: string
  estado: EstadoEnvio
  fecha: string
  ubicacion: string
  observacion?: string
}

export interface Envio {
  idEnvio?: string
  idPedidoRef: string
  estado?: EstadoEnvio
  transportista?: string
  idTransportista?: number
  fechaCreacion?: string
  fechaAsignacion?: string
  fechaEntrega?: string
  direccion: DireccionEnvio
  paquete: PaqueteEnvio
}

export interface EnvioResponse {
  idEnvio: string
  idPedidoRef: string
  estado: EstadoEnvio
  transportista?: string
  idTransportista?: number
  fechaCreacion: string
  fechaAsignacion?: string
  fechaEntrega?: string
  direccion: DireccionEnvio
}

export interface EnvioListaItem {
  idEnvio: string
  idPedidoRef: string
  estado: EstadoEnvio
  transportista?: string
  fechaCreacion: string
  fechaEntrega?: string
  direccion?: string
}

export interface CrearEnvioRequest {
  idPedidoRef: string
  direccion: DireccionEnvio
  paquete: PaqueteEnvio
}

export interface CambiarEstadoEnvioRequest {
  nuevoEstado: EstadoEnvio
  observacion?: string
}

export interface AsignarTransportistaRequest {
  idTransportista: number
}

export type EstadoEnvio =
  | 'PENDIENTE_ASIGNACION'
  | 'ASIGNADO'
  | 'EN_TRANSITO'
  | 'ENTREGADO'
  | 'INCIDENCIA'

export interface GuiaDespacho {
  idGuia?: number
  idEnvio: string
  numero?: string
  fechaEmision?: string
}

export interface RutaEntrega {
  idRuta?: number
  idEnvio: string
  orden: number
  ubicacion: string
  estado: 'PENDIENTE' | 'COMPLETADA'
  observacion?: string
}
