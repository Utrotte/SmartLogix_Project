package com.smartlogix.clientes.config;

import com.smartlogix.clientes.model.*;
import com.smartlogix.clientes.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Carga datos de ejemplo al iniciar la aplicación si la base de datos está vacía.
 */
@Configuration
public class InicializadorDatos {

    @Bean
    CommandLineRunner inicializarClientes(RepositorioTipoCliente repositorioTipoCliente,
                                          RepositorioCliente repositorioCliente,
                                          RepositorioDireccionCliente repositorioDireccionCliente,
                                          RepositorioContactoCliente repositorioContactoCliente) {
        return args -> {
            if (repositorioCliente.count() > 0) {
                return;
            }

            TipoCliente tipoPersona = repositorioTipoCliente.save(
                    TipoCliente.builder()
                            .nombre("PERSONA")
                            .descripcion("Cliente persona natural")
                            .build());

            TipoCliente tipoEmpresa = repositorioTipoCliente.save(
                    TipoCliente.builder()
                            .nombre("EMPRESA")
                            .descripcion("Cliente persona jurídica o empresa")
                            .build());

            Cliente clientePersona = repositorioCliente.save(
                    Cliente.builder()
                            .tipoCliente(tipoPersona)
                            .nombre("María")
                            .apellidoRazonSocial("González Pérez")
                            .documento("12345678-9")
                            .correo("maria.gonzalez@email.cl")
                            .telefono("+56912345678")
                            .estado("ACTIVO")
                            .build());

            Cliente clienteEmpresa = repositorioCliente.save(
                    Cliente.builder()
                            .tipoCliente(tipoEmpresa)
                            .nombre("SmartLogix")
                            .apellidoRazonSocial("SpA")
                            .documento("76.543.210-K")
                            .correo("contacto@smartlogix.cl")
                            .telefono("+56223456789")
                            .estado("ACTIVO")
                            .build());

            repositorioDireccionCliente.save(
                    DireccionCliente.builder()
                            .cliente(clientePersona)
                            .tipoDireccion("PARTICULAR")
                            .calle("Av. Providencia")
                            .numero("1234")
                            .comuna("Providencia")
                            .ciudad("Santiago")
                            .region("Metropolitana")
                            .codigoPostal("7500000")
                            .esPrincipal(true)
                            .build());

            repositorioDireccionCliente.save(
                    DireccionCliente.builder()
                            .cliente(clienteEmpresa)
                            .tipoDireccion("COMERCIAL")
                            .calle("Av. Apoquindo")
                            .numero("5678")
                            .comuna("Las Condes")
                            .ciudad("Santiago")
                            .region("Metropolitana")
                            .codigoPostal("7550000")
                            .esPrincipal(true)
                            .build());

            repositorioContactoCliente.save(
                    ContactoCliente.builder()
                            .cliente(clientePersona)
                            .tipoContacto("CORREO")
                            .valor("maria.gonzalez@email.cl")
                            .esPrincipal(true)
                            .build());

            repositorioContactoCliente.save(
                    ContactoCliente.builder()
                            .cliente(clientePersona)
                            .tipoContacto("TELEFONO")
                            .valor("+56987654321")
                            .esPrincipal(false)
                            .build());

            repositorioContactoCliente.save(
                    ContactoCliente.builder()
                            .cliente(clienteEmpresa)
                            .tipoContacto("CORREO")
                            .valor("ventas@smartlogix.cl")
                            .esPrincipal(true)
                            .build());

            repositorioContactoCliente.save(
                    ContactoCliente.builder()
                            .cliente(clienteEmpresa)
                            .tipoContacto("TELEFONO")
                            .valor("+56223456789")
                            .esPrincipal(false)
                            .build());
        };
    }
}
