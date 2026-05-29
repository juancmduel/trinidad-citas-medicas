package com.trinidad.citas.controller.web;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.dto.RecetaDTO;
import com.trinidad.citas.repository.RecetaRepository;
import com.trinidad.citas.repository.AtencionRepository;
import com.trinidad.citas.service.RecetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Profile({"web", "default"})
@Controller
@RequestMapping("/recetas")
@RequiredArgsConstructor
public class RecetaWebController {

    private final RecetaRepository recetaRepository;
    private final AtencionRepository atencionRepository;
    private final RecetaService recetaService;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("recetas", recetaRepository.findAllWithRelations());
        model.addAttribute("titulo", "Recetas Médicas");
        return "recetas/lista";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        recetaRepository.findByIdWithRelations(id).ifPresent(r -> model.addAttribute("receta", r));
        model.addAttribute("titulo", "Detalle de Receta");
        return "recetas/detalle";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("receta", new RecetaDTO());
        model.addAttribute("atenciones", atencionRepository.findAll());
        model.addAttribute("titulo", "Nueva Receta");
        return "recetas/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute RecetaDTO receta,
                          RedirectAttributes redirectAttributes) {
        recetaService.crear(receta);
        redirectAttributes.addFlashAttribute("ok", "Receta guardada correctamente.");
        return "redirect:/recetas";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        recetaService.eliminar(id);
        redirectAttributes.addFlashAttribute("ok", "Receta eliminada correctamente.");
        return "redirect:/recetas";
    }
}
