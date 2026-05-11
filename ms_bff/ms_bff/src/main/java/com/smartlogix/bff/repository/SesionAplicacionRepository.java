package com.smartlogix.bff.repository;

import com.smartlogix.bff.model.SesionAplicacion;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SesionAplicacionRepository extends JpaRepository<SesionAplicacion, Long> {

    /**
     * Carga usuario y roles en la misma consulta. {@link com.smartlogix.bff.security.SessionTokenFilter}
     * accede a {@code usuarioRoles} fuera de una transacción de servicio; sin esto aparece
     * LazyInitializationException y se responde 401, lo que en el frontend devuelve al login.
     */
    @EntityGraph(attributePaths = {"usuario", "usuario.usuarioRoles", "usuario.usuarioRoles.rol"})
    Optional<SesionAplicacion> findByTokenReferenciaAndEstado(String tokenReferencia, String estado);
    List<SesionAplicacion> findByUsuarioIdUsuario(Long idUsuario);
    boolean existsByTokenReferenciaAndEstado(String tokenReferencia, String estado);
}
