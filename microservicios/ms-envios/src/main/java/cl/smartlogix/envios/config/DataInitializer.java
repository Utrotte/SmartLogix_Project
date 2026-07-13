package cl.smartlogix.envios.config;

import cl.smartlogix.envios.model.EstadoEnvio;
import cl.smartlogix.envios.repository.EstadoEnvioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner inicializarEstados(EstadoEnvioRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                crear(repository, "PROGRAMADO", "Despacho listo para asignación");
                crear(repository, "EN_TRANSITO", "Carga en camino al destino");
                crear(repository, "ENTREGADO", "Entrega confirmada");
            }
        };
    }

    private void crear(EstadoEnvioRepository repository, String nombre, String descripcion) {
        EstadoEnvio estado = new EstadoEnvio();
        estado.setNombre(nombre);
        estado.setDescripcion(descripcion);
        estado.setActivo(true);
        repository.save(estado);
    }
}
