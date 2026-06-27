package com.trinidad.citas.controller.web;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.dto.AtencionDTO;
import com.trinidad.citas.model.Atencion;
import com.trinidad.citas.model.EstadoCita;
import com.trinidad.citas.service.HistoriaClinicaService;
import com.trinidad.citas.service.AtencionService;
import com.trinidad.citas.service.CitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Profile({"web", "default"})
@Controller
@RequestMapping("/atenciones")
@RequiredArgsConstructor
public class AtencionWebController {

    private final AtencionService atencionService;
    private final CitaService citaService;
    private final HistoriaClinicaService historiaClinicaService;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("atenciones", atencionService.listarEntidadesConRelaciones());
        model.addAttribute("titulo", "Atenciones Médicas");
        return "atenciones/lista";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("atencion", atencionService.obtenerEntidadConRelaciones(id));
        model.addAttribute("titulo", "Detalle Atención");
        return "atenciones/detalle";
    }

    @GetMapping("/cita/{citaId}/nuevo")
    public String nueva(@PathVariable Long citaId, Model model) {
        model.addAttribute("cita", citaService.obtenerEntidad(citaId));
        model.addAttribute("citas", citaService.listarEntidadesPorEstado(EstadoCita.EN_ATENCION));
        model.addAttribute("titulo", "Nueva Atención");
        return "atenciones/form";
    }

    @GetMapping("/nuevo")
    public String nuevoGeneral(Model model) {
        model.addAttribute("citas", citaService.listarEntidadesPorEstado(EstadoCita.EN_ATENCION));
        model.addAttribute("titulo", "Nueva Atención Médica");
        return "atenciones/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Atencion atencion,
                          @RequestParam Long citaId,
                          @RequestParam(required = false) Long historiaId,
                          @RequestParam(required = false) Long medicoId,
                          RedirectAttributes redirectAttributes) {
        try {
            var cita = citaService.obtenerEntidad(citaId);
            Long hcId = historiaId;
            if (hcId == null && cita.getPaciente() != null) {
                hcId = historiaClinicaService.buscarIdHistoriaPorPaciente(cita.getPaciente().getIdPaciente())
                    .orElse(null);
            }
            Long medId = medicoId != null ? medicoId : cita.getMedico().getIdMedico();
            AtencionDTO dto = new AtencionDTO();
            dto.setIdCita(citaId);
            dto.setIdHistoria(hcId);
            dto.setIdMedico(medId);
            dto.setFechaAtencion(atencion.getFechaAtencion() != null ? atencion.getFechaAtencion() : LocalDateTime.now());
            dto.setMotivoConsulta(atencion.getMotivoConsulta());
            dto.setAnamnesis(atencion.getAnamnesis());
            dto.setExamenFisico(atencion.getExamenFisico());
            dto.setDiagnosticoDesc(atencion.getDiagnosticoDesc());
            dto.setTratamiento(atencion.getTratamiento());
            dto.setObservaciones(atencion.getObservaciones());
            dto.setPresionArterial(atencion.getPresionArterial());
            dto.setFrecuenciaCardiaca(atencion.getFrecuenciaCardiaca());
            dto.setTemperatura(atencion.getTemperatura());
            dto.setPesoKg(atencion.getPesoKg());
            dto.setTallaCm(atencion.getTallaCm());
            atencionService.crear(dto);
            redirectAttributes.addFlashAttribute("ok", "Atencion medica guardada correctamente.");
            return "redirect:/atenciones";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/atenciones/nuevo";
        }
    }
}
