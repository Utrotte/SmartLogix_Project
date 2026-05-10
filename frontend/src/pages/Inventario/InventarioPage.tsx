import { useEffect, useState } from 'react'
import { Card, Badge, LoadingSpinner } from '@/components/UI'
import { inventarioService } from '@/services'
import type { Existencia } from '@/types'
import AjustarStockModal from './AjustarStockModal'

export default function InventarioPage() {
  const [existencias, setExistencias] = useState<Existencia[]>([])
  const [filteredExistencias, setFilteredExistencias] = useState<Existencia[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [searchTerm, setSearchTerm] = useState('')
  const [selectedExistencia, setSelectedExistencia] = useState<Existencia | null>(null)
  const [showModal, setShowModal] = useState(false)

  useEffect(() => {
    cargarExistencias()
  }, [])

  useEffect(() => {
    let filtered = existencias

    if (searchTerm) {
      const term = searchTerm.toLowerCase()
      filtered = filtered.filter(
        (e) =>
          e.nombreProducto?.toLowerCase().includes(term) ||
          e.idProducto.toString().includes(term) ||
          e.nombreBodega?.toLowerCase().includes(term)
      )
    }

    setFilteredExistencias(filtered)
  }, [existencias, searchTerm])

  const cargarExistencias = async () => {
    try {
      setLoading(true)
      setError(null)
      const bodegas = await inventarioService.listarBodegas()

      const allExistencias: Existencia[] = []
      for (const bodega of bodegas) {
        const existenciasEnBodega = await inventarioService.obtenerExistenciasPorBodega(
          bodega.idBodega
        )
        allExistencias.push(...existenciasEnBodega)
      }

      setExistencias(allExistencias)
    } catch (err) {
      setError('Error al cargar el inventario. Intenta nuevamente.')
      console.error('Error:', err)
    } finally {
      setLoading(false)
    }
  }

  const getStockBadgeColor = (disponible: number, minimo: number): 'success' | 'warning' | 'danger' => {
    if (disponible === 0) return 'danger'
    if (disponible <= minimo) return 'danger'
    if (disponible <= minimo * 1.5) return 'warning'
    return 'success'
  }

  const getStockLabel = (disponible: number, minimo: number): string => {
    if (disponible === 0) return 'Sin Stock'
    if (disponible < minimo) return 'Bajo Mínimo'
    if (disponible <= minimo * 1.5) return 'Cercano al Mínimo'
    return 'Normal'
  }

  const handleAjustarStock = (existencia: Existencia) => {
    setSelectedExistencia(existencia)
    setShowModal(true)
  }

  const handleCloseModal = () => {
    setShowModal(false)
    setSelectedExistencia(null)
  }

  const handleSuccessAjuste = () => {
    handleCloseModal()
    cargarExistencias()
  }

  if (loading) {
    return (
      <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '400px' }}>
        <div style={{ textAlign: 'center' }}>
          <LoadingSpinner size="lg" />
          <p style={{ marginTop: '20px', color: 'var(--neutral-600)' }}>
            Cargando inventario...
          </p>
        </div>
      </div>
    )
  }

  return (
    <div>
      {/* Header */}
      <div style={{ marginBottom: '30px' }}>
        <h1 style={{ margin: '0 0 10px 0', color: 'var(--neutral-900)' }}>
          Inventario
        </h1>
        <p style={{ margin: '0', color: 'var(--neutral-600)', fontSize: '16px' }}>
          Gestiona el stock de tus productos
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

      {/* Búsqueda */}
      <Card title="Filtros" padding="16px" style={{ marginBottom: '20px' }}>
        <div style={{ display: 'flex', gap: '16px', flexWrap: 'wrap' }}>
          <div style={{ flex: 1, minWidth: '200px' }}>
            <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', color: 'var(--neutral-700)' }}>
              Buscar por Producto, ID o Bodega
            </label>
            <input
              type="text"
              placeholder="Ej: Laptop, 1, Bodega Central"
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
        </div>
      </Card>

      {/* Tabla de existencias */}
      {filteredExistencias.length === 0 ? (
        <Card padding="40px" style={{ textAlign: 'center' }}>
          <p style={{ color: 'var(--neutral-500)', fontSize: '16px', margin: '0' }}>
            No hay existencias que mostrar
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
                    Producto
                  </th>
                  <th style={{ padding: '12px 16px', textAlign: 'left', fontWeight: '600', color: 'var(--neutral-700)' }}>
                    Bodega
                  </th>
                  <th style={{ padding: '12px 16px', textAlign: 'center', fontWeight: '600', color: 'var(--neutral-700)' }}>
                    Disponible
                  </th>
                  <th style={{ padding: '12px 16px', textAlign: 'center', fontWeight: '600', color: 'var(--neutral-700)' }}>
                    Reservado
                  </th>
                  <th style={{ padding: '12px 16px', textAlign: 'center', fontWeight: '600', color: 'var(--neutral-700)' }}>
                    Mínimo
                  </th>
                  <th style={{ padding: '12px 16px', textAlign: 'center', fontWeight: '600', color: 'var(--neutral-700)' }}>
                    Estado
                  </th>
                  <th style={{ padding: '12px 16px', textAlign: 'center', fontWeight: '600', color: 'var(--neutral-700)' }}>
                    Acciones
                  </th>
                </tr>
              </thead>
              <tbody>
                {filteredExistencias.map((existencia) => (
                  <tr
                    key={`${existencia.idProducto}-${existencia.idBodega}`}
                    style={{
                      borderBottom: '1px solid var(--neutral-200)',
                    }}
                  >
                    <td style={{ padding: '12px 16px', color: 'var(--neutral-900)', fontWeight: '500' }}>
                      {existencia.nombreProducto || `Producto #${existencia.idProducto}`}
                    </td>
                    <td style={{ padding: '12px 16px', color: 'var(--neutral-700)' }}>
                      {existencia.nombreBodega || `Bodega #${existencia.idBodega}`}
                    </td>
                    <td style={{ padding: '12px 16px', textAlign: 'center', color: 'var(--neutral-900)', fontWeight: '500' }}>
                      {existencia.cantidadDisponible}
                    </td>
                    <td style={{ padding: '12px 16px', textAlign: 'center', color: 'var(--neutral-700)' }}>
                      {existencia.cantidadReservada}
                    </td>
                    <td style={{ padding: '12px 16px', textAlign: 'center', color: 'var(--neutral-700)' }}>
                      {existencia.cantidadMínima}
                    </td>
                    <td style={{ padding: '12px 16px', textAlign: 'center' }}>
                      <Badge
                        variant={getStockBadgeColor(
                          existencia.cantidadDisponible,
                          existencia.cantidadMínima
                        )}
                      >
                        {getStockLabel(
                          existencia.cantidadDisponible,
                          existencia.cantidadMínima
                        )}
                      </Badge>
                    </td>
                    <td style={{ padding: '12px 16px', textAlign: 'center' }}>
                      <button
                        onClick={() => handleAjustarStock(existencia)}
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
                        Ajustar
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
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
        Mostrando {filteredExistencias.length} de {existencias.length} existencias
      </div>

      {/* Modal ajustar stock */}
      {showModal && selectedExistencia && (
        <AjustarStockModal
          existencia={selectedExistencia}
          onClose={handleCloseModal}
          onSuccess={handleSuccessAjuste}
        />
      )}
    </div>
  )
}
