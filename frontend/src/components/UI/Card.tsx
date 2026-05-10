/**
 * Componente Card para contenedores de contenido
 */

interface CardProps {
  children: React.ReactNode
  title?: string
  subtitle?: string
  padding?: string
  style?: React.CSSProperties
}

export default function Card({
  children,
  title,
  subtitle,
  padding = '20px',
  style,
}: CardProps) {
  return (
    <div
      style={{
        backgroundColor: 'white',
        borderRadius: '8px',
        boxShadow: '0 2px 8px rgba(0, 0, 0, 0.1)',
        padding,
        marginBottom: '20px',
        ...style,
      }}
    >
      {(title || subtitle) && (
        <div style={{ marginBottom: '15px' }}>
          {title && (
            <h3 style={{ margin: '0 0 5px 0', color: 'var(--neutral-900)' }}>
              {title}
            </h3>
          )}
          {subtitle && (
            <p style={{ margin: '0', color: 'var(--neutral-500)', fontSize: '14px' }}>
              {subtitle}
            </p>
          )}
        </div>
      )}
      {children}
    </div>
  )
}
