package com.trinidad.citas.controller.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trinidad.citas.dto.AtencionDTO;
import com.trinidad.citas.dto.HistoriaClinicaDTO;
import com.trinidad.citas.dto.OrdenExamenDTO;
import com.trinidad.citas.dto.RecetaDTO;
import com.trinidad.citas.dto.TriajeDTO;
import com.trinidad.citas.model.HistoriaClinica;
import com.trinidad.citas.repository.CitaRepository;
import com.trinidad.citas.repository.PacienteRepository;
import com.trinidad.citas.service.AntecedenteService;
import com.trinidad.citas.service.AtencionService;
import com.trinidad.citas.service.HistoriaClinicaService;
import com.trinidad.citas.service.MedicacionActualService;
import com.trinidad.citas.service.OrdenExamenService;
import com.trinidad.citas.service.RecetaService;
import com.trinidad.citas.service.TriajeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Profile({"web", "default"})
@Controller
@RequestMapping("/historia-clinica")
@RequiredArgsConstructor
public class HistoriaClinicaWebController {

    private static final Logger log = LoggerFactory.getLogger(HistoriaClinicaWebController.class);

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
    @SuppressWarnings({"rawtypes", "unchecked"})
    public String detalle(@PathVariable Long id, Model model) {
        try {
            var historia = historiaClinicaService.obtenerEntidad(id);
            var atenciones = atencionService.listarPorHistoria(id);

            Map<Long, TriajeDTO> triajesPorCita = new HashMap<>();
            Map<Long, List<OrdenExamenDTO>> ordenesPorAtencion = new HashMap<>();
            Map<Long, List<RecetaDTO>> recetasPorAtencion = new HashMap<>();

            // Limitar a las últimas 50 atenciones para evitar sobrecarga
            int maxAtenciones = Math.min(atenciones.size(), 50);
            for (int i = 0; i < maxAtenciones; i++) {
                AtencionDTO a = atenciones.get(i);
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

            // Limitar a últimas 50 citas para la línea de tiempo
            var citas = citaRepository.findTop50ByPaciente_IdPacienteOrderByFechaCitaDescHoraInicioDesc(idPaciente);
            for (var c : citas) {
                try {
                    String especialidad = c.getEspecialidad() != null ? c.getEspecialidad().getNombre() : "Sin especialidad";
                    timeline.add(evento("CITA", c.getFechaCita() != null ? c.getFechaCita().atStartOfDay() : LocalDateTime.now(),
                            "Cita " + (c.getEstado() != null ? c.getEstado() : ""),
                            (c.getFechaCita() != null ? c.getFechaCita().toString() : "") + " "
                            + (c.getHoraInicio() != null ? c.getHoraInicio().toString() : "") + " - " + especialidad,
                            "bi-calendar-event", "var(--accent)"));
                } catch (Exception ec) {
                    log.warn("Error al procesar cita {}: {}", c.getIdCita(), ec.getMessage());
                }
                try {
                    var t = triajeService.obtenerPorCita(c.getIdCita());
                    if (t != null) {
                        triajesPorCita.put(c.getIdCita(), t);
                        timeline.add(evento("TRIAJE", t.getFechaTriaje() != null ? t.getFechaTriaje() : LocalDateTime.now(),
                                "Triaje",
                                "PA: " + (t.getPresionArterial() != null ? t.getPresionArterial() : "-")
                                + " | FC: " + (t.getFrecuenciaCardiaca() != null ? t.getFrecuenciaCardiaca() : "-")
                                + " | Temp: " + (t.getTemperatura() != null ? t.getTemperatura() : "-"),
                                "bi-activity", "var(--warning)"));
                    }
                } catch (Exception e) {
                    log.warn("No se pudo obtener triaje para cita {}: {}", c.getIdCita(), e.getMessage());
                }
            }
            for (AtencionDTO a : atenciones) {
                try {
                    timeline.add(evento("ATENCION", a.getFechaAtencion() != null ? a.getFechaAtencion() : LocalDateTime.now(),
                            "Atención Médica",
                            a.getDiagnosticoDesc() != null ? a.getDiagnosticoDesc() : "Sin diagnóstico",
                            "bi-clipboard2-pulse", "var(--success)"));
                    var ords = ordenesPorAtencion.getOrDefault(a.getIdAtencion(), List.of());
                    for (OrdenExamenDTO o : ords) {
                        timeline.add(evento("ORDEN", o.getFechaSolicitud() != null ? o.getFechaSolicitud() : LocalDateTime.now(),
                                "Orden: " + (o.getTipoExamen() != null ? o.getTipoExamen() : ""),
                                o.getNombreExamen() != null ? o.getNombreExamen() : "",
                                "bi-clipboard-check", "var(--accent-green)"));
                    }
                    var recs = recetasPorAtencion.getOrDefault(a.getIdAtencion(), List.of());
                    for (RecetaDTO r : recs) {
                        timeline.add(evento("RECETA", r.getFechaEmision() != null ? r.getFechaEmision() : LocalDateTime.now(),
                                "Receta #" + (r.getNroReceta() != null ? r.getNroReceta() : ""),
                                r.getObservaciones() != null ? r.getObservaciones() : "",
                                "bi-file-earmark-text", "#8b5cf6"));
                    }
                } catch (Exception ea) {
                    log.warn("Error al procesar atención {}: {}", a.getIdAtencion(), ea.getMessage());
                }
            }
            timeline.sort((e1, e2) -> {
                try { return ((Comparable) e2.get("fecha")).compareTo(e1.get("fecha")); }
                catch (Exception es) { return 0; }
            });

            // Limitar timeline a 100 eventos
            if (timeline.size() > 100) {
                timeline = timeline.subList(0, 100);
            }

            model.addAttribute("historia", historia);
            model.addAttribute("antecedentes", antecedenteService.listarPorHistoria(id));
            model.addAttribute("medicacionActiva", medicacionActualService.listarActivasPorHistoria(id));
            // Filtrar nulos y limitar a 50 atenciones
            List<AtencionDTO> atencionesFiltradas = atenciones.stream()
                .filter(a -> a != null)
                .limit(50)
                .collect(java.util.stream.Collectors.toList());
            model.addAttribute("atenciones", atencionesFiltradas);
            model.addAttribute("triajesPorCita", triajesPorCita);
            model.addAttribute("ordenesPorAtencion", ordenesPorAtencion);
            model.addAttribute("recetasPorAtencion", recetasPorAtencion);
            model.addAttribute("timeline", timeline);
            return "historia-clinica/detalle";

        } catch (Exception e) {
            log.error("Error al cargar detalle de historia clínica {}: {}", id, e.getMessage(), e);
            model.addAttribute("error", "Error al cargar la historia clínica: " + e.getMessage());
            model.addAttribute("historias", historiaClinicaService.listarEntidadesConRelaciones());
            return "historia-clinica/lista";
        }
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

    @GetMapping("/paciente/{idPaciente}")
    public String redirigirPorPaciente(@PathVariable Long idPaciente) {
        var historiaOpt = historiaClinicaService.buscarIdHistoriaPorPaciente(idPaciente);
        if (historiaOpt.isPresent()) {
            return "redirect:/historia-clinica/" + historiaOpt.get();
        }
        return "redirect:/historia-clinica/nueva?pacienteId=" + idPaciente;
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
