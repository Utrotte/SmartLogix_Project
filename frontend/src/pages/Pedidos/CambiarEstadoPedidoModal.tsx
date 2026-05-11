import { useState } from 'react'
import { pedidosService } from '@/services'
import type { CambiarEstadoPedidoRequest } from '@/types'

interface CambiarEstadoPedidoModalProps {
  idPedido: number
  estadoActual: string
  onClose: () => void
  onSuccess: () => void
}

// Estados disponibles según requerimiento del profesor
const ESTADOS_DISPONIBLES = [
  { value: 'PENDIENTE_CONFIRMACION', label: 'Pendiente' },
  { value: 'CONFIRMADO', label: 'Confirmado' },
  { value: 'COMPLETADO', label: 'Completado' },
  { value: 'CANCELADO', label: 'Cancelado' },
]

const getEstadoLabel = (estado: string) => {
  switch (estado) {
    case 'PENDIENTE_CONFIRMACION':
    case 'PENDIENTE':
    case 'CREADO':
      return 'Pendiente'
    case 'CONFIRMADO':
      return 'Confirmado'
    case 'CANCELADO':
      return 'Cancelado'
    case 'COMPLETADO':
      return 'Completado'
    default:
      return estado
  }
}

const obtenerMensajeError = (error: any, fallback: string) => {
  const data = error?.response?.data;
  if (typeof data === 'string' && data.trim()) return data;
  if (data?.message) return data.message;
  if (data?.error) return data.error;
  return fallback;
}

export default function CambiarEstadoPedidoModal({
  idPedido,
  estadoActual,
  onClose,
  onSuccess,
}: CambiarEstadoPedidoModalProps) {
  const [nuevoEstado, setNuevoEstado] = useState('')
  const [observacion, setObservacion] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError(null)

    if (!nuevoEstado) {
      setError('Debes seleccionar un nuevo estado')
      return
    }

    if (!observacion.trim()) {
      setError('La observación es obligatoria')
      return
    }

    if (nuevoEstado === estadoActual) {
      setError('El nuevo estado debe ser diferente al actual')
      return
    }

    try {
      setLoading(true)

      console.log('Cambio estado pedido:', { idPedido, estadoNuevo: nuevoEstado })

      const cambio: CambiarEstadoPedidoRequest = {
        nuevoEstado,
        observacion: observacion.trim(),
        usuarioResponsable: 'SISTEMA',
      }

      await pedidosService.cambiarEstadoPedido(idPedido, cambio)
      onSuccess()
      onClose()
    } catch (err: any) {
      console.error('Error al cambiar estado:', {
        status: err?.response?.status,
        data: err?.response?.data,
        message: err?.message,
      })
      setError(obtenerMensajeError(err, 'Error al cambiar el estado del pedido'))
    } finally {
      setLoading(false)
    }
  }

  // Filtrar estados: no mostrar el estado actual
  const estadosFiltered = ESTADOS_DISPONIBLES.filter((e) => {
    // El estado actual puede venir como CREADO o PENDIENTE_CONFIRMACION — ambos equivalen a Pendiente
    const esActual =
      e.value === estadoActual ||
      (estadoActual === 'CREADO' && e.value === 'PENDIENTE_CONFIRMACION')
    return !esActual
  })

  return (
    <div
      style={{
        position: 'fixed',
        inset: '0',
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
          borderRadius: '12px',
          padding: '32px',
          maxWidth: '500px',
          width: '90%',
          boxShadow: '0 25px 50px -12px rgba(0, 0, 0, 0.25)',
        }}
        onClick={(e) => e.stopPropagation()}
      >
        {/* Header */}
        <div style={{ marginBottom: '24px' }}>
          <h2 style={{ margin: '0 0 8px 0', color: '#0D1B3D', fontSize: '20px', fontWeight: '700' }}>
            Cambiar Estado del Pedido
          </h2>
          <p style={{ margin: '0', color: 'var(--neutral-600)', fontSize: '14px' }}>
            Estado actual:{' '}
            <strong style={{ color: '#0D1B3D' }}>{getEstadoLabel(estadoActual)}</strong>
          </p>
        </div>

        {/* Error */}
        {error && (
          <div
            style={{
              backgroundColor: '#fee2e2',
              color: '#991b1b',
              padding: '12px',
              borderRadius: '6px',
              marginBottom: '20px',
              fontSize: '14px',
            }}
          >
            ⚠️ {error}
          </div>
        )}

        {/* Form */}
        <form onSubmit={handleSubmit}>
          {/* Nuevo estado */}
          <div style={{ marginBottom: '20px' }}>
            <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', fontWeight: '600', color: 'var(--neutral-700)' }}>
              Nuevo Estado *
            </label>
            <select
              required
              value={nuevoEstado}
              onChange={(e) => setNuevoEstado(e.target.value)}
              style={{
                width: '100%',
                padding: '10px',
                border: '1px solid var(--neutral-300)',
                borderRadius: '6px',
                fontSize: '14px',
                backgroundColor: 'white',
              }}
            >
              <option value="">Selecciona un estado</option>
              {estadosFiltered.map((estado) => (
                <option key={estado.value} value={estado.value}>
                  {estado.label}
                </option>
              ))}
            </select>
          </div>

          {/* Observación */}
          <div style={{ marginBottom: '24px' }}>
            <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', fontWeight: '600', color: 'var(--neutral-700)' }}>
              Observación *
            </label>
            <textarea
              required
              value={observacion}
              onChange={(e) => setObservacion(e.target.value)}
              placeholder="Explica el motivo del cambio de estado..."
              rows={3}
              style={{
                width: '100%',
                padding: '10px',
                border: '1px solid var(--neutral-300)',
                borderRadius: '6px',
                fontSize: '14px',
                fontFamily: 'inherit',
                resize: 'vertical',
                boxSizing: 'border-box',
              }}
            />
          </div>

          {/* Botones */}
          <div style={{ display: 'flex', gap: '12px', justifyContent: 'flex-end' }}>
            <button
              type="button"
              onClick={onClose}
              disabled={loading}
              style={{
                padding: '10px 16px',
                backgroundColor: '#F5F7FA',
                color: '#0D1B3D',
                border: '1px solid #D1D5DB',
                borderRadius: '6px',
                cursor: loading ? 'not-allowed' : 'pointer',
                fontSize: '14px',
                fontWeight: '600',
              }}
            >
              Cancelar
            </button>
            <button
              type="submit"
              disabled={loading}
              style={{
                padding: '10px 16px',
                backgroundColor: loading ? 'var(--neutral-300)' : '#0066CC',
                color: 'white',
                border: 'none',
                borderRadius: '6px',
                cursor: loading ? 'not-allowed' : 'pointer',
                fontSize: '14px',
                fontWeight: '600',
              }}
            >
              {loading ? 'Cambiando...' : 'Guardar Estado'}
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}
