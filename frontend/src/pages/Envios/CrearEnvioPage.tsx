import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Card } from '@/components/UI'
import { enviosService, pedidosService } from '@/services'
import type { DireccionEnvio, PaqueteEnvio, CrearEnvioRequest } from '@/types'

export default function CrearEnvioPage() {
  const navigate = useNavigate()
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  // Búsqueda de pedido
  const [idPedidoInput, setIdPedidoInput] = useState('')
  const [pedidoSeleccionado, setPedidoSeleccionado] = useState<any>(null)
  const [buscarLoading, setBuscarLoading] = useState(false)

  // Formulario
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

  const buscarPedido = async () => {
    if (!idPedidoInput.trim()) {
      setError('Ingresa un ID de pedido válido')
      return
    }

    try {
      setBuscarLoading(true)
      setError(null)
      const pedido = await pedidosService.obtenerPedido(Number(idPedidoInput))

      // Validar que sea un pedido confirmado
      if (pedido.estadoActual !== 'CONFIRMADO') {
        setError('El pedido debe estar en estado CONFIRMADO')
        return
      }

      setPedidoSeleccionado(pedido)
      // Pre-llenar dirección si está disponible
      if (pedido.direccionEntrega) {
        setDireccion(pedido.direccionEntrega)
      }
    } catch (err: any) {
      setError('Pedido no encontrado o error en la búsqueda')
      console.error('Error:', err)
    } finally {
      setBuscarLoading(false)
    }
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()

    if (!pedidoSeleccionado) {
      setError('Debes seleccionar un pedido')
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

    try {
      setLoading(true)
      setError(null)

      const envioRequest: CrearEnvioRequest = {
        idPedidoRef: pedidoSeleccionado.idPedido.toString(),
        direccion,
        paquete,
      }

      const response = await enviosService.crearEnvio(envioRequest)
      navigate(`/envios/${response.idEnvio}`)
    } catch (err: any) {
      setError('Error al crear el envío. Intenta nuevamente.')
      console.error('Error:', err)
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
        <p style={{ margin: '0', color: 'var(--neutral-600)', fontSize: '16px' }}>
          Ingresa los datos del envío
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
        {/* 1. Seleccionar Pedido */}
        <Card title="Seleccionar Pedido" padding="20px" style={{ marginBottom: '20px' }}>
          <div style={{ marginBottom: '20px' }}>
            <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', color: 'var(--neutral-700)' }}>
              ID del Pedido (Requerido)
            </label>
            <div style={{ display: 'flex', gap: '8px' }}>
              <input
                type="text"
                placeholder="Ej: PED-001"
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
              />
              <button
                type="button"
                onClick={buscarPedido}
                disabled={buscarLoading}
                style={{
                  padding: '8px 16px',
                  backgroundColor: 'var(--primary)',
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

          {pedidoSeleccionado && (
            <div
              style={{
                backgroundColor: 'var(--neutral-50)',
                padding: '12px',
                borderRadius: '6px',
                border: '1px solid var(--success)',
                color: 'var(--neutral-700)',
                fontSize: '14px',
              }}
            >
              <p style={{ margin: '0 0 8px 0' }}>
                <strong>✓ Pedido seleccionado:</strong> {pedidoSeleccionado.idPedido}
              </p>
              <p style={{ margin: '0 0 8px 0' }}>
                <strong>Cliente:</strong> {pedidoSeleccionado.nombreCliente || 'N/A'}
              </p>
              <p style={{ margin: '0' }}>
                <strong>Monto:</strong> ${pedidoSeleccionado.montoTotal?.toFixed(2) || '0.00'}
              </p>
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
            disabled={loading || !pedidoSeleccionado}
            style={{
              padding: '10px 20px',
              backgroundColor: loading || !pedidoSeleccionado ? 'var(--neutral-400)' : 'var(--primary)',
              color: 'white',
              border: 'none',
              borderRadius: '6px',
              cursor: loading || !pedidoSeleccionado ? 'not-allowed' : 'pointer',
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
