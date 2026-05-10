import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { Card, Badge, LoadingSpinner } from '@/components/UI'
import { enviosService } from '@/services'
import type { EnvioResponse, EstadoEnvio } from '@/types'
import CambiarEstadoEnvioModal from './CambiarEstadoEnvioModal'
import AsignarTransportistaModal from './AsignarTransportistaModal'

export default function DetalleEnvioPage() {
  const { idEnvio } = useParams<{ idEnvio: string }>()
  const navigate = useNavigate()
  const [envio, setEnvio] = useState<EnvioResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [showEstadoModal, setShowEstadoModal] = useState(false)
  const [showTransportistaModal, setShowTransportistaModal] = useState(false)

  useEffect(() => {
    cargarEnvio()
  }, [idEnvio])

  const cargarEnvio = async () => {
    if (!idEnvio) return

    try {
      setLoading(true)
      setError(null)
      const data = await enviosService.obtenerEnvio(idEnvio)
      setEnvio(data)
    } catch (err) {
      setError('Error al cargar los detalles del envío. Intenta nuevamente.')
      console.error('Error:', err)
    } finally {
      setLoading(false)
    }
  }

  const getEstadoBadgeColor = (estado: EstadoEnvio): 'default' | 'danger' | 'success' | 'warning' => {
    switch (estado) {
      case 'PENDIENTE_ASIGNACION':
        return 'warning'
      case 'ASIGNADO':
        return 'warning'
      case 'EN_TRANSITO':
        return 'warning'
      case 'ENTREGADO':
        return 'success'
      case 'INCIDENCIA':
        return 'danger'
      default:
        return 'default'
    }
  }

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

  if (loading) {
    return (
      <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '400px' }}>
        <div style={{ textAlign: 'center' }}>
          <LoadingSpinner size="lg" />
          <p style={{ marginTop: '20px', color: 'var(--neutral-600)' }}>Cargando envío...</p>
        </div>
      </div>
    )
  }

  if (error || !envio) {
    return (
      <div>
        <button
          onClick={() => navigate('/envios')}
          style={{
            backgroundColor: 'transparent',
            border: 'none',
            color: 'var(--primary)',
            cursor: 'pointer',
            fontSize: '14px',
            padding: '0',
            marginBottom: '20px',
          }}
        >
          ← Volver a Envíos
        </button>
        <div
          style={{
            backgroundColor: '#fee2e2',
            color: '#991b1b',
            padding: '20px',
            borderRadius: '6px',
            fontSize: '14px',
          }}
        >
          ⚠️ {error || 'Envío no encontrado'}
        </div>
      </div>
    )
  }

  return (
    <div>
      {/* Header */}
      <div style={{ marginBottom: '30px' }}>
        <button
          onClick={() => navigate('/envios')}
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
          ← Volver a Envíos
        </button>
        <h1 style={{ margin: '10px 0 5px 0', color: 'var(--neutral-900)' }}>Envío {envio.idEnvio}</h1>
        <p style={{ margin: '0', color: 'var(--neutral-600)', fontSize: '16px' }}>
          Detalles completos del envío
        </p>
      </div>

      {/* Info principal */}
      <Card title="Información Principal" padding="20px" style={{ marginBottom: '20px' }}>
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px' }}>
          <div>
            <p style={{ margin: '0 0 8px 0', color: 'var(--neutral-600)', fontSize: '12px', fontWeight: '600' }}>
              ESTADO
            </p>
            <Badge variant={getEstadoBadgeColor(envio.estado)}>{getEstadoLabel(envio.estado)}</Badge>
          </div>
          <div>
            <p style={{ margin: '0 0 8px 0', color: 'var(--neutral-600)', fontSize: '12px', fontWeight: '600' }}>
              PEDIDO ASOCIADO
            </p>
            <p
              style={{
                margin: '0',
                color: 'var(--primary)',
                cursor: 'pointer',
                textDecoration: 'underline',
                fontSize: '14px',
                fontWeight: '500',
              }}
              onClick={() => navigate(`/pedidos/${envio.idPedidoRef}`)}
            >
              {envio.idPedidoRef}
            </p>
          </div>
          <div>
            <p style={{ margin: '0 0 8px 0', color: 'var(--neutral-600)', fontSize: '12px', fontWeight: '600' }}>
              FECHA CREACIÓN
            </p>
            <p style={{ margin: '0', color: 'var(--neutral-700)', fontSize: '14px' }}>
              {new Date(envio.fechaCreacion).toLocaleDateString('es-CL')}
            </p>
          </div>
          <div>
            <p style={{ margin: '0 0 8px 0', color: 'var(--neutral-600)', fontSize: '12px', fontWeight: '600' }}>
              TRANSPORTISTA
            </p>
            <p style={{ margin: '0', color: 'var(--neutral-700)', fontSize: '14px' }}>
              {envio.transportista || 'Sin asignar'}
            </p>
          </div>
        </div>
      </Card>

      {/* Dirección */}
      <Card title="Dirección de Entrega" padding="20px" style={{ marginBottom: '20px' }}>
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px' }}>
          <div>
            <p style={{ margin: '0 0 4px 0', color: 'var(--neutral-600)', fontSize: '12px', fontWeight: '600' }}>
              CALLE
            </p>
            <p style={{ margin: '0', color: 'var(--neutral-700)', fontSize: '14px' }}>
              {envio.direccion.calle}
            </p>
          </div>
          <div>
            <p style={{ margin: '0 0 4px 0', color: 'var(--neutral-600)', fontSize: '12px', fontWeight: '600' }}>
              CIUDAD
            </p>
            <p style={{ margin: '0', color: 'var(--neutral-700)', fontSize: '14px' }}>
              {envio.direccion.ciudad}
            </p>
          </div>
          <div>
            <p style={{ margin: '0 0 4px 0', color: 'var(--neutral-600)', fontSize: '12px', fontWeight: '600' }}>
              REGIÓN
            </p>
            <p style={{ margin: '0', color: 'var(--neutral-700)', fontSize: '14px' }}>
              {envio.direccion.region}
            </p>
          </div>
          <div>
            <p style={{ margin: '0 0 4px 0', color: 'var(--neutral-600)', fontSize: '12px', fontWeight: '600' }}>
              CÓDIGO POSTAL
            </p>
            <p style={{ margin: '0', color: 'var(--neutral-700)', fontSize: '14px' }}>
              {envio.direccion.codigoPostal}
            </p>
          </div>
        </div>
      </Card>

      {/* Botones de acción */}
      <div style={{ display: 'flex', gap: '12px', marginBottom: '20px' }}>
        <button
          onClick={() => setShowEstadoModal(true)}
          style={{
            padding: '10px 16px',
            backgroundColor: 'var(--warning)',
            color: 'white',
            border: 'none',
            borderRadius: '6px',
            cursor: 'pointer',
            fontSize: '14px',
            fontWeight: '600',
          }}
        >
          Cambiar Estado
        </button>
        <button
          onClick={() => setShowTransportistaModal(true)}
          style={{
            padding: '10px 16px',
            backgroundColor: 'var(--success)',
            color: 'white',
            border: 'none',
            borderRadius: '6px',
            cursor: 'pointer',
            fontSize: '14px',
            fontWeight: '600',
          }}
        >
          Asignar Transportista
        </button>
        <button
          onClick={() => navigate(`/envios/${envio.idEnvio}/seguimiento`)}
          style={{
            padding: '10px 16px',
            backgroundColor: 'var(--neutral-500)',
            color: 'white',
            border: 'none',
            borderRadius: '6px',
            cursor: 'pointer',
            fontSize: '14px',
            fontWeight: '600',
          }}
        >
          Ver Seguimiento
        </button>
      </div>

      {/* Modales */}
      {showEstadoModal && (
        <CambiarEstadoEnvioModal
          envio={{ idEnvio: envio.idEnvio, idPedidoRef: envio.idPedidoRef, estado: envio.estado } as any}
          onClose={() => setShowEstadoModal(false)}
          onSuccess={() => {
            setShowEstadoModal(false)
            cargarEnvio()
          }}
        />
      )}
      {showTransportistaModal && (
        <AsignarTransportistaModal
          envio={{ idEnvio: envio.idEnvio, idPedidoRef: envio.idPedidoRef, estado: envio.estado } as any}
          onClose={() => setShowTransportistaModal(false)}
          onSuccess={() => {
            setShowTransportistaModal(false)
            cargarEnvio()
          }}
        />
      )}
    </div>
  )
}
