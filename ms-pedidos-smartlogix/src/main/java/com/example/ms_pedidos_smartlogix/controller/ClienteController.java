package com.example.ms_pedidos_smartlogix.controller;

import com.example.ms_pedidos_smartlogix.dto.ClienteRequestDTO;
import com.example.ms_pedidos_smartlogix.dto.ClienteResponseDTO;
import com.example.ms_pedidos_smartlogix.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> crearCliente(@RequestBody ClienteRequestDTO dto) {
        ClienteResponseDTO response = clienteService.crearCliente(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarClientes() {
        List<ClienteResponseDTO> clientes = clienteService.listarClientes();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{idCliente}")
    public ResponseEntity<ClienteResponseDTO> obtenerClientePorId(@PathVariable Long idCliente) {
        ClienteResponseDTO cliente = clienteService.obtenerClientePorId(idCliente);
        return ResponseEntity.ok(cliente);
    }

    @PutMapping("/{idCliente}")
    public ResponseEntity<ClienteResponseDTO> actualizarCliente(
            @PathVariable Long idCliente,
            @RequestBody ClienteRequestDTO dto) {
        ClienteResponseDTO response = clienteService.actualizarCliente(idCliente, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{idCliente}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long idCliente) {
        clienteService.eliminarCliente(idCliente);
        return ResponseEntity.noContent().build();
    }
}
