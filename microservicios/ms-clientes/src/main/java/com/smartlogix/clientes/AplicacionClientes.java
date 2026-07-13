package com.smartlogix.clientes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Punto de entrada del microservicio de clientes SmartLogix.
 * Registra el servicio en Eureka y expone la API REST bajo /api/clientes.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AplicacionClientes {

    public static void main(String[] args) {
        SpringApplication.run(AplicacionClientes.class, args);
    }
}
