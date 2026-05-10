import { useEffect, useState } from 'react'
import { Card, Badge, LoadingSpinner } from '@/components/UI'
import { useAuth } from '@/context/AuthContext'
import { pedidosService, inventarioService, enviosService } from '@/services'
import type { PedidoListaItem, EnvioListaItem } from '@/types'

interface DashboardStats {
  totalPedidos: number
  pedidosPendientes: number
  pedidosConfirmados: number
  stockBajo: number
  reservasActivas: number
  enviosActivos: number
  enviosEntregados: number
}

export default function DashboardPage() {
  const { usuario } = useAuth()
  const [stats, setStats] = useState<DashboardStats>({
    totalPedidos: 0,
    pedidosPendientes: 0,
    pedidosConfirmados: 0,
    stockBajo: 0,
    reservasActivas: 0,
    enviosActivos: 0,
    enviosEntregados: 0,
  })
  const [loading, setLoading] = useState(true)
  const [errors, setErrors] = useState<Record<string, string>>({})

  useEffect(() => {
    const cargarDatos = async () => {
      setLoading(true)
      const newErrors: Record<string, string> = {}

      try {
        // Cargar pedidos
        try {
          const pedidos = await pedidosService.listarPedidos()
          const pedidosPendientes = pedidos.filter(
            (p: PedidoListaItem) => p.estadoActual === 'PENDIENTE_CONFIRMACION'
          ).length
          const pedidosConfirmados = pedidos.filter(
            (p: PedidoListaItem) => p.estadoActual === 'CONFIRMADO'
          ).length

          setStats((prev) => ({
            ...prev,
            totalPedidos: pedidos.length,
            pedidosPendientes,
            pedidosConfirmados,
          }))
        } catch (error) {
          newErrors.pedidos = 'No se pudieron cargar los pedidos'
          console.error('Error cargando pedidos:', error)
        }

        // Cargar existencias para stock bajo
        try {
          await inventarioService.listarProductos()
          // Nota: Este endpoint devuelve productos, no existencias
          // Se actualizará cuando el BFF devuelva datos de existencias con cantidad mínima
          const stockBajo = 0
          setStats((prev) => ({
            ...prev,
            stockBajo,
          }))
        } catch (error) {
          newErrors.existencias = 'No se pudieron cargar las existencias'
          console.error('Error cargando existencias:', error)
        }

        // Cargar reservas
        try {
          const reservas = await inventarioService.listarReservas()
          const reservasActivas = reservas.filter(
            (r: any) => r.estado === 'ACTIVA'
          ).length

          setStats((prev) => ({
            ...prev,
            reservasActivas,
          }))
        } catch (error) {
          newErrors.reservas = 'No se pudieron cargar las reservas'
          console.error('Error cargando reservas:', error)
        }

        // Cargar envíos
        try {
          const envios = await enviosService.listarEnvios()
          const enviosActivos = envios.filter(
            (e: EnvioListaItem) =>
              e.estado !== 'ENTREGADO' && e.estado !== 'INCIDENCIA'
          ).length
          const enviosEntregados = envios.filter(
            (e: EnvioListaItem) => e.estado === 'ENTREGADO'
          ).length

          setStats((prev) => ({
            ...prev,
            enviosActivos,
            enviosEntregados,
          }))
        } catch (error) {
          newErrors.envios = 'No se pudieron cargar los envíos'
          console.error('Error cargando envíos:', error)
        }

        setErrors(newErrors)
      } finally {
        setLoading(false)
      }
    }

    cargarDatos()
  }, [])

  if (loading) {
    return (
      <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '400px' }}>
        <div style={{ textAlign: 'center' }}>
          <LoadingSpinner size="lg" />
          <p style={{ marginTop: '20px', color: 'var(--neutral-600)' }}>
            Cargando datos del dashboard...
          </p>
        </div>
      </div>
    )
  }

  return (
    <div>
      <div style={{ marginBottom: '30px' }}>
        <h1 style={{ margin: '0 0 10px 0', color: 'var(--neutral-900)' }}>
          Dashboard
        </h1>
        <p style={{ margin: '0', color: 'var(--neutral-600)', fontSize: '16px' }}>
          Bienvenido, {usuario?.nombre || 'usuario'}. Aquí está tu resumen de logística.
        </p>
      </div>

      {/* Error messages */}
      {Object.keys(errors).length > 0 && (
        <div style={{ marginBottom: '20px' }}>
          {Object.entries(errors).map(([key, error]) => (
            <div
              key={key}
              style={{
                backgroundColor: '#fee2e2',
                color: '#991b1b',
                padding: '12px 16px',
                borderRadius: '6px',
                marginBottom: '10px',
                fontSize: '14px',
              }}
            >
              ⚠️ {error}
            </div>
          ))}
        </div>
      )}

      {/* Stats Grid */}
      <div
        style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))',
          gap: '20px',
          marginBottom: '30px',
        }}
      >
        {/* Pedidos Totales */}
        <Card title="Pedidos Totales" padding="20px">
          <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
            <div style={{ fontSize: '48px' }}>📦</div>
            <div style={{ flex: 1 }}>
              <div
                style={{
                  fontSize: '36px',
                  fontWeight: 700,
                  color: 'var(--primary)',
                  lineHeight: '1',
                }}
              >
                {stats.totalPedidos}
              </div>
            </div>
          </div>
        </Card>

        {/* Pedidos Pendientes */}
        <Card title="Pedidos Pendientes" padding="20px">
          <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
            <div style={{ fontSize: '48px' }}>⏳</div>
            <div style={{ flex: 1 }}>
              <div
                style={{
                  fontSize: '36px',
                  fontWeight: 700,
                  color: 'var(--warning)',
                  lineHeight: '1',
                }}
              >
                {stats.pedidosPendientes}
              </div>
            </div>
          </div>
        </Card>

        {/* Pedidos Confirmados */}
        <Card title="Pedidos Confirmados" padding="20px">
          <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
            <div style={{ fontSize: '48px' }}>✅</div>
            <div style={{ flex: 1 }}>
              <div
                style={{
                  fontSize: '36px',
                  fontWeight: 700,
                  color: 'var(--success)',
                  lineHeight: '1',
                }}
              >
                {stats.pedidosConfirmados}
              </div>
            </div>
          </div>
        </Card>

        {/* Stock Bajo */}
        <Card title="Stock Bajo" padding="20px">
          <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
            <div style={{ fontSize: '48px' }}>⚠️</div>
            <div style={{ flex: 1 }}>
              <div
                style={{
                  fontSize: '36px',
                  fontWeight: 700,
                  color: 'var(--danger)',
                  lineHeight: '1',
                }}
              >
                {stats.stockBajo}
              </div>
            </div>
          </div>
        </Card>

        {/* Reservas Activas */}
        <Card title="Reservas Activas" padding="20px">
          <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
            <div style={{ fontSize: '48px' }}>🔒</div>
            <div style={{ flex: 1 }}>
              <div
                style={{
                  fontSize: '36px',
                  fontWeight: 700,
                  color: 'var(--info)',
                  lineHeight: '1',
                }}
              >
                {stats.reservasActivas}
              </div>
            </div>
          </div>
        </Card>

        {/* Envíos en Tránsito */}
        <Card title="Envíos en Tránsito" padding="20px">
          <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
            <div style={{ fontSize: '48px' }}>🚚</div>
            <div style={{ flex: 1 }}>
              <div
                style={{
                  fontSize: '36px',
                  fontWeight: 700,
                  color: 'var(--primary)',
                  lineHeight: '1',
                }}
              >
                {stats.enviosActivos}
              </div>
            </div>
          </div>
        </Card>

        {/* Envíos Entregados */}
        <Card title="Envíos Entregados" padding="20px">
          <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
            <div style={{ fontSize: '48px' }}>📍</div>
            <div style={{ flex: 1 }}>
              <div
                style={{
                  fontSize: '36px',
                  fontWeight: 700,
                  color: 'var(--success)',
                  lineHeight: '1',
                }}
              >
                {stats.enviosEntregados}
              </div>
            </div>
          </div>
        </Card>
      </div>

      {/* Summary section */}
      <Card title="Resumen de Actividad">
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px' }}>
          <div>
            <h4 style={{ margin: '0 0 10px 0', color: 'var(--neutral-900)' }}>
              Estado de Pedidos
            </h4>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '14px' }}>
                <span>Total:</span>
                <span style={{ fontWeight: 600 }}>{stats.totalPedidos}</span>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '14px' }}>
                <span>Confirmados:</span>
                <Badge variant="success">{stats.pedidosConfirmados}</Badge>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '14px' }}>
                <span>Pendientes:</span>
                <Badge variant="warning">{stats.pedidosPendientes}</Badge>
              </div>
            </div>
          </div>

          <div>
            <h4 style={{ margin: '0 0 10px 0', color: 'var(--neutral-900)' }}>
              Estado de Envíos
            </h4>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '14px' }}>
                <span>En tránsito:</span>
                <Badge variant="info">{stats.enviosActivos}</Badge>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '14px' }}>
                <span>Entregados:</span>
                <Badge variant="success">{stats.enviosEntregados}</Badge>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '14px' }}>
                <span>Reservas activas:</span>
                <Badge variant="default">{stats.reservasActivas}</Badge>
              </div>
            </div>
          </div>
        </div>
      </Card>

      {/* Last updated info */}
      <div
        style={{
          marginTop: '30px',
          padding: '15px',
          backgroundColor: 'var(--neutral-50)',
          borderRadius: '6px',
          textAlign: 'center',
          fontSize: '12px',
          color: 'var(--neutral-500)',
        }}
      >
        📊 Los datos se actualizan automáticamente cada vez que visitas esta página
      </div>
    </div>
  )
}
