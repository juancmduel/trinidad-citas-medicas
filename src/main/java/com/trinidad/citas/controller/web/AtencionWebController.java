package com.trinidad.citas.controller.web;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trinidad.citas.dto.AtencionDTO;
import com.trinidad.citas.dto.OrdenExamenDTO;
import com.trinidad.citas.dto.RecetaDTO;
import com.trinidad.citas.model.Atencion;
import com.trinidad.citas.model.EstadoCita;
import com.trinidad.citas.service.AtencionService;
import com.trinidad.citas.service.CitaService;
import com.trinidad.citas.service.HistoriaClinicaService;
import com.trinidad.citas.service.OrdenExamenService;
import com.trinidad.citas.service.RecetaService;
import com.trinidad.citas.service.TriajeService;

import lombok.RequiredArgsConstructor;

@Profile({"web", "default"})
@Controller
@RequestMapping("/atenciones")
@RequiredArgsConstructor
public class AtencionWebController {

    private final AtencionService atencionService;
    private final CitaService citaService;
    private final HistoriaClinicaService historiaClinicaService;
    private final OrdenExamenService ordenExamenService;
    private final RecetaService recetaService;
    private final TriajeService triajeService;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("atenciones", atencionService.listarEntidadesConRelaciones());
        model.addAttribute("titulo", "Atenciones M&eacute;dicas");
        return "atenciones/lista";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        var atencion = atencionService.obtenerEntidadConRelaciones(id);
        model.addAttribute("atencion", atencion);

        com.trinidad.citas.dto.TriajeDTO triaje = null;
        if (atencion.getCita() != null) {
            try {
                triaje = triajeService.obtenerPorCita(atencion.getCita().getIdCita());
            } catch (Exception e) {
                // sin triaje
            }
        }
        model.addAttribute("triaje", triaje);

        model.addAttribute("titulo", "Detalle Atenci&oacute;n");
        return "atenciones/detalle";
    }

    @GetMapping("/cita/{citaId}/nuevo")
    public String nueva(@PathVariable Long citaId, Model model) {
        model.addAttribute("cita", citaService.obtenerEntidad(citaId));
        model.addAttribute("citas", citaService.listarEntidadesPorEstado(EstadoCita.EN_ATENCION));
        model.addAttribute("titulo", "Nueva Atenci&oacute;n");
        return "atenciones/form";
    }

    @GetMapping("/nuevo")
    public String nuevoGeneral(Model model) {
        model.addAttribute("citas", citaService.listarEntidadesPorEstado(EstadoCita.EN_ATENCION));
        model.addAttribute("titulo", "Nueva Atenci&oacute;n M&eacute;dica");
        return "atenciones/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Atencion atencion,
                          @RequestParam Long citaId,
                          @RequestParam(required = false) Long historiaId,
                          @RequestParam(required = false) Long medicoId,
                          @RequestParam(required = false) List<String> ordenesTipo,
                          @RequestParam(required = false) List<String> ordenesNombre,
                          @RequestParam(required = false) List<String> ordenesIndicaciones,
                          @RequestParam(required = false) List<String> recetasNro,
                          @RequestParam(required = false) List<String> recetasObservaciones,
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
            dto.setDiagnosticoCie10Codigo(atencion.getDiagnosticoCie10() != null ? atencion.getDiagnosticoCie10().getCodigo() : null);

            AtencionDTO saved = atencionService.crear(dto);

            if (ordenesTipo != null) {
                for (int i = 0; i < ordenesTipo.size(); i++) {
                    String tipo = ordenesTipo.get(i);
                    String nombre = ordenesNombre != null && i < ordenesNombre.size() ? ordenesNombre.get(i) : null;
                    if (tipo == null || tipo.isBlank()) continue;
                    OrdenExamenDTO oDto = new OrdenExamenDTO();
                    oDto.setIdAtencion(saved.getIdAtencion());
                    oDto.setTipoExamen(tipo);
                    oDto.setNombreExamen(nombre != null ? nombre : tipo);
                    oDto.setIndicaciones(ordenesIndicaciones != null && i < ordenesIndicaciones.size() ? ordenesIndicaciones.get(i) : null);
                    ordenExamenService.crear(oDto);
                }
            }

            if (recetasNro != null) {
                for (int i = 0; i < recetasNro.size(); i++) {
                    String nro = recetasNro.get(i);
                    if (nro == null || nro.isBlank()) continue;
                    RecetaDTO rDto = new RecetaDTO();
                    rDto.setIdAtencion(saved.getIdAtencion());
                    rDto.setNroReceta(nro);
                    rDto.setObservaciones(recetasObservaciones != null && i < recetasObservaciones.size() ? recetasObservaciones.get(i) : null);
                    recetaService.crear(rDto);
                }
            }

            redirectAttributes.addFlashAttribute("ok", "Atenci&oacute;n m&eacute;dica guardada correctamente.");
            return "redirect:/atenciones";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/atenciones/nuevo";
        }
    }
}
