/**
 * Componente Button reutilizable
 */

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary' | 'danger' | 'success'
  size?: 'sm' | 'md' | 'lg'
  children: React.ReactNode
}

export default function Button({
  variant = 'primary',
  size = 'md',
  children,
  disabled,
  ...props
}: ButtonProps) {
  const variantStyles = {
    primary: {
      backgroundColor: 'var(--primary)',
      color: 'white',
    },
    secondary: {
      backgroundColor: 'var(--neutral-200)',
      color: 'var(--neutral-900)',
    },
    danger: {
      backgroundColor: 'var(--danger)',
      color: 'white',
    },
    success: {
      backgroundColor: 'var(--success)',
      color: 'white',
    },
  }

  const sizeStyles = {
    sm: { padding: '6px 12px', fontSize: '14px' },
    md: { padding: '8px 16px', fontSize: '16px' },
    lg: { padding: '12px 24px', fontSize: '18px' },
  }

  return (
    <button
      style={{
        ...variantStyles[variant],
        ...sizeStyles[size],
        border: 'none',
        borderRadius: '4px',
        cursor: disabled ? 'not-allowed' : 'pointer',
        opacity: disabled ? 0.6 : 1,
        transition: 'opacity 0.2s',
        fontWeight: 500,
      }}
      disabled={disabled}
      {...props}
    >
      {children}
    </button>
  )
}
