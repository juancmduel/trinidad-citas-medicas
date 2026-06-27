package com.trinidad.citas.controller.web;

import com.trinidad.citas.dto.EspecialidadDTO;
import com.trinidad.citas.service.EspecialidadService;
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
@RequestMapping("/especialidades")
@RequiredArgsConstructor
public class EspecialidadWebController {

    private final EspecialidadService especialidadService;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("especialidades", especialidadService.listarTodas());
        return "especialidades/lista";
    }

    @GetMapping("/nuevo")
    public String nueva(Model model) {
        model.addAttribute("especialidad", new EspecialidadDTO());
        return "especialidades/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("especialidad", especialidadService.obtener(id));
        return "especialidades/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("especialidad") EspecialidadDTO dto,
                          BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "especialidades/form";
        }
        try {
            if (dto.getIdEspecialidad() == null) {
                especialidadService.crear(dto);
                redirectAttributes.addFlashAttribute("ok", "Especialidad creada correctamente.");
            } else {
                especialidadService.actualizar(dto.getIdEspecialidad(), dto);
                redirectAttributes.addFlashAttribute("ok", "Especialidad actualizada correctamente.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
            return "redirect:/especialidades";
        }
        return "redirect:/especialidades";
    }

    @PostMapping("/toggle/{id}")
    public String toggle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            var dto = especialidadService.obtener(id);
            int nuevoEstado = dto.getActivo() == 1 ? 0 : 1;
            dto.setActivo(nuevoEstado);
            especialidadService.actualizar(id, dto);
            redirectAttributes.addFlashAttribute("ok",
                "Especialidad " + (nuevoEstado == 1 ? "activada" : "desactivada") + " correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
        }
        return "redirect:/especialidades";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            especialidadService.eliminar(id);
            redirectAttributes.addFlashAttribute("ok", "Especialidad desactivada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/especialidades";
    }
}
