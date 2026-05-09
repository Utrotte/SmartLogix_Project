package com.smartlogix.bff.repository;

import com.smartlogix.bff.model.SesionAplicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SesionAplicacionRepository extends JpaRepository<SesionAplicacion, Long> {
    Optional<SesionAplicacion> findByTokenReferenciaAndEstado(String tokenReferencia, String estado);
    List<SesionAplicacion> findByUsuarioIdUsuario(Long idUsuario);
    boolean existsByTokenReferenciaAndEstado(String tokenReferencia, String estado);
}
