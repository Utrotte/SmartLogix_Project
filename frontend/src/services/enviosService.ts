/**
 * Servicio de Envíos — todas las llamadas van al BFF.
 */

import apiClient from './apiClient'
import type {
  EnvioResponse,
  EnvioListaItem,
  SeguimientoEnvio,
  Transportista,
  CambiarEstadoEnvioRequest,
  AsignarTransportistaRequest,
  EstadoEnvio,
} from '@/types'

/** Soporta string ISO o arrays de fecha típicos de Jackson en JSON. */
function rawToIsoDate(raw: unknown): string {
  if (raw == null) return new Date().toISOString()
  if (typeof raw === 'string') {
    const d = new Date(raw)
    return Number.isNaN(d.getTime()) ? new Date().toISOString() : d.toISOString()
  }
  if (Array.isArray(raw) && raw.length >= 3) {
    const y = Number(raw[0])
    const mo = Number(raw[1]) - 1
    const d = Number(raw[2])
    const h = raw.length > 3 ? Number(raw[3]) : 0
    const mi = raw.length > 4 ? Number(raw[4]) : 0
    const s = raw.length > 5 ? Number(raw[5]) : 0
    const dt = new Date(y, mo, d, h, mi, s)
    return Number.isNaN(dt.getTime()) ? new Date().toISOString() : dt.toISOString()
  }
  return new Date().toISOString()
}

function normalizarEstadoEnvio(estado?: string): EstadoEnvio {
  if (!estado) return 'PENDIENTE_ASIGNACION'
  const upper = estado
    .trim()
    .toUpperCase()
    .replaceAll(' ', '_')
    .replaceAll('Á', 'A')
    .replaceAll('É', 'E')
    .replaceAll('Í', 'I')
    .replaceAll('Ó', 'O')
    .replaceAll('Ú', 'U')
  if (upper === 'PENDIENTE') return 'PENDIENTE_ASIGNACION'
  if (
    upper === 'PENDIENTE_ASIGNACION' ||
    upper === 'ASIGNADO' ||
    upper === 'EN_TRANSITO' ||
    upper === 'ENTREGADO' ||
    upper === 'INCIDENCIA'
  ) {
    return upper as EstadoEnvio
  }
  return 'PENDIENTE_ASIGNACION'
}

function mapEnvioListaItem(raw: Record<string, unknown>): EnvioListaItem {
  const idEnvio = String(raw.idEnvio ?? raw.id ?? '')
  const fechaCreacion =
    raw.fechaCreacion != null && raw.fechaCreacion !== ''
      ? rawToIsoDate(raw.fechaCreacion)
      : raw.fechaProgramada != null
        ? rawToIsoDate(raw.fechaProgramada)
        : new Date().toISOString()
  return {
    idEnvio,
    idPedidoRef: String(raw.idPedidoRef ?? ''),
    estado: normalizarEstadoEnvio(String(raw.estado ?? '')),
    transportista:
      (raw.nombreTransportista as string | undefined) ?? (raw.transportista as string | undefined),
    fechaCreacion,
    direccion: typeof raw.direccion === 'string' ? raw.direccion : undefined,
    codigoEnvio: (raw.codigoEnvio as string | undefined) ?? undefined,
  }
}

function mapEnvioResponse(raw: Record<string, unknown>): EnvioResponse {
  const dir = (raw.direccion ?? {}) as Record<string, unknown>
  return {
    idEnvio: String(raw.idEnvio ?? raw.id ?? ''),
    idPedidoRef: String(raw.idPedidoRef ?? ''),
    estado: normalizarEstadoEnvio(String(raw.estado ?? '')),
    transportista:
      (raw.nombreTransportista as string | undefined) ?? (raw.transportista as string | undefined),
    idTransportista: raw.idTransportista != null ? String(raw.idTransportista) : undefined,
    fechaCreacion:
      raw.fechaCreacion != null && raw.fechaCreacion !== ''
        ? rawToIsoDate(raw.fechaCreacion)
        : raw.fechaProgramada != null
          ? rawToIsoDate(raw.fechaProgramada)
          : new Date().toISOString(),
    direccion: {
      calle: String(dir.calle ?? ''),
      ciudad: String(dir.ciudad ?? ''),
      region: String(dir.region ?? ''),
      codigoPostal: String(dir.codigoPostal ?? ''),
      instrucciones: dir.instrucciones != null ? String(dir.instrucciones) : undefined,
    },
  }
}

function mapSeguimientoItem(raw: Record<string, unknown>): SeguimientoEnvio {
  const descripcion = String(raw.descripcion ?? raw.observacion ?? '')
  return {
    idSeguimiento: String(raw.id ?? raw.idSeguimiento ?? ''),
    idEnvio: String(raw.idEnvio ?? ''),
    estado: normalizarEstadoEnvio(String(raw.estado ?? 'EN_TRANSITO')),
    fecha: rawToIsoDate(raw.fecha ?? raw.fechaEvento),
    ubicacion: String(raw.ubicacion ?? ''),
    observacion: descripcion || undefined,
  }
}

function mapTransportista(raw: Record<string, unknown>): Transportista {
  return {
    idTransportista: String(raw.id ?? raw.idTransportista ?? ''),
    nombre: String(raw.nombre ?? ''),
    telefono: raw.telefono != null ? String(raw.telefono) : undefined,
    email: raw.correo != null ? String(raw.correo) : raw.email != null ? String(raw.email) : undefined,
    vehiculo: raw.tipoServicio != null ? String(raw.tipoServicio) : undefined,
    disponible: raw.activo === true,
  }
}

export const enviosService = {
  async listarEnvios(): Promise<EnvioListaItem[]> {
    const response = await apiClient.get<Record<string, unknown>[]>('/api/bff/envios')
    return (response.data ?? []).map((row) => mapEnvioListaItem(row))
  },

  async obtenerEnvio(idEnvio: string): Promise<EnvioResponse> {
    const response = await apiClient.get<Record<string, unknown>>(`/api/bff/envios/${idEnvio}`)
    return mapEnvioResponse(response.data ?? {})
  },

  async obtenerEnvioPorPedido(idPedidoRef: string): Promise<EnvioResponse> {
    const response = await apiClient.get<Record<string, unknown>>(
      `/api/bff/envios/pedido/${idPedidoRef}`
    )
    const data = response.data
    if (!data || (typeof data === 'object' && Object.keys(data as object).length === 0)) {
      throw new Error('Envío no encontrado para este pedido')
    }
    return mapEnvioResponse(data as Record<string, unknown>)
  },

  async crearEnvio(envio: Record<string, unknown>): Promise<EnvioResponse> {
    const response = await apiClient.post<Record<string, unknown>>('/api/bff/envios', envio)
    return mapEnvioResponse((response.data ?? {}) as Record<string, unknown>)
  },

  async cambiarEstado(idEnvio: string, cambio: CambiarEstadoEnvioRequest & { estadoEnvio?: string }) {
    const body =
      'estadoEnvio' in cambio && cambio.estadoEnvio
        ? cambio
        : {
            estadoEnvio: (cambio as { nuevoEstado?: EstadoEnvio }).nuevoEstado,
            observacion: cambio.observacion,
          }
    const response = await apiClient.put<Record<string, unknown>>(
      `/api/bff/envios/${idEnvio}/estado`,
      body
    )
    return mapEnvioResponse((response.data ?? {}) as Record<string, unknown>)
  },

  async asignarTransportista(idEnvio: string, asignacion: AsignarTransportistaRequest) {
    const response = await apiClient.patch<Record<string, unknown>>(
      `/api/bff/envios/${idEnvio}/transportista`,
      {
        idTransportista:
          typeof asignacion.idTransportista === 'number'
            ? String(asignacion.idTransportista)
            : asignacion.idTransportista,
      }
    )
    return mapEnvioResponse((response.data ?? {}) as Record<string, unknown>)
  },

  async obtenerSeguimiento(idEnvio: string): Promise<SeguimientoEnvio[]> {
    const response = await apiClient.get<Record<string, unknown>[]>(
      `/api/bff/envios/${idEnvio}/seguimiento`
    )
    return (response.data ?? []).map((row) => mapSeguimientoItem(row))
  },

  async listarTransportistas(): Promise<Transportista[]> {
    const response = await apiClient.get<Record<string, unknown>[]>('/api/bff/envios/transportistas')
    return (response.data ?? []).map((row) => mapTransportista(row))
  },
}
