package com.smartlogix.usuarios.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

/** Datos para crear o actualizar un usuario del sistema. */
@Data
public class SolicitudUsuario {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank @Email
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;

    /** Nombres de roles existentes, por ejemplo: ADMIN, OPERADOR */
    @NotEmpty(message = "Debe asignar al menos un rol")
    private List<String> nombresRoles;
}
