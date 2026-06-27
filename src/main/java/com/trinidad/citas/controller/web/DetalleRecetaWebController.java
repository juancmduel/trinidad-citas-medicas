package com.trinidad.citas.controller.web;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.dto.DetalleRecetaDTO;
import com.trinidad.citas.dto.RecetaDTO;
import com.trinidad.citas.service.DetalleRecetaService;
import com.trinidad.citas.service.RecetaService;
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

    private final DetalleRecetaService detalleRecetaService;
    private final RecetaService recetaService;

    @GetMapping
    public String lista(@PathVariable Long recetaId, Model model) {
        RecetaDTO receta = recetaService.obtenerPorId(recetaId);
        model.addAttribute("receta", receta);
        model.addAttribute("detalles", detalleRecetaService.listarPorReceta(recetaId));
        return "recetas/detalles/lista";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long recetaId, @PathVariable Long id, Model model) {
        model.addAttribute("detalle", detalleRecetaService.obtenerPorId(id));
        model.addAttribute("receta", recetaService.obtenerPorId(recetaId));
        return "recetas/detalles/detalle";
    }

    @GetMapping("/nuevo")
    public String nuevo(@PathVariable Long recetaId, Model model) {
        model.addAttribute("detalle", new DetalleRecetaDTO());
        model.addAttribute("receta", recetaService.obtenerPorId(recetaId));
        return "recetas/detalles/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long recetaId, @PathVariable Long id, Model model) {
        model.addAttribute("detalle", detalleRecetaService.obtenerPorId(id));
        model.addAttribute("receta", recetaService.obtenerPorId(recetaId));
        return "recetas/detalles/form";
    }

    @PostMapping("/guardar")
    public String guardar(@PathVariable Long recetaId,
                          @ModelAttribute DetalleRecetaDTO dto,
                          RedirectAttributes redirectAttributes) {
        dto.setIdReceta(recetaId);
        detalleRecetaService.crear(dto);
        redirectAttributes.addFlashAttribute("ok", "Medicamento guardado correctamente.");
        return "redirect:/recetas/" + recetaId;
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long recetaId, @PathVariable Long id,
                           RedirectAttributes redirectAttributes) {
        detalleRecetaService.eliminar(id);
        redirectAttributes.addFlashAttribute("ok", "Medicamento eliminado correctamente.");
        return "redirect:/recetas/" + recetaId;
    }
}
