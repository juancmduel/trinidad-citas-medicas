package com.trinidad.citas.controller.api;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.dto.DiagnosticoCie10DTO;
import com.trinidad.citas.service.DiagnosticoCie10Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Profile("api")
@RestController
@RequestMapping("/api/v1/diagnosticos")
@RequiredArgsConstructor
public class DiagnosticoCie10RestController {

    private final DiagnosticoCie10Service diagnosticoCie10Service;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'MEDICO', 'ENFERMERA')")
    public List<DiagnosticoCie10DTO> listAll() {
        return diagnosticoCie10Service.listarTodos();
    }

    @GetMapping("/{codigo}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'MEDICO', 'ENFERMERA')")
    public ResponseEntity<DiagnosticoCie10DTO> getByCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(diagnosticoCie10Service.obtenerPorCodigo(codigo));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    public ResponseEntity<DiagnosticoCie10DTO> crear(@Valid @RequestBody DiagnosticoCie10DTO dto) {
        return ResponseEntity.ok(diagnosticoCie10Service.crear(dto));
    }
}

