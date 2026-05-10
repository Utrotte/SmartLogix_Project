package cl.programadormaldito.ms_envios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.programadormaldito.ms_envios.model.Transportista;

public interface TransportistaRepository extends JpaRepository<Transportista, String> {

    // filtra solo los transportistas disponibles para recibir envíos
    List<Transportista> findByActivoTrue();
}
