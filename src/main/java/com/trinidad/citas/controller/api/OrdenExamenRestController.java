package com.trinidad.citas.controller.api;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.dto.OrdenExamenDTO;
import com.trinidad.citas.service.OrdenExamenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Profile("api")
@RestController
@RequestMapping("/api/v1/ordenes-examen")
@RequiredArgsConstructor
public class OrdenExamenRestController {

    private final OrdenExamenService ordenExamenService;

    @GetMapping
    public List<OrdenExamenDTO> listar() {
        return ordenExamenService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenExamenDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ordenExamenService.obtenerPorId(id));
    }

    @GetMapping("/atencion/{idAtencion}")
    public List<OrdenExamenDTO> porAtencion(@PathVariable Long idAtencion) {
        return ordenExamenService.listarPorAtencion(idAtencion);
    }

    @PostMapping
    public ResponseEntity<OrdenExamenDTO> crear(@Valid @RequestBody OrdenExamenDTO dto) {
        return ResponseEntity.ok(ordenExamenService.crear(dto));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<OrdenExamenDTO> cambiarEstado(@PathVariable Long id, @RequestParam String estado) {
        return ResponseEntity.ok(ordenExamenService.cambiarEstado(id, estado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ordenExamenService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

