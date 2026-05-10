package cl.programadormaldito.ms_envios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.programadormaldito.ms_envios.model.RutaEntrega;

public interface RutaEntregaRepository extends JpaRepository<RutaEntrega, String> {
}
