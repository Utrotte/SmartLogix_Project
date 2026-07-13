package com.smartlogix.bff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class BffFachadaInterfazApplication {

    public static void main(String[] args) {
        SpringApplication.run(BffFachadaInterfazApplication.class, args);
    }
}