import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { Card, Badge, LoadingSpinner } from '@/components/UI'
import { pedidosService, enviosService } from '@/services'
import type { PedidoResponse, EnvioResponse } from '@/types'
import CambiarEstadoPedidoModal from './CambiarEstadoPedidoModal'

export default function DetallePedidoPage() {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()

  const [pedido, setPedido] = useState<PedidoResponse | null>(null)
  const [envio, setEnvio] = useState<EnvioResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [showModal, setShowModal] = useState(false)
  const [creatingShipment, setCreatingShipment] = useState(false)

  useEffect(() => {
    cargarDatos()
  }, [id])

  const cargarDatos = async () => {
    if (!id) return

    try {
      setLoading(true)
      setError(null)

      // Cargar pedido
      const pedidoData = await pedidosService.obtenerPedido(Number(id))
      setPedido(pedidoData)

      // Intentar cargar envío si existe
      try {
        const envioData = await enviosService.obtenerEnvioPorPedido(id)
        setEnvio(envioData)
      } catch (err) {
        // El envío podría no existir, no es un error crítico
        console.log('No hay envío asociado a este pedido')
      }
    } catch (err) {
      setError('Error al cargar los datos del pedido')
      console.error('Error:', err)
    } finally {
      setLoading(false)
    }
  }

  const handleCrearEnvio = async () => {
    if (!pedido) return

    try {
      setCreatingShipment(true)
      // Aquí iría la lógica para crear envío
      // Por ahora solo mostramos un mensaje
      alert('Redirecciona a crear envío')
      // navigate(`/envios/crear?idPedido=${pedido.idPedido}`)
    } catch (err) {
      console.error('Error:', err)
    } finally {
      setCreatingShipment(false)
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
            Pedido {pedido.numeroReferencia}
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
              backgroundColor: 'var(--info)',
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
              onClick={handleCrearEnvio}
              disabled={creatingShipment}
              style={{
                padding: '10px 16px',
                backgroundColor: creatingShipment ? 'var(--neutral-300)' : 'var(--success)',
                color: 'white',
                border: 'none',
                borderRadius: '6px',
                cursor: creatingShipment ? 'not-allowed' : 'pointer',
                fontSize: '14px',
                fontWeight: '600',
              }}
            >
              {creatingShipment ? 'Creando...' : '📦 Crear Envío'}
            </button>
          )}
        </div>
      </div>

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
              ID Cliente
            </label>
            <p style={{ margin: '8px 0 0 0', fontSize: '16px', fontWeight: '500', color: 'var(--neutral-900)' }}>
              {pedido.idCliente}
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
              ${pedido.montoTotal.toLocaleString('es-CL', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
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
                {pedido.direccionEntrega.calle}
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
            {pedido.direccionEntrega.instruccionesEspeciales && (
              <div style={{ gridColumn: '1 / -1' }}>
                <label style={{ fontSize: '12px', color: 'var(--neutral-600)', fontWeight: '600' }}>
                  Instrucciones Especiales
                </label>
                <p style={{ margin: '4px 0 0 0', fontSize: '14px', color: 'var(--neutral-900)' }}>
                  {pedido.direccionEntrega.instruccionesEspeciales}
                </p>
              </div>
            )}
          </div>
        </Card>
      )}

      {/* Productos */}
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
            {pedido.detalles.map((detalle, index) => (
              <tr key={index} style={{ borderBottom: '1px solid var(--neutral-200)' }}>
                <td style={{ padding: '12px 16px', color: 'var(--neutral-900)' }}>
                  {detalle.idProducto}
                </td>
                <td style={{ padding: '12px 16px', color: 'var(--neutral-900)' }}>
                  {detalle.nombreProducto || 'N/A'}
                </td>
                <td style={{ padding: '12px 16px', textAlign: 'center', color: 'var(--neutral-900)' }}>
                  {detalle.cantidad}
                </td>
                <td style={{ padding: '12px 16px', textAlign: 'right', color: 'var(--neutral-900)' }}>
                  ${detalle.precioUnitario.toLocaleString('es-CL', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                </td>
                <td style={{ padding: '12px 16px', textAlign: 'right', color: 'var(--neutral-900)', fontWeight: '500' }}>
                  ${(detalle.cantidad * detalle.precioUnitario).toLocaleString('es-CL', {
                    minimumFractionDigits: 2,
                    maximumFractionDigits: 2,
                  })}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </Card>

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
          onSuccess={cargarDatos}
        />
      )}
    </div>
  )
}
