package com.trinidad.citas.controller.api;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.dto.PagoDTO;
import com.trinidad.citas.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Profile("api")
@RestController
@RequestMapping("/api/v1/pagos")
@RequiredArgsConstructor
public class PagoRestController {

    private final PagoService pagoService;

    @GetMapping
    public List<PagoDTO> listar() {
        return pagoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.obtenerPorId(id));
    }

    @GetMapping("/cita/{idCita}")
    public ResponseEntity<PagoDTO> porCita(@PathVariable Long idCita) {
        return ResponseEntity.ok(pagoService.obtenerPorCita(idCita));
    }

    @GetMapping("/estado/{estado}")
    public List<PagoDTO> porEstado(@PathVariable String estado) {
        return pagoService.listarPorEstado(estado);
    }

    @PostMapping
    public ResponseEntity<PagoDTO> crear(@Valid @RequestBody PagoDTO dto) {
        return ResponseEntity.ok(pagoService.crear(dto));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<PagoDTO> cambiarEstado(@PathVariable Long id, @RequestParam String estado) {
        return ResponseEntity.ok(pagoService.cambiarEstado(id, estado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

