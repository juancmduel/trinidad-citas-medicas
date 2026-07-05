package com.trinidad.citas.controller.api;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.dto.HistoriaClinicaDTO;
import com.trinidad.citas.service.HistoriaClinicaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Profile("api")
@RestController
@RequestMapping("/api/v1/historias-clinicas")
@RequiredArgsConstructor
public class HistoriaClinicaRestController {

    private final HistoriaClinicaService historiaClinicaService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'MEDICO')")
    public List<HistoriaClinicaDTO> listar() {
        return historiaClinicaService.listarTodos();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'MEDICO')")
    public ResponseEntity<HistoriaClinicaDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(historiaClinicaService.obtenerPorId(id));
    }

    @GetMapping("/paciente/{idPaciente}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'MEDICO', 'PACIENTE')")
    public ResponseEntity<HistoriaClinicaDTO> porPaciente(@PathVariable Long idPaciente) {
        return ResponseEntity.ok(historiaClinicaService.obtenerPorPaciente(idPaciente));
    }

    @GetMapping("/nro/{nroHistoria}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'MEDICO')")
    public ResponseEntity<HistoriaClinicaDTO> porNro(@PathVariable String nroHistoria) {
        return ResponseEntity.ok(historiaClinicaService.obtenerPorNro(nroHistoria));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO')")
    public ResponseEntity<HistoriaClinicaDTO> crear(@Valid @RequestBody HistoriaClinicaDTO dto) {
        return ResponseEntity.ok(historiaClinicaService.crear(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO')")
    public ResponseEntity<HistoriaClinicaDTO> actualizar(@PathVariable Long id, @RequestBody HistoriaClinicaDTO dto) {
        return ResponseEntity.ok(historiaClinicaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        historiaClinicaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

