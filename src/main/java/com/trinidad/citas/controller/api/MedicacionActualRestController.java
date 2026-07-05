package com.trinidad.citas.controller.api;

import com.trinidad.citas.dto.MedicacionActualDTO;
import com.trinidad.citas.service.MedicacionActualService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Profile("api")
@RestController
@RequestMapping("/api/v1/medicacion-actual")
@RequiredArgsConstructor
public class MedicacionActualRestController {

    private final MedicacionActualService medicacionActualService;

    @GetMapping("/historia/{idHistoria}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'MEDICO', 'ENFERMERA')")
    public ResponseEntity<List<MedicacionActualDTO>> listarPorHistoria(@PathVariable Long idHistoria) {
        return ResponseEntity.ok(medicacionActualService.listarActivasPorHistoria(idHistoria));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO')")
    public ResponseEntity<MedicacionActualDTO> crear(@RequestBody MedicacionActualDTO dto) {
        return ResponseEntity.ok(medicacionActualService.crear(dto));
    }

    @PutMapping("/{id}/desactivar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO')")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        medicacionActualService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
