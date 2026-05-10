package cl.programadormaldito.ms_envios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.programadormaldito.ms_envios.model.EventoOutboxEnvio;

public interface EventoOutboxEnvioRepository extends JpaRepository<EventoOutboxEnvio, String> {

    // se usa para obtener los eventos PENDIENTE que aún no fueron publicados al broker
    List<EventoOutboxEnvio> findByEstado(String estado);
}
