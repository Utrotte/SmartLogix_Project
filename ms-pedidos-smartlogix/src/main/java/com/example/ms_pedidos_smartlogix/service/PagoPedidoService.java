package com.example.ms_pedidos_smartlogix.service;

import com.example.ms_pedidos_smartlogix.dto.PagoPedidoRequestDTO;
import com.example.ms_pedidos_smartlogix.dto.PagoPedidoResponseDTO;
import com.example.ms_pedidos_smartlogix.model.Pedido;
import com.example.ms_pedidos_smartlogix.model.PagoPedido;
import com.example.ms_pedidos_smartlogix.repository.PagoPedidoRepository;
import com.example.ms_pedidos_smartlogix.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PagoPedidoService {

    private final PagoPedidoRepository pagoPedidoRepository;
    private final PedidoRepository pedidoRepository;

    public PagoPedidoService(PagoPedidoRepository pagoPedidoRepository, PedidoRepository pedidoRepository) {
        this.pagoPedidoRepository = pagoPedidoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public PagoPedidoResponseDTO registrarPago(PagoPedidoRequestDTO dto) {
        // Validar que el pedido existe
        Pedido pedido = pedidoRepository.findById(dto.getIdPedido())
                .orElseThrow(() -> new RuntimeException("Pedido con ID " + dto.getIdPedido() + " no existe"));

        // Crear el pago
        PagoPedido pago = new PagoPedido(
            pedido,
            dto.getMetodoPago(),
            dto.getEstadoPago(),
            dto.getMonto(),
            dto.getCodigoTransaccion()
        );

        PagoPedido pagoGuardado = pagoPedidoRepository.save(pago);
        return convertToResponseDTO(pagoGuardado);
    }

    public List<PagoPedidoResponseDTO> listarPagos() {
        List<PagoPedido> pagos = pagoPedidoRepository.findAll();
        return pagos.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<PagoPedidoResponseDTO> listarPagosPorPedido(Long idPedido) {
        if (!pedidoRepository.existsById(idPedido)) {
            throw new RuntimeException("Pedido con ID " + idPedido + " no existe");
        }
        List<PagoPedido> pagos = pagoPedidoRepository.findByPedido_IdPedido(idPedido);
        return pagos.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public PagoPedidoResponseDTO obtenerPagoPorId(Long idPago) {
        PagoPedido pago = pagoPedidoRepository.findById(idPago)
                .orElseThrow(() -> new RuntimeException("Pago con ID " + idPago + " no existe"));
        return convertToResponseDTO(pago);
    }

    private PagoPedidoResponseDTO convertToResponseDTO(PagoPedido pago) {
        return new PagoPedidoResponseDTO(
            pago.getIdPago(),
            pago.getPedido().getIdPedido(),
            pago.getMetodoPago(),
            pago.getEstadoPago(),
            pago.getMonto(),
            pago.getCodigoTransaccion(),
            pago.getFechaPago()
        );
    }
}
