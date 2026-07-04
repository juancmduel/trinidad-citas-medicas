package com.trinidad.citas.controller.api;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.dto.DetalleRecetaDTO;
import com.trinidad.citas.service.DetalleRecetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Profile("api")
@RestController
@RequestMapping("/api/v1/detalles-receta")
@RequiredArgsConstructor
public class DetalleRecetaRestController {

    private final DetalleRecetaService detalleRecetaService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'MEDICO')")
    public List<DetalleRecetaDTO> listAll() {
        return detalleRecetaService.listarTodos();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'MEDICO')")
    public ResponseEntity<DetalleRecetaDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(detalleRecetaService.obtenerPorId(id));
    }

    @GetMapping("/receta/{idReceta}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'MEDICO')")
    public List<DetalleRecetaDTO> getByReceta(@PathVariable Long idReceta) {
        return detalleRecetaService.listarPorReceta(idReceta);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO')")
    public ResponseEntity<DetalleRecetaDTO> crear(@RequestBody DetalleRecetaDTO dto) {
        return ResponseEntity.ok(detalleRecetaService.crear(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        detalleRecetaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

