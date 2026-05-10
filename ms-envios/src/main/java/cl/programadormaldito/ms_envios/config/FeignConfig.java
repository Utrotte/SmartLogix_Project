package cl.programadormaldito.ms_envios.config;

import feign.Logger;
import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

// Configuración centralizada para clientes Feign
@Configuration
public class FeignConfig {

    @Bean
    public Request.Options feignRequestOptions() {
        // Timeout de conexión: 5 segundos
        // Timeout de lectura: 10 segundos
        return new Request.Options(
            5, TimeUnit.SECONDS,
            10, TimeUnit.SECONDS,
            true  // followRedirects
        );
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        // BASIC: solo inicio de request y status de respuesta
        return Logger.Level.BASIC;
    }
}
