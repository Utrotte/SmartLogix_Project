import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { Card } from '@/components/UI'
import { pedidosService, inventarioService } from '@/services'
import type { DetallePedido, DireccionEntrega, Pedido, Producto } from '@/types'

export default function CrearPedidoPage() {
  const navigate = useNavigate()
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [productos, setProductos] = useState<Producto[]>([])

  // Cargar productos al montar
  useEffect(() => {
    const cargarProductos = async () => {
      try {
        const data = await inventarioService.listarProductos()
        setProductos(data)
      } catch (err) {
        console.error('Error cargando productos:', err)
        setError('No se pudieron cargar los productos del inventario')
      }
    }
    cargarProductos()
  }, [])

  // Datos del cliente
  const [cliente, setCliente] = useState({
    nombre: '',
    apellido: '',
    correo: '',
    telefono: '',
    documento: '',
  })
  const [observacion, setObservacion] = useState('')

  // Dirección de entrega
  const [direccion, setDireccion] = useState<DireccionEntrega>({
    calle: '',
    numero: '',
    comuna: '',
    ciudad: '',
    region: '',
    codigoPostal: '',
    referencia: '',
  })

  // Productos
  const [detalles, setDetalles] = useState<DetallePedido[]>([
    { idProductoRef: 0, codigoSkuRef: '', nombreProductoSnapshot: '', cantidad: 1, precioUnitario: 0 },
  ])

  const handleAgregarProducto = () => {
    setDetalles([
      ...detalles,
      { idProductoRef: 0, codigoSkuRef: '', nombreProductoSnapshot: '', cantidad: 1, precioUnitario: 0 },
    ])
  }

  const handleRemoverProducto = (index: number) => {
    setDetalles(detalles.filter((_, i) => i !== index))
  }

  const handleProductoChange = (index: number, field: string, value: any) => {
    const newDetalles = [...detalles]
    newDetalles[index] = { ...newDetalles[index], [field]: value }
    setDetalles(newDetalles)
  }

  const handleClienteChange = (field: string, value: string) => {
    setCliente({ ...cliente, [field]: value })
  }

  const handleDireccionChange = (field: string, value: string) => {
    setDireccion({ ...direccion, [field]: value })
  }

  const calcularTotal = () => {
    return detalles.reduce((sum, detalle) => {
      return sum + detalle.cantidad * detalle.precioUnitario
    }, 0)
  }

  const validarFormulario = (): boolean => {
    if (!cliente.nombre || !cliente.apellido || !cliente.correo) {
      setError('Nombre, apellido y correo del cliente son requeridos')
      return false
    }

    if (detalles.length === 0) {
      setError('Debes agregar al menos un producto')
      return false
    }

    for (const detalle of detalles) {
      if (!detalle.idProductoRef || detalle.idProductoRef === 0) {
        setError('Todos los productos deben tener un ID de referencia')
        return false
      }
      if (detalle.cantidad <= 0) {
        setError('La cantidad debe ser mayor a 0')
        return false
      }
      if (detalle.precioUnitario < 0) {
        setError('El precio debe ser válido')
        return false
      }
    }

    if (!direccion.calle || !direccion.numero || !direccion.comuna || !direccion.ciudad || !direccion.region) {
      setError('Debes completar los datos básicos de dirección (calle, número, comuna, ciudad, región)')
      return false
    }

    return true
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError(null)

    if (!validarFormulario()) return

    try {
      setLoading(true)

      const totalBruto = calcularTotal()
      const nuevoPedido: Pedido = {
        cliente,
        canalOrigen: 'WEB',
        observacion,
        detalles: detalles.map((d) => ({
          ...d,
          idProductoRef: Number(d.idProductoRef),
          cantidad: Number(d.cantidad),
          precioUnitario: Number(d.precioUnitario),
        })),
        direccionEntrega: direccion,
        totalBruto: totalBruto,
        descuentoTotal: 0,
        totalNeto: totalBruto,
      }

      const response = await pedidosService.crearPedido(nuevoPedido)
      navigate(`/pedidos/${response.idPedido}`)
    } catch (err: any) {
      const mensaje = err?.response?.data?.mensaje || 'Error al crear el pedido'
      setError(mensaje)
      console.error('Error:', err)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div>
      {/* Header */}
      <div style={{ marginBottom: '30px' }}>
        <h1 style={{ margin: '0 0 10px 0', color: 'var(--neutral-900)' }}>
          Crear Nuevo Pedido
        </h1>
        <p style={{ margin: '0', color: 'var(--neutral-600)', fontSize: '16px' }}>
          Completa los datos del pedido
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

      <form onSubmit={handleSubmit}>
        {/* Datos del cliente */}
        <Card title="Datos del Cliente" padding="20px" style={{ marginBottom: '20px' }}>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: '20px' }}>
            <div>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', fontWeight: '600', color: 'var(--neutral-700)' }}>
                Nombre *
              </label>
              <input
                type="text"
                required
                value={cliente.nombre}
                onChange={(e) => handleClienteChange('nombre', e.target.value)}
                placeholder="Ej: Juan"
                style={{
                  width: '100%',
                  padding: '10px',
                  border: '1px solid var(--neutral-300)',
                  borderRadius: '6px',
                  fontSize: '14px',
                }}
              />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', fontWeight: '600', color: 'var(--neutral-700)' }}>
                Apellido *
              </label>
              <input
                type="text"
                required
                value={cliente.apellido}
                onChange={(e) => handleClienteChange('apellido', e.target.value)}
                placeholder="Ej: González"
                style={{
                  width: '100%',
                  padding: '10px',
                  border: '1px solid var(--neutral-300)',
                  borderRadius: '6px',
                  fontSize: '14px',
                }}
              />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', fontWeight: '600', color: 'var(--neutral-700)' }}>
                Correo *
              </label>
              <input
                type="email"
                required
                value={cliente.correo}
                onChange={(e) => handleClienteChange('correo', e.target.value)}
                placeholder="Ej: juan@test.cl"
                style={{
                  width: '100%',
                  padding: '10px',
                  border: '1px solid var(--neutral-300)',
                  borderRadius: '6px',
                  fontSize: '14px',
                }}
              />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', fontWeight: '600', color: 'var(--neutral-700)' }}>
                Teléfono (opcional)
              </label>
              <input
                type="text"
                value={cliente.telefono}
                onChange={(e) => handleClienteChange('telefono', e.target.value)}
                placeholder="Ej: 999999999"
                style={{
                  width: '100%',
                  padding: '10px',
                  border: '1px solid var(--neutral-300)',
                  borderRadius: '6px',
                  fontSize: '14px',
                }}
              />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', fontWeight: '600', color: 'var(--neutral-700)' }}>
                Documento (opcional)
              </label>
              <input
                type="text"
                value={cliente.documento}
                onChange={(e) => handleClienteChange('documento', e.target.value)}
                placeholder="Ej: 11111111-1"
                style={{
                  width: '100%',
                  padding: '10px',
                  border: '1px solid var(--neutral-300)',
                  borderRadius: '6px',
                  fontSize: '14px',
                }}
              />
            </div>
            <div style={{ gridColumn: '1 / -1' }}>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', fontWeight: '600', color: 'var(--neutral-700)' }}>
                Observación (opcional)
              </label>
              <textarea
                value={observacion}
                onChange={(e) => setObservacion(e.target.value)}
                placeholder="Notas especiales sobre el pedido"
                rows={3}
                style={{
                  width: '100%',
                  padding: '10px',
                  border: '1px solid var(--neutral-300)',
                  borderRadius: '6px',
                  fontSize: '14px',
                  fontFamily: 'inherit',
                }}
              />
            </div>
          </div>
        </Card>

        {/* Dirección de entrega */}
        <Card title="Dirección de Entrega" padding="20px" style={{ marginBottom: '20px' }}>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: '20px' }}>
            <div>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', fontWeight: '600', color: 'var(--neutral-700)' }}>
                Calle *
              </label>
              <input
                type="text"
                required
                value={direccion.calle}
                onChange={(e) => handleDireccionChange('calle', e.target.value)}
                placeholder="Ej: Av. Principal"
                style={{
                  width: '100%',
                  padding: '10px',
                  border: '1px solid var(--neutral-300)',
                  borderRadius: '6px',
                  fontSize: '14px',
                }}
              />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', fontWeight: '600', color: 'var(--neutral-700)' }}>
                Número *
              </label>
              <input
                type="text"
                required
                value={direccion.numero}
                onChange={(e) => handleDireccionChange('numero', e.target.value)}
                placeholder="Ej: 123"
                style={{
                  width: '100%',
                  padding: '10px',
                  border: '1px solid var(--neutral-300)',
                  borderRadius: '6px',
                  fontSize: '14px',
                }}
              />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', fontWeight: '600', color: 'var(--neutral-700)' }}>
                Comuna *
              </label>
              <input
                type="text"
                required
                value={direccion.comuna}
                onChange={(e) => handleDireccionChange('comuna', e.target.value)}
                placeholder="Ej: Santiago Centro"
                style={{
                  width: '100%',
                  padding: '10px',
                  border: '1px solid var(--neutral-300)',
                  borderRadius: '6px',
                  fontSize: '14px',
                }}
              />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', fontWeight: '600', color: 'var(--neutral-700)' }}>
                Ciudad *
              </label>
              <input
                type="text"
                required
                value={direccion.ciudad}
                onChange={(e) => handleDireccionChange('ciudad', e.target.value)}
                placeholder="Ej: Santiago"
                style={{
                  width: '100%',
                  padding: '10px',
                  border: '1px solid var(--neutral-300)',
                  borderRadius: '6px',
                  fontSize: '14px',
                }}
              />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', fontWeight: '600', color: 'var(--neutral-700)' }}>
                Región *
              </label>
              <input
                type="text"
                required
                value={direccion.region}
                onChange={(e) => handleDireccionChange('region', e.target.value)}
                placeholder="Ej: Metropolitana"
                style={{
                  width: '100%',
                  padding: '10px',
                  border: '1px solid var(--neutral-300)',
                  borderRadius: '6px',
                  fontSize: '14px',
                }}
              />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', fontWeight: '600', color: 'var(--neutral-700)' }}>
                Código Postal *
              </label>
              <input
                type="text"
                required
                value={direccion.codigoPostal}
                onChange={(e) => handleDireccionChange('codigoPostal', e.target.value)}
                placeholder="Ej: 8340000"
                style={{
                  width: '100%',
                  padding: '10px',
                  border: '1px solid var(--neutral-300)',
                  borderRadius: '6px',
                  fontSize: '14px',
                }}
              />
            </div>
            <div style={{ gridColumn: '1 / -1' }}>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', fontWeight: '600', color: 'var(--neutral-700)' }}>
                Referencia / Instrucciones (opcional)
              </label>
              <textarea
                value={direccion.referencia || ''}
                onChange={(e) => handleDireccionChange('referencia', e.target.value)}
                placeholder="Ej: Dejar en recepción, timbre 2 veces"
                rows={2}
                style={{
                  width: '100%',
                  padding: '10px',
                  border: '1px solid var(--neutral-300)',
                  borderRadius: '6px',
                  fontSize: '14px',
                  fontFamily: 'inherit',
                }}
              />
            </div>
          </div>
        </Card>

        {/* Productos */}
        <Card title="Productos" padding="20px" style={{ marginBottom: '20px' }}>
          <div style={{ overflowX: 'auto' }}>
            <table
              style={{
                width: '100%',
                borderCollapse: 'collapse',
                fontSize: '14px',
              }}
            >
              <thead>
                <tr style={{ borderBottom: '2px solid var(--neutral-200)' }}>
                  <th style={{ padding: '10px', textAlign: 'left', fontWeight: '600', color: 'var(--neutral-700)' }}>
                    ID Producto
                  </th>
                  <th style={{ padding: '10px', textAlign: 'left', fontWeight: '600', color: 'var(--neutral-700)' }}>
                    Nombre (opcional)
                  </th>
                  <th style={{ padding: '10px', textAlign: 'center', fontWeight: '600', color: 'var(--neutral-700)' }}>
                    Cantidad
                  </th>
                  <th style={{ padding: '10px', textAlign: 'right', fontWeight: '600', color: 'var(--neutral-700)' }}>
                    Precio Unitario
                  </th>
                  <th style={{ padding: '10px', textAlign: 'right', fontWeight: '600', color: 'var(--neutral-700)' }}>
                    Subtotal
                  </th>
                  <th style={{ padding: '10px', textAlign: 'center', fontWeight: '600', color: 'var(--neutral-700)' }}>
                    Acción
                  </th>
                </tr>
              </thead>
              <tbody>
                {detalles.map((detalle, index) => (
                  <tr key={index} style={{ borderBottom: '1px solid var(--neutral-200)' }}>
                    <td style={{ padding: '10px' }}>
                      <select
                        required
                        value={detalle.idProductoRef || ''}
                        onChange={(e) => {
                          const producto = productos.find((p) => p.idProducto === Number(e.target.value))
                          handleProductoChange(index, 'idProductoRef', Number(e.target.value))
                          if (producto) {
                            handleProductoChange(index, 'nombreProductoSnapshot', producto.nombre)
                            handleProductoChange(index, 'precioUnitario', (producto.precioReferencia || producto.precioUnitario || 0) as number)
                          }
                        }}
                        style={{
                          width: '100%',
                          padding: '8px',
                          border: '1px solid var(--neutral-300)',
                          borderRadius: '4px',
                          fontSize: '13px',
                        }}
                      >
                        <option value="">Seleccionar producto</option>
                        {productos.map((p) => (
                          <option key={p.idProducto} value={p.idProducto}>
                            {p.nombre}
                          </option>
                        ))}
                      </select>
                    </td>
                    <td style={{ padding: '10px' }}>
                      <input
                        type="text"
                        value={detalle.nombreProductoSnapshot || ''}
                        onChange={(e) => handleProductoChange(index, 'nombreProductoSnapshot', e.target.value)}
                        placeholder="Nombre (opcional)"
                        style={{
                          width: '100%',
                          padding: '8px',
                          border: '1px solid var(--neutral-300)',
                          borderRadius: '4px',
                          fontSize: '13px',
                        }}
                      />
                    </td>
                    <td style={{ padding: '10px', textAlign: 'center' }}>
                      <input
                        type="number"
                        required
                        min="1"
                        value={detalle.cantidad}
                        onChange={(e) => handleProductoChange(index, 'cantidad', Number(e.target.value))}
                        style={{
                          width: '80px',
                          padding: '8px',
                          border: '1px solid var(--neutral-300)',
                          borderRadius: '4px',
                          fontSize: '13px',
                          textAlign: 'center',
                        }}
                      />
                    </td>
                    <td style={{ padding: '10px', textAlign: 'right' }}>
                      <input
                        type="number"
                        required
                        min="0"
                        step="0.01"
                        value={detalle.precioUnitario}
                        onChange={(e) => handleProductoChange(index, 'precioUnitario', Number(e.target.value))}
                        placeholder="0.00"
                        style={{
                          width: '120px',
                          padding: '8px',
                          border: '1px solid var(--neutral-300)',
                          borderRadius: '4px',
                          fontSize: '13px',
                          textAlign: 'right',
                        }}
                      />
                    </td>
                    <td style={{ padding: '10px', textAlign: 'right', color: 'var(--neutral-900)', fontWeight: '500' }}>
                      ${(detalle.cantidad * detalle.precioUnitario).toLocaleString('es-CL', {
                        minimumFractionDigits: 2,
                        maximumFractionDigits: 2,
                      })}
                    </td>
                    <td style={{ padding: '10px', textAlign: 'center' }}>
                      <button
                        type="button"
                        onClick={() => handleRemoverProducto(index)}
                        style={{
                          padding: '6px 10px',
                          backgroundColor: 'var(--danger)',
                          color: 'white',
                          border: 'none',
                          borderRadius: '4px',
                          cursor: 'pointer',
                          fontSize: '12px',
                        }}
                      >
                        Quitar
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          {/* Botón agregar producto */}
          <button
            type="button"
            onClick={handleAgregarProducto}
            style={{
              marginTop: '16px',
              padding: '10px 16px',
              backgroundColor: 'var(--neutral-200)',
              color: 'var(--neutral-900)',
              border: 'none',
              borderRadius: '6px',
              cursor: 'pointer',
              fontSize: '14px',
              fontWeight: '600',
            }}
          >
            + Agregar Producto
          </button>
        </Card>

        {/* Total */}
        <Card padding="20px" style={{ marginBottom: '20px', backgroundColor: 'var(--primary-light)' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <span style={{ fontSize: '16px', fontWeight: '600', color: 'var(--neutral-900)' }}>
              Monto Total:
            </span>
            <span
              style={{
                fontSize: '24px',
                fontWeight: '700',
                color: 'var(--primary)',
              }}
            >
              ${calcularTotal().toLocaleString('es-CL', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
            </span>
          </div>
        </Card>

        {/* Botones */}
        <div style={{ display: 'flex', gap: '12px', justifyContent: 'flex-end' }}>
          <button
            type="button"
            onClick={() => navigate('/pedidos')}
            style={{
              padding: '10px 24px',
              backgroundColor: 'var(--neutral-200)',
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
            type="submit"
            disabled={loading}
            style={{
              padding: '10px 24px',
              backgroundColor: loading ? 'var(--neutral-300)' : 'var(--success)',
              color: 'white',
              border: 'none',
              borderRadius: '6px',
              cursor: loading ? 'not-allowed' : 'pointer',
              fontSize: '14px',
              fontWeight: '600',
              display: 'flex',
              alignItems: 'center',
              gap: '8px',
            }}
          >
            {loading ? (
              <>
                <span style={{ width: '14px', height: '14px', border: '2px solid rgba(255,255,255,.3)', borderTop: '2px solid white', borderRadius: '50%', animation: 'spin 1s linear infinite' }} />
                Creando...
              </>
            ) : (
              'Crear Pedido'
            )}
          </button>
        </div>
      </form>
    </div>
  )
}
