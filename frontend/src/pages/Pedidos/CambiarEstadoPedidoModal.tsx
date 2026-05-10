import { useState } from 'react'
import { pedidosService } from '@/services'
import type { CambiarEstadoPedidoRequest } from '@/types'

interface CambiarEstadoPedidoModalProps {
  idPedido: number
  estadoActual: string
  onClose: () => void
  onSuccess: () => void
}

const estadosDisponibles = ['PENDIENTE_CONFIRMACION', 'CONFIRMADO', 'CANCELADO', 'COMPLETADO']

const getEstadoLabel = (estado: string) => {
  switch (estado) {
    case 'PENDIENTE_CONFIRMACION':
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

      const cambio: CambiarEstadoPedidoRequest = {
        nuevoEstado,
        observacion: observacion.trim(),
        usuarioResponsable: '', // Se completará en el backend con datos de sesión
      }

      await pedidosService.cambiarEstadoPedido(idPedido, cambio)
      onSuccess()
      onClose()
    } catch (err: any) {
      const mensaje = err?.response?.data?.mensaje || 'Error al cambiar el estado'
      setError(mensaje)
      console.error('Error:', err)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div
      style={{
        position: 'fixed',
        inset: '0',
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        zIndex: '1000',
      }}
      onClick={onClose}
    >
      <div
        style={{
          backgroundColor: 'white',
          borderRadius: '8px',
          padding: '28px',
          maxWidth: '500px',
          width: '90%',
          boxShadow: '0 20px 25px -5px rgba(0, 0, 0, 0.1)',
        }}
        onClick={(e) => e.stopPropagation()}
      >
        {/* Header */}
        <div style={{ marginBottom: '24px' }}>
          <h2 style={{ margin: '0 0 8px 0', color: 'var(--neutral-900)', fontSize: '20px' }}>
            Cambiar Estado del Pedido
          </h2>
          <p style={{ margin: '0', color: 'var(--neutral-600)', fontSize: '14px' }}>
            Estado actual: <strong>{getEstadoLabel(estadoActual)}</strong>
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
              }}
            >
              <option value="">Selecciona un estado</option>
              {estadosDisponibles
                .filter((e) => e !== estadoActual)
                .map((estado) => (
                  <option key={estado} value={estado}>
                    {getEstadoLabel(estado)}
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
                backgroundColor: 'var(--neutral-200)',
                color: 'var(--neutral-900)',
                border: 'none',
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
                backgroundColor: loading ? 'var(--neutral-300)' : 'var(--primary)',
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
        </form>
      </div>
    </div>
  )
}
