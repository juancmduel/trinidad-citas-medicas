package com.trinidad.citas.controller.web;

import com.trinidad.citas.dto.HistoriaClinicaDTO;
import com.trinidad.citas.model.HistoriaClinica;
import com.trinidad.citas.repository.PacienteRepository;
import com.trinidad.citas.service.HistoriaClinicaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Profile({"web", "default"})
@Controller
@RequestMapping("/historia-clinica")
@RequiredArgsConstructor
public class HistoriaClinicaWebController {

    private final HistoriaClinicaService historiaClinicaService;
    private final PacienteRepository pacienteRepository;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("historias", historiaClinicaService.listarEntidadesConRelaciones());
        return "historia-clinica/lista";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("historia", historiaClinicaService.obtenerEntidad(id));
        return "historia-clinica/detalle";
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
