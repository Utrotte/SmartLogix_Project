package com.example.ms_pedidos_smartlogix.service;

import com.example.ms_pedidos_smartlogix.dto.DetallePedidoRequestDTO;
import com.example.ms_pedidos_smartlogix.dto.DetallePedidoResponseDTO;
import com.example.ms_pedidos_smartlogix.model.DetallePedido;
import com.example.ms_pedidos_smartlogix.model.Pedido;
import com.example.ms_pedidos_smartlogix.repository.DetallePedidoRepository;
import com.example.ms_pedidos_smartlogix.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DetallePedidoService {

    private final DetallePedidoRepository detallePedidoRepository;
    private final PedidoRepository pedidoRepository;

    public DetallePedidoService(DetallePedidoRepository detallePedidoRepository, PedidoRepository pedidoRepository) {
        this.detallePedidoRepository = detallePedidoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public DetallePedidoResponseDTO agregarDetalle(DetallePedidoRequestDTO dto) {
        // Validar que el pedido existe
        Pedido pedido = pedidoRepository.findById(dto.getIdPedido())
                .orElseThrow(() -> new RuntimeException("Pedido con ID " + dto.getIdPedido() + " no existe"));

        // Calcular subtotal si viene null
        BigDecimal subtotal = dto.getSubtotal();
        if (subtotal == null && dto.getCantidad() != null && dto.getPrecioUnitario() != null) {
            subtotal = dto.getPrecioUnitario().multiply(new BigDecimal(dto.getCantidad()));
        }

        // Crear el detalle
        DetallePedido detalle = new DetallePedido(
            pedido,
            dto.getIdProductoRef(),
            dto.getCodigoSkuRef(),
            dto.getNombreProductoSnapshot(),
            dto.getCantidad(),
            dto.getPrecioUnitario(),
            subtotal,
            dto.getEstadoDetalle()
        );

        DetallePedido detalleGuardado = detallePedidoRepository.save(detalle);

        // Actualizar totalBruto del pedido sumando el subtotal
        if (subtotal != null) {
            BigDecimal nuevoTotalBruto = pedido.getTotalBruto().add(subtotal);
            pedido.setTotalBruto(nuevoTotalBruto);

            // Actualizar totalNeto = totalBruto - descuentoTotal
            BigDecimal nuevoTotalNeto = nuevoTotalBruto.subtract(pedido.getDescuentoTotal());
            pedido.setTotalNeto(nuevoTotalNeto);

            pedidoRepository.save(pedido);
        }

        return convertToResponseDTO(detalleGuardado);
    }

    public List<DetallePedidoResponseDTO> listarDetalles() {
        List<DetallePedido> detalles = detallePedidoRepository.findAll();
        return detalles.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<DetallePedidoResponseDTO> listarDetallesPorPedido(Long idPedido) {
        if (!pedidoRepository.existsById(idPedido)) {
            throw new RuntimeException("Pedido con ID " + idPedido + " no existe");
        }
        List<DetallePedido> detalles = detallePedidoRepository.findByPedido_IdPedido(idPedido);
        return detalles.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public DetallePedidoResponseDTO obtenerDetallePorId(Long idDetalle) {
        DetallePedido detalle = detallePedidoRepository.findById(idDetalle)
                .orElseThrow(() -> new RuntimeException("Detalle con ID " + idDetalle + " no existe"));
        return convertToResponseDTO(detalle);
    }

    public void eliminarDetalle(Long idDetalle) {
        if (!detallePedidoRepository.existsById(idDetalle)) {
            throw new RuntimeException("Detalle con ID " + idDetalle + " no existe");
        }
        detallePedidoRepository.deleteById(idDetalle);
    }

    private DetallePedidoResponseDTO convertToResponseDTO(DetallePedido detalle) {
        return new DetallePedidoResponseDTO(
            detalle.getIdDetalle(),
            detalle.getPedido().getIdPedido(),
            detalle.getIdProductoRef(),
            detalle.getCodigoSkuRef(),
            detalle.getNombreProductoSnapshot(),
            detalle.getCantidad(),
            detalle.getPrecioUnitario(),
            detalle.getSubtotal(),
            detalle.getEstadoDetalle()
        );
    }
}
