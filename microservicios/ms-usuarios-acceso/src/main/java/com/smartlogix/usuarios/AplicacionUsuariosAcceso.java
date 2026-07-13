package com.smartlogix.usuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Punto de entrada del microservicio de Usuarios y Acceso.
 * Gestiona autenticación JWT, usuarios internos, roles, sesiones y bitácora de acceso.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AplicacionUsuariosAcceso {

    public static void main(String[] args) {
        SpringApplication.run(AplicacionUsuariosAcceso.class, args);
    }
}
