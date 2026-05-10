package cl.programadormaldito.ms_envios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// beans de infraestructura compartidos en todo el microservicio
@Configuration
public class AppConfig {

    // permite hacer llamadas HTTP a otros microservicios (ej: consultar pedidos)
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
