package com.trinidad.citas.controller.api;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.dto.EspecialidadDTO;
import com.trinidad.citas.service.EspecialidadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Profile("api")
@RestController
@RequestMapping("/api/v1/especialidades")
@RequiredArgsConstructor
public class EspecialidadRestController {

    private final EspecialidadService especialidadService;

    @GetMapping
    @PreAuthorize("permitAll()")
    public List<EspecialidadDTO> listar() {
        return especialidadService.listarActivas();
    }

    @GetMapping("/todas")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    public List<EspecialidadDTO> listarTodas() {
        return especialidadService.listarTodas();
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<EspecialidadDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(especialidadService.obtener(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    public ResponseEntity<EspecialidadDTO> crear(@Valid @RequestBody EspecialidadDTO dto) {
        return ResponseEntity.ok(especialidadService.crear(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    public ResponseEntity<EspecialidadDTO> actualizar(@PathVariable Long id, @Valid @RequestBody EspecialidadDTO dto) {
        return ResponseEntity.ok(especialidadService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        especialidadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

