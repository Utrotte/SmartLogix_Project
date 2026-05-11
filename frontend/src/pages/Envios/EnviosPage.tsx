import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Card, Badge, LoadingSpinner } from '@/components/UI'
import { enviosService } from '@/services'
import type { EnvioListaItem, EstadoEnvio } from '@/types'
import CambiarEstadoEnvioModal from './CambiarEstadoEnvioModal'
import AsignarTransportistaModal from './AsignarTransportistaModal'

type EstadoFilter = 'TODOS' | EstadoEnvio

export default function EnviosPage() {
  const navigate = useNavigate()
  const [envios, setEnvios] = useState<EnvioListaItem[]>([])
  const [filteredEnvios, setFilteredEnvios] = useState<EnvioListaItem[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [estadoFilter, setEstadoFilter] = useState<EstadoFilter>('TODOS')
  const [searchTerm, setSearchTerm] = useState('')
  const [selectedEnvio, setSelectedEnvio] = useState<EnvioListaItem | null>(null)
  const [showEstadoModal, setShowEstadoModal] = useState(false)
  const [showTransportistaModal, setShowTransportistaModal] = useState(false)

  useEffect(() => {
    cargarEnvios()
  }, [])

  const normalizarEstadoEnvio = (estado?: string) => {
    if (!estado) return "PENDIENTE_ASIGNACION";

    const upper = estado.trim().toUpperCase()
      .replaceAll(" ", "_")
      .replaceAll("Á", "A")
      .replaceAll("É", "E")
      .replaceAll("Í", "I")
      .replaceAll("Ó", "O")
      .replaceAll("Ú", "U");

    if (upper === "PENDIENTE") return "PENDIENTE_ASIGNACION";
    if (upper === "EN_TRANSITO") return "EN_TRANSITO";

    return upper;
  };

  useEffect(() => {
    let filtered = envios

    if (estadoFilter !== 'TODOS') {
      filtered = filtered.filter((e) => normalizarEstadoEnvio(e.estado) === estadoFilter)
    }

    if (searchTerm) {
      const term = searchTerm.toLowerCase()
      filtered = filtered.filter(
        (e) =>
          (e.idEnvio && e.idEnvio.toLowerCase().includes(term)) ||
          (e.idPedidoRef && e.idPedidoRef.toLowerCase().includes(term)) ||
          (e.codigoEnvio && e.codigoEnvio.toLowerCase().includes(term))
      )
    }

    setFilteredEnvios(filtered)
  }, [envios, estadoFilter, searchTerm])

  const cargarEnvios = async () => {
    try {
      setLoading(true)
      setError(null)
      const data = await enviosService.listarEnvios()
      setEnvios(data)
    } catch (err) {
      setError('Error al cargar los envíos. Intenta nuevamente.')
      console.error('Error:', err)
    } finally {
      setLoading(false)
    }
  }

  const getEstadoBadgeColor = (estado: EstadoEnvio): 'default' | 'danger' | 'success' | 'warning' => {
    switch (estado) {
      case 'PENDIENTE_ASIGNACION':
        return 'warning'
      case 'ASIGNADO':
        return 'warning'
      case 'EN_TRANSITO':
        return 'warning'
      case 'ENTREGADO':
        return 'success'
      case 'INCIDENCIA':
        return 'danger'
      default:
        return 'default'
    }
  }

  const getEstadoLabel = (estado: EstadoEnvio): string => {
    switch (estado) {
      case 'PENDIENTE_ASIGNACION':
        return 'Pendiente Asignación'
      case 'ASIGNADO':
        return 'Asignado'
      case 'EN_TRANSITO':
        return 'En Tránsito'
      case 'ENTREGADO':
        return 'Entregado'
      case 'INCIDENCIA':
        return 'Incidencia'
      default:
        return estado
    }
  }

  const handleOpenEstadoModal = (envio: EnvioListaItem) => {
    setSelectedEnvio(envio)
    setShowEstadoModal(true)
  }

  const handleOpenTransportistaModal = (envio: EnvioListaItem) => {
    setSelectedEnvio(envio)
    setShowTransportistaModal(true)
  }

  const handleCloseModals = () => {
    setShowEstadoModal(false)
    setShowTransportistaModal(false)
    setSelectedEnvio(null)
  }

  const handleSuccessModal = () => {
    handleCloseModals()
    cargarEnvios()
  }

  if (loading) {
    return (
      <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '400px' }}>
        <div style={{ textAlign: 'center' }}>
          <LoadingSpinner size="lg" />
          <p style={{ marginTop: '20px', color: 'var(--neutral-600)' }}>
            Cargando envíos...
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
            Envíos
          </h1>
          <p style={{ margin: '0', color: 'var(--neutral-600)', fontSize: '16px' }}>
            Gestiona tus envíos
          </p>
        </div>
        <button
          onClick={() => navigate('/envios/crear')}
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
          + Nuevo Envío
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
              Buscar por ID Envío o Pedido
            </label>
            <input
              type="text"
              placeholder="Ej: ENV-001, PED-001"
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
              <option value="PENDIENTE_ASIGNACION">Pendiente Asignación</option>
              <option value="ASIGNADO">Asignado</option>
              <option value="EN_TRANSITO">En Tránsito</option>
              <option value="ENTREGADO">Entregado</option>
              <option value="INCIDENCIA">Incidencia</option>
            </select>
          </div>
        </div>
      </Card>

      {/* Tabla de envíos */}
      {filteredEnvios.length === 0 ? (
        <Card padding="40px" style={{ textAlign: 'center' }}>
          <p style={{ color: 'var(--neutral-500)', fontSize: '16px', margin: '0' }}>
            No hay envíos que mostrar
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
                    ID Envío
                  </th>
                  <th style={{ padding: '12px 16px', textAlign: 'left', fontWeight: '600', color: 'var(--neutral-700)' }}>
                    Pedido
                  </th>
                  <th style={{ padding: '12px 16px', textAlign: 'left', fontWeight: '600', color: 'var(--neutral-700)' }}>
                    Transportista
                  </th>
                  <th style={{ padding: '12px 16px', textAlign: 'left', fontWeight: '600', color: 'var(--neutral-700)' }}>
                    Estado
                  </th>
                  <th style={{ padding: '12px 16px', textAlign: 'left', fontWeight: '600', color: 'var(--neutral-700)' }}>
                    Fecha Creación
                  </th>
                  <th style={{ padding: '12px 16px', textAlign: 'center', fontWeight: '600', color: 'var(--neutral-700)' }}>
                    Acciones
                  </th>
                </tr>
              </thead>
              <tbody>
                {filteredEnvios.map((envio) => {
                  const estadoNormalizado = normalizarEstadoEnvio(envio.estado);
                  return (
                  <tr
                    key={envio.idEnvio}
                    style={{
                      borderBottom: '1px solid var(--neutral-200)',
                    }}
                  >
                    <td style={{ padding: '12px 16px', color: 'var(--neutral-900)', fontWeight: '500' }}>
                      {envio.idEnvio}
                    </td>
                    <td style={{ padding: '12px 16px', color: 'var(--neutral-700)' }}>
                      {envio.idPedidoRef}
                    </td>
                    <td style={{ padding: '12px 16px', color: 'var(--neutral-700)' }}>
                      {envio.transportista || 'Sin asignar'}
                    </td>
                    <td style={{ padding: '12px 16px' }}>
                      <Badge variant={getEstadoBadgeColor(estadoNormalizado as EstadoEnvio)}>
                        {getEstadoLabel(estadoNormalizado as EstadoEnvio)}
                      </Badge>
                    </td>
                    <td style={{ padding: '12px 16px', color: 'var(--neutral-700)', fontSize: '13px' }}>
                      {new Date(envio.fechaCreacion).toLocaleDateString('es-CL')}
                    </td>
                    <td style={{ padding: '12px 16px', textAlign: 'center' }}>
                      <div style={{ display: 'flex', gap: '6px', justifyContent: 'center', flexWrap: 'wrap' }}>
                        <button
                          onClick={() => navigate(`/envios/${envio.idEnvio}`)}
                          style={{
                            padding: '6px 10px',
                            backgroundColor: 'var(--primary)',
                            color: 'white',
                            border: 'none',
                            borderRadius: '4px',
                            cursor: 'pointer',
                            fontSize: '11px',
                          }}
                        >
                          Ver
                        </button>
                        <button
                          onClick={() => handleOpenEstadoModal(envio)}
                          style={{
                            padding: '6px 10px',
                            backgroundColor: 'var(--warning)',
                            color: 'white',
                            border: 'none',
                            borderRadius: '4px',
                            cursor: 'pointer',
                            fontSize: '11px',
                          }}
                        >
                          Estado
                        </button>
                        <button
                          onClick={() => handleOpenTransportistaModal(envio)}
                          style={{
                            padding: '6px 10px',
                            backgroundColor: 'var(--success)',
                            color: 'white',
                            border: 'none',
                            borderRadius: '4px',
                            cursor: 'pointer',
                            fontSize: '11px',
                          }}
                        >
                          Transportista
                        </button>
                        <button
                          onClick={() => navigate(`/envios/${envio.idEnvio}/seguimiento`)}
                          style={{
                            padding: '6px 10px',
                            backgroundColor: 'var(--neutral-500)',
                            color: 'white',
                            border: 'none',
                            borderRadius: '4px',
                            cursor: 'pointer',
                            fontSize: '11px',
                          }}
                        >
                          Seguimiento
                        </button>
                      </div>
                    </td>
                  </tr>
                  );
                })}
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
        Mostrando {filteredEnvios.length} de {envios.length} envíos
      </div>

      {/* Modales */}
      {showEstadoModal && selectedEnvio && (
        <CambiarEstadoEnvioModal
          envio={selectedEnvio}
          onClose={handleCloseModals}
          onSuccess={handleSuccessModal}
        />
      )}
      {showTransportistaModal && selectedEnvio && (
        <AsignarTransportistaModal
          envio={selectedEnvio}
          onClose={handleCloseModals}
          onSuccess={handleSuccessModal}
        />
      )}
    </div>
  )
}
