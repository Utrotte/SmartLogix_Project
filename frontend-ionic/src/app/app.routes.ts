import { Route, Routes } from '@angular/router';
import { authCanMatch, authGuard, guestGuard } from './guards/auth.guard';

const privateRoute = (path: string, loader: NonNullable<Route['loadComponent']>): Route => ({
  path,
  canMatch: [authCanMatch],
  canActivate: [authGuard],
  loadComponent: loader
});

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  { path: 'login', canActivate: [guestGuard], loadComponent: () => import('./pages/login/login.page').then(m => m.LoginPage) },
  privateRoute('dashboard', () => import('./pages/dashboard/dashboard.page').then(m => m.DashboardPage)),
  privateRoute('inventario', () => import('./pages/inventario/inventario.page').then(m => m.InventarioPage)),
  privateRoute('pedidos', () => import('./pages/pedidos/pedidos.page').then(m => m.PedidosPage)),
  privateRoute('avisos', () => import('./pages/avisos/avisos.page').then(m => m.AvisosPage)),
  privateRoute('clientes', () => import('./pages/clientes/clientes.page').then(m => m.ClientesPage)),
  privateRoute('productos', () => import('./pages/productos/productos.page').then(m => m.ProductosPage)),
  privateRoute('bodegas', () => import('./pages/bodegas/bodegas.page').then(m => m.BodegasPage)),
  privateRoute('pagos', () => import('./pages/pagos/pagos.page').then(m => m.PagosPage)),
  privateRoute('envios', () => import('./pages/envios/envios.page').then(m => m.EnviosPage)),
  privateRoute('transportistas', () => import('./pages/transportistas/transportistas.page').then(m => m.TransportistasPage)),
  privateRoute('rutas', () => import('./pages/rutas/rutas.page').then(m => m.RutasPage)),

  privateRoute('home', () => import('./home/home.page').then(m => m.HomePage)),

  { path: '**', redirectTo: 'login' }
];
