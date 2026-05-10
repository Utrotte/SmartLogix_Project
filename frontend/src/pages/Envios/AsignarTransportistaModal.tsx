import { useState, useEffect } from 'react'
import { enviosService } from '@/services'
import type { EnvioListaItem, Transportista } from '@/types'

interface Props {
  envio: EnvioListaItem
  onClose: () => void
  onSuccess: () => void
}

export default function AsignarTransportistaModal({ envio, onClose, onSuccess }: Props) {
  const [transportistas, setTransportistas] = useState<Transportista[]>([])
  const [selectedTransportista, setSelectedTransportista] = useState<number | ''>('')
  const [loading, setLoading] = useState(true)
  const [submitting, setSubmitting] = useState(false)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    cargarTransportistas()
  }, [])

  const cargarTransportistas = async () => {
    try {
      setLoading(true)
      setError(null)
      const data = await enviosService.listarTransportistas()
      setTransportistas(data)
      // Seleccionar el primero por defecto
      if (data.length > 0) {
        setSelectedTransportista(data[0].idTransportista)
      }
    } catch (err: any) {
      setError('Error al cargar los transportistas. Intenta nuevamente.')
      console.error('Error:', err)
    } finally {
      setLoading(false)
    }
  }

  const handleSubmit = async () => {
    if (!selectedTransportista) {
      setError('Selecciona un transportista')
      return
    }

    try {
      setSubmitting(true)
      setError(null)
      await enviosService.asignarTransportista(envio.idEnvio, {
        idTransportista: selectedTransportista as number,
      })
      onSuccess()
    } catch (err: any) {
      setError('Error al asignar el transportista. Intenta nuevamente.')
      console.error('Error:', err)
    } finally {
      setSubmitting(false)
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
        <h2 style={{ margin: '0 0 16px 0', color: 'var(--neutral-900)' }}>Asignar Transportista</h2>

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

        {loading ? (
          <p style={{ color: 'var(--neutral-600)', fontSize: '14px' }}>Cargando transportistas...</p>
        ) : transportistas.length === 0 ? (
          <p style={{ color: 'var(--neutral-600)', fontSize: '14px' }}>No hay transportistas disponibles</p>
        ) : (
          <>
            <div style={{ marginBottom: '20px' }}>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', color: 'var(--neutral-700)' }}>
                Selecciona un Transportista (Requerido)
              </label>
              <select
                value={selectedTransportista}
                onChange={(e) => setSelectedTransportista(Number(e.target.value) || '')}
                style={{
                  width: '100%',
                  padding: '8px 12px',
                  border: '1px solid var(--neutral-300)',
                  borderRadius: '6px',
                  fontSize: '14px',
                }}
              >
                <option value="">Selecciona...</option>
                {transportistas.map((transportista) => (
                  <option key={transportista.idTransportista} value={transportista.idTransportista}>
                    {transportista.nombre}
                    {transportista.vehiculo ? ` - ${transportista.vehiculo}` : ''}
                  </option>
                ))}
              </select>
            </div>

            {/* Info del transportista seleccionado */}
            {selectedTransportista && (
              <div
                style={{
                  backgroundColor: 'var(--neutral-50)',
                  padding: '12px',
                  borderRadius: '6px',
                  marginBottom: '20px',
                  fontSize: '13px',
                  color: 'var(--neutral-700)',
                }}
              >
                {transportistas.map((t) => (
                  t.idTransportista === selectedTransportista && (
                    <div key={t.idTransportista}>
                      <p style={{ margin: '0 0 6px 0' }}>
                        <strong>Nombre:</strong> {t.nombre}
                      </p>
                      {t.telefono && (
                        <p style={{ margin: '0 0 6px 0' }}>
                          <strong>Teléfono:</strong> {t.telefono}
                        </p>
                      )}
                      {t.email && (
                        <p style={{ margin: '0 0 6px 0' }}>
                          <strong>Email:</strong> {t.email}
                        </p>
                      )}
                      {t.vehiculo && (
                        <p style={{ margin: '0' }}>
                          <strong>Vehículo:</strong> {t.vehiculo}
                        </p>
                      )}
                    </div>
                  )
                ))}
              </div>
            )}

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
                disabled={submitting || !selectedTransportista}
                style={{
                  flex: 1,
                  padding: '10px',
                  backgroundColor: submitting || !selectedTransportista ? 'var(--neutral-400)' : 'var(--primary)',
                  color: 'white',
                  border: 'none',
                  borderRadius: '6px',
                  cursor: submitting || !selectedTransportista ? 'not-allowed' : 'pointer',
                  fontSize: '14px',
                  fontWeight: '600',
                }}
              >
                {submitting ? 'Asignando...' : 'Asignar'}
              </button>
            </div>
          </>
        )}
      </div>
    </div>
  )
}
