package com.trinidad.citas.controller.web;

import com.trinidad.citas.dto.*;
import com.trinidad.citas.model.HistoriaClinica;
import com.trinidad.citas.model.EstadoCita;
import com.trinidad.citas.repository.CitaRepository;
import com.trinidad.citas.repository.PacienteRepository;
import com.trinidad.citas.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Profile({"web", "default"})
@Controller
@RequestMapping("/historia-clinica")
@RequiredArgsConstructor
public class HistoriaClinicaWebController {

    private final HistoriaClinicaService historiaClinicaService;
    private final AntecedenteService antecedenteService;
    private final MedicacionActualService medicacionActualService;
    private final AtencionService atencionService;
    private final TriajeService triajeService;
    private final OrdenExamenService ordenExamenService;
    private final RecetaService recetaService;
    private final CitaRepository citaRepository;
    private final PacienteRepository pacienteRepository;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("historias", historiaClinicaService.listarEntidadesConRelaciones());
        return "historia-clinica/lista";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        var historia = historiaClinicaService.obtenerEntidad(id);
        var atenciones = atencionService.listarPorHistoria(id);

        Map<Long, TriajeDTO> triajesPorCita = new HashMap<>();
        Map<Long, List<OrdenExamenDTO>> ordenesPorAtencion = new HashMap<>();
        Map<Long, List<RecetaDTO>> recetasPorAtencion = new HashMap<>();

        for (AtencionDTO a : atenciones) {
            try {
                ordenesPorAtencion.put(a.getIdAtencion(), ordenExamenService.listarPorAtencion(a.getIdAtencion()));
            } catch (Exception e) {
                ordenesPorAtencion.put(a.getIdAtencion(), List.of());
            }
            try {
                recetasPorAtencion.put(a.getIdAtencion(), recetaService.listarPorAtencion(a.getIdAtencion()));
            } catch (Exception e) {
                recetasPorAtencion.put(a.getIdAtencion(), List.of());
            }
        }

        List<Map<String, Object>> timeline = new ArrayList<>();
        Long idPaciente = historia.getPaciente().getIdPaciente();
        var citas = citaRepository.findByPaciente_IdPacienteOrderByFechaCitaDescHoraInicioDesc(idPaciente);
        for (var c : citas) {
            timeline.add(evento("CITA", c.getFechaCita().atStartOfDay(), "Cita " + c.getEstado(),
                    c.getFechaCita() + " " + c.getHoraInicio() + " - " + c.getEspecialidad().getNombre(),
                    "bi-calendar-event", "var(--accent)"));
            try {
                var t = triajeService.obtenerPorCita(c.getIdCita());
                if (t != null) {
                    triajesPorCita.put(c.getIdCita(), t);
                    timeline.add(evento("TRIAJE", t.getFechaTriaje(), "Triaje",
                            "PA: " + t.getPresionArterial() + " | FC: " + t.getFrecuenciaCardiaca() + " | Temp: " + t.getTemperatura(),
                            "bi-activity", "var(--warning)"));
                }
            } catch (Exception ignored) {}
        }
        for (AtencionDTO a : atenciones) {
            timeline.add(evento("ATENCION", a.getFechaAtencion(), "Atención Médica",
                    a.getDiagnosticoDesc() != null ? a.getDiagnosticoDesc() : "Sin diagnóstico",
                    "bi-clipboard2-pulse", "var(--success)"));
            var ords = ordenesPorAtencion.getOrDefault(a.getIdAtencion(), List.of());
            for (OrdenExamenDTO o : ords) {
                timeline.add(evento("ORDEN", o.getFechaSolicitud(), "Orden: " + o.getTipoExamen(),
                        o.getNombreExamen(), "bi-clipboard-check", "var(--accent-green)"));
            }
            var recs = recetasPorAtencion.getOrDefault(a.getIdAtencion(), List.of());
            for (RecetaDTO r : recs) {
                timeline.add(evento("RECETA", r.getFechaEmision(), "Receta #" + r.getNroReceta(),
                        r.getObservaciones() != null ? r.getObservaciones() : "",
                        "bi-file-earmark-text", "#8b5cf6"));
            }
        }
        timeline.sort((e1, e2) -> ((Comparable) e2.get("fecha")).compareTo(e1.get("fecha")));

        model.addAttribute("historia", historia);
        model.addAttribute("antecedentes", antecedenteService.listarPorHistoria(id));
        model.addAttribute("medicacionActiva", medicacionActualService.listarActivasPorHistoria(id));
        model.addAttribute("atenciones", atenciones);
        model.addAttribute("triajesPorCita", triajesPorCita);
        model.addAttribute("ordenesPorAtencion", ordenesPorAtencion);
        model.addAttribute("recetasPorAtencion", recetasPorAtencion);
        model.addAttribute("timeline", timeline);
        return "historia-clinica/detalle";
    }

    private Map<String, Object> evento(String tipo, Object fecha, String titulo, String desc, String icono, String color) {
        Map<String, Object> e = new LinkedHashMap<>();
        e.put("tipo", tipo);
        e.put("fecha", fecha);
        e.put("titulo", titulo);
        e.put("descripcion", desc);
        e.put("icono", icono);
        e.put("color", color);
        return e;
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("historia", new HistoriaClinica());
        model.addAttribute("pacientes", pacienteRepository.findAll());
        return "historia-clinica/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        HistoriaClinica h = historiaClinicaService.obtenerEntidad(id);
        model.addAttribute("historia", h);
        model.addAttribute("pacientes", pacienteRepository.findAll());
        return "historia-clinica/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute HistoriaClinica historia,
                          @RequestParam Long pacienteId,
                          BindingResult result,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("pacientes", pacienteRepository.findAll());
            return "historia-clinica/form";
        }
        try {
            if (historia.getIdHistoria() == null) {
                HistoriaClinicaDTO dto = new HistoriaClinicaDTO();
                dto.setIdPaciente(pacienteId);
                dto.setNroHistoria(historia.getNroHistoria());
                dto.setObservaciones(historia.getObservaciones());
                dto.setActivo(historia.getActivo() != null ? historia.getActivo() : 1);
                historiaClinicaService.crear(dto);
                redirectAttributes.addFlashAttribute("ok", "Historia clínica creada correctamente.");
            } else {
                HistoriaClinicaDTO dto = new HistoriaClinicaDTO();
                dto.setObservaciones(historia.getObservaciones());
                dto.setActivo(historia.getActivo());
                historiaClinicaService.actualizar(historia.getIdHistoria(), dto);
                redirectAttributes.addFlashAttribute("ok", "Historia clínica actualizada correctamente.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
        }
        return "redirect:/historia-clinica";
    }

    @PostMapping("/toggle/{id}")
    public String toggle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            var dto = historiaClinicaService.obtenerPorId(id);
            dto.setActivo(dto.getActivo() == 1 ? 0 : 1);
            historiaClinicaService.actualizar(id, dto);
            redirectAttributes.addFlashAttribute("ok",
                "Historia clínica " + (dto.getActivo() == 1 ? "activada" : "desactivada") + " correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
        }
        return "redirect:/historia-clinica";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            historiaClinicaService.eliminar(id);
            redirectAttributes.addFlashAttribute("ok", "Historia clínica eliminada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/historia-clinica";
    }
}
