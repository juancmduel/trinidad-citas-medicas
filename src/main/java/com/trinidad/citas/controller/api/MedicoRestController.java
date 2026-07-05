package com.trinidad.citas.controller.api;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.dto.MedicoDTO;
import com.trinidad.citas.service.MedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Profile("api")
@RestController
@RequestMapping("/api/v1/medicos")
@RequiredArgsConstructor
public class MedicoRestController {

    private final MedicoService medicoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'RECEPCIONISTA', 'ENFERMERA', 'MEDICO', 'PACIENTE')")
    public List<MedicoDTO> listar() {
        return medicoService.listarActivos();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'RECEPCIONISTA', 'ENFERMERA', 'MEDICO', 'PACIENTE')")
    public ResponseEntity<MedicoDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(medicoService.obtener(id));
    }

    @GetMapping("/especialidad/{idEspecialidad}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'RECEPCIONISTA', 'ENFERMERA', 'MEDICO', 'PACIENTE')")
    public List<MedicoDTO> porEspecialidad(@PathVariable Long idEspecialidad) {
        return medicoService.listarPorEspecialidad(idEspecialidad);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    public ResponseEntity<MedicoDTO> crear(@Valid @RequestBody MedicoDTO dto) {
        return ResponseEntity.ok(medicoService.crear(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    public ResponseEntity<MedicoDTO> actualizar(@PathVariable Long id, @Valid @RequestBody MedicoDTO dto) {
        return ResponseEntity.ok(medicoService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        medicoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

