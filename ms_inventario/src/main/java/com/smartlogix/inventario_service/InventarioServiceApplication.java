package com.smartlogix.inventario_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient // Registra el servicio en Eureka
public class InventarioServiceApplication {

	// Punto de entrada de la aplicación
	public static void main(String[] args) {
		SpringApplication.run(InventarioServiceApplication.class, args);
	}

}
