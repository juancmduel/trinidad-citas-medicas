package com.trinidad.citas.controller.api;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.dto.HorarioMedicoDTO;
import com.trinidad.citas.repository.CitaRepository;
import com.trinidad.citas.repository.HorarioMedicoRepository;
import com.trinidad.citas.service.HorarioMedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Profile("api")
@RestController
@RequestMapping("/api/v1/horarios")
@RequiredArgsConstructor
public class HorarioMedicoRestController {

    private final HorarioMedicoService horarioMedicoService;
    private final HorarioMedicoRepository horarioMedicoRepository;
    private final CitaRepository citaRepository;

    @GetMapping
    public List<HorarioMedicoDTO> listar() {
        return horarioMedicoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HorarioMedicoDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(horarioMedicoService.obtenerPorId(id));
    }

    @GetMapping("/medico/{idMedico}")
    public List<HorarioMedicoDTO> porMedico(@PathVariable Long idMedico) {
        return horarioMedicoService.listarPorMedico(idMedico);
    }

    @PostMapping
    public ResponseEntity<HorarioMedicoDTO> crear(@Valid @RequestBody HorarioMedicoDTO dto) {
        return ResponseEntity.ok(horarioMedicoService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HorarioMedicoDTO> actualizar(@PathVariable Long id, @Valid @RequestBody HorarioMedicoDTO dto) {
        return ResponseEntity.ok(horarioMedicoService.actualizar(id, dto));
    }

    @GetMapping("/disponibilidad/{idMedico}")
    public ResponseEntity<?> verificarDisponibilidad(
            @PathVariable Long idMedico,
            @RequestParam String fecha,
            @RequestParam String hora) {
        try {
            LocalDate date = LocalDate.parse(fecha);
            LocalTime time = LocalTime.parse(hora);
            int dayOfWeek = date.getDayOfWeek().getValue();

            var horarios = horarioMedicoRepository.findByMedico_IdMedicoAndDiaSemanaAndActivo(idMedico, dayOfWeek, 1);

            Map<String, Object> response = new HashMap<>();
            response.put("disponible", false);

            for (var horario : horarios) {
                LocalTime inicio = LocalTime.parse(horario.getHoraInicio());
                LocalTime fin = LocalTime.parse(horario.getHoraFin());
                if (time.isAfter(inicio) && time.isBefore(fin)) {
                    long citasEnHora = citaRepository.findByMedico_IdMedicoAndFechaCita(idMedico, date).stream()
                            .filter(c -> c.getHoraInicio() != null && c.getHoraInicio().equals(hora))
                            .count();
                    response.put("disponible", citasEnHora == 0);
                    response.put("fecha", fecha);
                    response.put("hora", hora);
                    break;
                }
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Formato de fecha u hora inválido"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        horarioMedicoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
