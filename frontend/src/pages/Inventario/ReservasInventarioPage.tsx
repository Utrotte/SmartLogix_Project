import { useEffect, useState } from 'react'
import { Card, Badge, LoadingSpinner } from '@/components/UI'
import { inventarioService } from '@/services'
import type { ReservaInventario } from '@/types'

export default function ReservasInventarioPage() {
  const [reservas, setReservas] = useState<ReservaInventario[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    cargarReservas()
  }, [])

  const cargarReservas = async () => {
    try {
      setLoading(true)
      setError(null)
      const data = await inventarioService.listarReservas()
      setReservas(data)
    } catch (err) {
      setError('Error al cargar las reservas. Intenta nuevamente.')
      console.error('Error:', err)
    } finally {
      setLoading(false)
    }
  }

  const getEstadoBadgeColor = (estado: string): 'success' | 'warning' | 'danger' | 'default' => {
    switch (estado) {
      case 'ACTIVA':
        return 'success'
      case 'COMPLETADA':
        return 'default'
      case 'CANCELADA':
        return 'danger'
      default:
        return 'default'
    }
  }

  const getEstadoLabel = (estado: string): string => {
    switch (estado) {
      case 'ACTIVA':
        return 'Activa'
      case 'COMPLETADA':
        return 'Completada'
      case 'CANCELADA':
        return 'Cancelada'
      default:
        return estado
    }
  }

  if (loading) {
    return (
      <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '400px' }}>
        <div style={{ textAlign: 'center' }}>
          <LoadingSpinner size="lg" />
          <p style={{ marginTop: '20px', color: 'var(--neutral-600)' }}>
            Cargando reservas...
          </p>
        </div>
      </div>
    )
  }

  return (
    <div>
      {/* Header */}
      <div style={{ marginBottom: '30px' }}>
        <h1 style={{ margin: '0 0 10px 0', color: 'var(--neutral-900)' }}>
          Reservas de Inventario
        </h1>
        <p style={{ margin: '0', color: 'var(--neutral-600)', fontSize: '16px' }}>
          Control de reservas por pedido
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

      {/* Tabla de reservas */}
      {reservas.length === 0 ? (
        <Card padding="40px" style={{ textAlign: 'center' }}>
          <p style={{ color: 'var(--neutral-500)', fontSize: '16px', margin: '0' }}>
            No hay reservas registradas
          </p>
        </Card>
      ) : (
        <Card padding="0">
          <div style={{ overflowX: 'auto' }}>
            <table
              style={{
                width: '100%',
                borderCollapse: 'collapse',
                fontSize: '14px',
              }}
            >
              <thead>
                <tr style={{ borderBottom: '2px solid var(--neutral-200)', backgroundColor: 'var(--neutral-50)' }}>
                  <th style={{ padding: '12px 16px', textAlign: 'left', fontWeight: '600', color: 'var(--neutral-700)' }}>
                    ID Reserva
                  </th>
                  <th style={{ padding: '12px 16px', textAlign: 'left', fontWeight: '600', color: 'var(--neutral-700)' }}>
                    Pedido Asociado
                  </th>
                  <th style={{ padding: '12px 16px', textAlign: 'left', fontWeight: '600', color: 'var(--neutral-700)' }}>
                    Estado
                  </th>
                  <th style={{ padding: '12px 16px', textAlign: 'center', fontWeight: '600', color: 'var(--neutral-700)' }}>
                    Cantidad de Productos
                  </th>
                  <th style={{ padding: '12px 16px', textAlign: 'left', fontWeight: '600', color: 'var(--neutral-700)' }}>
                    Fecha de Creación
                  </th>
                </tr>
              </thead>
              <tbody>
                {reservas.map((reserva) => (
                  <tr
                    key={reserva.idReserva}
                    style={{
                      borderBottom: '1px solid var(--neutral-200)',
                    }}
                  >
                    <td style={{ padding: '12px 16px', color: 'var(--neutral-900)', fontWeight: '500' }}>
                      {reserva.idReserva || 'N/A'}
                    </td>
                    <td style={{ padding: '12px 16px', color: 'var(--neutral-900)' }}>
                      {reserva.idPedidoRef}
                    </td>
                    <td style={{ padding: '12px 16px' }}>
                      <Badge variant={getEstadoBadgeColor(reserva.estado)}>
                        {getEstadoLabel(reserva.estado)}
                      </Badge>
                    </td>
                    <td style={{ padding: '12px 16px', textAlign: 'center', color: 'var(--neutral-900)' }}>
                      {reserva.detalles.length}
                    </td>
                    <td style={{ padding: '12px 16px', color: 'var(--neutral-700)', fontSize: '13px' }}>
                      {reserva.fechaCreacion
                        ? new Date(reserva.fechaCreacion).toLocaleDateString('es-CL', {
                            year: 'numeric',
                            month: 'long',
                            day: 'numeric',
                          })
                        : 'N/A'}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
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
        Total de reservas: {reservas.length}
      </div>
    </div>
  )
}
