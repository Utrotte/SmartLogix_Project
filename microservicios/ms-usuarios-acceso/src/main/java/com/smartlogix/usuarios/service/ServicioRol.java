package com.smartlogix.usuarios.service;

import com.smartlogix.usuarios.dto.request.SolicitudRol;
import com.smartlogix.usuarios.dto.response.RespuestaRol;
import com.smartlogix.usuarios.exception.ExcepcionNegocio;
import com.smartlogix.usuarios.exception.ExcepcionRecursoNoEncontrado;
import com.smartlogix.usuarios.model.Rol;
import com.smartlogix.usuarios.repository.RepositorioRol;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ServicioRol {

    private final RepositorioRol repositorioRol;

    public ServicioRol(RepositorioRol repositorioRol) {
        this.repositorioRol = repositorioRol;
    }

    @Transactional
    public RespuestaRol crear(SolicitudRol solicitud) {
        if (repositorioRol.findByNombre(solicitud.getNombre()).isPresent()) {
            throw new ExcepcionNegocio("Ya existe un rol con ese nombre");
        }
        Rol rol = Rol.builder()
                .nombre(solicitud.getNombre())
                .descripcion(solicitud.getDescripcion())
                .build();
        return aRespuesta(repositorioRol.save(rol));
    }

    @Transactional(readOnly = true)
    public List<RespuestaRol> listar() {
        return repositorioRol.findAll().stream().map(this::aRespuesta).toList();
    }

    @Transactional(readOnly = true)
    public RespuestaRol obtenerPorId(Long id) {
        return aRespuesta(buscarPorId(id));
    }

    private Rol buscarPorId(Long id) {
        return repositorioRol.findById(id)
                .orElseThrow(() -> new ExcepcionRecursoNoEncontrado("Rol no encontrado con id: " + id));
    }

    private RespuestaRol aRespuesta(Rol rol) {
        return RespuestaRol.builder()
                .idRol(rol.getIdRol())
                .nombre(rol.getNombre())
                .descripcion(rol.getDescripcion())
                .build();
    }
}
