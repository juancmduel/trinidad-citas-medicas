package com.trinidad.citas.controller.api;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.dto.CitaDTO;
import com.trinidad.citas.model.EstadoCita;
import com.trinidad.citas.service.CitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Profile("api")
@RestController
@RequestMapping("/api/v1/citas")
@RequiredArgsConstructor
public class CitaRestController {

    private final CitaService citaService;

    @GetMapping
    public List<CitaDTO> listar(@RequestParam(required = false)
                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return fecha != null ? citaService.listarPorFecha(fecha) : citaService.listarTodas();
    }

    @GetMapping("/{id}")
    public CitaDTO obtener(@PathVariable Long id) {
        return citaService.obtenerDTO(id);
    }

    @GetMapping("/paciente/{idPaciente}")
    public List<CitaDTO> porPaciente(@PathVariable Long idPaciente) {
        return citaService.listarPorPaciente(idPaciente);
    }

    @PostMapping
    public ResponseEntity<CitaDTO> agendar(@Valid @RequestBody CitaDTO dto) {
        return ResponseEntity.ok(citaService.agendar(dto));
    }

    @PutMapping("/{id}/estado")
    public CitaDTO cambiarEstado(@PathVariable Long id, @RequestParam EstadoCita estado) {
        return citaService.cambiarEstado(id, estado);
    }

    @PostMapping("/{id}/cancelar")
    public CitaDTO cancelar(@PathVariable Long id) {
        return citaService.cancelar(id);
    }

    @PostMapping("/{id}/checkin")
    public CitaDTO checkin(@PathVariable Long id) {
        return citaService.checkin(id);
    }

    @PostMapping("/{id}/finalizar")
    public CitaDTO finalizar(@PathVariable Long id) {
        return citaService.finalizar(id);
    }
}
