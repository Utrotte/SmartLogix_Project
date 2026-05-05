-- Datos iniciales para el microservicio de inventario
-- Si deseas usar estos datos, descomenta las siguientes líneas

-- Insertar categorías
-- INSERT INTO categoria_producto (nombre_categoria, descripcion, activa) VALUES ('Electrónica', 'Productos electrónicos', true);
-- INSERT INTO categoria_producto (nombre_categoria, descripcion, activa) VALUES ('Accesorios', 'Accesorios y repuestos', true);

-- Insertar bodegas
-- INSERT INTO bodega (nombre, direccion, comuna, ciudad, region, activa) VALUES ('Bodega Centro', 'Avenida Principal 123', 'Santiago', 'Santiago', 'Metropolitana', true);
-- INSERT INTO bodega (nombre, direccion, comuna, ciudad, region, activa) VALUES ('Bodega Sur', 'Calle Secundaria 456', 'La Florida', 'Santiago', 'Metropolitana', true);

-- Insertar productos (después de insertar categorías)
-- INSERT INTO producto (id_categoria, codigo_sku, nombre, descripcion, marca, precio_referencia, activo, fecha_creacion) 
-- VALUES (1, 'SKU-001', 'Laptop Dell XPS', 'Laptop de alta performance', 'Dell', 1299.99, true, NOW());
-- INSERT INTO producto (id_categoria, codigo_sku, nombre, descripcion, marca, precio_referencia, activo, fecha_creacion) 
-- VALUES (1, 'SKU-002', 'Mouse Logitech', 'Mouse inalámbrico', 'Logitech', 49.99, true, NOW());
-- INSERT INTO producto (id_categoria, codigo_sku, nombre, descripcion, marca, precio_referencia, activo, fecha_creacion) 
-- VALUES (2, 'SKU-003', 'Cable HDMI', 'Cable HDMI 2.0', 'Genérico', 9.99, true, NOW());

-- Insertar existencias (después de insertar productos y bodegas)
-- INSERT INTO existencia (id_producto, id_bodega, stock_actual, stock_reservado, stock_disponible, stock_minimo, fecha_actualizacion)
-- VALUES (1, 1, 50, 0, 50, 10, NOW());
-- INSERT INTO existencia (id_producto, id_bodega, stock_actual, stock_reservado, stock_disponible, stock_minimo, fecha_actualizacion)
-- VALUES (2, 1, 150, 0, 150, 20, NOW());
-- INSERT INTO existencia (id_producto, id_bodega, stock_actual, stock_reservado, stock_disponible, stock_minimo, fecha_actualizacion)
-- VALUES (3, 2, 200, 0, 200, 30, NOW());
