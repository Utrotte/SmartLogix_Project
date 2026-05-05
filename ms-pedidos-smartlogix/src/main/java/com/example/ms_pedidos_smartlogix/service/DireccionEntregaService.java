package com.example.ms_pedidos_smartlogix.service;

import com.example.ms_pedidos_smartlogix.dto.DireccionEntregaRequestDTO;
import com.example.ms_pedidos_smartlogix.dto.DireccionEntregaResponseDTO;
import com.example.ms_pedidos_smartlogix.model.DireccionEntrega;
import com.example.ms_pedidos_smartlogix.model.Pedido;
import com.example.ms_pedidos_smartlogix.repository.DireccionEntregaRepository;
import com.example.ms_pedidos_smartlogix.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DireccionEntregaService {

    private final DireccionEntregaRepository direccionRepository;
    private final PedidoRepository pedidoRepository;

    public DireccionEntregaService(DireccionEntregaRepository direccionRepository, PedidoRepository pedidoRepository) {
        this.direccionRepository = direccionRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public DireccionEntregaResponseDTO crearDireccion(DireccionEntregaRequestDTO dto) {
        // Validar que el pedido existe
        Pedido pedido = pedidoRepository.findById(dto.getIdPedido())
                .orElseThrow(() -> new RuntimeException("Pedido con ID " + dto.getIdPedido() + " no existe"));

        // No permitir más de una dirección por pedido
        if (direccionRepository.existsByPedido_IdPedido(dto.getIdPedido())) {
            throw new RuntimeException("El pedido con ID " + dto.getIdPedido() + " ya tiene una dirección asignada");
        }

        // Crear la dirección
        DireccionEntrega direccion = new DireccionEntrega(
            pedido,
            dto.getCalle(),
            dto.getNumero(),
            dto.getComuna(),
            dto.getCiudad(),
            dto.getRegion(),
            dto.getCodigoPostal(),
            dto.getReferencia()
        );

        DireccionEntrega direccionGuardada = direccionRepository.save(direccion);
        return convertToResponseDTO(direccionGuardada);
    }

    public DireccionEntregaResponseDTO obtenerDireccionPorPedido(Long idPedido) {
        if (!pedidoRepository.existsById(idPedido)) {
            throw new RuntimeException("Pedido con ID " + idPedido + " no existe");
        }
        DireccionEntrega direccion = direccionRepository.findByPedido_IdPedido(idPedido)
                .orElseThrow(() -> new RuntimeException("No existe dirección para el pedido con ID " + idPedido));
        return convertToResponseDTO(direccion);
    }

    public List<DireccionEntregaResponseDTO> listarDirecciones() {
        List<DireccionEntrega> direcciones = direccionRepository.findAll();
        return direcciones.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public DireccionEntregaResponseDTO actualizarDireccion(Long idDireccion, DireccionEntregaRequestDTO dto) {
        DireccionEntrega direccion = direccionRepository.findById(idDireccion)
                .orElseThrow(() -> new RuntimeException("Dirección con ID " + idDireccion + " no existe"));

        // Si cambia el pedido, validar que el nuevo pedido existe y no tiene otra dirección
        if (!direccion.getPedido().getIdPedido().equals(dto.getIdPedido())) {
            Pedido nuevoPedido = pedidoRepository.findById(dto.getIdPedido())
                    .orElseThrow(() -> new RuntimeException("Pedido con ID " + dto.getIdPedido() + " no existe"));

            if (direccionRepository.existsByPedido_IdPedido(dto.getIdPedido())) {
                throw new RuntimeException("El pedido con ID " + dto.getIdPedido() + " ya tiene una dirección asignada");
            }

            direccion.setPedido(nuevoPedido);
        }

        direccion.setCalle(dto.getCalle());
        direccion.setNumero(dto.getNumero());
        direccion.setComuna(dto.getComuna());
        direccion.setCiudad(dto.getCiudad());
        direccion.setRegion(dto.getRegion());
        direccion.setCodigoPostal(dto.getCodigoPostal());
        direccion.setReferencia(dto.getReferencia());

        DireccionEntrega direccionActualizada = direccionRepository.save(direccion);
        return convertToResponseDTO(direccionActualizada);
    }

    public void eliminarDireccion(Long idDireccion) {
        if (!direccionRepository.existsById(idDireccion)) {
            throw new RuntimeException("Dirección con ID " + idDireccion + " no existe");
        }
        direccionRepository.deleteById(idDireccion);
    }

    private DireccionEntregaResponseDTO convertToResponseDTO(DireccionEntrega direccion) {
        return new DireccionEntregaResponseDTO(
            direccion.getIdDireccion(),
            direccion.getPedido().getIdPedido(),
            direccion.getCalle(),
            direccion.getNumero(),
            direccion.getComuna(),
            direccion.getCiudad(),
            direccion.getRegion(),
            direccion.getCodigoPostal(),
            direccion.getReferencia()
        );
    }
}
