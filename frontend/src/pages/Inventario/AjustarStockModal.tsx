import { useState } from 'react'
import { inventarioService } from '@/services'
import type { Existencia, AjustarStockRequest } from '@/types'

interface AjustarStockModalProps {
  existencia: Existencia
  onClose: () => void
  onSuccess: () => void
}

export default function AjustarStockModal({
  existencia,
  onClose,
  onSuccess,
}: AjustarStockModalProps) {
  const [ajuste, setAjuste] = useState<number | string>('')
  const [observacion, setObservacion] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError(null)

    if (ajuste === '' || ajuste === 0) {
      setError('Debes ingresar un ajuste válido')
      return
    }

    if (!observacion.trim()) {
      setError('La observación es obligatoria')
      return
    }

    try {
      setLoading(true)

      const request: AjustarStockRequest = {
        ajuste: Number(ajuste),
        observacion: observacion.trim(),
      }

      await inventarioService.ajustarStock(existencia.idExistencia, request)
      onSuccess()
      onClose()
    } catch (err: any) {
      const mensaje = err?.response?.data?.mensaje || 'Error al ajustar el stock'
      setError(mensaje)
      console.error('Error:', err)
    } finally {
      setLoading(false)
    }
  }

  const nuevoStock = (Number(ajuste) || 0) + existencia.cantidadDisponible

  return (
    <div
      style={{
        position: 'fixed',
        inset: '0',
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        zIndex: '1000',
      }}
      onClick={onClose}
    >
      <div
        style={{
          backgroundColor: 'white',
          borderRadius: '8px',
          padding: '28px',
          maxWidth: '500px',
          width: '90%',
          boxShadow: '0 20px 25px -5px rgba(0, 0, 0, 0.1)',
        }}
        onClick={(e) => e.stopPropagation()}
      >
        {/* Header */}
        <div style={{ marginBottom: '24px' }}>
          <h2 style={{ margin: '0 0 8px 0', color: 'var(--neutral-900)', fontSize: '20px' }}>
            Ajustar Stock
          </h2>
          <p style={{ margin: '0', color: 'var(--neutral-600)', fontSize: '14px' }}>
            {existencia.nombreProducto} • {existencia.nombreBodega}
          </p>
        </div>

        {/* Error */}
        {error && (
          <div
            style={{
              backgroundColor: '#fee2e2',
              color: '#991b1b',
              padding: '12px',
              borderRadius: '6px',
              marginBottom: '20px',
              fontSize: '14px',
            }}
          >
            ⚠️ {error}
          </div>
        )}

        {/* Form */}
        <form onSubmit={handleSubmit}>
          {/* Stock actual */}
          <div style={{ marginBottom: '20px', padding: '16px', backgroundColor: 'var(--neutral-50)', borderRadius: '6px' }}>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: '16px' }}>
              <div>
                <label style={{ fontSize: '12px', color: 'var(--neutral-600)', fontWeight: '600', textTransform: 'uppercase' }}>
                  Stock Actual
                </label>
                <p style={{ margin: '8px 0 0 0', fontSize: '20px', fontWeight: '700', color: 'var(--neutral-900)' }}>
                  {existencia.cantidadDisponible}
                </p>
              </div>
              <div>
                <label style={{ fontSize: '12px', color: 'var(--neutral-600)', fontWeight: '600', textTransform: 'uppercase' }}>
                  Stock Mínimo
                </label>
                <p style={{ margin: '8px 0 0 0', fontSize: '20px', fontWeight: '700', color: 'var(--primary)' }}>
                  {existencia.cantidadMínima}
                </p>
              </div>
            </div>
          </div>

          {/* Ajuste */}
          <div style={{ marginBottom: '20px' }}>
            <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', fontWeight: '600', color: 'var(--neutral-700)' }}>
              Ajuste (+ para agregar, - para restar) *
            </label>
            <input
              type="number"
              required
              value={ajuste}
              onChange={(e) => setAjuste(e.target.value)}
              placeholder="Ej: 10 o -5"
              style={{
                width: '100%',
                padding: '10px',
                border: '1px solid var(--neutral-300)',
                borderRadius: '6px',
                fontSize: '14px',
              }}
            />
            <p style={{ margin: '8px 0 0 0', fontSize: '12px', color: 'var(--neutral-600)' }}>
              Nuevo stock: <strong>{nuevoStock >= 0 ? nuevoStock : 'Inválido'}</strong>
            </p>
          </div>

          {/* Observación */}
          <div style={{ marginBottom: '24px' }}>
            <label style={{ display: 'block', fontSize: '14px', marginBottom: '8px', fontWeight: '600', color: 'var(--neutral-700)' }}>
              Observación *
            </label>
            <textarea
              required
              value={observacion}
              onChange={(e) => setObservacion(e.target.value)}
              placeholder="Explica el motivo del ajuste (compra, dañado, etc.)"
              rows={3}
              style={{
                width: '100%',
                padding: '10px',
                border: '1px solid var(--neutral-300)',
                borderRadius: '6px',
                fontSize: '14px',
                fontFamily: 'inherit',
                resize: 'vertical',
              }}
            />
          </div>

          {/* Botones */}
          <div style={{ display: 'flex', gap: '12px', justifyContent: 'flex-end' }}>
            <button
              type="button"
              onClick={onClose}
              disabled={loading}
              style={{
                padding: '10px 16px',
                backgroundColor: 'var(--neutral-200)',
                color: 'var(--neutral-900)',
                border: 'none',
                borderRadius: '6px',
                cursor: loading ? 'not-allowed' : 'pointer',
                fontSize: '14px',
                fontWeight: '600',
              }}
            >
              Cancelar
            </button>
            <button
              type="submit"
              disabled={loading || nuevoStock < 0}
              style={{
                padding: '10px 16px',
                backgroundColor: loading || nuevoStock < 0 ? 'var(--neutral-300)' : 'var(--primary)',
                color: 'white',
                border: 'none',
                borderRadius: '6px',
                cursor: loading || nuevoStock < 0 ? 'not-allowed' : 'pointer',
                fontSize: '14px',
                fontWeight: '600',
              }}
            >
              {loading ? 'Ajustando...' : 'Ajustar Stock'}
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}
