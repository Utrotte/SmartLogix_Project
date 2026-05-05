package com.example.smartlogix_eureka_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


@EnableEurekaServer
@SpringBootApplication
public class SmartlogixEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartlogixEurekaServerApplication.class, args);
	}

}
