package com.smartlogix.inventario_service.service;

import com.smartlogix.inventario_service.dto.request.CategoriaProductoRequest;
import com.smartlogix.inventario_service.dto.response.CategoriaProductoResponse;
import com.smartlogix.inventario_service.exception.ResourceNotFoundException;
import com.smartlogix.inventario_service.model.CategoriaProducto;
import com.smartlogix.inventario_service.repository.CategoriaProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaProductoService {

    private final CategoriaProductoRepository categoriaProductoRepository;

    @Transactional(readOnly = true)
    public CategoriaProductoResponse obtenerPorId(Long id) {
        return categoriaProductoRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<CategoriaProductoResponse> listarTodas() {
        return categoriaProductoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public CategoriaProductoResponse crear(CategoriaProductoRequest request) {
        CategoriaProducto categoria = CategoriaProducto.builder()
                .nombreCategoria(request.getNombreCategoria())
                .descripcion(request.getDescripcion())
                .activa(request.getActiva() != null ? request.getActiva() : true)
                .build();

        CategoriaProducto categoriaSaved = categoriaProductoRepository.save(categoria);
        return mapToResponse(categoriaSaved);
    }

    public CategoriaProductoResponse actualizar(Long id, CategoriaProductoRequest request) {
        CategoriaProducto categoria = categoriaProductoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));

        categoria.setNombreCategoria(request.getNombreCategoria());
        categoria.setDescripcion(request.getDescripcion());
        if (request.getActiva() != null) {
            categoria.setActiva(request.getActiva());
        }

        CategoriaProducto categoriaUpdated = categoriaProductoRepository.save(categoria);
        return mapToResponse(categoriaUpdated);
    }

    public CategoriaProductoResponse desactivar(Long id) {
        CategoriaProducto categoria = categoriaProductoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));

        categoria.setActiva(false);
        CategoriaProducto categoriaUpdated = categoriaProductoRepository.save(categoria);
        return mapToResponse(categoriaUpdated);
    }

    private CategoriaProductoResponse mapToResponse(CategoriaProducto categoria) {
        return CategoriaProductoResponse.builder()
                .idCategoria(categoria.getIdCategoria())
                .nombreCategoria(categoria.getNombreCategoria())
                .descripcion(categoria.getDescripcion())
                .activa(categoria.getActiva())
                .build();
    }
}
