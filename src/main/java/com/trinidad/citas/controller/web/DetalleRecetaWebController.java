package com.trinidad.citas.controller.web;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.model.DetalleReceta;
import com.trinidad.citas.repository.DetalleRecetaRepository;
import com.trinidad.citas.repository.RecetaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Profile({"web", "default"})
@Controller
@RequestMapping("/recetas/{recetaId}/detalles")
@RequiredArgsConstructor
public class DetalleRecetaWebController {

    private final DetalleRecetaRepository detalleRecetaRepository;
    private final RecetaRepository recetaRepository;

    @GetMapping
    public String lista(@PathVariable Long recetaId, Model model) {
        recetaRepository.findById(recetaId).ifPresent(r -> {
            model.addAttribute("receta", r);
            model.addAttribute("detalles", detalleRecetaRepository.findByReceta_IdReceta(recetaId));
        });
        return "recetas/detalles/lista";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long recetaId, @PathVariable Long id, Model model) {
        detalleRecetaRepository.findById(id).ifPresent(d -> model.addAttribute("detalle", d));
        recetaRepository.findById(recetaId).ifPresent(r -> model.addAttribute("receta", r));
        return "recetas/detalles/detalle";
    }

    @GetMapping("/nuevo")
    public String nuevo(@PathVariable Long recetaId, Model model) {
        model.addAttribute("detalle", new DetalleReceta());
        recetaRepository.findById(recetaId).ifPresent(r -> model.addAttribute("receta", r));
        return "recetas/detalles/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long recetaId, @PathVariable Long id, Model model) {
        detalleRecetaRepository.findById(id).ifPresent(d -> model.addAttribute("detalle", d));
        recetaRepository.findById(recetaId).ifPresent(r -> model.addAttribute("receta", r));
        return "recetas/detalles/form";
    }

    @PostMapping("/guardar")
    public String guardar(@PathVariable Long recetaId,
                          @ModelAttribute DetalleReceta detalle,
                          RedirectAttributes redirectAttributes) {
        recetaRepository.findById(recetaId).ifPresent(detalle::setReceta);
        detalleRecetaRepository.save(detalle);
        redirectAttributes.addFlashAttribute("ok", "Medicamento guardado correctamente.");
        return "redirect:/trinidad/recetas/" + recetaId;
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long recetaId, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        detalleRecetaRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("ok", "Medicamento eliminado correctamente.");
        return "redirect:/trinidad/recetas/" + recetaId;
    }
}
