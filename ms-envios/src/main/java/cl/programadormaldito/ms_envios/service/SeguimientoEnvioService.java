package cl.programadormaldito.ms_envios.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.programadormaldito.ms_envios.dto.SeguimientoRequestDTO;
import cl.programadormaldito.ms_envios.model.Envio;
import cl.programadormaldito.ms_envios.model.SeguimientoEnvio;
import cl.programadormaldito.ms_envios.repository.EnvioRepository;
import cl.programadormaldito.ms_envios.repository.SeguimientoEnvioRepository;

@Service
public class SeguimientoEnvioService {

    @Autowired
    private SeguimientoEnvioRepository seguimientoEnvioRepository;

    @Autowired
    private EnvioRepository envioRepository;

    public void seguimientoRegistrar(SeguimientoRequestDTO dto) {
        Envio envio = this.envioRepository.findById(dto.getIdEnvio()).orElse(null);

        // si el envío no existe, se ignora el registro
        if (envio != null) {
            SeguimientoEnvio seguimiento = new SeguimientoEnvio();
            seguimiento.setDescripcion(dto.getDescripcion());
            seguimiento.setUbicacion(dto.getUbicacion());
            seguimiento.setEnvio(envio);
            this.seguimientoEnvioRepository.save(seguimiento);

            // si el request incluye un nuevo estado, actualiza el envío en el mismo paso
            if (dto.getNuevoEstadoEnvio() != null && !dto.getNuevoEstadoEnvio().isEmpty()) {
                envio.setEstado(dto.getNuevoEstadoEnvio());
                this.envioRepository.save(envio);
            }
        }
    }

    // devuelve el historial ordenado cronológicamente (del evento más antiguo al más reciente)
    public List<SeguimientoEnvio> seguimientoListarPorEnvio(String idEnvio) {
        return this.seguimientoEnvioRepository.findByEnvioIdOrderByFechaEventoAsc(idEnvio);
    }
}
