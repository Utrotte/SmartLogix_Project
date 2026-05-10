package cl.programadormaldito.ms_envios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.programadormaldito.ms_envios.model.GuiaDespacho;

public interface GuiaDespachoRepository extends JpaRepository<GuiaDespacho, String> {

    // verifica que no exista otra guía con el mismo número antes de guardar
    boolean existsByNumeroGuia(String numeroGuia);
}
