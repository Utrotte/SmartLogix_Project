package com.smartlogix.catalogo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada del microservicio de catálogo de productos SmartLogix.
 * Registra el servicio en Eureka y expone la API REST bajo /api/catalogo.
 */
@SpringBootApplication
public class AplicacionCatalogoProductos {

    public static void main(String[] args) {
        SpringApplication.run(AplicacionCatalogoProductos.class, args);
    }
}
