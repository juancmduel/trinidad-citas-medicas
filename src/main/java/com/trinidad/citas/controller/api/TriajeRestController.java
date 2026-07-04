package com.trinidad.citas.controller.api;

import com.trinidad.citas.dto.TriajeDTO;
import com.trinidad.citas.service.TriajeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Profile("api")
@RestController
@RequestMapping("/api/v1/triaje")
@RequiredArgsConstructor
public class TriajeRestController {

    private final TriajeService triajeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'ENFERMERA', 'MEDICO')")
    public ResponseEntity<List<TriajeDTO>> listar() {
        return ResponseEntity.ok(triajeService.listarTodos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'ENFERMERA', 'MEDICO')")
    public ResponseEntity<TriajeDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(triajeService.obtenerPorId(id));
    }

    @GetMapping("/cita/{idCita}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'ENFERMERA', 'MEDICO')")
    public ResponseEntity<TriajeDTO> obtenerPorCita(@PathVariable Long idCita) {
        return ResponseEntity.ok(triajeService.obtenerPorCita(idCita));
    }

    @PostMapping("/cita/{idCita}/registrar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ENFERMERA')")
    public ResponseEntity<TriajeDTO> registrar(@PathVariable Long idCita,
                                                @RequestParam(required = false) Long idEnfermera,
                                                @RequestBody TriajeDTO dto) {
        return ResponseEntity.ok(triajeService.registrar(idCita, idEnfermera, dto));
    }
}
