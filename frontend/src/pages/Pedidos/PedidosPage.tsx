import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Card, Badge, LoadingSpinner } from '@/components/UI'
import { pedidosService } from '@/services'
import type { PedidoListaItem } from '@/types'

// ─── ESTADOS ─────────────────────────────────────────────────────────────────
// Valores internos que el backend puede devolver y cómo los mapeamos
const normalizarEstado = (estado?: string): string => {
  if (!estado) return 'PENDIENTE_CONFIRMACION'
  const e = estado.trim().toUpperCase()
  // Mapear estados legacy → estados actuales
  if (e === 'CREADO') return 'PENDIENTE_CONFIRMACION'
  if (e === 'APROBADO' || e === 'VALIDADO') return 'CONFIRMADO'
  if (e === 'ENTREGADO' || e === 'COMPLETADO') return 'COMPLETADO'
  if (e === 'CANCELADO') return 'CANCELADO'
  if (e === 'PENDIENTE_CONFIRMACION' || e === 'PENDIENTE') return 'PENDIENTE_CONFIRMACION'
  if (e === 'CONFIRMADO') return 'CONFIRMADO'
  return e
}

const formatearEstado = (estado: string): string => {
  const n = normalizarEstado(estado)
  const labels: Record<string, string> = {
    PENDIENTE_CONFIRMACION: 'Pendiente',
    CONFIRMADO: 'Confirmado',
    COMPLETADO: 'Completado',
    CANCELADO: 'Cancelado',
  }
  return labels[n] ?? n
}

// ─── NORMALIZADORES ───────────────────────────────────────────────────────────
// Extrae el estado de un item del listado (puede venir con distintos nombres)
const extraerEstado = (item: any): string =>
  normalizarEstado(
    item.estadoActual ??
    item.estado ??
    item.estadoPedido ??
    item.estado_actual
  )

// Extrae el total de un item
const extraerTotal = (item: any): number =>
  Number(
    item.totalNeto ??
    item.total ??
    item.montoTotal ??
    item.total_neto ??
    item.totalPedido ??
    0
  )

// Extrae el idPedido real (el backend puede devolverlo como id o idPedido)
const extraerId = (item: any): number =>
  Number(item.idPedido ?? item.id_pedido ?? item.id ?? 0)

const obtenerMensajeError = (error: any, fallback: string) => {
  const data = error?.response?.data;
  if (typeof data === 'string' && data.trim()) return data;
  if (data?.message) return data.message;
  if (data?.error) return data.error;
  return fallback;
}

type EstadoFilter = 'TODOS' | 'PENDIENTE_CONFIRMACION' | 'CONFIRMADO' | 'CANCELADO' | 'COMPLETADO'

export default function PedidosPage() {
  const navigate = useNavigate()
  const [pedidos, setPedidos] = useState<any[]>([])
  const [filteredPedidos, setFilteredPedidos] = useState<any[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [success, setSuccess] = useState<string | null>(null)
  const [estadoFilter, setEstadoFilter] = useState<EstadoFilter>('TODOS')
  const [searchTerm, setSearchTerm] = useState('')

  // Modal de confirmación cancelar
  const [pedidoACancelar, setPedidoACancelar] = useState<any | null>(null)
  const [cancelando, setCancelando] = useState(false)
  const [errorCancelar, setErrorCancelar] = useState<string | null>(null)

  useEffect(() => {
    cargarPedidos()
  }, [])

  useEffect(() => {
    let filtered = pedidos

    console.log('Estado filtro:', estadoFilter)
    console.log('Estados pedidos:', pedidos.map((p) => extraerEstado(p)))

    if (estadoFilter !== 'TODOS') {
      filtered = filtered.filter((p) => extraerEstado(p) === estadoFilter)
    }

    if (searchTerm) {
      const term = searchTerm.toLowerCase()
      const idBuscar = extraerId
      filtered = filtered.filter(
        (p) =>
          idBuscar(p).toString().includes(term) ||
          (p.codigoPedido ?? '').toLowerCase().includes(term)
      )
    }

    console.log('Pedidos filtrados:', filtered.length)
    setFilteredPedidos(filtered)
  }, [pedidos, estadoFilter, searchTerm])

  const cargarPedidos = async () => {
    try {
      setLoading(true)
      setError(null)
      const data = await pedidosService.listarPedidos()
      console.log('Pedidos recibidos raw:', data)
      const arr = Array.isArray(data) ? data : []
      console.log('Pedidos normalizados (estados):', arr.map((p: any) => ({
        id: extraerId(p),
        estadoRaw: p.estadoActual ?? p.estado ?? p.estadoPedido,
        estadoNorm: extraerEstado(p),
      })))
      setPedidos(arr)
    } catch (err: any) {
      console.error('Error cargando pedidos:', {
        status: err?.response?.status,
        data: err?.response?.data,
        message: err?.message,
        url: err?.config?.url,
      })
      setError(obtenerMensajeError(err, 'Error al cargar los pedidos. Intenta nuevamente.'))
    } finally {
      setLoading(false)
    }
  }

  // Navegar al detalle — usar el ID real del pedido
  const verPedido = (pedido: any) => {
    const id = extraerId(pedido)
    console.log('Pedido seleccionado para ver:', pedido)
    console.log('ID usado para navegar:', id)

    if (!id || isNaN(id) || id <= 0) {
      setError('ID de pedido inválido. No se puede abrir el detalle.')
      return
    }
    navigate(`/pedidos/${id}`)
  }

  // Modal cancelar (no eliminar físicamente)
  const abrirConfirmacionCancelar = (pedido: any) => {
    setPedidoACancelar(pedido)
    setErrorCancelar(null)
  }

  const cerrarConfirmacionCancelar = () => {
    setPedidoACancelar(pedidoACancelar) // Fix para evitar loop
    setPedidoACancelar(null)
    setErrorCancelar(null)
  }

  const confirmarCancelarPedido = async () => {
    if (!pedidoACancelar) return

    const id = extraerId(pedidoACancelar)
    console.log('Pedido seleccionado para cancelar:', pedidoACancelar)
    console.log('ID usado:', id)

    try {
      setCancelando(true)
      setErrorCancelar(null)

      // Cancelar = cambiar estado a CANCELADO (no eliminar físicamente)
      await pedidosService.cambiarEstadoPedido(id, {
        nuevoEstado: 'CANCELADO',
        observacion: 'Cancelado desde el panel de pedidos',
        usuarioResponsable: 'SISTEMA',
      } as any)

      await cargarPedidos()
      cerrarConfirmacionCancelar()
      const codigo = pedidoACancelar.codigoPedido || `#${id}`
      setSuccess(`Pedido ${codigo} cancelado correctamente.`)
      setTimeout(() => setSuccess(null), 4000)
    } catch (err: any) {
      console.error('Error cancelando pedido:', {
        status: err?.response?.status,
        data: err?.response?.data,
        message: err?.message,
        url: err?.config?.url,
        method: err?.config?.method,
      })
      setErrorCancelar(obtenerMensajeError(err, 'No se pudo cancelar el pedido. Intenta nuevamente.'))
    } finally {
      setCancelando(false)
    }
  }

  const getBadgeVariant = (estado: string) => {
    const n = normalizarEstado(estado)
    switch (n) {
      case 'PENDIENTE_CONFIRMACION': return 'warning' as const
      case 'CONFIRMADO': return 'success' as const
      case 'CANCELADO': return 'danger' as const
      case 'COMPLETADO': return 'default' as const
      default: return 'default' as const
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

      {/* Success */}
      {success && (
        <div
          style={{
            backgroundColor: '#dcfce7',
            color: '#166534',
            padding: '12px 16px',
            borderRadius: '6px',
            marginBottom: '20px',
            fontSize: '14px',
            border: '1px solid #86efac',
            display: 'flex',
            alignItems: 'center',
            gap: '8px',
          }}
        >
          ✅ {success}
        </div>
      )}

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

      {/* Filtros */}
      <Card title="Filtros" padding="16px" style={{ marginBottom: '20px' }}>
        <div style={{ display: 'flex', gap: '16px', flexWrap: 'wrap' }}>
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
              {/* Los values deben coincidir con lo que devuelve normalizarEstado() */}
              <option value="TODOS">Todos</option>
              <option value="PENDIENTE_CONFIRMACION">Pendiente</option>
              <option value="CONFIRMADO">Confirmado</option>
              <option value="CANCELADO">Cancelado</option>
              <option value="COMPLETADO">Completado</option>
            </select>
          </div>
        </div>
      </Card>

      {/* Tabla */}
      {filteredPedidos.length === 0 ? (
        <Card padding="40px" style={{ textAlign: 'center' }}>
          <p style={{ color: 'var(--neutral-500)', fontSize: '16px', margin: '0' }}>
            {pedidos.length === 0
              ? 'No hay pedidos registrados'
              : `No hay pedidos con estado "${formatearEstado(estadoFilter)}"`}
          </p>
        </Card>
      ) : (
        <Card padding="0">
          <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: '14px' }}>
            <thead>
              <tr style={{ borderBottom: '2px solid var(--neutral-200)', backgroundColor: 'var(--neutral-50)' }}>
                <th style={{ padding: '12px 16px', textAlign: 'left', fontWeight: '600', color: 'var(--neutral-700)' }}>ID</th>
                <th style={{ padding: '12px 16px', textAlign: 'left', fontWeight: '600', color: 'var(--neutral-700)' }}>Código</th>
                <th style={{ padding: '12px 16px', textAlign: 'left', fontWeight: '600', color: 'var(--neutral-700)' }}>Cliente</th>
                <th style={{ padding: '12px 16px', textAlign: 'left', fontWeight: '600', color: 'var(--neutral-700)' }}>Estado</th>
                <th style={{ padding: '12px 16px', textAlign: 'right', fontWeight: '600', color: 'var(--neutral-700)' }}>Monto</th>
                <th style={{ padding: '12px 16px', textAlign: 'left', fontWeight: '600', color: 'var(--neutral-700)' }}>Fecha</th>
                <th style={{ padding: '12px 16px', textAlign: 'center', fontWeight: '600', color: 'var(--neutral-700)' }}>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {filteredPedidos.map((pedido) => {
                const id = extraerId(pedido)
                const estado = extraerEstado(pedido)
                const total = extraerTotal(pedido)
                const fecha = pedido.fechaCreacion ?? pedido.fecha_creacion ?? pedido.fecha
                const cliente =
                  pedido.nombreCliente ??
                  pedido.clienteNombre ??
                  (pedido.cliente ? `${pedido.cliente.nombre ?? ''} ${pedido.cliente.apellido ?? ''}`.trim() : null) ??
                  `Cliente #${pedido.idCliente ?? id}`

                return (
                  <tr
                    key={id || pedido.codigoPedido}
                    style={{ borderBottom: '1px solid var(--neutral-200)' }}
                  >
                    <td style={{ padding: '12px 16px', color: 'var(--neutral-900)' }}>{id}</td>
                    <td style={{ padding: '12px 16px', color: 'var(--neutral-900)', fontWeight: '500' }}>
                      {pedido.codigoPedido ?? '-'}
                    </td>
                    <td style={{ padding: '12px 16px', color: 'var(--neutral-700)' }}>{cliente}</td>
                    <td style={{ padding: '12px 16px' }}>
                      <Badge variant={getBadgeVariant(estado)}>
                        {formatearEstado(estado)}
                      </Badge>
                    </td>
                    <td style={{ padding: '12px 16px', textAlign: 'right', color: 'var(--neutral-900)', fontWeight: '500' }}>
                      ${total.toLocaleString('es-CL', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                    </td>
                    <td style={{ padding: '12px 16px', color: 'var(--neutral-700)', fontSize: '13px' }}>
                      {fecha ? new Date(fecha).toLocaleDateString('es-CL') : '-'}
                    </td>
                    <td style={{ padding: '12px 16px', textAlign: 'center' }}>
                      <div style={{ display: 'flex', gap: '8px', justifyContent: 'center' }}>
                        <button
                          onClick={() => verPedido(pedido)}
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
                        {estado !== 'CANCELADO' && estado !== 'COMPLETADO' && (
                          <button
                            onClick={() => abrirConfirmacionCancelar(pedido)}
                            style={{
                              padding: '6px 12px',
                              backgroundColor: '#EF4444',
                              color: 'white',
                              border: 'none',
                              borderRadius: '4px',
                              cursor: 'pointer',
                              fontSize: '12px',
                            }}
                          >
                            Cancelar
                          </button>
                        )}
                      </div>
                    </td>
                  </tr>
                )
              })}
            </tbody>
          </table>
        </Card>
      )}

      {/* Footer */}
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

      {/* ===== MODAL CONFIRMACIÓN CANCELAR ===== */}
      {pedidoACancelar && (
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
          onClick={cerrarConfirmacionCancelar}
        >
          <div
            style={{
              backgroundColor: '#FFFFFF',
              borderRadius: '12px',
              padding: '32px',
              maxWidth: '440px',
              width: '90%',
              boxShadow: '0 25px 50px -12px rgba(0, 0, 0, 0.25)',
            }}
            onClick={(e) => e.stopPropagation()}
          >
            {/* Icono */}
            <div
              style={{
                width: '56px',
                height: '56px',
                borderRadius: '50%',
                backgroundColor: '#fee2e2',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                margin: '0 auto 20px',
                fontSize: '28px',
              }}
            >
              ⚠️
            </div>

            {/* Título */}
            <h2
              style={{
                margin: '0 0 12px 0',
                color: '#0D1B3D',
                fontSize: '20px',
                fontWeight: '700',
                textAlign: 'center',
              }}
            >
              Cancelar pedido
            </h2>

            {/* Mensaje */}
            <p
              style={{
                margin: '0 0 8px 0',
                color: '#374151',
                fontSize: '14px',
                textAlign: 'center',
                lineHeight: '1.5',
              }}
            >
              ¿Estás seguro de que deseas cancelar este pedido?
            </p>
            <p
              style={{
                margin: '0 0 20px 0',
                color: '#6B7280',
                fontSize: '14px',
                textAlign: 'center',
                lineHeight: '1.5',
              }}
            >
              Pedido:{' '}
              <strong style={{ color: '#0D1B3D' }}>
                {pedidoACancelar.codigoPedido || `#${extraerId(pedidoACancelar)}`}
              </strong>
              <br />
              El pedido quedará en estado <strong>CANCELADO</strong> y no se podrá reactivar.
            </p>

            {/* Error en modal */}
            {errorCancelar && (
              <div
                style={{
                  backgroundColor: '#fee2e2',
                  color: '#991b1b',
                  padding: '10px 14px',
                  borderRadius: '6px',
                  marginBottom: '20px',
                  fontSize: '13px',
                }}
              >
                ⚠️ {errorCancelar}
              </div>
            )}

            {/* Botones */}
            <div style={{ display: 'flex', gap: '12px' }}>
              <button
                type="button"
                onClick={cerrarConfirmacionCancelar}
                disabled={cancelando}
                style={{
                  flex: 1,
                  padding: '10px 16px',
                  backgroundColor: '#F5F7FA',
                  color: '#0D1B3D',
                  border: '1px solid #D1D5DB',
                  borderRadius: '8px',
                  cursor: cancelando ? 'not-allowed' : 'pointer',
                  fontSize: '14px',
                  fontWeight: '600',
                }}
              >
                Volver
              </button>
              <button
                type="button"
                onClick={confirmarCancelarPedido}
                disabled={cancelando}
                style={{
                  flex: 1,
                  padding: '10px 16px',
                  backgroundColor: cancelando ? '#fca5a5' : '#EF4444',
                  color: 'white',
                  border: 'none',
                  borderRadius: '8px',
                  cursor: cancelando ? 'not-allowed' : 'pointer',
                  fontSize: '14px',
                  fontWeight: '600',
                }}
              >
                {cancelando ? 'Cancelando...' : 'Confirmar cancelación'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
