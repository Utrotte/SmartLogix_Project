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

  // Detalles del pedido — idProductoRef usa string para el select controlado
  const [detalles, setDetalles] = useState<DetallePedido[]>([
    { idProductoRef: 0, codigoSkuRef: '', nombreProductoSnapshot: '', cantidad: 1, precioUnitario: 0, subtotal: 0 },
  ])

  // ——— FUNCIÓN CLAVE: seleccionar producto en un detalle ———
  // Se actualiza todo el detalle en un solo setDetalles para evitar
  // que llamadas múltiples se pisen entre sí (stale closure bug).
  const seleccionarProducto = (index: number, value: string) => {
    const idProducto = Number(value)

    if (!idProducto) {
      setDetalles((prev) =>
        prev.map((d, i) =>
          i === index
            ? { ...d, idProductoRef: 0, codigoSkuRef: '', nombreProductoSnapshot: '', precioUnitario: 0, subtotal: 0 }
            : d
        )
      )
      return
    }

    const prod = productos.find((p) => Number(p.idProducto) === idProducto)
    if (!prod) {
      console.error('Producto no encontrado para ID:', idProducto)
      return
    }

    console.log('Producto seleccionado:', prod)

    setDetalles((prev) =>
      prev.map((d, i) => {
        if (i !== index) return d
        const cantidad = Number(d.cantidad) > 0 ? Number(d.cantidad) : 1
        const precioUnitario = Number(prod.precioReferencia || prod.precioUnitario || 0)
        return {
          ...d,
          idProductoRef: prod.idProducto,
          codigoSkuRef: prod.codigoSku || prod.sku || '',
          nombreProductoSnapshot: prod.nombre,
          precioUnitario,
          subtotal: cantidad * precioUnitario,
        }
      })
    )
  }

  // ——— Cambiar cantidad recalcula subtotal ———
  const cambiarCantidad = (index: number, nuevaCantidad: number) => {
    setDetalles((prev) =>
      prev.map((d, i) => {
        if (i !== index) return d
        const cantidad = nuevaCantidad > 0 ? nuevaCantidad : 1
        const precioUnitario = Number(d.precioUnitario) || 0
        return { ...d, cantidad, subtotal: cantidad * precioUnitario }
      })
    )
  }

  // ——— Cambiar precio unitario manualmente ———
  const cambiarPrecio = (index: number, nuevoPrecio: number) => {
    setDetalles((prev) =>
      prev.map((d, i) => {
        if (i !== index) return d
        const precio = nuevoPrecio >= 0 ? nuevoPrecio : 0
        const cantidad = Number(d.cantidad) > 0 ? Number(d.cantidad) : 1
        return { ...d, precioUnitario: precio, subtotal: cantidad * precio }
      })
    )
  }

  const handleAgregarProducto = () => {
    setDetalles((prev) => [
      ...prev,
      { idProductoRef: 0, codigoSkuRef: '', nombreProductoSnapshot: '', cantidad: 1, precioUnitario: 0, subtotal: 0 },
    ])
  }

  const handleRemoverProducto = (index: number) => {
    setDetalles((prev) => {
      const nuevos = prev.filter((_, i) => i !== index)
      // Si quedan vacíos, dejar al menos una línea
      return nuevos.length > 0
        ? nuevos
        : [{ idProductoRef: 0, codigoSkuRef: '', nombreProductoSnapshot: '', cantidad: 1, precioUnitario: 0, subtotal: 0 }]
    })
  }

  const handleClienteChange = (field: string, value: string) => {
    setCliente((prev) => ({ ...prev, [field]: value }))
  }

  const handleDireccionChange = (field: string, value: string) => {
    setDireccion((prev) => ({ ...prev, [field]: value }))
  }

  // Monto total calculado desde los subtotales
  const calcularTotal = () =>
    detalles.reduce((sum, d) => sum + (Number(d.subtotal) || 0), 0)

  const validarFormulario = (): boolean => {
    if (!cliente.nombre || !cliente.apellido || !cliente.correo) {
      setError('Nombre, apellido y correo del cliente son requeridos')
      return false
    }

    if (detalles.length === 0) {
      setError('Debes agregar al menos un producto')
      return false
    }

    console.log('Detalles antes de validar:', detalles)

    for (const detalle of detalles) {
      if (!detalle.idProductoRef || Number(detalle.idProductoRef) === 0) {
        setError('Todos los productos deben tener un producto seleccionado')
        return false
      }
      if (!detalle.cantidad || Number(detalle.cantidad) <= 0) {
        setError('La cantidad debe ser mayor a 0')
        return false
      }
      if (Number(detalle.precioUnitario) < 0) {
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

    setLoading(true)

    try {
      const totalBruto = calcularTotal()

      const pedidoRequest: Pedido = {
        cliente,
        canalOrigen: 'WEB',
        observacion,
        detalles: detalles.map((d) => ({
          idProductoRef: Number(d.idProductoRef),
          codigoSkuRef: d.codigoSkuRef || '',
          nombreProductoSnapshot: d.nombreProductoSnapshot || '',
          cantidad: Number(d.cantidad),
          precioUnitario: Number(d.precioUnitario),
          subtotal: Number(d.subtotal),
        })),
        direccionEntrega: direccion,
        totalBruto,
        descuentoTotal: 0,
        totalNeto: totalBruto,
      }

      console.log('Cliente formulario:', cliente)
      console.log('Dirección formulario:', direccion)
      console.log('Detalles formulario:', detalles)
      console.log('Monto total:', totalBruto)
      console.log('Request final crear pedido:', JSON.stringify(pedidoRequest, null, 2))

      const response = await pedidosService.crearPedido(pedidoRequest)

      console.log('Respuesta crear pedido:', response)

      const idCreado =
        response?.idPedido ??
        (response as any)?.data?.idPedido ??
        null

      if (idCreado) {
        navigate(`/pedidos/${idCreado}`)
      } else {
        // Pedido creado pero sin ID en respuesta → volver a lista
        navigate('/pedidos')
      }
    } catch (err: any) {
      console.error('Error al crear pedido:', {
        status: err?.response?.status,
        data: err?.response?.data,
        message: err?.message,
        url: err?.config?.url,
        method: err?.config?.method,
      })
      const mensaje =
        err?.response?.data?.message ||
        err?.response?.data?.mensaje ||
        err?.response?.data?.error ||
        (typeof err?.response?.data === 'string' ? err.response.data : null) ||
        (err?.response?.data ? JSON.stringify(err.response.data) : null) ||
        err?.message ||
        'Error al crear el pedido'
      setError(mensaje)
    } finally {
      setLoading(false)
    }

  }


  const inputStyle = {
    width: '100%',
    padding: '10px',
    border: '1px solid var(--neutral-300)',
    borderRadius: '6px',
    fontSize: '14px',
    boxSizing: 'border-box' as const,
  }

  return (
    <div>
      {/* Header */}
      <div style={{ marginBottom: '30px' }}>
        <h1 style={{ margin: '0 0 10px 0', color: 'var(--neutral-900)' }}>Crear Nuevo Pedido</h1>
        <p style={{ margin: '0', color: 'var(--neutral-600)', fontSize: '16px' }}>
          Completa los datos del pedido
        </p>
      </div>

      {/* Error */}
      {error && (
        <div style={{ backgroundColor: '#fee2e2', color: '#991b1b', padding: '12px 16px', borderRadius: '6px', marginBottom: '20px', fontSize: '14px' }}>
          ⚠️ {error}
        </div>
      )}

      <form onSubmit={handleSubmit}>
        {/* Datos del cliente */}
        <Card title="Datos del Cliente" padding="20px" style={{ marginBottom: '20px' }}>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: '20px' }}>
            <div>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', fontWeight: '600', color: 'var(--neutral-700)' }}>Nombre *</label>
              <input type="text" required value={cliente.nombre} onChange={(e) => handleClienteChange('nombre', e.target.value)} placeholder="Ej: Juan" style={inputStyle} />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', fontWeight: '600', color: 'var(--neutral-700)' }}>Apellido *</label>
              <input type="text" required value={cliente.apellido} onChange={(e) => handleClienteChange('apellido', e.target.value)} placeholder="Ej: González" style={inputStyle} />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', fontWeight: '600', color: 'var(--neutral-700)' }}>Correo *</label>
              <input type="email" required value={cliente.correo} onChange={(e) => handleClienteChange('correo', e.target.value)} placeholder="Ej: juan@test.cl" style={inputStyle} />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', fontWeight: '600', color: 'var(--neutral-700)' }}>Teléfono (opcional)</label>
              <input type="text" value={cliente.telefono} onChange={(e) => handleClienteChange('telefono', e.target.value)} placeholder="Ej: 999999999" style={inputStyle} />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', fontWeight: '600', color: 'var(--neutral-700)' }}>Documento (opcional)</label>
              <input type="text" value={cliente.documento} onChange={(e) => handleClienteChange('documento', e.target.value)} placeholder="Ej: 11111111-1" style={inputStyle} />
            </div>
            <div style={{ gridColumn: '1 / -1' }}>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', fontWeight: '600', color: 'var(--neutral-700)' }}>Observación (opcional)</label>
              <textarea value={observacion} onChange={(e) => setObservacion(e.target.value)} placeholder="Notas especiales sobre el pedido" rows={3} style={{ ...inputStyle, fontFamily: 'inherit' }} />
            </div>
          </div>
        </Card>

        {/* Dirección de entrega */}
        <Card title="Dirección de Entrega" padding="20px" style={{ marginBottom: '20px' }}>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: '20px' }}>
            <div>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', fontWeight: '600', color: 'var(--neutral-700)' }}>Calle *</label>
              <input type="text" required value={direccion.calle} onChange={(e) => handleDireccionChange('calle', e.target.value)} placeholder="Ej: Av. Principal" style={inputStyle} />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', fontWeight: '600', color: 'var(--neutral-700)' }}>Número *</label>
              <input type="text" required value={direccion.numero} onChange={(e) => handleDireccionChange('numero', e.target.value)} placeholder="Ej: 123" style={inputStyle} />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', fontWeight: '600', color: 'var(--neutral-700)' }}>Comuna *</label>
              <input type="text" required value={direccion.comuna} onChange={(e) => handleDireccionChange('comuna', e.target.value)} placeholder="Ej: Santiago Centro" style={inputStyle} />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', fontWeight: '600', color: 'var(--neutral-700)' }}>Ciudad *</label>
              <input type="text" required value={direccion.ciudad} onChange={(e) => handleDireccionChange('ciudad', e.target.value)} placeholder="Ej: Santiago" style={inputStyle} />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', fontWeight: '600', color: 'var(--neutral-700)' }}>Región *</label>
              <input type="text" required value={direccion.region} onChange={(e) => handleDireccionChange('region', e.target.value)} placeholder="Ej: Metropolitana" style={inputStyle} />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', fontWeight: '600', color: 'var(--neutral-700)' }}>Código Postal *</label>
              <input type="text" required value={direccion.codigoPostal} onChange={(e) => handleDireccionChange('codigoPostal', e.target.value)} placeholder="Ej: 8340000" style={inputStyle} />
            </div>
            <div style={{ gridColumn: '1 / -1' }}>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', fontWeight: '600', color: 'var(--neutral-700)' }}>Referencia / Instrucciones (opcional)</label>
              <textarea value={direccion.referencia || ''} onChange={(e) => handleDireccionChange('referencia', e.target.value)} placeholder="Ej: Dejar en recepción, timbre 2 veces" rows={2} style={{ ...inputStyle, fontFamily: 'inherit' }} />
            </div>
          </div>
        </Card>

        {/* Productos */}
        <Card title="Productos" padding="20px" style={{ marginBottom: '20px' }}>
          <div style={{ overflowX: 'auto' }}>
            <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: '14px' }}>
              <thead>
                <tr style={{ borderBottom: '2px solid var(--neutral-200)' }}>
                  <th style={{ padding: '10px', textAlign: 'left', fontWeight: '600', color: 'var(--neutral-700)', minWidth: '200px' }}>Producto</th>
                  <th style={{ padding: '10px', textAlign: 'center', fontWeight: '600', color: 'var(--neutral-700)', width: '90px' }}>Cantidad</th>
                  <th style={{ padding: '10px', textAlign: 'right', fontWeight: '600', color: 'var(--neutral-700)', width: '130px' }}>Precio Unit.</th>
                  <th style={{ padding: '10px', textAlign: 'right', fontWeight: '600', color: 'var(--neutral-700)', width: '130px' }}>Subtotal</th>
                  <th style={{ padding: '10px', textAlign: 'center', fontWeight: '600', color: 'var(--neutral-700)', width: '80px' }}>Acción</th>
                </tr>
              </thead>
              <tbody>
                {detalles.map((detalle, index) => (
                  <tr key={index} style={{ borderBottom: '1px solid var(--neutral-200)' }}>
                    <td style={{ padding: '10px' }}>
                      {/*
                        CORRECCIÓN CLAVE:
                        - value usa String(idProductoRef) para evitar mismatch number vs string
                        - option también usa String(p.idProducto)
                        - seleccionarProducto actualiza todo el detalle en un solo setDetalles
                      */}
                      <select
                        value={detalle.idProductoRef ? String(detalle.idProductoRef) : ''}
                        onChange={(e) => seleccionarProducto(index, e.target.value)}
                        style={{
                          width: '100%',
                          padding: '8px',
                          border: '1px solid var(--neutral-300)',
                          borderRadius: '4px',
                          fontSize: '13px',
                          backgroundColor: 'white',
                        }}
                      >
                        <option value="">Seleccionar producto</option>
                        {productos.map((p) => (
                          <option key={p.idProducto} value={String(p.idProducto)}>
                            {p.nombre} {p.codigoSku ? `- ${p.codigoSku}` : ''}
                          </option>
                        ))}
                      </select>
                    </td>
                    <td style={{ padding: '10px', textAlign: 'center' }}>
                      <input
                        type="number"
                        min="1"
                        value={detalle.cantidad}
                        onChange={(e) => cambiarCantidad(index, Number(e.target.value))}
                        style={{ width: '70px', padding: '8px', border: '1px solid var(--neutral-300)', borderRadius: '4px', fontSize: '13px', textAlign: 'center' }}
                      />
                    </td>
                    <td style={{ padding: '10px', textAlign: 'right' }}>
                      <input
                        type="number"
                        min="0"
                        step="0.01"
                        value={detalle.precioUnitario}
                        onChange={(e) => cambiarPrecio(index, Number(e.target.value))}
                        style={{ width: '110px', padding: '8px', border: '1px solid var(--neutral-300)', borderRadius: '4px', fontSize: '13px', textAlign: 'right' }}
                      />
                    </td>
                    <td style={{ padding: '10px', textAlign: 'right', color: 'var(--neutral-900)', fontWeight: '500' }}>
                      ${(Number(detalle.subtotal) || 0).toLocaleString('es-CL', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                    </td>
                    <td style={{ padding: '10px', textAlign: 'center' }}>
                      <button
                        type="button"
                        onClick={() => handleRemoverProducto(index)}
                        style={{ padding: '6px 10px', backgroundColor: 'var(--danger)', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer', fontSize: '12px' }}
                      >
                        Quitar
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <button
            type="button"
            onClick={handleAgregarProducto}
            style={{ marginTop: '16px', padding: '10px 16px', backgroundColor: 'var(--neutral-200)', color: 'var(--neutral-900)', border: 'none', borderRadius: '6px', cursor: 'pointer', fontSize: '14px', fontWeight: '600' }}
          >
            + Agregar Producto
          </button>
        </Card>

        {/* Total */}
        <Card padding="20px" style={{ marginBottom: '20px', backgroundColor: 'var(--primary-light)' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <span style={{ fontSize: '16px', fontWeight: '600', color: 'var(--neutral-900)' }}>Monto Total:</span>
            <span style={{ fontSize: '24px', fontWeight: '700', color: 'var(--primary)' }}>
              ${calcularTotal().toLocaleString('es-CL', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
            </span>
          </div>
        </Card>

        {/* Botones */}
        <div style={{ display: 'flex', gap: '12px', justifyContent: 'flex-end' }}>
          <button
            type="button"
            onClick={() => navigate('/pedidos')}
            style={{ padding: '10px 24px', backgroundColor: 'var(--neutral-200)', color: 'var(--neutral-900)', border: 'none', borderRadius: '6px', cursor: 'pointer', fontSize: '14px', fontWeight: '600' }}
          >
            Cancelar
          </button>
          <button
            type="submit"
            disabled={loading}
            style={{ padding: '10px 24px', backgroundColor: loading ? 'var(--neutral-300)' : 'var(--success)', color: 'white', border: 'none', borderRadius: '6px', cursor: loading ? 'not-allowed' : 'pointer', fontSize: '14px', fontWeight: '600', display: 'flex', alignItems: 'center', gap: '8px' }}
          >
            {loading ? (
              <>
                <span style={{ width: '14px', height: '14px', border: '2px solid rgba(255,255,255,.3)', borderTop: '2px solid white', borderRadius: '50%', animation: 'spin 1s linear infinite' }} />
                Creando...
              </>
            ) : 'Crear Pedido'}
          </button>
        </div>
      </form>
    </div>
  )
}
