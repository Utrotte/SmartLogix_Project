package cl.programadormaldito.ms_envios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.programadormaldito.ms_envios.model.SeguimientoEnvio;

public interface SeguimientoEnvioRepository extends JpaRepository<SeguimientoEnvio, String> {

    // devuelve el historial completo de un envío ordenado del más antiguo al más reciente
    List<SeguimientoEnvio> findByEnvioIdOrderByFechaEventoAsc(String idEnvio);
}
