package com.smartlogix.bff.config;

import com.smartlogix.bff.model.Usuario;
import com.smartlogix.bff.model.Rol;
import com.smartlogix.bff.model.UsuarioRol;
import com.smartlogix.bff.repository.UsuarioRepository;
import com.smartlogix.bff.repository.RolRepository;
import com.smartlogix.bff.repository.UsuarioRolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner loadInitialData(UsuarioRepository usuarioRepository,
                                            RolRepository rolRepository,
                                            UsuarioRolRepository usuarioRolRepository,
                                            PasswordEncoder passwordEncoder) {
        return args -> {
            
            // Crear roles si no existen
            if (!rolRepository.existsByNombreRol("ADMIN")) {
                Rol rolAdmin = Rol.builder()
                        .nombreRol("ADMIN")
                        .descripcion("Administrador del sistema")
                        .activo(true)
                        .build();
                rolRepository.save(rolAdmin);
            }
            
            if (!rolRepository.existsByNombreRol("OPERADOR")) {
                Rol rolOperador = Rol.builder()
                        .nombreRol("OPERADOR")
                        .descripcion("Operador del sistema")
                        .activo(true)
                        .build();
                rolRepository.save(rolOperador);
            }
            
            if (!rolRepository.existsByNombreRol("CONSULTA")) {
                Rol rolConsulta = Rol.builder()
                        .nombreRol("CONSULTA")
                        .descripcion("Solo consulta")
                        .activo(true)
                        .build();
                rolRepository.save(rolConsulta);
            }
            
            // Crear usuario administrador si no existe
            if (!usuarioRepository.existsByCorreo("admin@smartlogix.cl")) {
                Usuario admin = Usuario.builder()
                        .nombre("Admin SmartLogix")
                        .correo("admin@smartlogix.cl")
                        .passwordHash(passwordEncoder.encode("admin123"))
                        .estado("ACTIVO")
                        .build();
                
                Usuario adminSaved = usuarioRepository.save(admin);
                
                // Asignar rol ADMIN
                Rol rolAdmin = rolRepository.findByNombreRol("ADMIN").orElseThrow();
                
                UsuarioRol usuarioRol = UsuarioRol.builder()
                        .usuario(adminSaved)
                        .rol(rolAdmin)
                        .fechaAsignacion(LocalDateTime.now())
                        .build();
                
                usuarioRolRepository.save(usuarioRol);
                
                System.out.println("✓ Usuario admin inicial creado: admin@smartlogix.cl / admin123");
            }
        };
    }
}
