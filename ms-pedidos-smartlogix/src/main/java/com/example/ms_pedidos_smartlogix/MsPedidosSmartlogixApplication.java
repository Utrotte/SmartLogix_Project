package com.example.ms_pedidos_smartlogix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@SpringBootApplication
public class MsPedidosSmartlogixApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsPedidosSmartlogixApplication.class, args);
	}

}
