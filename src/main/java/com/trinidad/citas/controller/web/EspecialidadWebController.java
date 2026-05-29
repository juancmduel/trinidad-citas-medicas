package com.trinidad.citas.controller.web;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.model.Especialidad;
import com.trinidad.citas.repository.EspecialidadRepository;
import com.trinidad.citas.service.EspecialidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Profile({"web", "default"})
@Controller
@RequestMapping("/especialidades")
@RequiredArgsConstructor
public class EspecialidadWebController {

    private final EspecialidadService especialidadService;
    private final EspecialidadRepository especialidadRepository;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("especialidades", especialidadService.listarActivas());
        return "especialidades/lista";
    }

    @GetMapping("/nuevo")
    public String nueva(Model model) {
        model.addAttribute("especialidad", new Especialidad());
        return "especialidades/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        especialidadRepository.findById(id).ifPresent(e -> model.addAttribute("especialidad", e));
        return "especialidades/form";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        especialidadRepository.findById(id).ifPresent(e -> model.addAttribute("especialidad", e));
        return "especialidades/detalle";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Especialidad especialidad, RedirectAttributes redirectAttributes) {
        especialidadRepository.save(especialidad);
        redirectAttributes.addFlashAttribute("ok", "Especialidad guardada correctamente.");
        return "redirect:/especialidades";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        especialidadRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("ok", "Especialidad eliminada correctamente.");
        return "redirect:/especialidades";
    }
}
