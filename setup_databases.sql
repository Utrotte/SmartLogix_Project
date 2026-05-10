-- ========================================
-- SmartLogix - Database Setup Script
-- ========================================

-- BFF Database
CREATE DATABASE IF NOT EXISTS smartlogix_bff_db 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Pedidos Database
CREATE DATABASE IF NOT EXISTS smartlogix_pedidos_db 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Inventario Database
CREATE DATABASE IF NOT EXISTS inventario_db 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Envios Database
CREATE DATABASE IF NOT EXISTS smartlogix_envios_db 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Mostrar bases de datos creadas
SHOW DATABASES;
