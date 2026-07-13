package com.smartlogix.usuarios.config;

import com.smartlogix.usuarios.model.Rol;
import com.smartlogix.usuarios.model.UsuarioRol;
import com.smartlogix.usuarios.model.UsuarioSistema;
import com.smartlogix.usuarios.repository.RepositorioRol;
import com.smartlogix.usuarios.repository.RepositorioUsuarioSistema;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Carga datos iniciales si la base de datos está vacía.
 * Facilita probar el microservicio sin insertar manualmente usuarios y roles.
 */
@Configuration
public class InicializadorDatos {

    @Bean
    CommandLineRunner cargarDatosIniciales(
            RepositorioRol repositorioRol,
            RepositorioUsuarioSistema repositorioUsuario,
            PasswordEncoder codificadorContrasena) {
        return args -> {
            if (repositorioRol.count() > 0) {
                return;
            }

            Rol rolAdmin = repositorioRol.save(Rol.builder()
                    .nombre("ADMIN").descripcion("Administrador del sistema").build());
            Rol rolOperador = repositorioRol.save(Rol.builder()
                    .nombre("OPERADOR").descripcion("Operador logístico").build());
            Rol rolConsulta = repositorioRol.save(Rol.builder()
                    .nombre("CONSULTA").descripcion("Solo lectura").build());

            crearUsuario(repositorioUsuario, codificadorContrasena, "Administrador SmartLogix",
                    "admin@smartlogix.cl", "admin123", rolAdmin);
            crearUsuario(repositorioUsuario, codificadorContrasena, "Operador Bodega",
                    "operador@smartlogix.cl", "operador123", rolOperador);
            crearUsuario(repositorioUsuario, codificadorContrasena, "Usuario Consulta",
                    "consulta@smartlogix.cl", "consulta123", rolConsulta);
        };
    }

    private void crearUsuario(RepositorioUsuarioSistema repositorio, PasswordEncoder codificador,
                              String nombre, String correo, String contrasena, Rol rol) {
        UsuarioSistema usuario = UsuarioSistema.builder()
                .nombre(nombre)
                .correo(correo)
                .hashContrasena(codificador.encode(contrasena))
                .estado("ACTIVO")
                .build();
        usuario.getRolesAsignados().add(UsuarioRol.builder().usuario(usuario).rol(rol).build());
        repositorio.save(usuario);
    }
}
