# SmartLogix - Guía Paso a Paso para Ejecutar y Probar

## Requisitos Previos

- ✅ MySQL/MariaDB corriendo en puerto 3308
- ✅ Java 17+ instalado
- ✅ Node.js 18+ para el frontend
- ✅ Maven (incluido con mvnw)
- ✅ Git

## Paso 0: Verificación de Base de Datos

### Crear las bases de datos necesarias

Si aún no existen, ejecuta este script SQL:

```sql
CREATE DATABASE IF NOT EXISTS smartlogix_bff_db 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS smartlogix_pedidos_db 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS inventario_db 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS smartlogix_envios_db 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

SHOW DATABASES;
```

**Desde PowerShell:**

```powershell
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -h localhost -P 3308 -u root
# Pega el SQL arriba y presiona Ctrl+D para salir
```

O si tienes un archivo SQL:

```powershell
Get-Content "C:\ruta\a\setup_databases.sql" | & "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -h localhost -P 3308 -u root
```

### Verificar credenciales

- **Host:** localhost
- **Puerto:** 3308
- **Usuario:** root
- **Contraseña:** (vacía)

## Paso 1: Levantar Eureka Server

Abre una terminal PowerShell en la carpeta `smartlogix-eureka-server`:

```powershell
cd C:\Users\victo\Desktop\SmartLogix_Project\smartlogix-eureka-server
.\mvnw.cmd spring-boot:run
```

**Espera que veas:**
```
Started SmartlogixEurekaServerApplication in X seconds
```

**Verifica en navegador:**
```
http://localhost:8761
```

Debe mostrar el dashboard de Eureka con 0 servicios registrados (aún).

**No cierres esta terminal.**

## Paso 2: Levantar ms-inventario

Abre una segunda terminal PowerShell:

```powershell
cd C:\Users\victo\Desktop\SmartLogix_Project\ms_inventario
.\mvnw.cmd spring-boot:run
```

**Espera que veas:**
```
Started InventarioServiceApplication in X seconds
Registered with Eureka
```

**Verifica en navegador:**
```
http://localhost:8761
```

Debe mostrar `ms-inventario` en estado UP.

**Prueba rápida:**
```
GET http://localhost:8081/api/inventario/productos
GET http://localhost:8081/api/inventario/bodegas
```

**No cierres esta terminal.**

## Paso 3: Levantar ms-pedidos-smartlogix

Abre una tercera terminal PowerShell:

```powershell
cd C:\Users\victo\Desktop\SmartLogix_Project\ms-pedidos-smartlogix
.\mvnw.cmd spring-boot:run
```

**Espera que veas:**
```
Started MsPedidosSmartlogixApplication in X seconds
Registered with Eureka
```

**Verifica en navegador:**
```
http://localhost:8761
```

Debe mostrar `ms-pedidos-smartlogix` en estado UP.

**Prueba rápida:**
```
GET http://localhost:8082/api/pedidos
GET http://localhost:8082/api/clientes
```

**No cierres esta terminal.**

## Paso 4: Levantar ms-envios

Abre una cuarta terminal PowerShell:

```powershell
cd C:\Users\victo\Desktop\SmartLogix_Project\ms-envios
.\mvnw.cmd spring-boot:run
```

**Espera que veas:**
```
Started MsEnviosApplication in X seconds
Registered with Eureka
```

**Verifica en navegador:**
```
http://localhost:8761
```

Debe mostrar `ms-envios` en estado UP.

**Prueba rápida:**
```
GET http://localhost:8083/envios
GET http://localhost:8083/transportistas
```

**No cierres esta terminal.**

## Paso 5: Levantar el BFF

Abre una quinta terminal PowerShell:

```powershell
cd C:\Users\victo\Desktop\SmartLogix_Project\ms_bff\ms_bff
.\mvnw.cmd spring-boot:run
```

**Espera que veas:**
```
Started MsBffApplication in X seconds
Registered with Eureka
```

**Verifica en navegador:**
```
http://localhost:8761
```

Debe mostrar `ms-bff` en estado UP junto con los otros 3 servicios.

**Prueba rápida:**
```
GET http://localhost:8080/api/bff/ping
```

Debe responder con un JSON pequeño de ping.

**No cierres esta terminal.**

## Paso 6: Levantar el Frontend

Abre una sexta terminal PowerShell:

```powershell
cd C:\Users\victo\Desktop\SmartLogix_Project\frontend
npm install    # Solo si es primera vez o hay cambios en package.json
npm run dev
```

**Espera que veas:**
```
VITE v5.0.0  ready in XXX ms

➜  Local:   http://localhost:3000/
```

**Abre en navegador:**
```
http://localhost:3000
```

Deberías ver la pantalla de login.

**No cierres esta terminal.**

## Paso 7: Pruebas Funcionales

### Login

1. Abre `http://localhost:3000`
2. Ingresa un usuario y contraseña válidos (usa los datos inicializados en el BFF)
   - Usuario: `admin` (por defecto del DataInitializer)
   - Contraseña: revisar en `DataInitializer.java` del BFF

3. Deberías ser redirigido a `/dashboard`

### Dashboard

- Visualiza métricas de pedidos, inventario y envíos
- Si hay datos, verás stats; si no, verás ceros o placeholders

### Pedidos

1. Navega a `Pedidos`
2. Intenta listar (debe estar vacío o mostrar pedidos existentes)
3. Intenta crear un pedido nuevo:
   - Busca o crea un cliente
   - Agrega dirección
   - Agrega productos
   - Completa el pedido

### Inventario

1. Navega a `Inventario`
2. Intenta listar productos y bodegas
3. Intenta ajustar stock de un producto

### Envíos

1. Navega a `Envíos`
2. Intenta listar envíos
3. Para crear un envío:
   - Necesita un pedido en estado CONFIRMADO
   - Búscalo en la interfaz
   - Completa la información de dirección y paquete

## Troubleshooting

### Error: "Connection refused" al levantar un servicio

**Causa:** MySQL no está corriendo en puerto 3308

**Solución:**
```powershell
# Verifica que MySQL esté escuchando
netstat -ano | findstr :3308
# Si no aparece, inicia MySQL manualmente
```

### Error: "Access denied for user 'root'"

**Causa:** Contraseña incorrecta en application.properties

**Solución:**
1. Verifica que el application.properties tenga `spring.datasource.password=` (sin contraseña)
2. Si MySQL tiene contraseña, actualiza en `application.properties`:
   ```properties
   spring.datasource.password=tu_password
   ```

### Error: "Database not found"

**Causa:** Las bases de datos no existen

**Solución:** Corre el script SQL del Paso 0 de nuevo

### El frontend no conecta con el BFF

**Causa:** Variable de entorno VITE_API_BFF_URL no configurada

**Solución:**
1. Verifica que exista `.env` en `frontend/`:
   ```
   VITE_API_BFF_URL=http://localhost:8080
   VITE_APP_NAME=SmartLogix
   ```
2. Si lo creaste, reinicia el dev server:
   ```powershell
   # Ctrl+C en la terminal del frontend
   npm run dev
   ```

### Error en el navegador: "401 Unauthorized"

**Causa:** Token de sesión inválido o expirado

**Solución:**
1. Cierra sesión (logout)
2. Vuelve a iniciar sesión
3. Si persiste, limpia localStorage:
   ```javascript
   // En consola del navegador (F12)
   localStorage.clear();
   // Recarga la página
   location.reload();
   ```

## Resumen de Puertos

| Servicio | Puerto | URL |
|---|---|---|
| Eureka Server | 8761 | http://localhost:8761 |
| ms-inventario | 8081 | http://localhost:8081 |
| ms-pedidos | 8082 | http://localhost:8082 |
| ms-envios | 8083 | http://localhost:8083 |
| ms-bff | 8080 | http://localhost:8080 |
| Frontend | 3000 | http://localhost:3000 |
| MySQL/MariaDB | 3308 | localhost:3308 |

## Endpoints de Prueba Rápida (Postman/Insomnia/curl)

### BFF - Autenticación

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

Respuesta esperada:
```json
{
  "success": true,
  "message": "Login exitoso",
  "data": {
    "sessionToken": "abc123...",
    "usuario": {
      "idUsuario": 1,
      "username": "admin",
      "correoElectronico": "admin@smartlogix.com",
      "roles": ["ADMIN"]
    }
  }
}
```

### BFF - Dashboard

```bash
curl -X GET http://localhost:8080/api/bff/dashboard/resumen \
  -H "X-Session-Token: tu_token_aqui"
```

### BFF - Pedidos

```bash
# Listar todos
curl http://localhost:8080/api/bff/pedidos/ \
  -H "X-Session-Token: tu_token_aqui"

# Crear (POST)
curl -X POST http://localhost:8080/api/bff/pedidos/ \
  -H "Content-Type: application/json" \
  -H "X-Session-Token: tu_token_aqui" \
  -d '{"clienteId":1,"total":1000.00}'
```

### BFF - Inventario

```bash
# Listar productos
curl http://localhost:8080/api/bff/inventario/productos \
  -H "X-Session-Token: tu_token_aqui"

# Listar bodegas
curl http://localhost:8080/api/bff/inventario/bodegas \
  -H "X-Session-Token: tu_token_aqui"
```

### BFF - Envíos

```bash
# Listar envíos
curl http://localhost:8080/api/bff/envios/ \
  -H "X-Session-Token: tu_token_aqui"
```

## Notas Finales

1. **Orden de inicio:** Eureka → Inventario → Pedidos → Envíos → BFF → Frontend
2. **Logs:** Observa las terminales para ver logs en tiempo real
3. **Reinicio:** Si algo no funciona, reinicia en orden inverso (Frontend → BFF → servicios)
4. **Base de datos:** Hibernate crea/actualiza automáticamente las tablas con `ddl-auto=update`

¡Éxito con SmartLogix!
