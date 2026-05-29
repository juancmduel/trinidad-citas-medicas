package com.trinidad.citas.controller.web;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.model.OrdenExamen;
import com.trinidad.citas.repository.AtencionRepository;
import com.trinidad.citas.repository.OrdenExamenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Profile({"web", "default"})
@Controller
@RequestMapping("/ordenes-examen")
@RequiredArgsConstructor
public class OrdenExamenWebController {

    private final OrdenExamenRepository ordenExamenRepository;
    private final AtencionRepository atencionRepository;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("ordenes", ordenExamenRepository.findAllWithRelations());
        return "ordenes-examen/lista";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        ordenExamenRepository.findByIdWithRelations(id).ifPresent(o -> model.addAttribute("orden", o));
        return "ordenes-examen/detalle";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("orden", new OrdenExamen());
        model.addAttribute("atenciones", atencionRepository.findAllWithRelations());
        return "ordenes-examen/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        ordenExamenRepository.findByIdWithRelations(id).ifPresent(o -> {
            model.addAttribute("orden", o);
            model.addAttribute("atenciones", atencionRepository.findAllWithRelations());
        });
        return "ordenes-examen/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute OrdenExamen orden,
                          @RequestParam Long atencionId,
                          RedirectAttributes redirectAttributes) {
        atencionRepository.findById(atencionId).ifPresent(orden::setAtencion);
        ordenExamenRepository.save(orden);
        redirectAttributes.addFlashAttribute("ok", "Orden de examen guardada correctamente.");
        return "redirect:/ordenes-examen";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        ordenExamenRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("ok", "Orden eliminada correctamente.");
        return "redirect:/ordenes-examen";
    }
}
