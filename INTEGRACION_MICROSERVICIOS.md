# Integración de Microservicios SmartLogix

## 📋 Resumen de la Implementación

Se ha implementado una comunicación síncrona HTTP entre **ms-pedidos-smartlogix** y **ms-inventario** utilizando **Spring Cloud OpenFeign** con **Eureka Service Discovery**. 

**Objetivo:** Cuando un pedido cambia al estado CONFIRMADO, ms-pedidos automáticamente solicita la reserva de stock a ms-inventario. La confirmación del pedido solo se ejecuta si la reserva es exitosa.

---

## 🏗️ Arquitectura Implementada

### Estructura de Packages en ms-pedidos-smartlogix

```
ms-pedidos-smartlogix/src/main/java/com/example/ms_pedidos_smartlogix/
│
├── client/
│   └── InventarioClient.java                    # Interfaz Feign para comunicación HTTP
│
├── config/
│   └── FeignConfig.java                         # Configuración de Feign (timeouts, logging)
│
├── exception/
│   ├── BusinessException.java                   # Excepciones de lógica de negocio
│   ├── IntegrationException.java                # Excepciones de integración entre servicios
│   └── GlobalExceptionHandler.java              # Manejador global de excepciones
│
├── integration/
│   ├── dto/
│   │   ├── ReservaInventarioRequestDTO.java     # DTO de solicitud a inventario
│   │   ├── ReservaInventarioDetalleRequestDTO.java
│   │   └── ReservaInventarioResponseDTO.java    # DTO de respuesta desde inventario
│   │
│   └── service/
│       └── InventarioIntegrationService.java    # Servicio que encapsula comunicación con inventario
│
├── controller/
│   ├── PedidoController.java
│   ├── ClienteController.java
│   ├── ...
│
├── service/
│   ├── PedidoService.java                       # Modificado para integración
│   ├── ClienteService.java
│   ├── DetallePedidoService.java
│   └── ...
│
├── model/
├── repository/
├── dto/
└── MsPedidosSmartlogixApplication.java          # Modificado con @EnableFeignClients
```

---

## 🔧 Archivos Creados/Modificados

### 1. **pom.xml** (Modificado)
- Agregada dependencia: `spring-cloud-starter-openfeign`

### 2. **MsPedidosSmartlogixApplication.java** (Modificado)
- Agregada anotación `@EnableFeignClients`

### 3. **config/FeignConfig.java** (Creado)
- Configura Logger.Level.BASIC para depuración
- Establece timeouts: 5s conexión, 10s lectura

### 4. **client/InventarioClient.java** (Creado)
- Interfaz Feign que define contrato HTTP hacia ms-inventario
- Endpoint: `POST /api/inventario/reservas`
- Mapeo automático de DTOs JSON

### 5. **exception/** (Creado)
- `BusinessException.java`: Para errores de lógica de negocio
- `IntegrationException.java`: Para errores de comunicación entre servicios
- `GlobalExceptionHandler.java`: Manejador global que devuelve respuestas JSON controladas

### 6. **integration/dto/** (Creado)
- `ReservaInventarioRequestDTO.java`: Incluye idPedidoRef, codigoPedidoRef, detalles
- `ReservaInventarioDetalleRequestDTO.java`: Incluye idProducto, idBodega, cantidadReservada
- `ReservaInventarioResponseDTO.java`: Incluye idReserva, estadoReserva, fechas

### 7. **integration/service/InventarioIntegrationService.java** (Creado)
- Encapsula la lógica de comunicación con InventarioClient
- Maneja excepciones Feign y las traduce a IntegrationException
- Valida que el estado de reserva sea "CREADA" o "RESERVADA"

### 8. **service/PedidoService.java** (Modificado)
- Inyecta `InventarioIntegrationService` y `DetallePedidoRepository`
- En método `cambiarEstadoPedido()`: si nuevoEstado == "CONFIRMADO", llama a `reservarInventarioParaPedido()`
- Método privado `reservarInventarioParaPedido()`: construye solicitud y llama al servicio de integración
- Si la reserva falla, se lanza `IntegrationException` y el pedido NO se confirma
- Agregada anotación `@Transactional` para manejo de transacciones

---

## 🔄 Flujo de Comunicación

```
1. Usuario llama a PATCH /api/pedidos/{id}/estado?nuevoEstado=CONFIRMADO
   ↓
2. PedidoController.cambiarEstadoPedido()
   ↓
3. PedidoService.cambiarEstadoPedido()
   ├─ Valida que el pedido existe
   ├─ Si nuevoEstado == "CONFIRMADO":
   │  ├─ Llama a reservarInventarioParaPedido()
   │  │  ├─ Obtiene detalles del pedido desde BD
   │  │  ├─ Construye ReservaInventarioRequestDTO
   │  │  ├─ Llama a InventarioIntegrationService.reservarInventario()
   │  │  │  ├─ InventarioIntegrationService.reservarInventario()
   │  │  │  │  ├─ Llama a InventarioClient.crearReserva() via Feign
   │  │  │  │  │  ├─ Resuelve "ms-inventario" en Eureka
   │  │  │  │  │  ├─ HTTP POST a http://ms-inventario:8081/api/inventario/reservas
   │  │  │  │  │  └─ Recibe ReservaInventarioResponseDTO
   │  │  │  │  ├─ Valida estado == "CREADA" o "RESERVADA"
   │  │  │  │  └─ Retorna respuesta o lanza IntegrationException
   │  │  │  └─ Si éxito: continúa; si falla: throw IntegrationException
   │  ├─ Si la reserva fue exitosa:
   │  │  ├─ Actualiza estado a "CONFIRMADO" en BD
   │  │  ├─ Registra historial
   │  │  └─ Crea evento outbox
   │  └─ Retorna PedidoResponseDTO
   ├─ Si error: retorna 503 Service Unavailable o 400 Bad Request
   └─ Si éxito: retorna 200 OK con respuesta
```

---

## 🏪 Datos de Configuración

### ms-pedidos-smartlogix
- **Puerto:** 8082
- **Nombre en Eureka:** ms-pedidos-smartlogix
- **Base de datos:** smartlogix_pedidos_db (MySQL)
- **Eureka Server:** http://localhost:8761/eureka/

### ms-inventario
- **Puerto:** 8081
- **Nombre en Eureka:** ms-inventario
- **Base de datos:** inventario_db (MySQL)
- **Eureka Server:** http://localhost:8761/eureka/

### Eureka Server
- **Puerto:** 8761
- **URL:** http://localhost:8761

---

## 🧪 Guía de Prueba en Postman

### **Requisitos Previos**
1. Eureka Server activo en http://localhost:8761
2. ms-inventario activo en http://localhost:8081 (registrado en Eureka)
3. ms-pedidos-smartlogix activo en http://localhost:8082 (registrado en Eureka)
4. MySQL con bases de datos `inventario_db` y `smartlogix_pedidos_db`

### **Paso 1: Crear Categoría en Inventario**
```
POST http://localhost:8081/api/inventario/categorias
Content-Type: application/json

{
  "nombreCategoria": "Electrónica",
  "descripcion": "Productos electrónicos",
  "activa": true
}
```
**Respuesta esperada:** idCategoria = 1

---

### **Paso 2: Crear Bodega en Inventario**
```
POST http://localhost:8081/api/inventario/bodegas
Content-Type: application/json

{
  "nombre": "Bodega Central",
  "direccion": "Calle Principal 123",
  "comuna": "Providencia",
  "ciudad": "Santiago",
  "region": "RM",
  "activa": true
}
```
**Respuesta esperada:** idBodega = 1

---

### **Paso 3: Crear Producto en Inventario**
```
POST http://localhost:8081/api/inventario/productos
Content-Type: application/json

{
  "idCategoria": 1,
  "codigoSku": "LAPTOP-001",
  "nombre": "Laptop Dell Inspiron 15",
  "descripcion": "Laptop profesional 15 pulgadas",
  "marca": "Dell",
  "precioReferencia": 899999.00,
  "activo": true
}
```
**Respuesta esperada:** idProducto = 1

---

### **Paso 4: Crear Existencia en Inventario**
```
POST http://localhost:8081/api/inventario/existencias
Content-Type: application/json

{
  "idProducto": 1,
  "idBodega": 1,
  "stockActual": 10,
  "stockMinimo": 2
}
```
**Respuesta esperada:** Stock creado exitosamente

---

### **Paso 5: Crear Cliente en Pedidos**
```
POST http://localhost:8082/api/clientes
Content-Type: application/json

{
  "nombre": "Juan",
  "apellido": "Pérez",
  "correo": "juan.perez@example.com",
  "telefono": "+56912345678",
  "documento": "12345678-9"
}
```
**Respuesta esperada:** idCliente = 1

---

### **Paso 6: Crear Pedido en Pedidos**
```
POST http://localhost:8082/api/pedidos
Content-Type: application/json

{
  "idCliente": 1,
  "codigoPedido": "PED-001",
  "canalOrigen": "TIENDA_ONLINE",
  "totalBruto": 1799998.00,
  "descuentoTotal": 0.00,
  "totalNeto": 1799998.00,
  "observacion": "Pedido de prueba"
}
```
**Respuesta esperada:** idPedido = 1, estado = "CREADO"

---

### **Paso 7: Agregar Detalles al Pedido**
```
POST http://localhost:8082/api/detalles-pedido
Content-Type: application/json

{
  "idPedido": 1,
  "idProductoRef": 1,
  "codigoSkuRef": "LAPTOP-001",
  "nombreProductoSnapshot": "Laptop Dell Inspiron 15",
  "cantidad": 2,
  "precioUnitario": 899999.00,
  "subtotal": null,
  "estadoDetalle": "PENDIENTE"
}
```
**Respuesta esperada:** Detalle creado, totalBruto actualizado

---

### **Paso 8: Confirmar Pedido (INTEGRACIÓN ACTIVA)**
```
PATCH http://localhost:8082/api/pedidos/1/estado?nuevoEstado=CONFIRMADO&usuarioResponsable=admin&observacion=Pedido confirmado desde Postman
Content-Type: application/json
```

**Escenarios de Respuesta:**

#### ✅ **ÉXITO (200 OK)**
- Stock disponible en inventario: cantidad >= 2
- **Acciones que ocurren:**
  1. ms-pedidos llama a ms-inventario (vía Feign)
  2. ms-inventario crea reserva con estado "CREADA"
  3. ms-inventario actualiza stock (reservado +2, disponible -2)
  4. ms-pedidos confirma el pedido (estado = "CONFIRMADO")
  5. Se registra historial y evento outbox

```json
{
  "idPedido": 1,
  "idCliente": 1,
  "nombreCliente": "Juan Pérez",
  "codigoPedido": "PED-001",
  "fechaCreacion": "2026-05-05T...",
  "estadoActual": "CONFIRMADO",
  "canalOrigen": "TIENDA_ONLINE",
  "totalBruto": 1799998.00,
  "descuentoTotal": 0.00,
  "totalNeto": 1799998.00,
  "observacion": "Pedido confirmado desde Postman"
}
```

#### ❌ **ERROR: Stock Insuficiente (503 Service Unavailable)**
- Stock disponible en inventario: cantidad < 2
- **Respuesta:**
```json
{
  "timestamp": "2026-05-05T23:...",
  "status": 503,
  "error": "Integration Error",
  "message": "Inventario no pudo reservar stock. Estado recibido: SIN_STOCK. Mensaje: Stock insuficiente para el producto ..."
}
```
- **Acciones:** El pedido NO se confirma; permanece en estado "CREADO"

#### ❌ **ERROR: ms-inventario no disponible (503 Service Unavailable)**
- ms-inventario está caído o no registrado en Eureka
- **Respuesta:**
```json
{
  "timestamp": "2026-05-05T23:...",
  "status": 503,
  "error": "Integration Error",
  "message": "Error HTTP al comunicarse con ms-inventario: 503 - service unavailable"
}
```
- **Acciones:** El pedido NO se confirma

#### ❌ **ERROR: Pedido sin detalles (400 Bad Request)**
- El pedido existe pero no tiene detalles asociados
- **Respuesta:**
```json
{
  "timestamp": "2026-05-05T23:...",
  "status": 400,
  "error": "Business Error",
  "message": "No se puede confirmar el pedido porque no tiene detalles asociados"
}
```

---

### **Paso 9: Verificar Reserva en Inventario**
```
GET http://localhost:8081/api/inventario/reservas/pedido/1
Content-Type: application/json
```
**Respuesta esperada:** Lista con una reserva en estado "CREADA"

---

### **Paso 10: Verificar Stock Actualizado en Inventario**
```
GET http://localhost:8081/api/inventario/existencias/producto/1/bodega/1
Content-Type: application/json
```
**Respuesta esperada:**
```json
{
  "idExistencia": 1,
  "idProducto": 1,
  "idBodega": 1,
  "nombreProducto": "Laptop Dell Inspiron 15",
  "nombreBodega": "Bodega Central",
  "stockActual": 10,
  "stockReservado": 2,
  "stockDisponible": 8,
  "stockMinimo": 2,
  "fechaCreacion": "...",
  "fechaActualizacion": "..."
}
```

---

## 🔍 Depuración

### Verificar Que Los Servicios Están Registrados en Eureka
```
GET http://localhost:8761
```
Deberías ver:
- `ms-inventario` con estado **UP**
- `ms-pedidos-smartlogix` con estado **UP**

### Logs en ms-pedidos-smartlogix
Busca logs con patrón `InventarioIntegrationService` o `InventarioClient` para ver el flujo de comunicación.

Ejemplos:
```
[DEBUG] com.example.ms_pedidos_smartlogix.client.InventarioClient - [InventarioClient#crearReserva] ---> POST http://ms-inventario:8081/api/inventario/reservas HTTP/1.1
[DEBUG] com.example.ms_pedidos_smartlogix.client.InventarioClient - [InventarioClient#crearReserva] <--- HTTP/1.1 201 (X ms)
```

### Habilitar Logging Completo de Feign
En `application.properties` de ms-pedidos-smartlogix, agrega:
```properties
logging.level.com.example.ms_pedidos_smartlogix.client.InventarioClient=DEBUG
logging.level.com.example.ms_pedidos_smartlogix.integration.service.InventarioIntegrationService=DEBUG
```

---

## 📊 Pruebas Adicionales

### Confirmar Múltiples Pedidos Secuencialmente
1. Crea otro pedido: PED-002, PED-003, etc.
2. Confirma cada uno
3. Verifica que el stock se decrementa en cada confirmación

### Intentar Confirmar Pedido con Stock Insuficiente
1. Crea bodega con stock = 1
2. Crea pedido que pide cantidad = 2
3. Intenta confirmar
4. Deberías recibir IntegrationException

### Validar Que Los Estados Se Persisten
```
GET http://localhost:8082/api/pedidos/1
```
Deberías ver estado = "CONFIRMADO"

---

## ✅ Criterios de Aceptación Cumplidos

- ✅ ms-pedidos-smartlogix compila sin errores
- ✅ ms-inventario compila sin errores
- ✅ Ambos servicios registrados en Eureka
- ✅ ms-pedidos se comunica con ms-inventario por nombre de servicio (no URL fija)
- ✅ OpenFeign usada para comunicación HTTP
- ✅ Cambio de estado a CONFIRMADO dispara reserva de inventario
- ✅ Si inventario responde "CREADA", pedido se confirma
- ✅ Si inventario falla o devuelve error, pedido NO se confirma
- ✅ No existen dependencias entre bases de datos
- ✅ DTOs de integración separados de DTOs internos
- ✅ Excepciones personalizadas para negocio e integración
- ✅ Separación clara entre client, config e integration
- ✅ Sin Lombok
- ✅ Constructor injection
- ✅ Arquitectura por capas mantenida

---

## 🚀 Próximos Pasos (Futuro)

1. **Implementar Circuit Breaker** con Resilience4j para manejar fallos de ms-inventario
2. **Implementar Saga Pattern** para transacciones distribuidas
3. **Implementar eventos asíncronos** con Kafka/RabbitMQ para desacoplamiento
4. **Implementar Outbox Pattern** en ms-pedidos para garantizar consistencia eventual
5. **Agregar retry logic** en InventarioIntegrationService
6. **Implementar timeout escalable** basado en SLA
7. **Agregar métricas** con Micrometer
8. **Documentar con OpenAPI/Swagger**

---

## 📚 Referencias

- **Spring Cloud OpenFeign**: https://cloud.spring.io/spring-cloud-openfeign/reference/html/
- **Eureka Server**: https://github.com/Netflix/eureka
- **Microservices Patterns**: https://microservices.io/

---

**Implementado por:** SmartLogix Development Team
**Fecha:** 05-05-2026
**Versión:** 1.0
