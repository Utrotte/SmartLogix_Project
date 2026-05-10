/**
 * Componente LoadingSpinner para estados de carga
 */

interface LoadingSpinnerProps {
  size?: 'sm' | 'md' | 'lg'
  color?: string
}

export default function LoadingSpinner({
  size = 'md',
  color = 'var(--primary)',
}: LoadingSpinnerProps) {
  const sizeMap = {
    sm: { width: '20px', height: '20px' },
    md: { width: '40px', height: '40px' },
    lg: { width: '60px', height: '60px' },
  }

  const keyframes = `
    @keyframes spin {
      to { transform: rotate(360deg); }
    }
  `

  return (
    <>
      <style>{keyframes}</style>
      <div
        style={{
          ...sizeMap[size],
          border: `3px solid ${color}33`,
          borderTop: `3px solid ${color}`,
          borderRadius: '50%',
          animation: 'spin 1s linear infinite',
        }}
      />
    </>
  )
}
