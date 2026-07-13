package com.smartlogix.catalogo.config;

import com.smartlogix.catalogo.model.*;
import com.smartlogix.catalogo.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Carga datos de ejemplo al iniciar la aplicación si la base de datos está vacía.
 */
@Configuration
public class InicializadorDatos {

    @Bean
    CommandLineRunner inicializarCatalogo(RepositorioCategoriaProducto repositorioCategoriaProducto,
                                          RepositorioMarcaProducto repositorioMarcaProducto,
                                          RepositorioProducto repositorioProducto,
                                          RepositorioPrecioProducto repositorioPrecioProducto,
                                          RepositorioImagenProducto repositorioImagenProducto) {
        return args -> {
            if (repositorioProducto.count() > 0) {
                return;
            }

            CategoriaProducto categoriaElectronica = repositorioCategoriaProducto.save(
                    CategoriaProducto.builder()
                            .nombre("Electrónica")
                            .descripcion("Dispositivos y accesorios electrónicos")
                            .activo(true)
                            .build());

            CategoriaProducto categoriaHogar = repositorioCategoriaProducto.save(
                    CategoriaProducto.builder()
                            .nombre("Hogar")
                            .descripcion("Artículos para el hogar y decoración")
                            .activo(true)
                            .build());

            MarcaProducto marcaSamsung = repositorioMarcaProducto.save(
                    MarcaProducto.builder()
                            .nombre("Samsung")
                            .descripcion("Tecnología y electrodomésticos")
                            .activo(true)
                            .build());

            MarcaProducto marcaIkea = repositorioMarcaProducto.save(
                    MarcaProducto.builder()
                            .nombre("IKEA")
                            .descripcion("Muebles y accesorios para el hogar")
                            .activo(true)
                            .build());

            Producto televisor = repositorioProducto.save(
                    Producto.builder()
                            .categoria(categoriaElectronica)
                            .marca(marcaSamsung)
                            .codigoSku("SKU-TV-001")
                            .nombre("Televisor Smart 55 pulgadas")
                            .descripcion("Televisor UHD 4K con sistema operativo Tizen")
                            .activo(true)
                            .build());

            Producto lampara = repositorioProducto.save(
                    Producto.builder()
                            .categoria(categoriaHogar)
                            .marca(marcaIkea)
                            .codigoSku("SKU-LMP-001")
                            .nombre("Lámpara de escritorio LED")
                            .descripcion("Lámpara ajustable con luz cálida y fría")
                            .activo(true)
                            .build());

            repositorioPrecioProducto.save(
                    PrecioProducto.builder()
                            .producto(televisor)
                            .precio(new BigDecimal("499990.00"))
                            .moneda("CLP")
                            .vigenteDesde(LocalDate.now().minusMonths(1))
                            .activo(true)
                            .build());

            repositorioPrecioProducto.save(
                    PrecioProducto.builder()
                            .producto(lampara)
                            .precio(new BigDecimal("24990.00"))
                            .moneda("CLP")
                            .vigenteDesde(LocalDate.now().minusWeeks(2))
                            .activo(true)
                            .build());

            repositorioImagenProducto.save(
                    ImagenProducto.builder()
                            .producto(televisor)
                            .urlImagen("https://cdn.smartlogix.cl/catalogo/tv-55-principal.jpg")
                            .esPrincipal(true)
                            .orden(1)
                            .build());

            repositorioImagenProducto.save(
                    ImagenProducto.builder()
                            .producto(televisor)
                            .urlImagen("https://cdn.smartlogix.cl/catalogo/tv-55-lateral.jpg")
                            .esPrincipal(false)
                            .orden(2)
                            .build());

            repositorioImagenProducto.save(
                    ImagenProducto.builder()
                            .producto(lampara)
                            .urlImagen("https://cdn.smartlogix.cl/catalogo/lampara-led-principal.jpg")
                            .esPrincipal(true)
                            .orden(1)
                            .build());
        };
    }
}
