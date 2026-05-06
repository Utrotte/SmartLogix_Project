package com.example.ms_pedidos_smartlogix.integration.service;

import com.example.ms_pedidos_smartlogix.client.InventarioClient;
import com.example.ms_pedidos_smartlogix.exception.IntegrationException;
import com.example.ms_pedidos_smartlogix.integration.dto.ReservaInventarioRequestDTO;
import com.example.ms_pedidos_smartlogix.integration.dto.ReservaInventarioResponseDTO;
import feign.FeignException;
import org.springframework.stereotype.Service;

@Service
public class InventarioIntegrationService {

    private final InventarioClient inventarioClient;

    public InventarioIntegrationService(InventarioClient inventarioClient) {
        this.inventarioClient = inventarioClient;
    }

    public ReservaInventarioResponseDTO reservarInventario(ReservaInventarioRequestDTO requestDTO) {
        try {
            ReservaInventarioResponseDTO response = inventarioClient.crearReserva(requestDTO);

            if (response == null) {
                throw new IntegrationException("El microservicio de inventario respondió sin contenido");
            }

            // Aceptar estados CREADA o RESERVADA como exitosos
            if (!"CREADA".equalsIgnoreCase(response.getEstadoReserva()) && 
                !"RESERVADA".equalsIgnoreCase(response.getEstadoReserva())) {
                throw new IntegrationException("Inventario no pudo reservar stock. Estado recibido: "
                        + response.getEstadoReserva() + ". Mensaje: " + response.getMensaje());
            }

            return response;

        } catch (FeignException e) {
            throw new IntegrationException("Error HTTP al comunicarse con ms-inventario: "
                    + e.status() + " - " + e.getMessage());
        } catch (IntegrationException e) {
            throw e;
        } catch (Exception e) {
            throw new IntegrationException("Error inesperado al comunicarse con inventario: "
                    + e.getMessage());
        }
    }
}
