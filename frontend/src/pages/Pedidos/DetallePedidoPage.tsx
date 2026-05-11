import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { Card, Badge, LoadingSpinner } from '@/components/UI'
import { pedidosService, enviosService } from '@/services'
import type { PedidoResponse, EnvioResponse } from '@/types'
import CambiarEstadoPedidoModal from './CambiarEstadoPedidoModal'

// Normaliza un pedido para leer campos con distintos nombres
const normalizarPedido = (data: any): PedidoResponse => ({
  idPedido: Number(data.idPedido ?? data.id_pedido ?? data.id ?? 0),
  idCliente: Number(data.idCliente ?? data.id_cliente ?? 0),
  nombreCliente:
    data.nombreCliente ??
    data.clienteNombre ??
    (data.cliente
      ? `${data.cliente.nombre ?? ''} ${data.cliente.apellido ?? ''}`.trim()
      : null) ??
    `Cliente #${data.idCliente ?? ''}`,
  codigoPedido: data.codigoPedido ?? data.codigo_pedido ?? `PED-${data.idPedido}`,
  fechaCreacion: data.fechaCreacion ?? data.fecha_creacion ?? data.fecha ?? new Date().toISOString(),
  estadoActual:
    data.estadoActual ??
    data.estado ??
    data.estadoPedido ??
    data.estado_actual ??
    'PENDIENTE_CONFIRMACION',
  canalOrigen: data.canalOrigen ?? data.canal_origen ?? 'WEB',
  totalBruto: Number(data.totalBruto ?? data.total_bruto ?? data.totalNeto ?? data.total ?? 0),
  descuentoTotal: Number(data.descuentoTotal ?? data.descuento_total ?? 0),
  totalNeto: Number(data.totalNeto ?? data.total_neto ?? data.total ?? data.montoTotal ?? 0),
  observacion: data.observacion ?? null,
  detalles: Array.isArray(data.detalles)
    ? data.detalles
    : Array.isArray(data.detallePedido)
    ? data.detallePedido
    : [],
  direccionEntrega: data.direccionEntrega ?? data.direccion_entrega ?? data.direccion ?? null,
})

export default function DetallePedidoPage() {
  // La ruta es /pedidos/:idPedido — leer el param correcto
  const { idPedido: idPedidoParam } = useParams<{ idPedido: string }>()
  const navigate = useNavigate()

  const [pedido, setPedido] = useState<PedidoResponse | null>(null)
  const [envio, setEnvio] = useState<EnvioResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [success, setSuccess] = useState<string | null>(null)
  const [showModal, setShowModal] = useState(false)

  useEffect(() => {
    cargarDatos()
  }, [idPedidoParam])

  const cargarDatos = async () => {
    console.log('Params detalle:', { idPedidoParam })

    if (!idPedidoParam) {
      setError('No se proporcionó un ID de pedido válido.')
      setLoading(false)
      return
    }

    const idNum = Number(idPedidoParam)
    console.log('ID detalle parseado:', idNum)

    if (isNaN(idNum) || idNum <= 0) {
      setError(`ID de pedido inválido: "${idPedidoParam}"`)
      setLoading(false)
      return
    }

    try {
      setLoading(true)
      setError(null)

      console.log('Cargando pedido con ID:', idNum)

      // Cargar pedido
      const pedidoRaw = await pedidosService.obtenerPedido(idNum)
      console.log('Detalle pedido recibido (raw):', pedidoRaw)

      if (!pedidoRaw) {
        setError('No se encontró el pedido solicitado.')
        return
      }

      const pedidoNorm = normalizarPedido(pedidoRaw)
      console.log('Detalle pedido normalizado:', pedidoNorm)
      setPedido(pedidoNorm)

      // Intentar cargar envío — si falla no es error crítico
      try {
        const envioData = await enviosService.obtenerEnvioPorPedido(id)
        setEnvio(envioData)
      } catch (_envioErr) {
        console.log('No hay envío asociado a este pedido (OK)')
        setEnvio(null)
      }
    } catch (err: any) {
      console.error('Error cargando detalle del pedido:', {
        status: err?.response?.status,
        data: err?.response?.data,
        message: err?.message,
        url: err?.config?.url,
        method: err?.config?.method,
      })
      setError(
        err?.response?.data?.message ||
        err?.response?.data?.error ||
        (typeof err?.response?.data === 'string' ? err.response.data : null) ||
        (err?.response?.data ? JSON.stringify(err.response.data) : null) ||
        err?.message ||
        'No se pudieron cargar los detalles del pedido'
      )
    } finally {
      setLoading(false)
    }
  }

  const getEstadoBadgeColor = (estado: string) => {
    switch (estado) {
      case 'PENDIENTE_CONFIRMACION':
      case 'PENDIENTE':
      case 'CREADO':
        return 'warning' as const
      case 'CONFIRMADO':
        return 'success' as const
      case 'CANCELADO':
        return 'danger' as const
      case 'COMPLETADO':
        return 'default' as const
      default:
        return 'default' as const
    }
  }

  const getEstadoLabel = (estado: string) => {
    switch (estado) {
      case 'PENDIENTE_CONFIRMACION':
      case 'CREADO':
        return 'Pendiente'
      case 'PENDIENTE':
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
            Cargando detalles del pedido...
          </p>
        </div>
      </div>
    )
  }

  if (error || !pedido) {
    return (
      <div>
        <button
          onClick={() => navigate('/pedidos')}
          style={{
            padding: '8px 16px',
            backgroundColor: 'var(--neutral-200)',
            color: 'var(--neutral-900)',
            border: 'none',
            borderRadius: '6px',
            cursor: 'pointer',
            marginBottom: '20px',
          }}
        >
          ← Volver a Pedidos
        </button>
        <Card padding="40px" style={{ textAlign: 'center' }}>
          <p style={{ color: 'var(--danger)', fontSize: '16px', margin: '0' }}>
            {error || 'Pedido no encontrado'}
          </p>
        </Card>
      </div>
    )
  }

  // Calcular total desde detalles si viene en 0
  const calcTotalDeDetalles = () =>
    (pedido.detalles || []).reduce((acc: number, d: any) => {
      const sub =
        Number(d.subtotal ?? d.subTotal ?? 0) ||
        Number(d.precioUnitario ?? 0) * Number(d.cantidad ?? 0)
      return acc + sub
    }, 0)

  const totalMostrar = pedido.totalNeto > 0 ? pedido.totalNeto : calcTotalDeDetalles()

  return (
    <div>
      {/* Header */}
      <div style={{ marginBottom: '30px', display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
        <div>
          <button
            onClick={() => navigate('/pedidos')}
            style={{
              padding: '8px 16px',
              backgroundColor: 'var(--neutral-200)',
              color: 'var(--neutral-900)',
              border: 'none',
              borderRadius: '6px',
              cursor: 'pointer',
              marginBottom: '16px',
              fontSize: '14px',
            }}
          >
            ← Volver a Pedidos
          </button>
          <h1 style={{ margin: '0 0 10px 0', color: 'var(--neutral-900)' }}>
            Pedido {pedido.codigoPedido}
          </h1>
          <p style={{ margin: '0', color: 'var(--neutral-600)', fontSize: '16px' }}>
            ID: {pedido.idPedido}
          </p>
        </div>
        <div style={{ display: 'flex', gap: '12px' }}>
          <button
            onClick={() => setShowModal(true)}
            style={{
              padding: '10px 16px',
              backgroundColor: '#0066CC',
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
          {pedido.estadoActual === 'CONFIRMADO' && !envio && (
            <button
              onClick={() => navigate(`/envios/crear?idPedido=${pedido.idPedido}`)}
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
              📦 Crear Envío
            </button>
          )}
        </div>
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
          }}
        >
          ✅ {success}
        </div>
      )}

      {/* Información principal */}
      <Card title="Información del Pedido" padding="20px" style={{ marginBottom: '20px' }}>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: '20px' }}>
          <div>
            <label style={{ fontSize: '12px', color: 'var(--neutral-600)', textTransform: 'uppercase', fontWeight: '600' }}>
              Estado Actual
            </label>
            <div style={{ marginTop: '8px' }}>
              <Badge variant={getEstadoBadgeColor(pedido.estadoActual)}>
                {getEstadoLabel(pedido.estadoActual)}
              </Badge>
            </div>
          </div>

          <div>
            <label style={{ fontSize: '12px', color: 'var(--neutral-600)', textTransform: 'uppercase', fontWeight: '600' }}>
              Cliente
            </label>
            <p style={{ margin: '8px 0 0 0', fontSize: '16px', fontWeight: '500', color: 'var(--neutral-900)' }}>
              {pedido.nombreCliente || `ID: ${pedido.idCliente}`}
            </p>
          </div>

          <div>
            <label style={{ fontSize: '12px', color: 'var(--neutral-600)', textTransform: 'uppercase', fontWeight: '600' }}>
              Fecha de Creación
            </label>
            <p style={{ margin: '8px 0 0 0', fontSize: '16px', color: 'var(--neutral-900)' }}>
              {new Date(pedido.fechaCreacion).toLocaleDateString('es-CL', {
                year: 'numeric',
                month: 'long',
                day: 'numeric',
              })}
            </p>
          </div>

          <div>
            <label style={{ fontSize: '12px', color: 'var(--neutral-600)', textTransform: 'uppercase', fontWeight: '600' }}>
              Monto Total
            </label>
            <p style={{ margin: '8px 0 0 0', fontSize: '20px', fontWeight: '700', color: 'var(--primary)' }}>
              ${totalMostrar.toLocaleString('es-CL', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
            </p>
          </div>

          {pedido.observacion && (
            <div style={{ gridColumn: '1 / -1' }}>
              <label style={{ fontSize: '12px', color: 'var(--neutral-600)', textTransform: 'uppercase', fontWeight: '600' }}>
                Observación
              </label>
              <p style={{ margin: '8px 0 0 0', fontSize: '14px', color: 'var(--neutral-700)' }}>
                {pedido.observacion}
              </p>
            </div>
          )}
        </div>
      </Card>

      {/* Dirección de entrega */}
      {pedido.direccionEntrega && (
        <Card title="Dirección de Entrega" padding="20px" style={{ marginBottom: '20px' }}>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: '16px' }}>
            <div>
              <label style={{ fontSize: '12px', color: 'var(--neutral-600)', fontWeight: '600' }}>
                Calle
              </label>
              <p style={{ margin: '4px 0 0 0', fontSize: '14px', color: 'var(--neutral-900)' }}>
                {pedido.direccionEntrega.calle} {pedido.direccionEntrega.numero}
              </p>
            </div>
            <div>
              <label style={{ fontSize: '12px', color: 'var(--neutral-600)', fontWeight: '600' }}>
                Ciudad
              </label>
              <p style={{ margin: '4px 0 0 0', fontSize: '14px', color: 'var(--neutral-900)' }}>
                {pedido.direccionEntrega.ciudad}
              </p>
            </div>
            <div>
              <label style={{ fontSize: '12px', color: 'var(--neutral-600)', fontWeight: '600' }}>
                Región
              </label>
              <p style={{ margin: '4px 0 0 0', fontSize: '14px', color: 'var(--neutral-900)' }}>
                {pedido.direccionEntrega.region}
              </p>
            </div>
            <div>
              <label style={{ fontSize: '12px', color: 'var(--neutral-600)', fontWeight: '600' }}>
                Código Postal
              </label>
              <p style={{ margin: '4px 0 0 0', fontSize: '14px', color: 'var(--neutral-900)' }}>
                {pedido.direccionEntrega.codigoPostal}
              </p>
            </div>
            {pedido.direccionEntrega.referencia && (
              <div style={{ gridColumn: '1 / -1' }}>
                <label style={{ fontSize: '12px', color: 'var(--neutral-600)', fontWeight: '600' }}>
                  Referencia
                </label>
                <p style={{ margin: '4px 0 0 0', fontSize: '14px', color: 'var(--neutral-900)' }}>
                  {pedido.direccionEntrega.referencia}
                </p>
              </div>
            )}
          </div>
        </Card>
      )}

      {/* Productos */}
      {pedido.detalles && pedido.detalles.length > 0 ? (
        <Card title="Productos" padding="0" style={{ marginBottom: '20px' }}>
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
                  ID Producto
                </th>
                <th style={{ padding: '12px 16px', textAlign: 'left', fontWeight: '600', color: 'var(--neutral-700)' }}>
                  Nombre
                </th>
                <th style={{ padding: '12px 16px', textAlign: 'center', fontWeight: '600', color: 'var(--neutral-700)' }}>
                  Cantidad
                </th>
                <th style={{ padding: '12px 16px', textAlign: 'right', fontWeight: '600', color: 'var(--neutral-700)' }}>
                  Precio Unitario
                </th>
                <th style={{ padding: '12px 16px', textAlign: 'right', fontWeight: '600', color: 'var(--neutral-700)' }}>
                  Subtotal
                </th>
              </tr>
            </thead>
            <tbody>
              {pedido.detalles.map((detalle: any, index: number) => {
                const precio = Number(detalle.precioUnitario ?? detalle.precio_unitario ?? 0)
                const cantidad = Number(detalle.cantidad ?? 0)
                const subtotal = Number(detalle.subtotal ?? detalle.subTotal ?? 0) || precio * cantidad
                return (
                  <tr key={detalle.idDetalle ?? index} style={{ borderBottom: '1px solid var(--neutral-200)' }}>
                    <td style={{ padding: '12px 16px', color: 'var(--neutral-900)' }}>
                      {detalle.idProductoRef ?? detalle.idProducto ?? '-'}
                    </td>
                    <td style={{ padding: '12px 16px', color: 'var(--neutral-900)' }}>
                      {detalle.nombreProductoSnapshot ?? detalle.nombreProducto ?? 'N/A'}
                    </td>
                    <td style={{ padding: '12px 16px', textAlign: 'center', color: 'var(--neutral-900)' }}>
                      {cantidad}
                    </td>
                    <td style={{ padding: '12px 16px', textAlign: 'right', color: 'var(--neutral-900)' }}>
                      ${precio.toLocaleString('es-CL', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                    </td>
                    <td style={{ padding: '12px 16px', textAlign: 'right', color: 'var(--neutral-900)', fontWeight: '500' }}>
                      ${subtotal.toLocaleString('es-CL', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                    </td>
                  </tr>
                )
              })}
            </tbody>
          </table>
        </Card>
      ) : (
        <Card title="Productos" padding="24px" style={{ marginBottom: '20px' }}>
          <p style={{ color: 'var(--neutral-500)', margin: 0 }}>Sin detalles de productos.</p>
        </Card>
      )}

      {/* Envío asociado */}
      {envio && (
        <Card title="Envío Asociado" padding="20px" style={{ marginBottom: '20px', backgroundColor: '#f0f9ff' }}>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: '16px' }}>
            <div>
              <label style={{ fontSize: '12px', color: 'var(--neutral-600)', fontWeight: '600' }}>
                ID Envío
              </label>
              <p style={{ margin: '4px 0 0 0', fontSize: '14px', color: 'var(--neutral-900)', fontWeight: '500' }}>
                {envio.idEnvio}
              </p>
            </div>
            <div>
              <label style={{ fontSize: '12px', color: 'var(--neutral-600)', fontWeight: '600' }}>
                Estado
              </label>
              <div style={{ marginTop: '4px' }}>
                <Badge variant={getEstadoBadgeColor(envio.estado)}>
                  {getEstadoLabel(envio.estado)}
                </Badge>
              </div>
            </div>
            <div style={{ gridColumn: '1 / -1' }}>
              <button
                onClick={() => navigate(`/envios/${envio.idEnvio}`)}
                style={{
                  padding: '8px 16px',
                  backgroundColor: 'var(--primary)',
                  color: 'white',
                  border: 'none',
                  borderRadius: '6px',
                  cursor: 'pointer',
                  fontSize: '14px',
                  fontWeight: '600',
                }}
              >
                Ver Detalles del Envío
              </button>
            </div>
          </div>
        </Card>
      )}

      {/* Modal cambiar estado */}
      {showModal && (
        <CambiarEstadoPedidoModal
          idPedido={pedido.idPedido}
          estadoActual={pedido.estadoActual}
          onClose={() => setShowModal(false)}
          onSuccess={() => {
            cargarDatos()
            setSuccess('Estado actualizado correctamente.')
            setTimeout(() => setSuccess(null), 4000)
          }}
        />
      )}
    </div>
  )
}
