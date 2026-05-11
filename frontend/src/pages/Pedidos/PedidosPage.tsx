import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Card, Badge, LoadingSpinner } from '@/components/UI'
import { pedidosService } from '@/services'
import type { PedidoListaItem } from '@/types'

type EstadoFilter = 'TODOS' | 'PENDIENTE_CONFIRMACION' | 'CONFIRMADO' | 'CANCELADO' | 'COMPLETADO'

export default function PedidosPage() {
  const navigate = useNavigate()
  const [pedidos, setPedidos] = useState<PedidoListaItem[]>([])
  const [filteredPedidos, setFilteredPedidos] = useState<PedidoListaItem[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [estadoFilter, setEstadoFilter] = useState<EstadoFilter>('TODOS')
  const [searchTerm, setSearchTerm] = useState('')
  const [deletingId, setDeletingId] = useState<number | null>(null)

  // Cargar pedidos
  useEffect(() => {
    cargarPedidos()
  }, [])

  // Filtrar y buscar
  useEffect(() => {
    let filtered = pedidos

    // Filtro por estado
    if (estadoFilter !== 'TODOS') {
      filtered = filtered.filter((p) => p.estadoActual === estadoFilter)
    }

    // Búsqueda por ID o número de referencia
    if (searchTerm) {
      const term = searchTerm.toLowerCase()
      filtered = filtered.filter(
        (p) =>
          p.idPedido.toString().includes(term) ||
          p.codigoPedido.toLowerCase().includes(term)
      )
    }

    setFilteredPedidos(filtered)
  }, [pedidos, estadoFilter, searchTerm])

  const cargarPedidos = async () => {
    try {
      setLoading(true)
      setError(null)
      const data = await pedidosService.listarPedidos()
      setPedidos(data)
    } catch (err) {
      setError('Error al cargar los pedidos. Intenta nuevamente.')
      console.error('Error:', err)
    } finally {
      setLoading(false)
    }
  }

  const handleEliminar = async (idPedido: number) => {
    if (!confirm('¿Estás seguro de que deseas eliminar este pedido?')) return

    try {
      setDeletingId(idPedido)
      await pedidosService.eliminarPedido(idPedido)
      setPedidos(pedidos.filter((p) => p.idPedido !== idPedido))
    } catch (err) {
      alert('Error al eliminar el pedido')
      console.error('Error:', err)
    } finally {
      setDeletingId(null)
    }
  }

  const getEstadoBadgeColor = (estado: string) => {
    switch (estado) {
      case 'PENDIENTE_CONFIRMACION':
        return 'warning'
      case 'CONFIRMADO':
        return 'success'
      case 'CANCELADO':
        return 'danger'
      case 'COMPLETADO':
        return 'default'
      default:
        return 'default'
    }
  }

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

  if (loading) {
    return (
      <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '400px' }}>
        <div style={{ textAlign: 'center' }}>
          <LoadingSpinner size="lg" />
          <p style={{ marginTop: '20px', color: 'var(--neutral-600)' }}>
            Cargando pedidos...
          </p>
        </div>
      </div>
    )
  }

  return (
    <div>
      {/* Header */}
      <div style={{ marginBottom: '30px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <h1 style={{ margin: '0 0 10px 0', color: 'var(--neutral-900)' }}>
            Pedidos
          </h1>
          <p style={{ margin: '0', color: 'var(--neutral-600)', fontSize: '16px' }}>
            Gestiona tus pedidos
          </p>
        </div>
        <button
          onClick={() => navigate('/pedidos/crear')}
          style={{
            padding: '10px 20px',
            backgroundColor: 'var(--primary)',
            color: 'white',
            border: 'none',
            borderRadius: '6px',
            cursor: 'pointer',
            fontSize: '14px',
            fontWeight: '600',
          }}
        >
          + Nuevo Pedido
        </button>
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

      {/* Filtros y búsqueda */}
      <Card title="Filtros" padding="16px" style={{ marginBottom: '20px' }}>
        <div style={{ display: 'flex', gap: '16px', flexWrap: 'wrap' }}>
          {/* Búsqueda */}
          <div style={{ flex: 1, minWidth: '200px' }}>
            <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', color: 'var(--neutral-700)' }}>
              Buscar por ID o Código
            </label>
            <input
              type="text"
              placeholder="Ej: 1, PED-001"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              style={{
                width: '100%',
                padding: '8px 12px',
                border: '1px solid var(--neutral-300)',
                borderRadius: '6px',
                fontSize: '14px',
              }}
            />
          </div>

          {/* Filtro por estado */}
          <div style={{ flex: 1, minWidth: '200px' }}>
            <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', color: 'var(--neutral-700)' }}>
              Estado
            </label>
            <select
              value={estadoFilter}
              onChange={(e) => setEstadoFilter(e.target.value as EstadoFilter)}
              style={{
                width: '100%',
                padding: '8px 12px',
                border: '1px solid var(--neutral-300)',
                borderRadius: '6px',
                fontSize: '14px',
              }}
            >
              <option value="TODOS">Todos</option>
              <option value="PENDIENTE_CONFIRMACION">Pendiente</option>
              <option value="CONFIRMADO">Confirmado</option>
              <option value="CANCELADO">Cancelado</option>
              <option value="COMPLETADO">Completado</option>
            </select>
          </div>
        </div>
      </Card>

      {/* Tabla de pedidos */}
      {filteredPedidos.length === 0 ? (
        <Card padding="40px" style={{ textAlign: 'center' }}>
          <p style={{ color: 'var(--neutral-500)', fontSize: '16px', margin: '0' }}>
            No hay pedidos que mostrar
          </p>
        </Card>
      ) : (
        <Card padding="0">
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
                  ID
                </th>
                <th style={{ padding: '12px 16px', textAlign: 'left', fontWeight: '600', color: 'var(--neutral-700)' }}>
                  Código
                </th>
                <th style={{ padding: '12px 16px', textAlign: 'left', fontWeight: '600', color: 'var(--neutral-700)' }}>
                  Cliente
                </th>
                <th style={{ padding: '12px 16px', textAlign: 'left', fontWeight: '600', color: 'var(--neutral-700)' }}>
                  Estado
                </th>
                <th style={{ padding: '12px 16px', textAlign: 'right', fontWeight: '600', color: 'var(--neutral-700)' }}>
                  Monto
                </th>
                <th style={{ padding: '12px 16px', textAlign: 'left', fontWeight: '600', color: 'var(--neutral-700)' }}>
                  Fecha
                </th>
                <th style={{ padding: '12px 16px', textAlign: 'center', fontWeight: '600', color: 'var(--neutral-700)' }}>
                  Acciones
                </th>
              </tr>
            </thead>
            <tbody>
              {filteredPedidos.map((pedido) => (
                <tr
                  key={pedido.idPedido}
                  style={{
                    borderBottom: '1px solid var(--neutral-200)',
                  }}
                >
                  <td style={{ padding: '12px 16px', color: 'var(--neutral-900)' }}>
                    {pedido.idPedido}
                  </td>
                  <td style={{ padding: '12px 16px', color: 'var(--neutral-900)', fontWeight: '500' }}>
                    {pedido.codigoPedido}
                  </td>
                  <td style={{ padding: '12px 16px', color: 'var(--neutral-700)' }}>
                    {pedido.nombreCliente || `Cliente #${pedido.idCliente}`}
                  </td>
                  <td style={{ padding: '12px 16px' }}>
                    <Badge variant={getEstadoBadgeColor(pedido.estadoActual)}>
                      {getEstadoLabel(pedido.estadoActual)}
                    </Badge>
                  </td>
                  <td style={{ padding: '12px 16px', textAlign: 'right', color: 'var(--neutral-900)', fontWeight: '500' }}>
                    ${pedido.totalNeto.toLocaleString('es-CL', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                  </td>
                  <td style={{ padding: '12px 16px', color: 'var(--neutral-700)', fontSize: '13px' }}>
                    {new Date(pedido.fechaCreacion).toLocaleDateString('es-CL')}
                  </td>
                  <td style={{ padding: '12px 16px', textAlign: 'center' }}>
                    <div style={{ display: 'flex', gap: '8px', justifyContent: 'center' }}>
                      <button
                        onClick={() => navigate(`/pedidos/${pedido.idPedido}`)}
                        style={{
                          padding: '6px 12px',
                          backgroundColor: 'var(--primary)',
                          color: 'white',
                          border: 'none',
                          borderRadius: '4px',
                          cursor: 'pointer',
                          fontSize: '12px',
                        }}
                      >
                        Ver
                      </button>
                      <button
                        onClick={() => handleEliminar(pedido.idPedido)}
                        disabled={deletingId === pedido.idPedido}
                        style={{
                          padding: '6px 12px',
                          backgroundColor: deletingId === pedido.idPedido ? 'var(--neutral-300)' : 'var(--danger)',
                          color: 'white',
                          border: 'none',
                          borderRadius: '4px',
                          cursor: deletingId === pedido.idPedido ? 'not-allowed' : 'pointer',
                          fontSize: '12px',
                        }}
                      >
                        {deletingId === pedido.idPedido ? 'Eliminando...' : 'Eliminar'}
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
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
        Mostrando {filteredPedidos.length} de {pedidos.length} pedidos
      </div>
    </div>
  )
}
