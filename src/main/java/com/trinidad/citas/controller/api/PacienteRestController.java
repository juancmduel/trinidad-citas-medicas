package com.trinidad.citas.controller.api;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.dto.PacienteDTO;
import com.trinidad.citas.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Profile("api")
@RestController
@RequestMapping("/api/v1/pacientes")
@RequiredArgsConstructor
public class PacienteRestController {

    private final PacienteService pacienteService;

    @GetMapping
    public List<PacienteDTO> listar() {
        return pacienteService.listarTodos();
    }

    @GetMapping("/{id}")
    public PacienteDTO obtener(@PathVariable Long id) {
        return pacienteService.obtenerDTO(id);
    }

    @GetMapping("/dni/{dni}")
    public PacienteDTO porDni(@PathVariable String dni) {
        return pacienteService.obtenerPorDni(dni);
    }

    @PostMapping
    public ResponseEntity<PacienteDTO> crear(@Valid @RequestBody PacienteDTO dto) {
        return ResponseEntity.ok(pacienteService.crear(dto));
    }

    @PutMapping("/{id}")
    public PacienteDTO actualizar(@PathVariable Long id, @Valid @RequestBody PacienteDTO dto) {
        return pacienteService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pacienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
