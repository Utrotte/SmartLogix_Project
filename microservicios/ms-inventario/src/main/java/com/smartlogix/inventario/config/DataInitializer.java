package com.smartlogix.inventario.config;

import com.smartlogix.inventario.domain.Existencia;
import com.smartlogix.inventario.repository.ExistenciaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(ExistenciaRepository existenciaRepository) {
        return args -> {
            if (existenciaRepository.count() == 0) {
                Existencia e = new Existencia(1L, 1L, 100);
                existenciaRepository.save(e);
            }
        };
    }
}
