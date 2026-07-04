package com.trinidad.citas.controller.api;

import com.trinidad.citas.dto.AntecedenteDTO;
import com.trinidad.citas.service.AntecedenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Profile("api")
@RestController
@RequestMapping("/api/v1/antecedentes")
@RequiredArgsConstructor
public class AntecedenteRestController {

    private final AntecedenteService antecedenteService;

    @GetMapping("/historia/{idHistoria}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'MEDICO', 'ENFERMERA')")
    public ResponseEntity<List<AntecedenteDTO>> listarPorHistoria(@PathVariable Long idHistoria) {
        return ResponseEntity.ok(antecedenteService.listarPorHistoria(idHistoria));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO', 'ENFERMERA')")
    public ResponseEntity<AntecedenteDTO> crear(@RequestBody AntecedenteDTO dto) {
        return ResponseEntity.ok(antecedenteService.crear(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        antecedenteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
