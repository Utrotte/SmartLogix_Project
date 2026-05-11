package com.smartlogix.inventario_service.config;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.smartlogix.inventario_service.model.Bodega;
import com.smartlogix.inventario_service.model.CategoriaProducto;
import com.smartlogix.inventario_service.model.Existencia;
import com.smartlogix.inventario_service.model.Producto;
import com.smartlogix.inventario_service.repository.BodegaRepository;
import com.smartlogix.inventario_service.repository.CategoriaProductoRepository;
import com.smartlogix.inventario_service.repository.ExistenciaRepository;
import com.smartlogix.inventario_service.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final CategoriaProductoRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final BodegaRepository bodegaRepository;
    private final ExistenciaRepository existenciaRepository;

    @Override
    public void run(String... args) {
        log.info("Verificando existencia de productos en el inventario...");

        if (productoRepository.count() == 0) {
            log.info("No se encontraron productos. Iniciando carga de datos de prueba...");

            // 1. Crear Categorías
            CategoriaProducto electronica = CategoriaProducto.builder()
                    .nombreCategoria("Electrónica")
                    .descripcion("Dispositivos electrónicos y gadgets")
                    .activa(true)
                    .build();

            CategoriaProducto hogar = CategoriaProducto.builder()
                    .nombreCategoria("Hogar")
                    .descripcion("Artículos para el hogar y electrodomésticos")
                    .activa(true)
                    .build();

            CategoriaProducto ropa = CategoriaProducto.builder()
                    .nombreCategoria("Ropa")
                    .descripcion("Vestimenta y accesorios")
                    .activa(true)
                    .build();

            categoriaRepository.saveAll(List.of(electronica, hogar, ropa));

            // 2. Crear Productos
            Producto smartphone = Producto.builder()
                    .categoria(electronica)
                    .codigoSku("SKU-ELEC-001")
                    .nombre("Smartphone Super X")
                    .descripcion("Teléfono inteligente de última generación con 128GB")
                    .marca("TechBrand")
                    .precioReferencia(new BigDecimal("699.99"))
                    .activo(true)
                    .build();

            Producto laptop = Producto.builder()
                    .categoria(electronica)
                    .codigoSku("SKU-ELEC-002")
                    .nombre("Laptop Pro 15")
                    .descripcion("Laptop para profesionales con 16GB RAM y 512GB SSD")
                    .marca("TechBrand")
                    .precioReferencia(new BigDecimal("1299.99"))
                    .activo(true)
                    .build();

            Producto batidora = Producto.builder()
                    .categoria(hogar)
                    .codigoSku("SKU-HOG-001")
                    .nombre("Batidora de Mano 500W")
                    .descripcion("Batidora potente con 5 velocidades y accesorios")
                    .marca("HomePlus")
                    .precioReferencia(new BigDecimal("49.99"))
                    .activo(true)
                    .build();

            Producto cafetera = Producto.builder()
                    .categoria(hogar)
                    .codigoSku("SKU-HOG-002")
                    .nombre("Cafetera Automática")
                    .descripcion("Cafetera programable de 12 tazas")
                    .marca("HomePlus")
                    .precioReferencia(new BigDecimal("79.99"))
                    .activo(true)
                    .build();

            Producto camiseta = Producto.builder()
                    .categoria(ropa)
                    .codigoSku("SKU-ROP-001")
                    .nombre("Camiseta Básica Algodón")
                    .descripcion("Camiseta 100% algodón, talla L, color blanco")
                    .marca("WearIt")
                    .precioReferencia(new BigDecimal("15.99"))
                    .activo(true)
                    .build();

            List<Producto> productosGuardados = productoRepository.saveAll(
                    List.of(smartphone, laptop, batidora, cafetera, camiseta));

            log.info("Productos creados exitosamente: {}", productosGuardados.size());

            // 3. Crear Bodegas
            Bodega bodegaPrincipal = Bodega.builder()
                    .nombre("Bodega Principal")
                    .direccion("Calle Principal 123")
                    .comuna("Providencia")
                    .ciudad("Santiago")
                    .region("Metropolitana")
                    .activa(true)
                    .build();

            Bodega bodegaSecundaria = Bodega.builder()
                    .nombre("Bodega Secundaria")
                    .direccion("Calle Secundaria 456")
                    .comuna("Las Condes")
                    .ciudad("Santiago")
                    .region("Metropolitana")
                    .activa(true)
                    .build();

            List<Bodega> bodegasGuardadas = bodegaRepository.saveAll(List.of(bodegaPrincipal, bodegaSecundaria));
            log.info("Bodegas creadas exitosamente: {}", bodegasGuardadas.size());

            // 4. Crear Existencias para todos los productos en ambas bodegas
            List<Existencia> existencias = new java.util.ArrayList<>();
            
            for (Bodega bodega : bodegasGuardadas) {
                for (Producto producto : productosGuardados) {
                    Existencia existencia = Existencia.builder()
                            .producto(producto)
                            .bodega(bodega)
                            .stockActual(100)  // Stock inicial de 100 unidades
                            .stockReservado(0)
                            .stockDisponible(100)
                            .stockMinimo(10)
                            .fechaActualizacion(LocalDateTime.now())
                            .build();
                    existencias.add(existencia);
                }
            }

            existenciaRepository.saveAll(existencias);
            log.info("Existencias creadas exitosamente: {}", existencias.size());

            log.info("✅ Datos de prueba (Categorías, Productos, Bodegas y Existencias) insertados exitosamente.");
        } else {
            log.info("Los productos ya existen en la base de datos. Se omite la carga de prueba.");
        }
    }
}
