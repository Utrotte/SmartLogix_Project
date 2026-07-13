# SmartLogix — Informe de arquitectura y funcionamiento

## 1. Descripción general

SmartLogix es una plataforma logística distribuida para administrar clientes, catálogo, bodegas, inventario, pedidos, pagos, transportistas, rutas, envíos y avisos. La solución utiliza una arquitectura de microservicios, con responsabilidades separadas por dominio y una base de datos independiente para cada servicio.

La interfaz está desarrollada con Ionic y Angular y puede ejecutarse como aplicación web o empaquetarse como aplicación Android mediante Capacitor. Todas las solicitudes del cliente ingresan al backend a través de un API Gateway.

## 2. Tecnologías

- Java 17.
- Spring Boot y Spring Cloud.
- Netflix Eureka para registro y descubrimiento.
- Spring Cloud Gateway como punto único de entrada.
- OpenFeign para comunicación interna por nombre lógico.
- Spring Data JPA e Hibernate.
- MySQL con una base de datos por microservicio.
- Node.js, Express y MySQL para el servicio de avisos.
- Angular 20, Ionic 8 y Capacitor para web y Android.

## 3. Arquitectura lógica

```text
Web / Android
      |
      v
API Gateway :8080  <------>  Eureka :8761
      |
      +-- Autenticación y usuarios :8084
      +-- Clientes :8085
      +-- Catálogo :8086
      +-- Pagos :8087
      +-- Envíos :8088
      +-- Transportistas y rutas :8089
      +-- BFF de dashboard :8090
      +-- Bodegas :8091
      +-- Inventario :8092
      +-- Pedidos :8093
      +-- Avisos Node.js :8094
```

El frontend no conoce los puertos internos. Consume rutas bajo `/api` y el Gateway determina el servicio responsable. Los servicios Spring Boot se registran en Eureka; avisos es la excepción y se enruta mediante su URL configurada.

## 4. Componentes y puertos

| Componente | Puerto | Responsabilidad |
|---|---:|---|
| Servidor Eureka | 8761 | Registro y descubrimiento |
| API Gateway | 8080 | Entrada única y enrutamiento |
| BFF fachada | 8090 | Agregación del dashboard |
| Usuarios y acceso | 8084 | Autenticación, usuarios y roles |
| Clientes | 8085 | Clientes, direcciones y contactos |
| Catálogo | 8086 | Productos, categorías, marcas y precios |
| Pagos | 8087 | Pagos, estados y validaciones |
| Envíos | 8088 | Despachos, estados, guías y seguimiento |
| Transportistas y rutas | 8089 | Flota, rutas, tarifas y disponibilidad |
| Bodegas | 8091 | Bodegas, zonas y ubicaciones |
| Inventario | 8092 | Existencias, movimientos y reservas |
| Pedidos | 8093 | Pedidos y detalles |
| Avisos | 8094 | Alertas y notificaciones operativas |

## 5. API Gateway

El Gateway se registra en Eureka y publica las siguientes rutas:

| Ruta pública | Destino |
|---|---|
| `/api/autenticacion/**` | `MS-USUARIOS-ACCESO` |
| `/api/clientes/**` | `MS-CLIENTES` |
| `/api/catalogo/**` | `MS-CATALOGO-PRODUCTOS` |
| `/api/pagos/**` | `MS-PAGOS-VALIDACION` |
| `/api/envios/**` | `MS-ENVIOS` |
| `/api/transportistas/**`, `/api/rutas/**` | `MS-TRANSPORTISTAS-RUTAS` |
| `/api/bff/**` | `BFF-FACHADA-INTERFAZ` |
| `/api/bodegas/**`, `/api/ubicaciones/**` | `MS-BODEGAS-UBICACIONES` |
| `/api/inventario/**` | `MS-INVENTARIO` |
| `/api/pedidos/**` | `MS-PEDIDOS` |
| `/api/avisos/**` | `http://localhost:8094` |

## 6. Flujo operativo principal

1. El usuario inicia sesión por medio del Gateway y obtiene un token Bearer.
2. El frontend consulta clientes, productos, bodegas y existencias.
3. Al crear un pedido, `MS-PEDIDOS` persiste la orden y solicita la reserva a `MS-INVENTARIO`.
4. Si no existe stock, la creación falla y no se confirma la orden.
5. Si la reserva resulta exitosa, el pedido queda disponible para el proceso financiero y operativo.
6. `MS-PAGOS-VALIDACION` registra el pago, sus cambios de estado y las validaciones realizadas.
7. El envío se programa en `MS-ENVIOS`, asociando el pedido y el transportista.
8. Los estados de envío permiten representar programación, tránsito y entrega.
9. `MS-INTEGRACION-AVISOS` registra eventos operativos. Un fallo de avisos no revierte la creación de un pedido.
10. El BFF consulta los servicios y consolida métricas para el panel principal.

## 7. BFF del dashboard

El endpoint `GET /api/bff/dashboard/resumen` agrega clientes, productos, bodegas, inventario, pedidos, pagos, envíos, transportistas, rutas y avisos. Además calcula totales y existencias disponibles, reservadas y actuales.

El BFF es de lectura y composición. No reemplaza a los servicios de dominio ni mantiene una base de datos compartida.

## 8. Persistencia

La solución aplica el patrón *database per service*:

- `bd_usuarios`
- `bd_clientes`
- `bd_catalogo`
- `bd_bodegas`
- `bd_inventario`
- `bd_pedidos`
- `bd_pagos`
- `bd_envios`
- `bd_transportistas`
- `bd_integracion`

No existe una base de datos central para toda la plataforma. Cada servicio es propietario de sus tablas y los intercambios entre dominios se realizan mediante APIs.

## 9. Frontend web y móvil

`ApiService` utiliza `/api` como ruta base. En desarrollo, Angular redirige esa ruta al Gateway en `http://localhost:8080` mediante `proxy.conf.json`. El token y los datos mínimos de sesión se conservan en `localStorage`; la información logística proviene de los microservicios.

La aplicación Android contiene la misma interfaz Angular a través de Capacitor. En un despliegue móvil real, la URL pública del Gateway debe configurarse para que sea accesible desde el dispositivo; no se deben usar direcciones internas de los microservicios.

## 10. Ejecución local

Requisitos:

- JDK 17.
- Maven 3.9 o superior.
- Node.js y npm.
- MySQL con las bases y credenciales indicadas en cada `application.properties`.
- Variables de entorno `DB_PASSWORD` y `JWT_SECRET` configuradas antes del arranque.

Los componentes se ejecutan desde sus módulos. Primero debe iniciarse Eureka:

```powershell
cd .\Infraestructura\servidor-eureka
mvn spring-boot:run
```

Después se inicia cada microservicio Spring Boot desde su carpeta mediante:

```powershell
mvn spring-boot:run
```

El servicio de avisos se inicia desde `microservicios/ms-integracion-avisos`:

```powershell
npm ci
npm start
```

Una vez registrados los servicios, se inician el BFF y el API Gateway desde sus carpetas con `mvn spring-boot:run`. Finalmente, el frontend se ejecuta desde `frontend-ionic`:

```powershell
npm ci
npm start
```

## 11. Reconstrucción de artefactos excluidos

El repositorio conserva el código fuente y los descriptores de dependencias. No versiona `target`, `node_modules`, `www`, cachés, binarios ni la plataforma Android generada, porque todos ellos se reconstruyen desde el proyecto.

Para compilar un módulo Java:

```powershell
mvn clean package
```

Para generar el frontend web:

```powershell
cd .\frontend-ionic
npm ci
npm run build
```

Para regenerar el proyecto nativo y abrirlo en Android Studio:

```powershell
cd .\frontend-ionic
npm ci
npx cap add android
npm run android:sync
npm run android:open
```

## 12. Decisiones arquitectónicas

- El Gateway es la única entrada desde el frontend.
- Eureka desacopla ubicaciones físicas y nombres de servicio.
- El BFF evita que el frontend realice múltiples consultas para construir el dashboard.
- Inventario mantiene la responsabilidad de reservar stock.
- Pedidos coordina la creación de la orden sin acceder directamente a tablas de otros dominios.
- Avisos es auxiliar: su indisponibilidad no invalida una operación principal ya confirmada.
- La separación de bases evita acoplamiento por esquema y permite evolución independiente.

## 13. Conclusión

SmartLogix implementa una arquitectura distribuida coherente con los límites del dominio logístico. El uso de Gateway, Eureka, BFF y persistencia independiente permite centralizar el acceso sin convertir la solución en un backend monolítico. La interfaz web y móvil actúa como cliente de la misma arquitectura y mantiene la lógica de negocio en los servicios correspondientes.
