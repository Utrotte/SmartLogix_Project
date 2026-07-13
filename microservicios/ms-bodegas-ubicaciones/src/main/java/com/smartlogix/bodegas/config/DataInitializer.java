package com.smartlogix.bodegas.config;

import com.smartlogix.bodegas.domain.Bodega;
import com.smartlogix.bodegas.domain.TipoUbicacion;
import com.smartlogix.bodegas.domain.UbicacionBodega;
import com.smartlogix.bodegas.domain.ZonaBodega;
import com.smartlogix.bodegas.repository.BodegaRepository;
import com.smartlogix.bodegas.repository.TipoUbicacionRepository;
import com.smartlogix.bodegas.repository.UbicacionBodegaRepository;
import com.smartlogix.bodegas.repository.ZonaBodegaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(BodegaRepository bodegaRepo, ZonaBodegaRepository zonaRepo, UbicacionBodegaRepository ubicRepo, TipoUbicacionRepository tipoRepo) {
        return args -> {
            if (tipoRepo.count() == 0) {
                tipoRepo.save(new TipoUbicacion("PALLET", "Ubicación para pallets"));
            }
            if (bodegaRepo.count() == 0) {
                Bodega b = bodegaRepo.save(new Bodega("Bodega Central", "Av. Principal 1"));
                ZonaBodega z = zonaRepo.save(new ZonaBodega(b.getId(), "Zona A"));
                ubicRepo.save(new UbicacionBodega(b.getId(), z.getId(), "A-01", "LIBRE"));
            }
        };
    }
}
