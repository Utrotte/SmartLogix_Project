# BFF - bff-fachada-interfaz

Endpoint (dashboard resumen):

- Local BFF: GET http://localhost:8090/api/bff/dashboard/resumen
- Via API Gateway: GET http://localhost:8080/api/bff/dashboard/resumen

Behavior and flow:

- The BFF composes a consolidated dashboard by calling downstream services:
  Frontend → API Gateway → BFF → (MS-CLIENTES, MS-CATALOGO-PRODUCTOS,
  MS-BODEGAS-UBICACIONES, MS-INVENTARIO, MS-PEDIDOS, ms-integracion-avisos)
- Services registered in Eureka are consumed via OpenFeign using their
  logical names (e.g., `MS-CLIENTES`).
- `ms-integracion-avisos` is a Node.js service not registered in Eureka and is
  consumed by the BFF using a direct URL configured in `application.properties`:
  `avisos.service.url=http://localhost:8094`

Notes:
- The BFF only aggregates counts and inventory sums; it does not perform
  business logic or write to downstream databases.
- Do not change the endpoint or the established flow unless necessary.
