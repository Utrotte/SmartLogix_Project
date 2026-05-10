/**
 * Componente Badge para etiquetas de estado
 */

interface BadgeProps {
  children: React.ReactNode
  variant?: 'default' | 'success' | 'danger' | 'warning' | 'info'
}

export default function Badge({ children, variant = 'default' }: BadgeProps) {
  const variantStyles = {
    default: {
      backgroundColor: 'var(--neutral-200)',
      color: 'var(--neutral-900)',
    },
    success: {
      backgroundColor: '#d1fae5',
      color: '#065f46',
    },
    danger: {
      backgroundColor: '#fee2e2',
      color: '#991b1b',
    },
    warning: {
      backgroundColor: '#fef3c7',
      color: '#92400e',
    },
    info: {
      backgroundColor: '#dbeafe',
      color: '#0c2d6b',
    },
  }

  return (
    <span
      style={{
        display: 'inline-block',
        padding: '4px 12px',
        borderRadius: '12px',
        fontSize: '12px',
        fontWeight: 600,
        ...variantStyles[variant],
      }}
    >
      {children}
    </span>
  )
}
