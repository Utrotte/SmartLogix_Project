package com.smartlogix.usuarios.service;

import com.smartlogix.usuarios.dto.response.RespuestaBitacoraAcceso;
import com.smartlogix.usuarios.model.BitacoraAcceso;
import com.smartlogix.usuarios.repository.RepositorioBitacoraAcceso;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Consulta de la bitácora de accesos para auditoría. */
@Service
public class ServicioBitacoraAcceso {

    private final RepositorioBitacoraAcceso repositorioBitacora;

    public ServicioBitacoraAcceso(RepositorioBitacoraAcceso repositorioBitacora) {
        this.repositorioBitacora = repositorioBitacora;
    }

    @Transactional(readOnly = true)
    public List<RespuestaBitacoraAcceso> listar() {
        return repositorioBitacora.findAll().stream().map(this::aRespuesta).toList();
    }

    private RespuestaBitacoraAcceso aRespuesta(BitacoraAcceso bitacora) {
        return RespuestaBitacoraAcceso.builder()
                .idBitacora(bitacora.getIdBitacora())
                .idUsuario(bitacora.getIdUsuario())
                .correoIntento(bitacora.getCorreoIntento())
                .tipoEvento(bitacora.getTipoEvento())
                .descripcion(bitacora.getDescripcion())
                .ipOrigen(bitacora.getIpOrigen())
                .fechaEvento(bitacora.getFechaEvento())
                .exitoso(bitacora.getExitoso())
                .build();
    }
}
