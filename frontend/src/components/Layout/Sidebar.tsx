import { Link, useLocation } from 'react-router-dom'

interface NavLink {
  icon: string
  label: string
  path: string
}

const navLinks: NavLink[] = [
  { icon: '📊', label: 'Dashboard', path: '/dashboard' },
  { icon: '📦', label: 'Pedidos', path: '/pedidos' },
  { icon: '📈', label: 'Inventario', path: '/inventario' },
  { icon: '🚚', label: 'Envíos', path: '/envios' },
]

export default function Sidebar() {
  const location = useLocation()
  const currentPath = location.pathname
  
  const isActive = (path: string) => {
    return currentPath === path || currentPath.startsWith(path + '/')
  }

  return (
    <aside style={{
      width: '250px',
      backgroundColor: 'var(--neutral-900)',
      color: 'var(--neutral-50)',
      padding: '30px 20px',
      overflowY: 'auto',
      height: '100vh',
      boxShadow: '2px 0 8px rgba(0, 0, 0, 0.15)',
    }}>
      {/* Logo */}
      <h2 style={{
        margin: '0 0 40px 0',
        padding: '0 10px',
        color: 'var(--primary)',
        fontSize: '24px',
        fontWeight: 700,
      }}>
        ◆ SL
      </h2>

      {/* Navigation */}
      <nav>
        <ul style={{ listStyle: 'none', margin: 0, padding: 0 }}>
          {navLinks.map((link) => {
            const active = isActive(link.path)
            return (
              <li key={link.path} style={{ marginBottom: '10px' }}>
                <Link
                  to={link.path}
                  style={{
                    color: active ? 'var(--primary)' : 'var(--neutral-300)',
                    display: 'flex',
                    alignItems: 'center',
                    gap: '12px',
                    padding: '12px 15px',
                    borderRadius: '6px',
                    backgroundColor: active ? 'rgba(0, 102, 204, 0.1)' : 'transparent',
                    textDecoration: 'none',
                    fontSize: '15px',
                    fontWeight: active ? 600 : 500,
                    transition: 'all 0.2s',
                    borderLeft: active ? '3px solid var(--primary)' : '3px solid transparent',
                    paddingLeft: '12px',
                  }}
                >
                  <span style={{ fontSize: '20px' }}>{link.icon}</span>
                  <span>{link.label}</span>
                </Link>
              </li>
            )
          })}
        </ul>
      </nav>

      {/* Footer info */}
      <div style={{
        marginTop: '40px',
        paddingTop: '20px',
        borderTop: '1px solid var(--neutral-700)',
        fontSize: '12px',
        color: 'var(--neutral-400)',
        textAlign: 'center',
      }}>
        <p style={{ margin: '0' }}>SmartLogix v1.0</p>
        <p style={{ margin: '5px 0 0 0' }}>Sistema de Logística</p>
      </div>
    </aside>
  )
}
