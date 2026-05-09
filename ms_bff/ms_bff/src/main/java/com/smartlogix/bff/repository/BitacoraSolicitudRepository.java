package com.smartlogix.bff.repository;

import com.smartlogix.bff.model.BitacoraSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BitacoraSolicitudRepository extends JpaRepository<BitacoraSolicitud, Long> {
    List<BitacoraSolicitud> findByIdUsuario(Long idUsuario);
    List<BitacoraSolicitud> findByServicioDestino(String servicioDestino);
}
