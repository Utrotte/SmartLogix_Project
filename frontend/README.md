# SmartLogix Frontend

Frontend de la plataforma SmartLogix - Sistema de Gestión de Logística Inteligente.

## 🏗️ Estructura del Proyecto

```
src/
├── components/
│   ├── Layout/          # Componentes de layout (Navbar, Sidebar, MainLayout)
│   └── UI/              # Componentes reutilizables (Buttons, Cards, Modals, etc.)
├── context/             # Contextos de React (Auth, App)
├── hooks/               # Hooks personalizados
├── pages/               # Páginas principales
│   ├── Login/
│   ├── Dashboard/
│   ├── Pedidos/
│   ├── Inventario/
│   ├── Envios/
│   └── NotFound/
├── services/            # Servicios HTTP (apiClient, authService, etc.)
├── types/               # Definiciones de tipos TypeScript
├── utils/               # Funciones de utilidad
├── App.tsx              # Componente raíz
├── main.tsx             # Punto de entrada
└── index.css            # Estilos globales
```

## 🚀 Comenzar

### Requisitos
- Node.js 16+
- npm o yarn

### Instalación

```bash
cd frontend
npm install
```

### Desarrollo

```bash
npm run dev
```

La aplicación estará disponible en `http://localhost:3000`.

### Build

```bash
npm run build
```

## 🔌 Arquitectura

- **API Base:** `http://localhost:8080` (BFF)
- **Cliente HTTP:** `axios` con interceptores para autenticación
- **Autenticación:** Session Token (header `X-Session-Token`)
- **Enrutamiento:** React Router v6
- **Estado:** React Context + LocalStorage

## 📋 Módulos Principales

- **Login:** Autenticación de usuarios
- **Dashboard:** Panel principal con KPIs
- **Pedidos:** CRUD de pedidos y gestión de estados
- **Inventario:** Gestión de stock y reservas
- **Envíos:** Gestión de envíos y seguimiento

## 🔐 Flujo de Autenticación

1. Usuario ingresa credenciales en `/login`
2. Frontend envía POST a `/api/auth/login` (BFF)
3. BFF responde con token y datos del usuario
4. Frontend almacena token en localStorage
5. Todas las peticiones incluyen header `X-Session-Token`
6. En caso de 401, limpiar sesión y redirigir a `/login`

## 📝 Notas de Desarrollo

### Reglas Permanentes

1. ✅ Frontend consume **SOLO el BFF** en `http://localhost:8080`
2. ✅ NO hacer llamadas directas a microservicios (8081, 8082, 8083)
3. ✅ El BFF intermedia con los microservicios usando Eureka/OpenFeign
4. ✅ El cliente HTTP se define en `src/services/apiClient.ts`
5. ✅ Usar `VITE_API_BFF_URL` como URL base
6. ✅ Sin rutas duplicadas
7. ✅ Sin cambios de nombres sin avisar
8. ✅ Sin librerías adicionales sin justificación
9. ✅ Funcionalidad > Estética
10. ✅ Después de cada fase, el proyecto debe compilar
11. ✅ Endpoints faltantes: comentar que requieren implementación backend

## 🎨 Estilos

Se utiliza CSS vanilla con variables CSS. Ver `src/index.css` para la paleta de colores.

## 📚 Referencias

- [React Documentation](https://react.dev)
- [Vite Documentation](https://vitejs.dev)
- [React Router](https://reactrouter.com)
- [Axios](https://axios-http.com)
