package com.trinidad.citas.controller.web;

import com.trinidad.citas.dto.OrdenExamenDTO;
import com.trinidad.citas.service.AtencionService;
import com.trinidad.citas.service.OrdenExamenService;
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
@RequestMapping("/ordenes-examen")
@RequiredArgsConstructor
public class OrdenExamenWebController {

    private final OrdenExamenService ordenExamenService;
    private final AtencionService atencionService;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("ordenes", ordenExamenService.listarEntidadesConRelaciones());
        return "ordenes-examen/lista";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("orden", ordenExamenService.obtenerEntidadConRelaciones(id));
        return "ordenes-examen/detalle";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("orden", new OrdenExamenDTO());
        model.addAttribute("atenciones", atencionService.listarEntidadesConRelaciones());
        return "ordenes-examen/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        var dto = ordenExamenService.obtenerPorId(id);
        model.addAttribute("orden", dto);
        model.addAttribute("atenciones", atencionService.listarEntidadesConRelaciones());
        return "ordenes-examen/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("orden") OrdenExamenDTO dto,
                          BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "ordenes-examen/form";
        }
        try {
            if (dto.getIdOrden() == null) {
                ordenExamenService.crear(dto);
                redirectAttributes.addFlashAttribute("ok", "Orden de examen creada correctamente.");
            } else {
                ordenExamenService.actualizar(dto.getIdOrden(), dto);
                redirectAttributes.addFlashAttribute("ok", "Orden de examen actualizada correctamente.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
        }
        return "redirect:/ordenes-examen";
    }

    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable Long id, @RequestParam String estado,
                                RedirectAttributes redirectAttributes) {
        try {
            ordenExamenService.cambiarEstado(id, estado);
            redirectAttributes.addFlashAttribute("ok", "Estado actualizado a " + estado + ".");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
        }
        return "redirect:/ordenes-examen";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            ordenExamenService.eliminar(id);
            redirectAttributes.addFlashAttribute("ok", "Orden eliminada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/ordenes-examen";
    }
}
