package com.smartlogix.usuarios.service;

import com.smartlogix.usuarios.dto.request.SolicitudInicioSesion;
import com.smartlogix.usuarios.dto.response.RespuestaInicioSesion;
import com.smartlogix.usuarios.dto.response.RespuestaUsuario;
import com.smartlogix.usuarios.exception.ExcepcionNegocio;
import com.smartlogix.usuarios.model.BitacoraAcceso;
import com.smartlogix.usuarios.model.SesionUsuario;
import com.smartlogix.usuarios.model.UsuarioSistema;
import com.smartlogix.usuarios.repository.RepositorioBitacoraAcceso;
import com.smartlogix.usuarios.repository.RepositorioSesionUsuario;
import com.smartlogix.usuarios.repository.RepositorioUsuarioSistema;
import com.smartlogix.usuarios.security.ServicioJwt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Lógica de autenticación: login, logout y consulta del usuario actual.
 * Registra cada intento en bitacora_acceso para auditoría.
 */
@Service
public class ServicioAutenticacion {

    private final RepositorioUsuarioSistema repositorioUsuario;
    private final RepositorioSesionUsuario repositorioSesion;
    private final RepositorioBitacoraAcceso repositorioBitacora;
    private final PasswordEncoder codificadorContrasena;
    private final ServicioJwt servicioJwt;
    private final MapeadorUsuario mapeadorUsuario;

    public ServicioAutenticacion(
            RepositorioUsuarioSistema repositorioUsuario,
            RepositorioSesionUsuario repositorioSesion,
            RepositorioBitacoraAcceso repositorioBitacora,
            PasswordEncoder codificadorContrasena,
            ServicioJwt servicioJwt,
            MapeadorUsuario mapeadorUsuario) {
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioSesion = repositorioSesion;
        this.repositorioBitacora = repositorioBitacora;
        this.codificadorContrasena = codificadorContrasena;
        this.servicioJwt = servicioJwt;
        this.mapeadorUsuario = mapeadorUsuario;
    }

    @Transactional
    public RespuestaInicioSesion iniciarSesion(SolicitudInicioSesion solicitud, String ipOrigen, String agenteUsuario) {
        UsuarioSistema usuario = repositorioUsuario.findByCorreo(solicitud.getCorreo()).orElse(null);

        boolean credencialesValidas = usuario != null
                && "ACTIVO".equals(usuario.getEstado())
                && codificadorContrasena.matches(solicitud.getContrasena(), usuario.getHashContrasena());

        if (!credencialesValidas) {
            registrarBitacora(null, solicitud.getCorreo(), "LOGIN", "Intento de inicio de sesión fallido", ipOrigen, false);
            throw new ExcepcionNegocio("Credenciales inválidas");
        }

        List<String> roles = usuario.getRolesAsignados().stream()
                .map(ur -> ur.getRol().getNombre())
                .collect(Collectors.toList());

        String token = servicioJwt.generarToken(usuario.getIdUsuario(), usuario.getCorreo(), roles);

        repositorioSesion.save(SesionUsuario.builder()
                .usuario(usuario)
                .tokenJwt(token)
                .fechaInicio(LocalDateTime.now())
                .fechaExpiracion(servicioJwt.calcularFechaExpiracion())
                .activa(true)
                .ipOrigen(ipOrigen)
                .agenteUsuario(agenteUsuario)
                .build());

        registrarBitacora(usuario.getIdUsuario(), usuario.getCorreo(), "LOGIN", "Inicio de sesión exitoso", ipOrigen, true);

        return RespuestaInicioSesion.builder()
                .token(token)
                .tipoToken("Bearer")
                .expiraEnMinutos(servicioJwt.getMinutosExpiracion())
                .usuario(mapeadorUsuario.aRespuesta(usuario))
                .build();
    }

    @Transactional
    public void cerrarSesion(String token) {
        repositorioSesion.findByTokenJwtAndActivaTrue(token).ifPresent(sesion -> {
            sesion.setActiva(false);
            repositorioSesion.save(sesion);
            registrarBitacora(sesion.getUsuario().getIdUsuario(), sesion.getUsuario().getCorreo(),
                    "LOGOUT", "Cierre de sesión", sesion.getIpOrigen(), true);
        });
    }

    @Transactional(readOnly = true)
    public RespuestaUsuario obtenerUsuarioActual(String token) {
        SesionUsuario sesion = repositorioSesion.findByTokenJwtAndActivaTrue(token)
                .orElseThrow(() -> new ExcepcionNegocio("Sesión inválida o expirada"));
        return mapeadorUsuario.aRespuesta(sesion.getUsuario());
    }

    private void registrarBitacora(Long idUsuario, String correo, String tipo, String descripcion,
                                   String ip, boolean exitoso) {
        repositorioBitacora.save(BitacoraAcceso.builder()
                .idUsuario(idUsuario)
                .correoIntento(correo)
                .tipoEvento(tipo)
                .descripcion(descripcion)
                .ipOrigen(ip)
                .fechaEvento(LocalDateTime.now())
                .exitoso(exitoso)
                .build());
    }
}
