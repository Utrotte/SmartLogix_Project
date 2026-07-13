package com.smartlogix.usuarios.service;

import com.smartlogix.usuarios.dto.response.RespuestaUsuario;
import com.smartlogix.usuarios.model.UsuarioSistema;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/** Convierte entidades JPA a DTOs de respuesta para la API REST. */
@Component
public class MapeadorUsuario {

    public RespuestaUsuario aRespuesta(UsuarioSistema usuario) {
        List<String> roles = usuario.getRolesAsignados().stream()
                .map(ur -> ur.getRol().getNombre())
                .collect(Collectors.toList());
        return RespuestaUsuario.builder()
                .idUsuario(usuario.getIdUsuario())
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .estado(usuario.getEstado())
                .roles(roles)
                .build();
    }
}
