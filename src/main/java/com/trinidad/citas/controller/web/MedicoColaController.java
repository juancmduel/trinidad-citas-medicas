package com.trinidad.citas.controller.web;

import com.trinidad.citas.dto.TriajeDTO;
import com.trinidad.citas.model.EstadoCita;
import com.trinidad.citas.repository.CitaRepository;
import com.trinidad.citas.repository.MedicoRepository;
import com.trinidad.citas.service.CitaService;
import com.trinidad.citas.service.HorarioMedicoService;
import com.trinidad.citas.service.TriajeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Profile({"web", "default"})
@Controller
@RequestMapping("/medico")
@RequiredArgsConstructor
public class MedicoColaController {

    private final CitaRepository citaRepository;
    private final MedicoRepository medicoRepository;
    private final CitaService citaService;
    private final TriajeService triajeService;
    private final HorarioMedicoService horarioMedicoService;

    @GetMapping("/cola")
    public String cola(Model model, Principal principal) {
        var medico = medicoRepository.findByUsuario_Username(principal.getName())
                .orElseThrow(() -> new RuntimeException("Medico no encontrado para el usuario: " + principal.getName()));
        var citas = citaRepository.findByMedico_IdMedicoAndEstadoOrderByFechaCitaAscHoraInicioAsc(
                medico.getIdMedico(), EstadoCita.EN_ATENCION);

        Map<Long, TriajeDTO> triajesPorCita = new HashMap<>();
        long countAlta = 0, countMedia = 0, countBaja = 0;
        for (var c : citas) {
            try {
                var triaje = triajeService.obtenerPorCita(c.getIdCita());
                triajesPorCita.put(c.getIdCita(), triaje);
                String urg = triaje.getNivelUrgencia();
                if ("ALTA".equals(urg)) countAlta++;
                else if ("MEDIA".equals(urg)) countMedia++;
                else countBaja++;
            } catch (Exception e) {
                // sin triaje todavia
            }
        }

        model.addAttribute("citas", citas);
        model.addAttribute("medico", medico);
        model.addAttribute("triajesPorCita", triajesPorCita);
        model.addAttribute("totalCola", citas.size());
        model.addAttribute("countAlta", countAlta);
        model.addAttribute("countMedia", countMedia);
        model.addAttribute("countBaja", countBaja);
        model.addAttribute("titulo", "Mi Cola - Pacientes en Espera");
        return "medico/cola";
    }

    @GetMapping("/horarios")
    public String horarios(Model model, Principal principal) {
        var medico = medicoRepository.findByUsuario_Username(principal.getName())
                .orElseThrow(() -> new RuntimeException("Medico no encontrado para: " + principal.getName()));
        model.addAttribute("horarios", horarioMedicoService.listarPorMedico(medico.getIdMedico()));
        model.addAttribute("medico", medico);
        model.addAttribute("titulo", "Mis Horarios");
        return "medico/horarios";
    }

    @GetMapping("/api/cola")
    @ResponseBody
    public Map<String, Object> apiCola(Principal principal) {
        var medico = medicoRepository.findByUsuario_Username(principal.getName())
                .orElseThrow(() -> new RuntimeException("Medico no encontrado"));
        var citas = citaRepository.findByMedico_IdMedicoAndEstadoOrderByFechaCitaAscHoraInicioAsc(
                medico.getIdMedico(), EstadoCita.EN_ATENCION);

        long countAlta = 0, countMedia = 0, countBaja = 0;
        List<Map<String, Object>> lista = new ArrayList<>();
        for (var c : citas) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("idCita", c.getIdCita());
            item.put("horaInicio", c.getHoraInicio());
            item.put("numeroTurno", c.getNumeroTurno());
            item.put("nombrePaciente", c.getPaciente().getNombreCompleto());
            item.put("dniPaciente", c.getPaciente().getDni());
            item.put("consultorio", c.getMedico().getConsultorio());

            TriajeDTO triaje = null;
            try {
                triaje = triajeService.obtenerPorCita(c.getIdCita());
            } catch (Exception e) {
                // sin triaje
            }
            if (triaje != null) {
                String urg = triaje.getNivelUrgencia();
                if ("ALTA".equals(urg)) countAlta++;
                else if ("MEDIA".equals(urg)) countMedia++;
                else if (urg != null) countBaja++;
                item.put("presionArterial", triaje.getPresionArterial());
                item.put("frecuenciaCardiaca", triaje.getFrecuenciaCardiaca());
                item.put("temperatura", triaje.getTemperatura());
                item.put("saturacionO2", triaje.getSaturacionO2());
                item.put("nivelUrgencia", triaje.getNivelUrgencia());
            }
            lista.add(item);
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("pacientes", lista);
        response.put("total", lista.size());
        response.put("countAlta", countAlta);
        response.put("countMedia", countMedia);
        response.put("countBaja", countBaja);
        return response;
    }
}
