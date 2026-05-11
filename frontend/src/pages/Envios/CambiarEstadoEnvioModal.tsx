import { useState } from 'react'
import { enviosService } from '@/services'
import type { EnvioListaItem, EstadoEnvio } from '@/types'

interface Props {
  envio: EnvioListaItem
  onClose: () => void
  onSuccess: () => void
}

const ESTADOS_DISPONIBLES: EstadoEnvio[] = [
  'PENDIENTE_ASIGNACION',
  'ASIGNADO',
  'EN_TRANSITO',
  'ENTREGADO',
  'INCIDENCIA',
]

const getEstadoLabel = (estado: EstadoEnvio): string => {
  switch (estado) {
    case 'PENDIENTE_ASIGNACION':
      return 'Pendiente Asignación'
    case 'ASIGNADO':
      return 'Asignado'
    case 'EN_TRANSITO':
      return 'En Tránsito'
    case 'ENTREGADO':
      return 'Entregado'
    case 'INCIDENCIA':
      return 'Incidencia'
    default:
      return estado
  }
}

export default function CambiarEstadoEnvioModal({ envio, onClose, onSuccess }: Props) {
  const [nuevoEstado, setNuevoEstado] = useState<EstadoEnvio>('PENDIENTE_ASIGNACION')
  const [observacion, setObservacion] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const handleSubmit = async () => {
    if (!nuevoEstado) {
      setError('Selecciona un nuevo estado')
      return
    }

    if (!observacion.trim()) {
      setError('La observación es requerida')
      return
    }

    if (nuevoEstado === envio.estado) {
      setError('El nuevo estado debe ser diferente al estado actual')
      return
    }

    try {
      setLoading(true)
      setError(null)
      
      const request = {
        estadoEnvio: nuevoEstado,
        observacion,
      }

      console.log("Frontend -> cambiar estado envío:", {
        idEnvio: envio.idEnvio,
        request,
      });

      await enviosService.cambiarEstado(envio.idEnvio, request as any)
      onSuccess()
    } catch (err: any) {
      console.error("Error API envíos:", {
        status: err?.response?.status,
        data: err?.response?.data,
        url: err?.config?.url,
        method: err?.config?.method,
        message: err.message,
      });
      const data = err?.response?.data;
      let msg = 'Error al cambiar el estado del envío. Intenta nuevamente.';
      if (typeof data === 'string' && data.trim()) msg = data;
      else if (data?.message) msg = data.message;
      else if (data?.error) msg = data.error;
      setError(msg)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div
      style={{
        position: 'fixed',
        top: 0,
        left: 0,
        right: 0,
        bottom: 0,
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        zIndex: 1000,
      }}
      onClick={onClose}
    >
      <div
        style={{
          backgroundColor: 'white',
          borderRadius: '8px',
          padding: '24px',
          maxWidth: '400px',
          width: '90%',
          maxHeight: '90vh',
          overflowY: 'auto',
        }}
        onClick={(e) => e.stopPropagation()}
      >
        <h2 style={{ margin: '0 0 16px 0', color: 'var(--neutral-900)' }}>Cambiar Estado del Envío</h2>

        {error && (
          <div
            style={{
              backgroundColor: '#fee2e2',
              color: '#991b1b',
              padding: '12px',
              borderRadius: '6px',
              marginBottom: '16px',
              fontSize: '14px',
            }}
          >
            ⚠️ {error}
          </div>
        )}

        <div style={{ marginBottom: '16px' }}>
          <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', color: 'var(--neutral-700)' }}>
            Nuevo Estado (Requerido)
          </label>
          <select
            value={nuevoEstado}
            onChange={(e) => setNuevoEstado(e.target.value as EstadoEnvio)}
            style={{
              width: '100%',
              padding: '8px 12px',
              border: '1px solid var(--neutral-300)',
              borderRadius: '6px',
              fontSize: '14px',
            }}
          >
            {ESTADOS_DISPONIBLES.filter((e) => e !== envio.estado).map((estado) => (
              <option key={estado} value={estado}>
                {getEstadoLabel(estado)}
              </option>
            ))}
          </select>
        </div>

        <div style={{ marginBottom: '20px' }}>
          <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', color: 'var(--neutral-700)' }}>
            Observación (Requerido)
          </label>
          <textarea
            placeholder="Ej: Entregado en bodega de destino"
            value={observacion}
            onChange={(e) => setObservacion(e.target.value)}
            style={{
              width: '100%',
              padding: '8px 12px',
              border: '1px solid var(--neutral-300)',
              borderRadius: '6px',
              fontSize: '14px',
              fontFamily: 'inherit',
              minHeight: '80px',
              resize: 'vertical',
            }}
          />
        </div>

        <div style={{ display: 'flex', gap: '12px' }}>
          <button
            onClick={onClose}
            style={{
              flex: 1,
              padding: '10px',
              backgroundColor: 'var(--neutral-300)',
              color: 'var(--neutral-900)',
              border: 'none',
              borderRadius: '6px',
              cursor: 'pointer',
              fontSize: '14px',
              fontWeight: '600',
            }}
          >
            Cancelar
          </button>
          <button
            onClick={handleSubmit}
            disabled={loading}
            style={{
              flex: 1,
              padding: '10px',
              backgroundColor: loading ? 'var(--neutral-400)' : 'var(--primary)',
              color: 'white',
              border: 'none',
              borderRadius: '6px',
              cursor: loading ? 'not-allowed' : 'pointer',
              fontSize: '14px',
              fontWeight: '600',
            }}
          >
            {loading ? 'Cambiando...' : 'Cambiar Estado'}
          </button>
        </div>
      </div>
    </div>
  )
}
