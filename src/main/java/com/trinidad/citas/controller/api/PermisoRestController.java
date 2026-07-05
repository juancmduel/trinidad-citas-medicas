package com.trinidad.citas.controller.api;

import com.trinidad.citas.dto.PermisoDTO;
import com.trinidad.citas.service.PermisoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Profile("api")
@RestController
@RequestMapping("/api/v1/permisos")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class PermisoRestController {

    private final PermisoService permisoService;

    @GetMapping
    public List<PermisoDTO> listAll() {
        return permisoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermisoDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(permisoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<PermisoDTO> create(@Valid @RequestBody PermisoDTO dto) {
        return ResponseEntity.ok(permisoService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PermisoDTO> update(@PathVariable Long id, @Valid @RequestBody PermisoDTO dto) {
        return ResponseEntity.ok(permisoService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        permisoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
