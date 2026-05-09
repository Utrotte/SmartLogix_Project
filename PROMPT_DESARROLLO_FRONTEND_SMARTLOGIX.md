# 📋 PROMPT PARA DESARROLLO DE INTERFAZ FRONTEND - SmartLogix

## 🎯 CONTEXTO GENERAL DEL PROYECTO

**Nombre del Proyecto:** SmartLogix - Sistema de Gestión de Logística Inteligente  
**Alcance:** Plataforma web para administración de pedidos, inventario y envíos con autenticación centralizada  
**Arquitectura:** Microservicios Spring Boot 3.5.14 con Eureka Service Discovery  
**Base de Datos:** MySQL 8 con esquemas separados por servicio  
**Tipo de Cliente:** SPA (Single Page Application) - ReactJS, Vue, o Angular  
**URL de Producción:** localhost:3000 (Frontend) - Consumir APIs en puertos específicos

---

## 🏗️ ARQUITECTURA DE MICROSERVICIOS

### Estructura General
```
SmartLogix_Project/
├── smartlogix-eureka-server/        # Service Discovery (Puerto 8761)
├── ms_bff/ms_bff/                   # Backend For Frontend - Auth & Security (Puerto 8080)
├── ms-pedidos-smartlogix/           # Gestión de Pedidos (Puerto 8082)
├── ms_inventario/                   # Gestión de Inventario (Puerto 8081)
├── ms-envios/                       # Gestión de Envíos (Puerto 8080 → CONFLICTO, usar 8080)
└── frontend/                        # 🎨 NUEVO - Interfaz a desarrollar (Puerto 3000)
```

### Tabla de Microservicios
| Servicio | Puerto | Función | Base de Datos |
|----------|--------|---------|---------------|
| **Eureka Server** | 8761 | Service Discovery | - |
| **ms_bff** | 8080 | Login, Auth, Seguridad, Gateway | smartlogix_bff_db |
| **ms-pedidos** | 8082 | CRUD Pedidos, Estados | smartlogix_pedidos_db |
| **ms-inventario** | 8081 | Stock, Productos, Reservas | inventario_db |
| **ms-envios** | 8083 | Envíos, Transportistas, Seguimiento | smartlogix_envios_db |
| **Frontend** | 3000 | UI/UX - Angular/React/Vue | - |

---

## 🔐 AUTENTICACIÓN Y SEGURIDAD

### Flujo de Login

1. **Usuario ingresa credenciales** (correo + password) en formulario de login
2. **Frontend envía POST** a `/api/auth/login`
3. **Backend (ms_bff) valida**:
   - Credenciales correctas
   - Usuario activo
   - Genera SessionToken (JWT/custom)
4. **Respuesta exitosa incluye**:
   - Token de sesión (`tokenReferencia`)
   - ID Usuario
   - Nombre, Correo, Roles
   - Fecha de expiración (120 minutos por defecto)
5. **Frontend almacena token** en localStorage/sessionStorage
6. **Todas las peticiones** incluyen header `X-Session-Token: {token}`

### Endpoints de Autenticación (ms_bff - Puerto 8080)

#### POST /api/auth/login
```json
BODY:
{
  "correo": "usuario@example.com",
  "password": "MiPassword123!"
}

RESPONSE 200 OK:
{
  "idUsuario": 1,
  "nombre": "Juan Pérez",
  "correo": "usuario@example.com",
  "roles": ["ADMIN", "GERENTE_PEDIDOS"],
  "tokenReferencia": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "fechaExpiracion": "2026-05-09T10:30:45",
  "mensaje": "Login exitoso"
}

RESPONSE 401 UNAUTHORIZED:
{
  "success": false,
  "message": "Credenciales inválidas",
  "data": null
}
```

#### GET /api/auth/me
**Headers:** `X-Session-Token: {token}`

```json
RESPONSE 200 OK:
{
  "idUsuario": 1,
  "nombre": "Juan Pérez",
  "correo": "usuario@example.com",
  "roles": ["ADMIN", "GERENTE_PEDIDOS"]
}

RESPONSE 401 UNAUTHORIZED:
{
  "success": false,
  "message": "Token inválido o expirado",
  "data": null
}
```

#### POST /api/auth/logout
**Headers:** `X-Session-Token: {token}`

```json
RESPONSE 200 OK:
{
  "success": true,
  "message": "Logout exitoso",
  "data": null
}
```

---

## 📦 ENDPOINTS DE PEDIDOS (ms-pedidos-smartlogix - Puerto 8082)

### GET /api/pedidos
Listar todos los pedidos

```json
RESPONSE 200 OK:
[
  {
    "idPedido": 1,
    "idCliente": 5,
    "numeroReferencia": "PED-2026-001",
    "estadoActual": "CONFIRMADO",
    "fechaCreacion": "2026-05-08T14:30:00",
    "fechaConfirmacion": "2026-05-08T15:00:00",
    "montoTotal": 15000.00,
    "observacion": "Entregar con cuidado"
  },
  ...
]
```

### POST /api/pedidos
Crear nuevo pedido

```json
BODY:
{
  "idCliente": 5,
  "detalles": [
    {
      "idProducto": 10,
      "cantidad": 2,
      "precioUnitario": 7500.00
    },
    {
      "idProducto": 12,
      "cantidad": 1,
      "precioUnitario": 0.00
    }
  ],
  "direccion": {
    "calle": "Calle Principal 123",
    "ciudad": "Santiago",
    "region": "Metropolitana",
    "codigoPostal": "8320000",
    "instruccionesEspeciales": "Llamar antes de entregar"
  },
  "observacion": "Expedito"
}

RESPONSE 201 CREATED:
{
  "idPedido": 101,
  "idCliente": 5,
  "numeroReferencia": "PED-2026-101",
  "estadoActual": "PENDIENTE_CONFIRMACION",
  "fechaCreacion": "2026-05-09T10:15:00",
  "montoTotal": 15000.00
}
```

### GET /api/pedidos/{idPedido}
Obtener detalle de un pedido

```json
RESPONSE 200 OK:
{
  "idPedido": 1,
  "idCliente": 5,
  "numeroReferencia": "PED-2026-001",
  "estadoActual": "CONFIRMADO",
  "fechaCreacion": "2026-05-08T14:30:00",
  "montoTotal": 15000.00,
  "detalles": [
    {
      "idDetalle": 1,
      "idProducto": 10,
      "nombreProducto": "Laptop Dell",
      "cantidad": 2,
      "precioUnitario": 7500.00,
      "subtotal": 15000.00
    }
  ],
  "direccionEntrega": {
    "calle": "Calle Principal 123",
    "ciudad": "Santiago",
    "region": "Metropolitana"
  }
}
```

### GET /api/pedidos/cliente/{idCliente}
Listar pedidos de un cliente específico

```json
RESPONSE 200 OK:
[
  { "idPedido": 1, "numeroReferencia": "PED-2026-001", ... },
  { "idPedido": 2, "numeroReferencia": "PED-2026-002", ... }
]
```

### PATCH /api/pedidos/{idPedido}/estado
Cambiar estado del pedido

```json
PARAMETERS:
?nuevoEstado=CONFIRMADO
&usuarioResponsable=juan.perez
&observacion=Pago verificado

RESPONSE 200 OK:
{
  "idPedido": 1,
  "estadoActual": "CONFIRMADO",
  "fechaConfirmacion": "2026-05-09T10:30:00",
  "mensaje": "Estado actualizado exitosamente"
}
```

### DELETE /api/pedidos/{idPedido}
Eliminar un pedido

```json
RESPONSE 204 NO CONTENT
```

---

## 📊 ENDPOINTS DE INVENTARIO (ms-inventario - Puerto 8081)

### GET /api/inventario/existencias/producto/{idProducto}
Listar existencias de un producto en todas las bodegas

```json
RESPONSE 200 OK:
[
  {
    "idExistencia": 1,
    "idProducto": 10,
    "idBodega": 1,
    "nombreProducto": "Laptop Dell",
    "nombreBodega": "Bodega Central",
    "cantidadDisponible": 45,
    "cantidadReservada": 5,
    "cantidadMínima": 10
  },
  {
    "idExistencia": 2,
    "idProducto": 10,
    "idBodega": 2,
    "nombreProducto": "Laptop Dell",
    "nombreBodega": "Bodega Sur",
    "cantidadDisponible": 20,
    "cantidadReservada": 0,
    "cantidadMínima": 10
  }
]
```

### GET /api/inventario/existencias/bodega/{idBodega}
Listar todas las existencias en una bodega

```json
RESPONSE 200 OK:
[
  {
    "idExistencia": 1,
    "idProducto": 10,
    "nombreProducto": "Laptop Dell",
    "cantidadDisponible": 45,
    "cantidadReservada": 5,
    "cantidadMínima": 10
  },
  { ... }
]
```

### GET /api/inventario/existencias/producto/{idProducto}/bodega/{idBodega}
Obtener existencia específica

```json
RESPONSE 200 OK:
{
  "idExistencia": 1,
  "idProducto": 10,
  "idBodega": 1,
  "nombreProducto": "Laptop Dell",
  "nombreBodega": "Bodega Central",
  "cantidadDisponible": 45,
  "cantidadReservada": 5,
  "cantidadMínima": 10,
  "precioUnitario": 7500.00
}
```

### POST /api/inventario/existencias
Crear nueva existencia

```json
BODY:
{
  "idProducto": 10,
  "idBodega": 1,
  "cantidadDisponible": 100,
  "cantidadMínima": 10
}

RESPONSE 201 CREATED:
{
  "idExistencia": 5,
  "idProducto": 10,
  "idBodega": 1,
  "cantidadDisponible": 100
}
```

### PUT /api/inventario/existencias/{idExistencia}/ajustar-stock
Ajustar cantidad de stock

```json
BODY:
{
  "ajuste": -5,
  "observacion": "Ajuste por pérdida de mercancía"
}

RESPONSE 200 OK:
{
  "idExistencia": 1,
  "cantidadDisponible": 40,
  "cantidadReservada": 5,
  "mensaje": "Stock ajustado exitosamente"
}
```

---

## 🚚 ENDPOINTS DE ENVÍOS (ms-envios - Puerto 8080)

### POST /enviosvía BFF - Puerto 8080/8083)

### POST /api/bff/envios/
```json
BODY:
{
  "idPedidoRef": "1",
  "direccion": {
    "calle": "Calle Principal 123",
    "ciudad": "Santiago",
    "region": "Metropolitana",
    "codigoPostal": "8320000",
    "instrucciones": "Llamar al llegar"
  },
  "paquete": {
    "peso": 2.5,
    "dimensiones": "30x20x15",
    "contenido": "Electrónico"
  }
}

RESPONSE 201 CREATED:
{
  "idEnvio": "ENV-001",
  "idPedidoRef": "1",
  "estado": "PENDIENTE_ASIGNACION",
  "fechaCreacion": "2026-05-09T10:15:00",
  "mensaje": "Envío creado exitosamente"
}
```

### GET /api/bff/envios/
Listar todos los envíos

```json
RESPONSE 200 OK:
[
  {
    "idEnvio": "ENV-001",
    "idPedidoRef": "1",
    "estado": "EN_TRANSITO",
    "transportista": "Juan García",
    "fechaCreacion": "2026-05-09T10:15:00",
    "fechaEntrega": "2026-05-10T15:30:00",
    "direccion": "Calle Principal 123, Santiago"
  },
  { ... }
]
```

### GET /api/bff/envios/{idEnvio}
Obtener detalle de un envío

```json
RESPONSE 200 OK:
{
  "idEnvio": "ENV-001",
  "idPedidoRef": "1",
  "estado": "EN_TRANSITO",
  "transportista": "Juan García",
  "fechaCreacion": "2026-05-09T10:15:00",
  "direccion": {
    "calle": "Calle Principal 123",
    "ciudad": "Santiago",
    "region": "Metropolitana"
  },
  "seguimientos": [
    {
      "fecha": "2026-05-09T10:30:00",
      "estado": "RECIBIDO_EN_DEPOSITO",
      "ubicacion": "Centro de Distribución Santiago"
    },
    {
      "fecha": "2026-05-09T14:00:00",
      "estado": "EN_TRANSITO",
      "ubicacion": "Ruta Santiago - Valparaíso"
    }
  ]
}
```

### PATCH /api/bff/envios/{idEnvio}/estado
Actualizar estado del envío

```json
BODY:
{
  "nuevoEstado": "ENTREGADO",
  "observacion": "Entregado exitosamente"
}

RESPONSE 200 OK:
{
  "idEnvio": "ENV-001",
  "estado": "ENTREGADO",
  "fechaEntrega": "2026-05-10T15:30:00",
  "mensaje": "Estado actualizado exitosamente"
}
```

### PATCH /api/bff/envios/{idEnvio}/transportista
Asignar transportista al envío

```json
BODY:
{
  "idTransportista": 5
}

RESPONSE 200 OK:
{
  "idEnvio": "ENV-001",
  "estado": "ASIGNADO",
  "idTransportista": 5,
  "transportista": "Juan García",
  "fechaAsignacion": "2026-05-09T11:00:00"
}
```

### GET /api/bff/envios/{idEnvio}/seguimiento
Listar historial de seguimiento del envío

```json
RESPONSE 200 OK:
[
  {
    "idSeguimiento": 1,
    "idEnvio": "ENV-001",
    "estado": "RECIBIDO_EN_DEPOSITO",
    "fecha": "2026-05-09T10:30:00",
    "ubicacion": "Centro de Distribución Santiago",
    "observacion": "Paquete ingresado al sistema"
  },
  {
    "idSeguimiento": 2,
    "idEnvio": "ENV-001",
    "estado": "EN_TRANSITO",
    "fecha": "2026-05-09T14:00:00",
    "ubicacion": "Ruta Santiago - Valparaíso",
    "observacion": "Enviado con transportista Juan García"
  },
  {
    "idSeguimiento": 3,
    "idEnvio": "ENV-001",
    "estado": "ENTREGADO",
    "fecha": "2026-05-10T15:30:00",
    "ubicacion": "Destino final",
    "observacion": "Entregado al cliente"
  }
]
```

---

## 🎨 ESTRUCTURA DEL FRONTEND A DESARROLLAR

### Carpetas principales

```
src/
├── pages/
│   ├── Login/                       # 🔐 Login & Autenticación
│   ├── Dashboard/                   # 📊 Panel Principal
│   ├── Pedidos/
│   │   ├── ListarPedidos/
│   │   ├── CrearPedido/
│   │   ├── DetallesPedido/
│   │   └── EstadoPedido/
│   ├── Inventario/
│   │   ├── ListarInventario/
│   │   ├── GestionarStock/
│   │   └── ReservasInventario/
│   ├── Envios/
│   │   ├── ListarEnvios/
│   │   ├── CrearEnvio/
│   │   ├── DetallesEnvio/
│   │   ├── Seguimiento/
│   │   └── AsignarTransportista/
│   └── NotFound/
├── components/
│   ├── Navbar/                      # Barra de navegación
│   ├── Sidebar/                     # Menú lateral
│   ├── Card/                        # Componente reutilizable
│   ├── Modal/                       # Diálogos
│   ├── Table/                       # Tablas dinámicas
│   ├── Form/                        # Formularios
│   └── Toast/                       # Notificaciones
├── services/
│   ├── authService.js               # Manejo de Login/Logout
│   ├── pedidosService.js            # Llamadas a ms-pedidos
│   ├── inventarioService.js         # Llamadas a ms-inventario
│   ├── enviosService.js             # Llamadas a ms-envios
│   └── apiClient.js                 # Cliente HTTP con interceptor
├── hooks/
│   ├── useAuth.js                   # Gestión de autenticación
│   ├── useFetch.js                  # Fetching de datos
│   └── useForm.js                   # Validación de formularios
├── context/
│   ├── AuthContext.js               # Contexto global de auth
│   └── AppContext.js                # Contexto global de la app
├── utils/
│   ├── formatters.js                # Funciones de formato (fecha, moneda)
│   ├── validators.js                # Validadores
│   └── constants.js                 # Constantes globales
├── styles/
│   ├── globals.css                  # Estilos globales
│   ├── colors.css                   # Paleta de colores
│   └── responsive.css               # Media queries
└── App.jsx
    └── index.jsx
```

---

## 🖼️ PÁGINAS PRINCIPALES A DESARROLLAR

### 1️⃣ **Página de Login** (Acceso Público)
- Formulario con campos: Email y Contraseña
- Validación en tiempo real
- Mostrar errores clara
- Botón "Olvidé mi contraseña" (opcional)
- Redirect a Dashboard si ya está autenticado
- Token guardado en localStorage/sessionStorage

**Flujo:**
1. Usuario ingresa credenciales
2. POST a `/api/auth/login`
3. Guardar token y datos de usuario
4. Redirect a `/dashboard`

### 2️⃣ **Dashboard** (Acceso Autenticado)
- Resumen de KPIs: Total pedidos, Pendientes, Inventario bajo, Envíos activos
- Gráficos de tendencias (últimos 30 días)
- Tabla rápida de últimos 5 pedidos
- Accesos directos a módulos principales
- Información del usuario conectado (nombre, rol)

### 3️⃣ **Gestión de Pedidos**

#### 3.1 Listar Pedidos
- Tabla con paginación (20 por página)
- Filtros: Estado, Fecha, Cliente, Rango montos
- Búsqueda por referencia o ID
- Columnas: ID, Referencia, Cliente, Estado, Fecha, Total
- Acciones: Ver Detalles, Cambiar Estado, Eliminar
- Botón: Crear Nuevo Pedido

#### 3.2 Crear Pedido
- Formulario multi-paso:
  1. Seleccionar Cliente
  2. Agregar Productos (búsqueda + cantidad)
  3. Ingresar Dirección de Entrega
  4. Revisar y Confirmar
- Validar cantidad disponible en inventario
- Mostrar subtotal y total dinámicamente
- Botones: Guardar, Cancelar, Volver

#### 3.3 Detalles del Pedido
- Mostrar información completa
- Lista de artículos con precios
- Dirección de entrega
- Historial de cambios de estado
- Información del envío asociado (si existe)
- Botones: Cambiar Estado, Editar, Cancelar

#### 3.4 Cambiar Estado
- Modal/Form con dropdown de estados válidos
- Campo de observación obligatorio
- Validar transiciones de estado permitidas
- Guardar cambio y actualizar tabla
 Envío, ID Pedido, Estado, Transportista, Fecha Creación, Acción
- Filtros: Estado, Fecha, Transportista, Rango de fechas
- Búsqueda por ID de envío o ID de pedido
- Iconos de estado (Pendiente Asignación, Asignado, En Tránsito, Entregado, etc.)
- Ordenable por columnas

#### 5.2 Crear Envío (desde Pedido)
- Buscar/Seleccionar Pedido CONFIRMADO
- Validar que el pedido tenga stock en inventario (validación en real-time)
- Confirmar/Editar dirección de entrega (desde pedido)
- Especificar contenido del paquete (peso, dimensiones, tipo)
- Guardar usando POST `/api/bff/envios/`
- Mostrar confirmación con ID del envío generado

#### 5.3 Detalles del Envío
- Información principal (ID Envío, ID Pedido Ref, Estado Actual)
- Fechas: Creación, Asignación, Entrega
- Dirección de entrega completa
- Información del paquete
- Transportista asignado (nombre, contacto)
- Timeline visual de seguimiento (con GET `/api/bff/envios/{idEnvio}/seguimiento`)
- Botones: Asignar Transportista, Cambiar Estado, Imprimir Etiqueta (futuro)

#### 5.4 Seguimiento de Envío (Public/Login)
- Timeline visual interactivo de eventos del envío
- Mostrar cada evento: Fecha, Estado, Ubicación, Observación
- Estados mostrados en orden cronológico
- Información del transportista actual
- Contacto/Teléfono del transportista

#### 5.5 Asignar Transportista
- Modal/Form para seleccionar transportista disponible
- Mostrar lista de transportistas con carga actual
- Seleccionar y confirmar asignación
- PATCH a `/api/bff/envios/{idEnvio}/transportista`
- Estado cambia automáticamente a "ASIGNADO"
- Confirmación visual en tablantregado, etc.)

#### 5.2 Crear Envío
- Seleccionar Pedido CONFIRMADO
- Validar que el pedido tenga stock
- Ingresar/Confirmar dirección de entrega
- Especificar contenido del paquete (peso, dimensiones)
- Guardar y mostrar confirmación

#### 5.3 Detalles del Envío
- Información principal (ID, Estado, Fechas)
- Dirección de entrega
- Información del paquete
- Transportista asignado
- Historial de seguimiento (tabla o timeline)
- Botones: Asignar Transportista, Cambiar Estado, Imprimir Etiqueta

#### 5.4 Seguimiento de Envío
- Timeline visual de eventos del envío
- Mostrar: Fecha, Estado, Ubicación, Detalles
- Mapa interactivo (opcional) con ubicación actual
- Información del transportista
- Teléfono o contacto para consultas

#### 5.5 Asignar Transportista
- Modal/Form para seleccionar transportista
- Mostrar disponibilidad y carga actual
- Guardar asignación
- Cambiar estado a "ASIGNADO" automáticamente

---

## 🔧 STACK TECNOLÓGICO RECOMENDADO

### Opción 1: React + TypeScript (Recomendado)
```json
{
  "dependencies": {
    "react": "^18.2.0",
    "react-dom": "^18.2.0",
    "react-router-dom": "^6.x",
    "axios": "^1.x",
    "zustand": "^4.x",
    "react-query": "^3.x",
    "tailwindcss": "^3.x",
    "react-hook-form": "^7.x",
    "zod": "^3.x"
  },
  "devDependencies": {
    "typescript": "^5.x",
    "vite": "^4.x"
  }
}
```

### Opción 2: Vue 3 + TypeScript
```json
{
  "dependencies": {
    "vue": "^3.3.0",
    "vue-router": "^4.x",
    "pinia": "^2.x",
    "axios": "^1.x",
    "tailwindcss": "^3.x"
  }
}
```

### Opción 3: Angular 16+
```json
{
  "dependencies": {
    "@angular/core": "^16.x",
    "@angular/router": "^16.x",
    "@angular/material": "^16.x",
    "rxjs": "^7.x"
  }
}
```

---

## 📡 CONFIGURACIÓN DE CLIENTE HTTP (API)

### Cliente Axios con Interceptor (React)
```javascript
// services/apiClient.js
import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor para agregar token
apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem('sessionToken');
  if (token) {
    config.headers['X-Session-Token'] = token;
  }
  return config;
});

// Interceptor para manejar errores
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Token expirado o inválido
      localStorage.removeItem('sessionToken');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default apiClient;
```

---

## 🔒 MANEJO DE SESIÓN Y TOKENS

### Almacenamiento del Token
```javascript
// Después de login exitoso
localStorage.setItem('sessionToken', response.data.tokenReferencia);
localStorage.setItem('usuario', JSON.stringify({
  id: response.data.idUsuario,
  nombre: response.data.nombre,
  correo: response.data.correo,
  roles: response.data.roles,
  expiracion: response.data.fechaExpiracion
}));
```

### Validación de Sesión en Rutas Protegidas
```javascript
// Componente ProtectedRoute
const ProtectedRoute = ({ children }) => {
  const token = localStorage.getItem('sessionToken');
  const usuario = JSON.parse(localStorage.getItem('usuario') || '{}');
  
  if (!token) {
    return <Navigate to="/login" />;
  }
  
  return children;
};
```

### Logout
```javascript
const logout = () => {
  // Llamar a API
  await apiClient.post('/api/auth/logout');
  
  // Limpiar storage
  localStorage.removeItem('sessionToken');
  localStorage.removeItem('usuario');
  
  // Redirigir
  navigate('/login');
};
```

---

## 🎨 GUÍA DE ESTILOS Y DISEÑO

### Paleta de Colores
```css
--primary: #0066cc;           /* Azul corporativo */
--secondary: #ff9900;         /* Naranja */
--success: #22c55e;           /* Verde */
--warning: #f59e0b;           /* Amarillo */
--danger: #ef4444;            /* Rojo */
--info: #3b82f6;              /* Azul */
--neutral-50: #f9fafb;        /* Blanco */
--neutral-900: #111827;       /* Negro */
--neutral-500: #6b7280;       /* Gris */
```

### Estados de Pedidos (Badges)
- **PENDIENTE_CONFIRMACION**: Gris
- **CONFIRMADO**: Verde
- **EN_PREPARACION**: Azul
- **LISTO_PARA_ENVIO**: Naranja
- **CANCELADO**: Rojo

### Estados de Envíos (Badges)
- **PENDIENTE_ASIGNACION**: Gris
- **ASIGNADO**: Azul
- **EN_TRANSITO**: Naranja
- **ENTREGADO**: Verde
- **INCIDENCIA**: Rojo

---

## 📋 FUNCIONALIDADES PRINCIPALES

### Por Rol de Usuario

#### 👤 Rol: ADMIN
- Acceso a todos los módulos
- Ver Dashboard con todos los KPIs
- CRUD completo de Pedidos, Inventario, Envíos
- Cambiar estados
- Gestión de usuarios (crear, editar, eliminar)

#### 📦 Rol: GERENTE_PEDIDOS
- Ver Dashboard (pedidos específicamente)
- CRUD Pedidos
- Ver Inventario (read-only)
- Cambiar estado de Pedidos
- Crear Envíos (desde pedidos confirmados)

#### 📊 Rol: GERENTE_INVENTARIO
- Ver Dashboard (inventario específicamente)
- Ver Inventario
- Ajustar Stock
- Ver Reservas
- Reportes de stock

#### 🚚 Rol: GERENTE_ENVIOS
- Ver Dashboard (envíos específicamente)
- Listar Envíos
- Cambiar estado de Envíos
- Asignar Transportistas
- Ver Seguimiento

---

## 🧪 TESTING Y VALIDACIONES

### Validaciones en Formularios
- Email válido
- Contraseña mínimo 8 caracteres3
- Números positivos para cantidades
- Campos obligatorios

### Manejo de Errores
- Mostrar mensajes claros en toast/alerts
- Capturar errores de red
- Reintentos automáticos (opcional)
- Logging de errores

---

## 🚀 INSTRUCCIONES DE DESARROLLO

### 1. Estructura del Proyecto
```bash
npm create vite@latest smartlogix-frontend -- --template react-ts
cd smartlogix-frontend
npm install
```

### 2. Instalación de Dependencias
```bash
npm install axios react-router-dom zustand react-query tailwindcss
npm install -D typescript eslint prettier
```

### 3. Variables de Entorno
```env
# .env
VITE_API_BFF_URL=http://localhost:8080
VITE_API_PEDIDOS_URL=http://localhost:8082
VITE_API_INVENTARIO_URL=http://localhost:8081
VITE_API_ENVIOS_URL=http://localhost:8080
VITE_APP_NAME=SmartLogix
```

### 4. Rutas Principales
```
/login                          - Login público
/dashboard                      - Panel principal
/pedidos                        - Listar pedidos
/pedidos/crear                  - Crear pedido
/pedidos/:id                    - Detalle de pedido
/inventario                     - Listar inventario
/inventario/ajustar             - Ajustar stock
/envios                         - Listar envíos
/envios/crear                   - Crear envío
/envios/:id                     - Detalle de envío
/envios/:id/seguimiento         - Seguimiento
/usuarios                       - Gestión de usuarios (ADMIN)
/perfil                         - Perfil del usuario
/logout                         - Cerrar sesión
```

### 5. Comandos de Desarrollo
```bash
npm run dev                 # Iniciar servidor de desarrollo (Vite)
npm run build              # Compilar para producción
npm run preview            # Vista previa de build
npm run lint               # Verificar código
npm run format             # Formatear código
```

---

## 📝 CONVENCIONES DE CÓDIGO

### Nombres de Archivos
- Componentes: `PascalCase.jsx` → `LoginForm.jsx`
- Services: `camelCase.js` → `authService.js`
- Hooks: `camelCase.js` con prefijo `use` → `useAuth.js`
- Constantes: `UPPER_SNAKE_CASE` → `API_BASE_URL`

### Estructura de Componentes
```jsx
// LoginForm.jsx
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import authService from '../../services/authService';

export default function LoginForm() {
  const [formData, setFormData] = useState({ correo: '', password: '' });
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await authService.login(formData);
      localStorage.setItem('sessionToken', response.tokenReferencia);
      navigate('/dashboard');
    } catch (err) {
      setError(err.message);
    } (búsqueda en ms-pedidos)
3. Validar que tiene stock (llamar a inventario)
4. Confirmar/Editar dirección de entrega
5. POST a `/api/bff/envios/` con detalles del paquete
6. Redirect a `/envios/{idEnvioSubmit}>
      {/* Formulario JSX */}
    </form>
  );
} (búsqueda desde ms-envios)
3. Seleccionar y confirmar
4. PATCH a `/api/bff/envios/{idEnvio}/transportista` con idTransportista en body
5. Estado cambia automáticamente a ASIGNADO

## 🔗 FLUJOS DE INTEGRACIÓN PRINCIPALES

### Flujo 1: Crear Pedido
1. Ir a `/pedidos/crear`
2. Seleccionar cliente (consultar ms-pedidos GET /clientes)
3. Agregar productos (búsqueda desde ms-inventario)
4. Validar disponibilidad en real-time
5. Ingresar dirección de entrega
6. POST a `/api/pedidos` en ms-pedidos
7. Mostrar confirmación con ID del pedido

### Flujo 2: Cambiar Estado de Pedido
1. Click en "Cambiar Estado" en listado
2. Modal con dropdown de estados válidos
3. Campo de observación (required)
4. PATCH a `/api/pedidos/{id}/estado`
5. Refrescar tabla y mostrar toast de éxito

### Flujo 3: Crear Envío desde Pedido
1. Ir a `/envios/crear`
2. Seleccionar pedido CONFIRMADO
3. Validar que tiene stock (llamar a inventario)
4. Confirmar/Editar dirección
5. POST a `/envios` en ms-envios
6. Redirect a `/envios/{id}`

### Flujo 4: Asignar Transportista
1. En detalles de envío, click "Asignar Transportista"
2. Modal con lista de transportistas disponibles
3. Seleccionar y confirmar
4. PUT a `/envios/{id}/asignar-transportista/{idTransportista}`
5. Cambiar estado a ASIGNADO automáticamente

---

## 📞 CONTACTO Y SOPORTE

- **Repositorio:** https://github.com/Utrotte/SmartLogix_Project
- **Rama Frontend:** `feature/frontend` (crear)
- **Issues:** Usar GitHub Issues con etiquetas `frontend`, `bug`, `feature`
- **Commits:** Formato `feat(pedidos): crear modal de cambio de estado`

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

- [ ] Setup inicial del proyecto (Vite + React)
- [ ] Instalación de dependencias
- [ ] Configuración de rutas (React Router)
- [ ] Componente de Login (form + validación)
- [ ] AuthContext y Hook useAuth
- [ ] Cliente HTTP (axios + interceptors)
- [ ] Componente ProtectedRoute
- [ ] Dashboard base
- [ ] Módulo Pedidos (CRUD)
- [ ] Módulo Inventario (CRUD)
- [ ] Módulo Envíos (CRUD)
- [ ] Gestión de errores globales
- [ ] Notificaciones (Toast)
- [ ] Responsive Design (Mobile)
- [ ] Pruebas unitarias básicas
- [ ] Documentación de componentes
- [ ] Build y deployable

---

## 📚 RECURSOS ÚTILES

- [React Documentation](https://react.dev)
- [Vite Documentation](https://vitejs.dev)
- [TailwindCSS](https://tailwindcss.com)
- [React Router](https://reactrouter.com)
- [Axios Documentation](https://axios-http.com)

---

**Fecha de Creación:** 2026-05-09  
**Versión:** 1.0  
**Estado:** Listo para desarrollo

