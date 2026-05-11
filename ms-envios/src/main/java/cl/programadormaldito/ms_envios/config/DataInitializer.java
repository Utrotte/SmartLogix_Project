package cl.programadormaldito.ms_envios.config;

import cl.programadormaldito.ms_envios.model.Transportista;
import cl.programadormaldito.ms_envios.repository.TransportistaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private final TransportistaRepository transportistaRepository;

    public DataInitializer(TransportistaRepository transportistaRepository) {
        this.transportistaRepository = transportistaRepository;
    }

    @Override
    public void run(String... args) {
        log.info("Verificando existencia de transportistas en el servicio de envíos...");

        if (transportistaRepository.count() == 0) {
            log.info("No se encontraron transportistas. Iniciando carga de datos de prueba...");

            Transportista express = new Transportista();
            express.setNombre("Logística Rápida Express");
            express.setTelefono("+56912345678");
            express.setCorreo("contacto@logisticaexpress.cl");
            express.setTipoServicio("EXPRESS");
            express.setActivo(true);

            Transportista eco = new Transportista();
            eco.setNombre("Transportes Económicos");
            eco.setTelefono("+56987654321");
            eco.setCorreo("info@transporteseconomicos.cl");
            eco.setTipoServicio("ECONOMICO");
            eco.setActivo(true);
            
            Transportista mismoDia = new Transportista();
            mismoDia.setNombre("Entregas Mismo Día S.A.");
            mismoDia.setTelefono("+56955554444");
            mismoDia.setCorreo("ventas@entregasmismodia.cl");
            mismoDia.setTipoServicio("MISMO_DIA");
            mismoDia.setActivo(true);

            transportistaRepository.saveAll(List.of(express, eco, mismoDia));

            log.info("Datos de prueba (Transportistas) insertados exitosamente.");
        } else {
            log.info("Los transportistas ya existen en la base de datos. Se omite la carga de prueba.");
        }
    }
}
