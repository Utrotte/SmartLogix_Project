import { useEffect, useState } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { Card } from '@/components/UI'
import { enviosService, pedidosService } from '@/services'
import type { DireccionEnvio, PaqueteEnvio, CrearEnvioRequest } from '@/types'

const parseDimensiones = (dimensiones: string) => {
  const partes = dimensiones
    .toLowerCase()
    .replaceAll(" ", "")
    .split("x")
    .map((n) => Number(n));

  if (partes.length !== 3 || partes.some((n) => Number.isNaN(n) || n <= 0)) {
    throw new Error("Las dimensiones deben tener formato AltoXAnchoXLargo, por ejemplo 20X15X10.");
  }

  return {
    altoCm: partes[0],
    anchoCm: partes[1],
    largoCm: partes[2],
  };
};

// Normaliza el pedido recibido del backend para extraer todos los campos útiles
const normalizarPedidoParaEnvio = (raw: any) => {
  const estado =
    raw.estadoActual ?? raw.estado ?? raw.estadoPedido ?? raw.estado_actual ?? ''

  const totalNeto = Number(
    raw.totalNeto ?? raw.total_neto ?? raw.total ?? raw.montoTotal ?? 0
  )

  // Calcular total desde detalles si viene 0
  const detalles = Array.isArray(raw.detalles)
    ? raw.detalles
    : Array.isArray(raw.detallePedido)
    ? raw.detallePedido
    : []

  const totalDeDetalles = detalles.reduce((acc: number, d: any) => {
    const sub =
      Number(d.subtotal ?? d.subTotal ?? 0) ||
      Number(d.precioUnitario ?? 0) * Number(d.cantidad ?? 0)
    return acc + sub
  }, 0)

  const total = totalNeto > 0 ? totalNeto : totalDeDetalles

  const cliente = raw.cliente ?? null
  const nombreCliente =
    raw.nombreCliente ??
    raw.clienteNombre ??
    (cliente ? `${cliente.nombre ?? ''} ${cliente.apellido ?? ''}`.trim() : '')

  const correoCliente = cliente?.correo ?? raw.correoCliente ?? ''
  const telefonoCliente = cliente?.telefono ?? raw.telefonoCliente ?? ''

  const dir = raw.direccionEntrega ?? raw.direccion_entrega ?? raw.direccion ?? null

  return {
    idPedido: Number(raw.idPedido ?? raw.id_pedido ?? raw.id ?? 0),
    codigoPedido: raw.codigoPedido ?? raw.codigo_pedido ?? `PED-${raw.idPedido}`,
    estado,
    total,
    nombreCliente,
    correoCliente,
    telefonoCliente,
    detalles,
    direccionEntrega: dir,
  }
}

export default function CrearEnvioPage() {
  const navigate = useNavigate()
  const [searchParams] = useSearchParams()

  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [success, setSuccess] = useState<string | null>(null)

  // Búsqueda de pedido
  const [idPedidoInput, setIdPedidoInput] = useState(searchParams.get('idPedido') ?? '')
  const [pedidoNormalizado, setPedidoNormalizado] = useState<ReturnType<typeof normalizarPedidoParaEnvio> | null>(null)
  const [buscarLoading, setBuscarLoading] = useState(false)

  // Formulario de dirección (auto-rellenado desde pedido)
  const [direccion, setDireccion] = useState<DireccionEnvio>({
    calle: '',
    ciudad: '',
    region: '',
    codigoPostal: '',
    instrucciones: '',
  })

  const [paquete, setPaquete] = useState<PaqueteEnvio>({
    peso: 0,
    dimensiones: '',
    contenido: '',
  })

  // Si hay idPedido en URL, buscar automáticamente
  useEffect(() => {
    const idDesdeURL = searchParams.get('idPedido')
    if (idDesdeURL) {
      buscarPedido(idDesdeURL)
    }
  }, [])

  const buscarPedido = async (id?: string) => {
    const idBuscar = id ?? idPedidoInput
    if (!idBuscar.trim()) {
      setError('Ingresa un ID de pedido válido')
      return
    }

    const idNum = Number(idBuscar)
    if (isNaN(idNum) || idNum <= 0) {
      setError('El ID debe ser un número válido')
      return
    }

    try {
      setBuscarLoading(true)
      setError(null)
      setPedidoNormalizado(null)

      console.log('Buscando pedido para envío:', idNum)

      const raw = await pedidosService.obtenerPedido(idNum)

      console.log('Pedido recibido para envío (raw):', raw)

      const norm = normalizarPedidoParaEnvio(raw)

      console.log('Pedido normalizado para envío:', norm)

      // Validar: solo CONFIRMADO puede crear envío
      if (norm.estado !== 'CONFIRMADO') {
        setError(
          `Solo se pueden crear envíos para pedidos CONFIRMADOS. ` +
          `Estado actual: ${norm.estado || 'DESCONOCIDO'}. ` +
          `Cambia el estado del pedido a CONFIRMADO antes de crear el envío.`
        )
        return
      }

      setPedidoNormalizado(norm)

      // Auto-rellenar dirección desde pedido
      if (norm.direccionEntrega) {
        const dir = norm.direccionEntrega
        setDireccion({
          calle: [dir.calle, dir.numero].filter(Boolean).join(' ') || '',
          ciudad: dir.ciudad ?? '',
          region: dir.region ?? '',
          codigoPostal: dir.codigoPostal ?? dir.codigo_postal ?? '',
          instrucciones: dir.referencia ?? '',
        })
      }

      // Auto-rellenar contenido del paquete con resumen de productos
      if (norm.detalles.length > 0) {
        const resumen = norm.detalles
          .map((d: any) => {
            const nombre = d.nombreProductoSnapshot ?? d.nombreProducto ?? 'Producto'
            const cant = d.cantidad ?? 1
            return `${cant}x ${nombre}`
          })
          .join(', ')
        setPaquete((prev) => ({ ...prev, contenido: resumen }))
      }
    } catch (err: any) {
      console.error('Error buscando pedido para envío:', {
        status: err?.response?.status,
        data: err?.response?.data,
        message: err?.message,
        url: err?.config?.url,
      })
      setError(
        err?.response?.data?.message ||
        err?.response?.data?.error ||
        'Pedido no encontrado. Verifica el ID ingresado.'
      )
    } finally {
      setBuscarLoading(false)
    }
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError(null)
    setSuccess(null)

    if (!pedidoNormalizado) {
      setError('Debes seleccionar un pedido confirmado')
      return
    }

    // Validación de negocio: solo CONFIRMADO
    if (pedidoNormalizado.estado !== 'CONFIRMADO') {
      setError('Solo se pueden crear envíos para pedidos confirmados.')
      return
    }

    if (!direccion.calle || !direccion.ciudad || !direccion.region || !direccion.codigoPostal) {
      setError('Todos los campos de dirección son requeridos')
      return
    }

    if (paquete.peso <= 0) {
      setError('El peso debe ser mayor a 0')
      return
    }

    if (!paquete.dimensiones.trim()) {
      setError('Las dimensiones son requeridas')
      return
    }

    if (!paquete.contenido.trim()) {
      setError('La descripción del contenido es requerida')
      return
    }

    setLoading(true)

    try {
      const dim = parseDimensiones(paquete.dimensiones)

      const envioRequest = {
        idPedidoRef: pedidoNormalizado.idPedido.toString(),
        calle: direccion.calle,
        numero: '', // El formulario actualmente no tiene campo número separado, así que puede ir vacío o en calle
        comuna: '',
        ciudad: direccion.ciudad,
        region: direccion.region,
        referencia: direccion.instrucciones,
        pesoKg: paquete.peso,
        altoCm: dim.altoCm,
        anchoCm: dim.anchoCm,
        largoCm: dim.largoCm,
        descripcionContenido: paquete.contenido
      }

      console.log("Pedido seleccionado para envío:", pedidoNormalizado);
      console.log("Dirección usada para envío:", direccion);
      console.log("Datos paquete:", paquete);
      console.log("Request final crear envío:", envioRequest);

      console.log('Request crear envío:', envioRequest)
      console.log('Pedido normalizado para envío:', pedidoNormalizado)

      const response = await enviosService.crearEnvio(envioRequest)

      console.log('Respuesta crear envío:', response)

      const idEnvio = (response as any)?.idEnvio ?? (response as any)?.data?.idEnvio

      if (idEnvio) {
        navigate(`/envios/${idEnvio}`)
      } else {
        navigate('/envios')
      }
    } catch (err: any) {
      console.error('Error al crear envío:', {
        status: err?.response?.status,
        data: err?.response?.data,
        message: err?.message,
        url: err?.config?.url,
        method: err?.config?.method,
        headers: err?.config?.headers,
      })

      if (err?.response?.status === 403) {
        setError('No tienes permisos para crear envíos o tu sesión no es válida. Vuelve a iniciar sesión.')
        return
      }

      if (err?.response?.status === 401) {
        setError('Sesión expirada o inválida. Inicia sesión nuevamente.')
        return
      }

      const data = err?.response?.data
      const mensaje =
        typeof data === 'string'
          ? data
          : data?.message ||
            data?.error ||
            JSON.stringify(data) ||
            'Error al crear el envío. Intenta nuevamente.'

      setError(mensaje)
    } finally {
      setLoading(false)
    }
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
        <h1 style={{ margin: '10px 0 5px 0', color: 'var(--neutral-900)' }}>
          Crear Nuevo Envío
        </h1>
        <p style={{ margin: '0', color: 'var(--neutral-600)', fontSize: '14px' }}>
          Solo se pueden crear envíos para pedidos con estado <strong>CONFIRMADO</strong>.
        </p>
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
        {/* 1. Seleccionar Pedido */}
        <Card title="Seleccionar Pedido Confirmado" padding="20px" style={{ marginBottom: '20px' }}>
          <div style={{ marginBottom: '20px' }}>
            <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', color: 'var(--neutral-700)' }}>
              ID del Pedido (Requerido)
            </label>
            <div style={{ display: 'flex', gap: '8px' }}>
              <input
                type="number"
                placeholder="Ej: 1"
                value={idPedidoInput}
                onChange={(e) => setIdPedidoInput(e.target.value)}
                style={{
                  flex: 1,
                  padding: '8px 12px',
                  border: '1px solid var(--neutral-300)',
                  borderRadius: '6px',
                  fontSize: '14px',
                }}
                disabled={buscarLoading}
                min="1"
              />
              <button
                type="button"
                onClick={() => buscarPedido()}
                disabled={buscarLoading}
                style={{
                  padding: '8px 16px',
                  backgroundColor: '#0066CC',
                  color: 'white',
                  border: 'none',
                  borderRadius: '6px',
                  cursor: buscarLoading ? 'not-allowed' : 'pointer',
                  fontSize: '14px',
                  fontWeight: '600',
                }}
              >
                {buscarLoading ? 'Buscando...' : 'Buscar'}
              </button>
            </div>
          </div>

          {/* Información del pedido seleccionado */}
          {pedidoNormalizado && (
            <div
              style={{
                backgroundColor: '#f0fdf4',
                padding: '16px',
                borderRadius: '8px',
                border: '1px solid #86efac',
              }}
            >
              <p style={{ margin: '0 0 4px 0', fontWeight: '700', color: '#166534', fontSize: '14px' }}>
                ✅ Pedido Confirmado
              </p>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '8px', marginTop: '12px' }}>
                <div>
                  <span style={{ fontSize: '12px', color: '#6B7280', fontWeight: '600' }}>ID</span>
                  <p style={{ margin: '2px 0 0 0', color: '#111827', fontSize: '14px' }}>
                    {pedidoNormalizado.idPedido} — {pedidoNormalizado.codigoPedido}
                  </p>
                </div>
                <div>
                  <span style={{ fontSize: '12px', color: '#6B7280', fontWeight: '600' }}>Estado</span>
                  <p style={{ margin: '2px 0 0 0', color: '#059669', fontSize: '14px', fontWeight: '600' }}>
                    {pedidoNormalizado.estado}
                  </p>
                </div>
                <div>
                  <span style={{ fontSize: '12px', color: '#6B7280', fontWeight: '600' }}>Cliente</span>
                  <p style={{ margin: '2px 0 0 0', color: '#111827', fontSize: '14px' }}>
                    {pedidoNormalizado.nombreCliente || 'N/A'}
                  </p>
                </div>
                <div>
                  <span style={{ fontSize: '12px', color: '#6B7280', fontWeight: '600' }}>Monto Total</span>
                  <p style={{ margin: '2px 0 0 0', color: '#0D1B3D', fontSize: '16px', fontWeight: '700' }}>
                    ${pedidoNormalizado.total.toLocaleString('es-CL', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                  </p>
                </div>
                {pedidoNormalizado.correoCliente && (
                  <div>
                    <span style={{ fontSize: '12px', color: '#6B7280', fontWeight: '600' }}>Correo</span>
                    <p style={{ margin: '2px 0 0 0', color: '#111827', fontSize: '14px' }}>
                      {pedidoNormalizado.correoCliente}
                    </p>
                  </div>
                )}
                {pedidoNormalizado.telefonoCliente && (
                  <div>
                    <span style={{ fontSize: '12px', color: '#6B7280', fontWeight: '600' }}>Teléfono</span>
                    <p style={{ margin: '2px 0 0 0', color: '#111827', fontSize: '14px' }}>
                      {pedidoNormalizado.telefonoCliente}
                    </p>
                  </div>
                )}
                {pedidoNormalizado.detalles.length > 0 && (
                  <div style={{ gridColumn: '1 / -1' }}>
                    <span style={{ fontSize: '12px', color: '#6B7280', fontWeight: '600' }}>Productos</span>
                    <p style={{ margin: '2px 0 0 0', color: '#111827', fontSize: '13px' }}>
                      {pedidoNormalizado.detalles
                        .map((d: any) => `${d.cantidad}x ${d.nombreProductoSnapshot ?? 'Producto'}`)
                        .join(', ')}
                    </p>
                  </div>
                )}
              </div>
            </div>
          )}
        </Card>

        {/* 2. Dirección de Entrega */}
        <Card title="Dirección de Entrega" padding="20px" style={{ marginBottom: '20px' }}>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px', marginBottom: '16px' }}>
            <div>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', color: 'var(--neutral-700)' }}>
                Calle (Requerido)
              </label>
              <input
                type="text"
                placeholder="Ej: Av. Paulina 2530"
                value={direccion.calle}
                onChange={(e) => setDireccion({ ...direccion, calle: e.target.value })}
                style={{
                  width: '100%',
                  padding: '8px 12px',
                  border: '1px solid var(--neutral-300)',
                  borderRadius: '6px',
                  fontSize: '14px',
                  boxSizing: 'border-box',
                }}
              />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', color: 'var(--neutral-700)' }}>
                Ciudad (Requerido)
              </label>
              <input
                type="text"
                placeholder="Ej: Santiago"
                value={direccion.ciudad}
                onChange={(e) => setDireccion({ ...direccion, ciudad: e.target.value })}
                style={{
                  width: '100%',
                  padding: '8px 12px',
                  border: '1px solid var(--neutral-300)',
                  borderRadius: '6px',
                  fontSize: '14px',
                  boxSizing: 'border-box',
                }}
              />
            </div>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px', marginBottom: '16px' }}>
            <div>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', color: 'var(--neutral-700)' }}>
                Región (Requerido)
              </label>
              <input
                type="text"
                placeholder="Ej: Región Metropolitana"
                value={direccion.region}
                onChange={(e) => setDireccion({ ...direccion, region: e.target.value })}
                style={{
                  width: '100%',
                  padding: '8px 12px',
                  border: '1px solid var(--neutral-300)',
                  borderRadius: '6px',
                  fontSize: '14px',
                  boxSizing: 'border-box',
                }}
              />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', color: 'var(--neutral-700)' }}>
                Código Postal (Requerido)
              </label>
              <input
                type="text"
                placeholder="Ej: 8340000"
                value={direccion.codigoPostal}
                onChange={(e) => setDireccion({ ...direccion, codigoPostal: e.target.value })}
                style={{
                  width: '100%',
                  padding: '8px 12px',
                  border: '1px solid var(--neutral-300)',
                  borderRadius: '6px',
                  fontSize: '14px',
                  boxSizing: 'border-box',
                }}
              />
            </div>
          </div>

          <div>
            <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', color: 'var(--neutral-700)' }}>
              Instrucciones Especiales (Opcional)
            </label>
            <textarea
              placeholder="Ej: Dejar en la puerta"
              value={direccion.instrucciones || ''}
              onChange={(e) => setDireccion({ ...direccion, instrucciones: e.target.value })}
              style={{
                width: '100%',
                padding: '8px 12px',
                border: '1px solid var(--neutral-300)',
                borderRadius: '6px',
                fontSize: '14px',
                fontFamily: 'inherit',
                minHeight: '80px',
                resize: 'vertical',
                boxSizing: 'border-box',
              }}
            />
          </div>
        </Card>

        {/* 3. Datos del Paquete */}
        <Card title="Datos del Paquete" padding="20px" style={{ marginBottom: '20px' }}>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px', marginBottom: '16px' }}>
            <div>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', color: 'var(--neutral-700)' }}>
                Peso (kg) (Requerido)
              </label>
              <input
                type="number"
                placeholder="Ej: 2.5"
                value={paquete.peso || ''}
                onChange={(e) => setPaquete({ ...paquete, peso: parseFloat(e.target.value) || 0 })}
                step="0.1"
                min="0"
                style={{
                  width: '100%',
                  padding: '8px 12px',
                  border: '1px solid var(--neutral-300)',
                  borderRadius: '6px',
                  fontSize: '14px',
                  boxSizing: 'border-box',
                }}
              />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', color: 'var(--neutral-700)' }}>
                Dimensiones (Requerido)
              </label>
              <input
                type="text"
                placeholder="Ej: 20x15x10 cm"
                value={paquete.dimensiones}
                onChange={(e) => setPaquete({ ...paquete, dimensiones: e.target.value })}
                style={{
                  width: '100%',
                  padding: '8px 12px',
                  border: '1px solid var(--neutral-300)',
                  borderRadius: '6px',
                  fontSize: '14px',
                  boxSizing: 'border-box',
                }}
              />
            </div>
          </div>

          <div>
            <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', color: 'var(--neutral-700)' }}>
              Descripción del Contenido (Requerido)
            </label>
            <textarea
              placeholder="Ej: 2x Libro, 1x Abrigo rojo"
              value={paquete.contenido}
              onChange={(e) => setPaquete({ ...paquete, contenido: e.target.value })}
              style={{
                width: '100%',
                padding: '8px 12px',
                border: '1px solid var(--neutral-300)',
                borderRadius: '6px',
                fontSize: '14px',
                fontFamily: 'inherit',
                minHeight: '80px',
                resize: 'vertical',
                boxSizing: 'border-box',
              }}
            />
          </div>
        </Card>

        {/* Botones */}
        <div style={{ display: 'flex', gap: '12px', marginTop: '30px' }}>
          <button
            type="button"
            onClick={() => navigate('/envios')}
            style={{
              padding: '10px 20px',
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
            type="submit"
            disabled={loading || !pedidoNormalizado}
            style={{
              padding: '10px 20px',
              backgroundColor: loading || !pedidoNormalizado ? 'var(--neutral-400)' : '#0066CC',
              color: 'white',
              border: 'none',
              borderRadius: '6px',
              cursor: loading || !pedidoNormalizado ? 'not-allowed' : 'pointer',
              fontSize: '14px',
              fontWeight: '600',
            }}
          >
            {loading ? 'Creando...' : 'Crear Envío'}
          </button>
        </div>
      </form>
    </div>
  )
}
