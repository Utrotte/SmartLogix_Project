/**
 * Componente EmptyState para mostrar cuando no hay datos
 */

interface EmptyStateProps {
  icon?: string
  title: string
  description?: string
  action?: {
    label: string
    onClick: () => void
  }
}

export default function EmptyState({
  icon = '📭',
  title,
  description,
  action,
}: EmptyStateProps) {
  return (
    <div
      style={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        padding: '60px 20px',
        textAlign: 'center',
        backgroundColor: 'var(--neutral-50)',
        borderRadius: '8px',
        minHeight: '300px',
      }}
    >
      <div style={{ fontSize: '64px', marginBottom: '20px' }}>{icon}</div>
      <h3 style={{ margin: '0 0 10px 0', color: 'var(--neutral-900)' }}>{title}</h3>
      {description && (
        <p style={{ margin: '0 0 20px 0', color: 'var(--neutral-500)', maxWidth: '400px' }}>
          {description}
        </p>
      )}
      {action && (
        <button
          onClick={action.onClick}
          style={{
            backgroundColor: 'var(--primary)',
            color: 'white',
            padding: '10px 20px',
            borderRadius: '4px',
            border: 'none',
            cursor: 'pointer',
            fontSize: '16px',
            fontWeight: 500,
          }}
        >
          {action.label}
        </button>
      )}
    </div>
  )
}
