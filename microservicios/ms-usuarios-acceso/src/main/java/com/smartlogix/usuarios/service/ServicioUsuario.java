package com.smartlogix.usuarios.service;

import com.smartlogix.usuarios.dto.request.SolicitudUsuario;
import com.smartlogix.usuarios.dto.response.RespuestaUsuario;
import com.smartlogix.usuarios.exception.ExcepcionNegocio;
import com.smartlogix.usuarios.exception.ExcepcionRecursoNoEncontrado;
import com.smartlogix.usuarios.model.Rol;
import com.smartlogix.usuarios.model.UsuarioRol;
import com.smartlogix.usuarios.model.UsuarioSistema;
import com.smartlogix.usuarios.repository.RepositorioRol;
import com.smartlogix.usuarios.repository.RepositorioUsuarioSistema;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** CRUD de usuarios internos del sistema. */
@Service
public class ServicioUsuario {

    private final RepositorioUsuarioSistema repositorioUsuario;
    private final RepositorioRol repositorioRol;
    private final PasswordEncoder codificadorContrasena;
    private final MapeadorUsuario mapeadorUsuario;

    public ServicioUsuario(
            RepositorioUsuarioSistema repositorioUsuario,
            RepositorioRol repositorioRol,
            PasswordEncoder codificadorContrasena,
            MapeadorUsuario mapeadorUsuario) {
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioRol = repositorioRol;
        this.codificadorContrasena = codificadorContrasena;
        this.mapeadorUsuario = mapeadorUsuario;
    }

    @Transactional
    public RespuestaUsuario crear(SolicitudUsuario solicitud) {
        if (repositorioUsuario.existsByCorreo(solicitud.getCorreo())) {
            throw new ExcepcionNegocio("Ya existe un usuario con ese correo");
        }
        UsuarioSistema usuario = UsuarioSistema.builder()
                .nombre(solicitud.getNombre())
                .correo(solicitud.getCorreo())
                .hashContrasena(codificadorContrasena.encode(solicitud.getContrasena()))
                .estado("ACTIVO")
                .build();
        asignarRoles(usuario, solicitud.getNombresRoles());
        return mapeadorUsuario.aRespuesta(repositorioUsuario.save(usuario));
    }

    @Transactional(readOnly = true)
    public List<RespuestaUsuario> listar() {
        return repositorioUsuario.findAll().stream().map(mapeadorUsuario::aRespuesta).toList();
    }

    @Transactional(readOnly = true)
    public RespuestaUsuario obtenerPorId(Long id) {
        return mapeadorUsuario.aRespuesta(buscarPorId(id));
    }

    @Transactional
    public RespuestaUsuario actualizar(Long id, SolicitudUsuario solicitud) {
        UsuarioSistema usuario = buscarPorId(id);
        usuario.setNombre(solicitud.getNombre());
        usuario.setCorreo(solicitud.getCorreo());
        if (solicitud.getContrasena() != null && !solicitud.getContrasena().isBlank()) {
            usuario.setHashContrasena(codificadorContrasena.encode(solicitud.getContrasena()));
        }
        usuario.getRolesAsignados().clear();
        asignarRoles(usuario, solicitud.getNombresRoles());
        return mapeadorUsuario.aRespuesta(repositorioUsuario.save(usuario));
    }

    @Transactional
    public RespuestaUsuario desactivar(Long id) {
        UsuarioSistema usuario = buscarPorId(id);
        usuario.setEstado("INACTIVO");
        return mapeadorUsuario.aRespuesta(repositorioUsuario.save(usuario));
    }

    private void asignarRoles(UsuarioSistema usuario, List<String> nombresRoles) {
        for (String nombreRol : nombresRoles) {
            Rol rol = repositorioRol.findByNombre(nombreRol)
                    .orElseThrow(() -> new ExcepcionNegocio("Rol no encontrado: " + nombreRol));
            usuario.getRolesAsignados().add(UsuarioRol.builder().usuario(usuario).rol(rol).build());
        }
    }

    private UsuarioSistema buscarPorId(Long id) {
        return repositorioUsuario.findById(id)
                .orElseThrow(() -> new ExcepcionRecursoNoEncontrado("Usuario no encontrado con id: " + id));
    }
}
