package com.smartlogix.inventario_service.service;

import com.smartlogix.inventario_service.dto.request.ProductoRequest;
import com.smartlogix.inventario_service.dto.response.ProductoResponse;
import com.smartlogix.inventario_service.exception.DuplicateResourceException;
import com.smartlogix.inventario_service.exception.ResourceNotFoundException;
import com.smartlogix.inventario_service.model.CategoriaProducto;
import com.smartlogix.inventario_service.model.Producto;
import com.smartlogix.inventario_service.repository.CategoriaProductoRepository;
import com.smartlogix.inventario_service.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional // Todas las operaciones son transaccionales
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaProductoRepository categoriaProductoRepository;

    // Lectura: obtener producto por ID
    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
    }

    // Lectura: obtener producto por código SKU
    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorSku(String codigoSku) {
        return productoRepository.findByCodigoSku(codigoSku)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con SKU: " + codigoSku));
    }

    // Lectura: listar todos los productos
    @Transactional(readOnly = true)
    public List<ProductoResponse> listarTodos() {
        return productoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Crear nuevo producto
    public ProductoResponse crear(ProductoRequest request) {
        // Validar que el SKU no exista (restricción única)
        if (productoRepository.existsByCodigoSku(request.getCodigoSku())) {
            throw new DuplicateResourceException("Ya existe un producto con el SKU: " + request.getCodigoSku());
        }

        // Obtener la categoría
        CategoriaProducto categoria = categoriaProductoRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + request.getIdCategoria()));

        Producto producto = Producto.builder()
                .categoria(categoria)
                .codigoSku(request.getCodigoSku())
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .marca(request.getMarca())
                .precioReferencia(request.getPrecioReferencia())
                .activo(request.getActivo() != null ? request.getActivo() : true)
                .build();

        Producto productoSaved = productoRepository.save(producto);
        return mapToResponse(productoSaved);
    }

    // Actualizar producto existente
    public ProductoResponse actualizar(Long id, ProductoRequest request) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        // Validar SKU única solo si cambió
        if (!producto.getCodigoSku().equals(request.getCodigoSku()) &&
                productoRepository.existsByCodigoSku(request.getCodigoSku())) {
            throw new DuplicateResourceException("Ya existe un producto con el SKU: " + request.getCodigoSku());
        }

        // Obtener la categoría
        CategoriaProducto categoria = categoriaProductoRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + request.getIdCategoria()));

        producto.setCategoria(categoria);
        producto.setCodigoSku(request.getCodigoSku());
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setMarca(request.getMarca());
        producto.setPrecioReferencia(request.getPrecioReferencia());
        if (request.getActivo() != null) {
            producto.setActivo(request.getActivo());
        }

        Producto productoUpdated = productoRepository.save(producto);
        return mapToResponse(productoUpdated);
    }

    // Desactivar producto (baja lógica)
    public ProductoResponse desactivar(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        producto.setActivo(false);
        Producto productoUpdated = productoRepository.save(producto);
        return mapToResponse(productoUpdated);
    }

    private ProductoResponse mapToResponse(Producto producto) {
        return ProductoResponse.builder()
                .idProducto(producto.getIdProducto())
                .idCategoria(producto.getCategoria().getIdCategoria())
                .nombreCategoria(producto.getCategoria().getNombreCategoria())
                .codigoSku(producto.getCodigoSku())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .marca(producto.getMarca())
                .precioReferencia(producto.getPrecioReferencia())
                .activo(producto.getActivo())
                .fechaCreacion(producto.getFechaCreacion())
                .build();
    }
}
