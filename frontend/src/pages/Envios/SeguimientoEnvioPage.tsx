import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { Card, Badge, LoadingSpinner } from '@/components/UI'
import { enviosService } from '@/services'
import type { SeguimientoEnvio } from '@/types'

export default function SeguimientoEnvioPage() {
  const { idEnvio } = useParams<{ idEnvio: string }>()
  const navigate = useNavigate()
  const [eventos, setEventos] = useState<SeguimientoEnvio[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    cargarSeguimiento()
  }, [idEnvio])

  const cargarSeguimiento = async () => {
    if (!idEnvio) return

    try {
      setLoading(true)
      setError(null)
      const data = await enviosService.obtenerSeguimiento(idEnvio)
      setEventos(data)
    } catch (err) {
      setError('Error al cargar el seguimiento. Intenta nuevamente.')
      console.error('Error:', err)
    } finally {
      setLoading(false)
    }
  }

  const getEstadoBadgeColor = (estado: string): 'default' | 'danger' | 'success' | 'warning' => {
    const stateMap: Record<string, 'default' | 'danger' | 'success' | 'warning'> = {
      PENDIENTE_ASIGNACION: 'warning',
      ASIGNADO: 'warning',
      EN_TRANSITO: 'warning',
      ENTREGADO: 'success',
      INCIDENCIA: 'danger',
    }
    return stateMap[estado] || 'default'
  }

  const getEstadoLabel = (estado: string): string => {
    const labelMap: Record<string, string> = {
      PENDIENTE_ASIGNACION: 'Pendiente Asignación',
      ASIGNADO: 'Asignado',
      EN_TRANSITO: 'En Tránsito',
      ENTREGADO: 'Entregado',
      INCIDENCIA: 'Incidencia',
    }
    return labelMap[estado] || estado
  }

  if (loading) {
    return (
      <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '400px' }}>
        <div style={{ textAlign: 'center' }}>
          <LoadingSpinner size="lg" />
          <p style={{ marginTop: '20px', color: 'var(--neutral-600)' }}>Cargando seguimiento...</p>
        </div>
      </div>
    )
  }

  return (
    <div>
      {/* Header */}
      <div style={{ marginBottom: '30px' }}>
        <button
          onClick={() => navigate(`/envios/${idEnvio}`)}
          style={{
            backgroundColor: 'transparent',
            border: 'none',
            color: 'var(--primary)',
            cursor: 'pointer',
            fontSize: '14px',
            padding: '0',
            marginBottom: '10px',
          }}
        >
          ← Volver a Detalle
        </button>
        <h1 style={{ margin: '10px 0 5px 0', color: 'var(--neutral-900)' }}>
          Seguimiento del Envío
        </h1>
        <p style={{ margin: '0', color: 'var(--neutral-600)', fontSize: '16px' }}>
          Historial de eventos: {idEnvio}
        </p>
      </div>

      {/* Error */}
      {error && (
        <div
          style={{
            backgroundColor: '#fee2e2',
            color: '#991b1b',
            padding: '12px 16px',
            borderRadius: '6px',
            marginBottom: '20px',
            fontSize: '14px',
          }}
        >
          ⚠️ {error}
        </div>
      )}

      {/* Timeline */}
      {eventos.length === 0 ? (
        <Card padding="40px" style={{ textAlign: 'center' }}>
          <p style={{ color: 'var(--neutral-500)', fontSize: '16px', margin: '0' }}>
            No hay eventos registrados aún
          </p>
        </Card>
      ) : (
        <Card padding="0">
          <div style={{ position: 'relative', padding: '30px' }}>
            {/* Línea vertical central */}
            <div
              style={{
                position: 'absolute',
                left: '30px',
                top: '0',
                bottom: '0',
                width: '2px',
                backgroundColor: 'var(--neutral-300)',
              }}
            />

            {/* Eventos */}
            <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
              {eventos.map((evento) => (
                <div key={evento.idSeguimiento} style={{ display: 'flex', gap: '20px' }}>
                  {/* Punto en la línea */}
                  <div
                    style={{
                      position: 'relative',
                      zIndex: 10,
                    }}
                  >
                    <div
                      style={{
                        width: '20px',
                        height: '20px',
                        backgroundColor:
                          evento.estado === 'ENTREGADO'
                            ? 'var(--success)'
                            : evento.estado === 'INCIDENCIA'
                              ? 'var(--danger)'
                              : 'var(--primary)',
                        borderRadius: '50%',
                        border: '3px solid white',
                        boxShadow: '0 0 0 2px var(--primary)',
                        marginTop: '2px',
                      }}
                    />
                  </div>

                  {/* Contenido del evento */}
                  <div style={{ paddingTop: '2px', flex: 1 }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '12px', marginBottom: '8px' }}>
                      <h3 style={{ margin: '0', color: 'var(--neutral-900)', fontSize: '16px', fontWeight: '600' }}>
                        {getEstadoLabel(evento.estado)}
                      </h3>
                      <Badge variant={getEstadoBadgeColor(evento.estado)}>{evento.estado}</Badge>
                    </div>

                    <p style={{ margin: '0 0 8px 0', color: 'var(--neutral-600)', fontSize: '13px' }}>
                      {new Date(evento.fecha).toLocaleDateString('es-CL', {
                        weekday: 'long',
                        year: 'numeric',
                        month: 'long',
                        day: 'numeric',
                        hour: '2-digit',
                        minute: '2-digit',
                      })}
                    </p>

                    {evento.ubicacion && (
                      <p style={{ margin: '0 0 8px 0', color: 'var(--neutral-700)', fontSize: '14px' }}>
                        <strong>📍 Ubicación:</strong> {evento.ubicacion}
                      </p>
                    )}

                    {evento.observacion && (
                      <p style={{ margin: '0', color: 'var(--neutral-700)', fontSize: '14px' }}>
                        <strong>📋 Observación:</strong> {evento.observacion}
                      </p>
                    )}
                  </div>
                </div>
              ))}
            </div>
          </div>
        </Card>
      )}

      {/* Footer info */}
      <div
        style={{
          marginTop: '20px',
          padding: '12px',
          backgroundColor: 'var(--neutral-50)',
          borderRadius: '6px',
          fontSize: '12px',
          color: 'var(--neutral-500)',
        }}
      >
        Total de eventos: {eventos.length}
      </div>
    </div>
  )
}
