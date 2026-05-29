package com.trinidad.citas.controller.api;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.dto.HistoriaClinicaDTO;
import com.trinidad.citas.service.HistoriaClinicaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Profile("api")
@RestController
@RequestMapping("/api/v1/historias-clinicas")
@RequiredArgsConstructor
public class HistoriaClinicaRestController {

    private final HistoriaClinicaService historiaClinicaService;

    @GetMapping
    public List<HistoriaClinicaDTO> listar() {
        return historiaClinicaService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoriaClinicaDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(historiaClinicaService.obtenerPorId(id));
    }

    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<HistoriaClinicaDTO> porPaciente(@PathVariable Long idPaciente) {
        return ResponseEntity.ok(historiaClinicaService.obtenerPorPaciente(idPaciente));
    }

    @GetMapping("/nro/{nroHistoria}")
    public ResponseEntity<HistoriaClinicaDTO> porNro(@PathVariable String nroHistoria) {
        return ResponseEntity.ok(historiaClinicaService.obtenerPorNro(nroHistoria));
    }

    @PostMapping
    public ResponseEntity<HistoriaClinicaDTO> crear(@Valid @RequestBody HistoriaClinicaDTO dto) {
        return ResponseEntity.ok(historiaClinicaService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistoriaClinicaDTO> actualizar(@PathVariable Long id, @RequestBody HistoriaClinicaDTO dto) {
        return ResponseEntity.ok(historiaClinicaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        historiaClinicaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

