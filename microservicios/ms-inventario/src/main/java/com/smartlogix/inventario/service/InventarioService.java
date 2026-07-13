package com.smartlogix.inventario.service;

import com.smartlogix.inventario.domain.Existencia;
import com.smartlogix.inventario.domain.MovimientoInventario;
import com.smartlogix.inventario.domain.ReservaInventario;
import com.smartlogix.inventario.domain.enums.EstadoReserva;
import com.smartlogix.inventario.domain.enums.TipoMovimiento;
import com.smartlogix.inventario.dto.*;
import com.smartlogix.inventario.repository.ExistenciaRepository;
import com.smartlogix.inventario.repository.MovimientoInventarioRepository;
import com.smartlogix.inventario.repository.ReservaInventarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventarioService {

    private final ExistenciaRepository existenciaRepository;
    private final MovimientoInventarioRepository movimientoRepository;
    private final ReservaInventarioRepository reservaRepository;

    public InventarioService(ExistenciaRepository existenciaRepository, MovimientoInventarioRepository movimientoRepository, ReservaInventarioRepository reservaRepository) {
        this.existenciaRepository = existenciaRepository;
        this.movimientoRepository = movimientoRepository;
        this.reservaRepository = reservaRepository;
    }

    public List<ExistenciaResponse> listarExistencias() {
        return existenciaRepository.findAll().stream().map(e -> new ExistenciaResponse(e.getId(), e.getIdProducto(), e.getIdBodega(), e.getStockActual(), e.getStockReservado(), e.isActivo())).collect(Collectors.toList());
    }

    public List<ExistenciaResponse> listarPorProducto(Long idProducto) {
        return existenciaRepository.findByIdProducto(idProducto).stream().map(e -> new ExistenciaResponse(e.getId(), e.getIdProducto(), e.getIdBodega(), e.getStockActual(), e.getStockReservado(), e.isActivo())).collect(Collectors.toList());
    }

    public List<ExistenciaResponse> listarPorBodega(Long idBodega) {
        return existenciaRepository.findByIdBodega(idBodega).stream().map(e -> new ExistenciaResponse(e.getId(), e.getIdProducto(), e.getIdBodega(), e.getStockActual(), e.getStockReservado(), e.isActivo())).collect(Collectors.toList());
    }

    @Transactional
    public ExistenciaResponse crearExistencia(CrearExistenciaRequest req) {
        Existencia existing = existenciaRepository.findByIdProductoAndIdBodega(req.getIdProducto(), req.getIdBodega());
        if (existing != null) {
            throw new RuntimeException("Existencia ya existe para producto y bodega");
        }
        Existencia e = new Existencia(req.getIdProducto(), req.getIdBodega(), req.getStockActual());
        e = existenciaRepository.save(e);
        return new ExistenciaResponse(e.getId(), e.getIdProducto(), e.getIdBodega(), e.getStockActual(), e.getStockReservado(), e.isActivo());
    }

    @Transactional
    public MovimientoInventarioResponse crearMovimiento(CrearMovimientoRequest req) {
        Existencia e = existenciaRepository.findByIdProductoAndIdBodega(req.getIdProducto(), req.getIdBodega());
        if (e == null) {
            throw new RuntimeException("Existencia no encontrada");
        }
        int cantidad = req.getCantidad();
        if (req.getTipoMovimiento() == TipoMovimiento.ENTRADA) {
            e.setStockActual(e.getStockActual() + cantidad);
        } else if (req.getTipoMovimiento() == TipoMovimiento.SALIDA) {
            int disponible = e.getStockDisponible();
            if (disponible < cantidad) throw new RuntimeException("Stock insuficiente");
            e.setStockActual(e.getStockActual() - cantidad);
        }
        existenciaRepository.save(e);

        MovimientoInventario m = new MovimientoInventario(req.getIdProducto(), req.getIdBodega(), req.getTipoMovimiento(), req.getCantidad(), req.getMotivo());
        m = movimientoRepository.save(m);
        return new MovimientoInventarioResponse(m.getId(), m.getIdProducto(), m.getIdBodega(), m.getTipoMovimiento(), m.getCantidad(), m.getMotivo(), m.getFechaMovimiento());
    }

    @Transactional
    public ReservaInventarioResponse crearReserva(CrearReservaRequest req) {
        Existencia e = existenciaRepository.findByIdProductoAndIdBodega(req.getIdProducto(), req.getIdBodega());
        if (e == null) throw new RuntimeException("Existencia no encontrada");
        int disponible = e.getStockDisponible();
        if (disponible < req.getCantidadReservada()) throw new RuntimeException("No hay stock disponible para reservar");
        e.setStockReservado(e.getStockReservado() + req.getCantidadReservada());
        existenciaRepository.save(e);

        ReservaInventario r = new ReservaInventario(req.getIdProducto(), req.getIdBodega(), req.getIdPedido(), req.getCantidadReservada(), EstadoReserva.ACTIVA);
        r = reservaRepository.save(r);
        return new ReservaInventarioResponse(r.getId(), r.getIdProducto(), r.getIdBodega(), r.getIdPedido(), r.getCantidadReservada(), r.getEstadoReserva(), r.getFechaReserva());
    }

    @Transactional
    public void liberarReserva(Long idReserva) {
        ReservaInventario r = reservaRepository.findById(idReserva).orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        if (r.getEstadoReserva() != EstadoReserva.ACTIVA) throw new RuntimeException("Reserva no activa");
        Existencia e = existenciaRepository.findByIdProductoAndIdBodega(r.getIdProducto(), r.getIdBodega());
        if (e == null) throw new RuntimeException("Existencia no encontrada");
        e.setStockReservado(Math.max(0, e.getStockReservado() - r.getCantidadReservada()));
        existenciaRepository.save(e);
        r.setEstadoReserva(EstadoReserva.LIBERADA);
        reservaRepository.save(r);
    }

    public List<MovimientoInventarioResponse> movimientosPorProducto(Long idProducto) {
        return movimientoRepository.findByIdProducto(idProducto).stream().map(m -> new MovimientoInventarioResponse(m.getId(), m.getIdProducto(), m.getIdBodega(), m.getTipoMovimiento(), m.getCantidad(), m.getMotivo(), m.getFechaMovimiento())).collect(Collectors.toList());
    }

    public List<ReservaInventarioResponse> reservasPorPedido(Long idPedido) {
        return reservaRepository.findByIdPedido(idPedido).stream().map(r -> new ReservaInventarioResponse(r.getId(), r.getIdProducto(), r.getIdBodega(), r.getIdPedido(), r.getCantidadReservada(), r.getEstadoReserva(), r.getFechaReserva())).collect(Collectors.toList());
    }
}
