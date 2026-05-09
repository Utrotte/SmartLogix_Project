package com.smartlogix.bff.service;

import com.smartlogix.bff.client.EnviosClient;
import com.smartlogix.bff.exception.ExternalServiceException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class BffEnviosService {

    private final EnviosClient enviosClient;

    public BffEnviosService(EnviosClient enviosClient) {
        this.enviosClient = enviosClient;
    }

    public List<?> listarEnvios() {
        try {
            return enviosClient.listarEnvios();
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo conectar con el servicio de envíos", e);
        }
    }

    public Object crearEnvio(Map<String, Object> request) {
        try {
            return enviosClient.crearEnvio(request);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo crear el envío", e);
        }
    }

    public Object buscarEnvio(Long idEnvio) {
        try {
            return enviosClient.buscarEnvio(idEnvio);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo obtener el envío", e);
        }
    }

    public Object actualizarEnvio(Long idEnvio, Map<String, Object> request) {
        try {
            return enviosClient.actualizarEnvio(idEnvio, request);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo actualizar el envío", e);
        }
    }

    public Object asignarTransportista(Long idEnvio, Map<String, Object> request) {
        try {
            return enviosClient.asignarTransportista(idEnvio, request);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo asignar el transportista", e);
        }
    }

    public Object actualizarEstadoEnvio(Long idEnvio, Map<String, Object> request) {
        try {
            return enviosClient.actualizarEstadoEnvio(idEnvio, request);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo actualizar el estado del envío", e);
        }
    }

    public List<?> listarSeguimiento(Long idEnvio) {
        try {
            return enviosClient.listarSeguimiento(idEnvio);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo obtener el seguimiento", e);
        }
    }
}
