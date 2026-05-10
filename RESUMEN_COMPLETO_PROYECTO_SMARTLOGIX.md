# Resumen Completo del Proyecto SmartLogix

Este documento resume la arquitectura completa del proyecto SmartLogix para que pueda ser entregado a otra IA o a una persona técnica que necesite entender rápidamente la solución, su estructura y sus endpoints.

## 1. Visión general

SmartLogix es una solución compuesta por varios sistemas Spring Boot y un frontend React. La arquitectura está dividida en:

- Un frontend React + TypeScript
- Un BFF central en Spring Boot
- Tres microservicios funcionales de dominio
- Un servidor Eureka para descubrimiento de servicios

El objetivo general es gestionar usuarios, pedidos, inventario y envíos con separación clara de responsabilidades.

## 2. Arquitectura general

### Componentes principales

- **Frontend**: interfaz de usuario en React
- **ms_bff**: backend for frontend que centraliza autenticación y exposición de endpoints consumibles por el frontend
- **ms-pedidos-smartlogix**: microservicio de pedidos
- **ms_inventario**: microservicio de inventario
- **ms-envios**: microservicio de envíos
- **smartlogix-eureka-server**: registro y descubrimiento de servicios

### Flujo general de comunicación

- El frontend consume principalmente el BFF
- El BFF enruta y consolida llamadas hacia los microservicios internos
- Los microservicios también pueden comunicarse entre sí mediante Feign/clients internos cuando es necesario
- Eureka permite que los servicios se registren y sean descubiertos

## 3. Puertos y aplicaciones

| Proyecto | Nombre de aplicación | Puerto |
|---|---|---:|
| smartlogix-eureka-server | smartlogix-eureka-server | 8761 |
| ms_bff | ms-bff | 8080 |
| ms-inventario | ms-inventario | 8081 |
| ms-pedidos-smartlogix | ms-pedidos-smartlogix | 8082 |
| ms-envios | ms-envios | 8083 |
| frontend | SmartLogix Frontend | 3000 |

## 4. Estructura del repositorio

### Raíz del workspace

- `frontend/`
- `ms_bff/`
- `ms_inventario/`
- `ms-pedidos-smartlogix/`
- `ms-envios/`
- `smartlogix-eureka-server/`

## 5. Frontend

### Tecnología

- React 18
- TypeScript en modo strict
- Vite
- React Router v6
- Axios con cliente centralizado
- Sin librerías UI externas

### Estructura general del frontend

- `src/components/` componentes reutilizables
- `src/components/Layout/` layout general
- `src/components/UI/` componentes visuales base
- `src/context/` contexto de autenticación
- `src/hooks/` hooks reutilizables
- `src/pages/` páginas por módulo
- `src/services/` servicios HTTP
- `src/types/` tipos TypeScript centralizados
- `src/utils/` utilidades

### Módulos funcionales del frontend

- **Login**: autenticación
- **Dashboard**: resumen general
- **Pedidos**: listado, creación, detalle, cambio de estado
- **Inventario**: productos, bodegas, existencias, reservas, ajustes de stock
- **Envíos**: listado, creación, detalle, seguimiento, cambio de estado, asignación de transportista

### Rutas del frontend

- `/login`
- `/dashboard`
- `/pedidos`
- `/pedidos/crear`
- `/pedidos/:idPedido`
- `/inventario`
- `/inventario/reservas`
- `/envios`
- `/envios/crear`
- `/envios/:idEnvio`
- `/envios/:idEnvio/seguimiento`
- `/404`

### Patrón de consumo

- El frontend no llama directamente a los microservicios
- Consume el BFF mediante una URL base configurada por variable de entorno
- El token de sesión se inyecta en las llamadas HTTP mediante interceptores

## 6. BFF

El BFF concentra el acceso del frontend y expone endpoints de alto nivel para login, dashboard, usuarios, roles, bitácora y los módulos funcionales de pedidos, inventario y envíos.

### Estructura general del BFF

- `controller/`: controladores REST
- `service/`: lógica de negocio y orquestación
- `client/`: clientes hacia microservicios
- `dto/`: objetos de entrada y salida
- `model/`: entidades de persistencia
- `repository/`: acceso a datos
- `config/`: configuración de seguridad, CORS y carga inicial
- `exception/`: manejo centralizado de errores
- `security/`: filtros y autenticación por sesión

### Endpoints públicos y de alto nivel del BFF

#### Ping y salud
- `GET /api/bff/ping`

#### Autenticación
- `POST /api/auth/login`
- `POST /api/auth/logout`
- `GET /api/auth/me`

#### Dashboard
- `GET /api/bff/dashboard/resumen`

#### Usuarios
- `POST /api/usuarios/`
- `GET /api/usuarios/`
- `GET /api/usuarios/{idUsuario}`
- `PUT /api/usuarios/{idUsuario}`
- `PATCH /api/usuarios/{idUsuario}/desactivar`

#### Roles
- `POST /api/roles/`
- `GET /api/roles/`
- `GET /api/roles/{idRol}`

#### Bitácora
- `GET /api/bitacora/`
- `GET /api/bitacora/servicio/{servicioDestino}`
- `GET /api/bitacora/usuario/{idUsuario}`

### BFF de Pedidos
Base path: `/api/bff/pedidos`

- `GET /api/bff/pedidos/`
- `POST /api/bff/pedidos/`
- `GET /api/bff/pedidos/{idPedido}`
- `PUT /api/bff/pedidos/{idPedido}`
- `PATCH /api/bff/pedidos/{idPedido}/cancelar`
- `PATCH /api/bff/pedidos/{idPedido}/estado`

### BFF de Inventario
Base path: `/api/bff/inventario`

- `POST /api/bff/inventario/categorias`
- `GET /api/bff/inventario/categorias`
- `GET /api/bff/inventario/categorias/{id}`
- `PUT /api/bff/inventario/categorias/{id}`
- `PATCH /api/bff/inventario/categorias/{id}/desactivar`
- `POST /api/bff/inventario/productos`
- `GET /api/bff/inventario/productos`
- `GET /api/bff/inventario/productos/{id}`
- `GET /api/bff/inventario/productos/sku/{codigoSku}`
- `PUT /api/bff/inventario/productos/{id}`
- `PATCH /api/bff/inventario/productos/{id}/desactivar`
- `POST /api/bff/inventario/bodegas`
- `GET /api/bff/inventario/bodegas`
- `GET /api/bff/inventario/bodegas/{id}`
- `PUT /api/bff/inventario/bodegas/{id}`
- `PATCH /api/bff/inventario/bodegas/{id}/desactivar`
- `POST /api/bff/inventario/existencias`
- `GET /api/bff/inventario/existencias/producto/{idProducto}`
- `GET /api/bff/inventario/existencias/bodega/{idBodega}`
- `GET /api/bff/inventario/existencias/producto/{idProducto}/bodega/{idBodega}`
- `PUT /api/bff/inventario/existencias/{idExistencia}/ajustar-stock`
- `POST /api/bff/inventario/reservas`
- `GET /api/bff/inventario/reservas`
- `GET /api/bff/inventario/reservas/{idReserva}`
- `GET /api/bff/inventario/reservas/pedido/{idPedidoRef}`
- `POST /api/bff/inventario/reservas/{idReserva}/confirmar`
- `POST /api/bff/inventario/reservas/{idReserva}/liberar`
- `GET /api/bff/inventario/movimientos`
- `GET /api/bff/inventario/movimientos/{idMovimiento}`
- `GET /api/bff/inventario/movimientos/producto/{idProducto}`
- `GET /api/bff/inventario/movimientos/bodega/{idBodega}`

### BFF de Envíos
Base path: `/api/bff/envios`

- `GET /api/bff/envios/`
- `POST /api/bff/envios/`
- `GET /api/bff/envios/{idEnvio}`
- `PUT /api/bff/envios/{idEnvio}`
- `PATCH /api/bff/envios/{idEnvio}/transportista`
- `PATCH /api/bff/envios/{idEnvio}/estado`
- `GET /api/bff/envios/{idEnvio}/seguimiento`

## 7. Microservicio de pedidos

### Tecnología y propósito

- Spring Boot
- Manejo de pedidos y sus relaciones asociadas
- Integra con inventario para reservas
- Usa DTOs, controllers, repositories, services y manejo de excepciones propio

### Estructura general

- `controller/`
- `service/`
- `repository/`
- `model/`
- `dto/`
- `integration/`
- `config/`
- `exception/`

### Endpoints de pedidos
Base path: `/api/pedidos`

- `POST /api/pedidos`
- `GET /api/pedidos`
- `GET /api/pedidos/{idPedido}`
- `GET /api/pedidos/cliente/{idCliente}`
- `PATCH /api/pedidos/{idPedido}/estado`
- `DELETE /api/pedidos/{idPedido}`

### Endpoints de clientes
Base path: `/api/clientes`

- `POST /api/clientes`
- `GET /api/clientes`
- `GET /api/clientes/{idCliente}`
- `PUT /api/clientes/{idCliente}`
- `DELETE /api/clientes/{idCliente}`

### Endpoints de detalle de pedido
Base path: `/api/detalles-pedido`

- `POST /api/detalles-pedido`
- `GET /api/detalles-pedido`
- `GET /api/detalles-pedido/{idDetalle}`
- `GET /api/detalles-pedido/pedido/{idPedido}`
- `DELETE /api/detalles-pedido/{idDetalle}`

### Endpoints de dirección de entrega
Base path: `/api/direcciones-entrega`

- `POST /api/direcciones-entrega`
- `GET /api/direcciones-entrega`
- `GET /api/direcciones-entrega/pedido/{idPedido}`
- `PUT /api/direcciones-entrega/{idDireccion}`
- `DELETE /api/direcciones-entrega/{idDireccion}`

### Endpoints de historial de estado del pedido
Base path: `/api/historial-pedidos`

- `POST /api/historial-pedidos`
- `GET /api/historial-pedidos`
- `GET /api/historial-pedidos/pedido/{idPedido}`

### Endpoints de pagos del pedido
Base path: `/api/pagos-pedido`

- `POST /api/pagos-pedido`
- `GET /api/pagos-pedido`
- `GET /api/pagos-pedido/{idPago}`
- `GET /api/pagos-pedido/pedido/{idPedido}`

### Endpoints de outbox de pedidos
Base path: `/api/eventos-outbox-pedido`

- `POST /api/eventos-outbox-pedido`
- `GET /api/eventos-outbox-pedido`
- `GET /api/eventos-outbox-pedido/pendientes`
- `PATCH /api/eventos-outbox-pedido/{idEvento}/publicar`

## 8. Microservicio de inventario

### Tecnología y propósito

- Spring Boot
- Gestión de productos, categorías, bodegas, existencias, reservas y movimientos de inventario
- Incluye endpoints de ajuste de stock y confirmación/liberación de reservas

### Estructura general

- `controller/`
- `service/`
- `repository/`
- `model/`
- `dto/request/`
- `dto/response/`
- `exception/`
- `config/`

### Endpoints de categorías
Base path: `/api/inventario/categorias`

- `POST /api/inventario/categorias`
- `GET /api/inventario/categorias`
- `GET /api/inventario/categorias/{id}`
- `PUT /api/inventario/categorias/{id}`
- `PATCH /api/inventario/categorias/{id}/desactivar`

### Endpoints de productos
Base path: `/api/inventario/productos`

- `POST /api/inventario/productos`
- `GET /api/inventario/productos`
- `GET /api/inventario/productos/{id}`
- `GET /api/inventario/productos/sku/{codigoSku}`
- `PUT /api/inventario/productos/{id}`
- `PATCH /api/inventario/productos/{id}/desactivar`

### Endpoints de bodegas
Base path: `/api/inventario/bodegas`

- `POST /api/inventario/bodegas`
- `GET /api/inventario/bodegas`
- `GET /api/inventario/bodegas/{id}`
- `PUT /api/inventario/bodegas/{id}`
- `PATCH /api/inventario/bodegas/{id}/desactivar`

### Endpoints de existencias
Base path: `/api/inventario/existencias`

- `POST /api/inventario/existencias`
- `GET /api/inventario/existencias/producto/{idProducto}`
- `GET /api/inventario/existencias/bodega/{idBodega}`
- `GET /api/inventario/existencias/producto/{idProducto}/bodega/{idBodega}`
- `PUT /api/inventario/existencias/{idExistencia}/ajustar-stock`

### Endpoints de reservas
Base path: `/api/inventario/reservas`

- `POST /api/inventario/reservas`
- `GET /api/inventario/reservas`
- `GET /api/inventario/reservas/{idReserva}`
- `GET /api/inventario/reservas/pedido/{idPedidoRef}`
- `POST /api/inventario/reservas/{idReserva}/confirmar`
- `POST /api/inventario/reservas/{idReserva}/liberar`

### Endpoints de movimientos
Base path: `/api/inventario/movimientos`

- `GET /api/inventario/movimientos`
- `GET /api/inventario/movimientos/{idMovimiento}`
- `GET /api/inventario/movimientos/producto/{idProducto}`
- `GET /api/inventario/movimientos/bodega/{idBodega}`

## 9. Microservicio de envíos

### Tecnología y propósito

- Spring Boot
- Gestión de envíos, transportistas y seguimiento
- Integra con pedidos e inventario mediante clientes Feign
- Incluye validaciones de negocio para crear y actualizar envíos

### Estructura general

- `controller/`
- `service/`
- `integration/`
- `client/`
- `dto/`
- `model/`
- `repository/`
- `exception/`
- `config/`
- `util/`

### Endpoints de envíos
Base path: `/envios`

- `POST /envios`
- `GET /envios`
- `GET /envios/{id}`
- `GET /envios/pedido/{idPedidoRef}`
- `PUT /envios/{id}/estado`
- `PUT /envios/{idEnvio}/asignar-transportista/{idTransportista}`

### Endpoints de seguimiento
Base path: `/seguimientos`

- `POST /seguimientos`
- `GET /seguimientos/envio/{idEnvio}`

### Endpoints de transportistas
Base path: `/transportistas`

- `POST /transportistas`
- `GET /transportistas`
- `GET /transportistas/activos`

### Clientes internos usados por el microservicio de envíos

#### Cliente de pedidos
- `GET /api/pedidos/{idPedido}`
- `GET /api/pedidos/codigo/{codigoPedido}`

#### Cliente de inventario
- `GET /api/inventario/existencias/producto/{idProducto}/bodega/{idBodega}`

## 10. Eureka Server

### Propósito

- Registrar los servicios Spring Boot
- Permitir descubrimiento automático entre aplicaciones
- Servir como punto de coordinación para el ecosistema

### Configuración

- `server.port=8761`
- `eureka.client.register-with-eureka=false`
- `eureka.client.fetch-registry=false`

## 11. Base de datos

Cada servicio usa su propia base de datos MySQL o esquema independiente.

### Bases detectadas

- `smartlogix_bff_db`
- `smartlogix_pedidos_db`
- `inventario_db`
- `smartlogix_envios_db`

## 12. Configuración y convenciones técnicas

### BFF

- Usa `spring.cloud.openfeign.circuitbreaker.enabled=true`
- Usa configuración de sesión con duración en minutos
- Incluye CORS y seguridad por sesión
- Tiene manejador global de excepciones

### Microservicios

- Usan `spring.jpa.hibernate.ddl-auto=update`
- Exponen endpoints REST con controladores separados por dominio
- Usan DTOs para requests y responses
- Manejan errores con `GlobalExceptionHandler`
- Registran servicios en Eureka

### Frontend

- Usa un cliente HTTP centralizado
- Consume el BFF y no los microservicios directamente
- Tiene rutas protegidas por autenticación
- Mantiene tipos centralizados para pedidos, inventario, envíos y autenticación

## 13. Orden recomendado para levantar la solución

1. Levantar `smartlogix-eureka-server`
2. Levantar `ms_inventario`
3. Levantar `ms-pedidos-smartlogix`
4. Levantar `ms-envios`
5. Levantar `ms_bff`
6. Levantar el `frontend`

## 14. Resumen funcional por módulo

### Autenticación y usuarios

- Login y logout por BFF
- Gestión de usuarios y roles
- Sesión basada en token

### Dashboard

- Resumen de pedidos, inventario y envíos
- Vista agregada desde el BFF

### Pedidos

- Crear pedidos
- Consultarlos
- Cambiar estado
- Cancelarlos
- Gestionar detalles, pagos e historial

### Inventario

- Crear y administrar productos
- Categorías y bodegas
- Control de existencias
- Reservas de stock
- Movimientos de inventario

### Envíos

- Crear envíos desde pedidos confirmados
- Asignar transportistas
- Cambiar estado del envío
- Consultar seguimiento

## 15. Conclusión

El proyecto SmartLogix está organizado como una plataforma modular con un frontend React, un BFF central y microservicios de dominio bien separados. La integración principal se realiza a través del BFF, mientras que Eureka facilita la comunicación entre servicios. La estructura es coherente para crecimiento incremental y ya cubre autenticación, pedidos, inventario, envíos, dashboard, usuarios, roles y bitácora.
