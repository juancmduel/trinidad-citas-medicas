package com.trinidad.citas.controller.api;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.dto.CitaDTO;
import com.trinidad.citas.model.EstadoCita;
import com.trinidad.citas.service.CitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'MEDICO', 'RECEPCIONISTA', 'ENFERMERA')")
    public ResponseEntity<List<CitaDTO>> listar(@RequestParam(required = false)
                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<CitaDTO> citas = fecha != null ? citaService.listarPorFecha(fecha) : citaService.listarTodas();
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'RECEPCIONISTA', 'ENFERMERA') " +
                  "or (hasRole('MEDICO') and @citaSecurity.isMedicoAsignado(#id, principal)) " +
                  "or (hasRole('PACIENTE') and @citaSecurity.isOwnCita(#id, principal))")
    public ResponseEntity<CitaDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.obtenerDTO(id));
    }

    @GetMapping("/paciente/{idPaciente}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'MEDICO', 'RECEPCIONISTA', 'ENFERMERA') " +
                  "or (hasRole('PACIENTE') and @pacienteSecurity.isOwnPaciente(#idPaciente, principal))")
    public ResponseEntity<List<CitaDTO>> porPaciente(@PathVariable Long idPaciente) {
        return ResponseEntity.ok(citaService.listarPorPaciente(idPaciente));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'RECEPCIONISTA', 'PACIENTE')")
    public ResponseEntity<CitaDTO> agendar(@Valid @RequestBody CitaDTO dto) {
        return ResponseEntity.ok(citaService.agendar(dto));
    }

    @PutMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE') " +
                  "or (hasRole('MEDICO') and @citaSecurity.isMedicoAsignado(#id, principal))")
    public ResponseEntity<CitaDTO> cambiarEstado(@PathVariable Long id, @RequestParam EstadoCita estado) {
        return ResponseEntity.ok(citaService.cambiarEstado(id, estado));
    }

    @PostMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'RECEPCIONISTA') " +
                  "or (hasRole('PACIENTE') and @citaSecurity.isOwnCita(#id, principal))")
    public ResponseEntity<CitaDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.cancelar(id));
    }

    @PostMapping("/{id}/checkin")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ENFERMERA')")
    public ResponseEntity<CitaDTO> checkin(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.checkin(id));
    }

    @PostMapping("/{id}/finalizar")
    @PreAuthorize("hasRole('ADMINISTRADOR') " +
                  "or (hasRole('MEDICO') and @citaSecurity.isMedicoAsignado(#id, principal))")
    public ResponseEntity<CitaDTO> finalizar(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.finalizar(id));
    }
}
