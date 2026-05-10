import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider } from './context/AuthContext'
import ProtectedRoute from './components/ProtectedRoute'
import MainLayout from './components/Layout/MainLayout'
import LoginPage from './pages/Login/LoginPage'
import DashboardPage from './pages/Dashboard/DashboardPage'
import NotFoundPage from './pages/NotFound/NotFoundPage'
import PedidosPage from './pages/Pedidos/PedidosPage'
import CrearPedidoPage from './pages/Pedidos/CrearPedidoPage'
import DetallePedidoPage from './pages/Pedidos/DetallePedidoPage'
import InventarioPage from './pages/Inventario/InventarioPage'
import ReservasInventarioPage from './pages/Inventario/ReservasInventarioPage'
import EnviosPage from './pages/Envios/EnviosPage'
import CrearEnvioPage from './pages/Envios/CrearEnvioPage'
import DetalleEnvioPage from './pages/Envios/DetalleEnvioPage'
import SeguimientoEnvioPage from './pages/Envios/SeguimientoEnvioPage'

function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          {/* Rutas públicas */}
          <Route path="/login" element={<LoginPage />} />

          {/* Rutas protegidas */}
          <Route
            path="/*"
            element={
              <ProtectedRoute>
                <MainLayout>
                  <Routes>
                    <Route path="/" element={<Navigate to="/dashboard" replace />} />
                    <Route path="/dashboard" element={<DashboardPage />} />
                    
                    {/* Módulo Pedidos */}
                    <Route path="/pedidos" element={<PedidosPage />} />
                    <Route path="/pedidos/crear" element={<CrearPedidoPage />} />
                    <Route path="/pedidos/:idPedido" element={<DetallePedidoPage />} />
                    
                    {/* Módulo Inventario */}
                    <Route path="/inventario" element={<InventarioPage />} />
                    <Route path="/inventario/reservas" element={<ReservasInventarioPage />} />
                    
                    {/* Módulo Envíos */}
                    <Route path="/envios" element={<EnviosPage />} />
                    <Route path="/envios/crear" element={<CrearEnvioPage />} />
                    <Route path="/envios/:idEnvio" element={<DetalleEnvioPage />} />
                    <Route path="/envios/:idEnvio/seguimiento" element={<SeguimientoEnvioPage />} />
                    
                    {/* 404 */}
                    <Route path="*" element={<NotFoundPage />} />
                  </Routes>
                </MainLayout>
              </ProtectedRoute>
            }
          />
        </Routes>
      </Router>
    </AuthProvider>
  )
}

export default App
