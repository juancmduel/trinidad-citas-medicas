package com.trinidad.citas.controller.api;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.dto.AtencionDTO;
import com.trinidad.citas.service.AtencionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Profile("api")
@RestController
@RequestMapping("/api/v1/atenciones")
@RequiredArgsConstructor
public class AtencionRestController {

    private final AtencionService atencionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'MEDICO')")
    public List<AtencionDTO> listar() {
        return atencionService.listarTodos();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'MEDICO')")
    public ResponseEntity<AtencionDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(atencionService.obtenerPorId(id));
    }

    @GetMapping("/cita/{idCita}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'MEDICO')")
    public ResponseEntity<AtencionDTO> porCita(@PathVariable Long idCita) {
        return ResponseEntity.ok(atencionService.obtenerPorCita(idCita));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO')")
    public ResponseEntity<AtencionDTO> crear(@Valid @RequestBody AtencionDTO dto) {
        return ResponseEntity.ok(atencionService.crear(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        atencionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

