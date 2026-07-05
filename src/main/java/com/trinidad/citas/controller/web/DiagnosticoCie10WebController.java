package com.trinidad.citas.controller.web;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.dto.DiagnosticoCie10DTO;
import com.trinidad.citas.service.DiagnosticoCie10Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Profile({"web", "default"})
@Controller
@RequestMapping("/diagnosticos")
@RequiredArgsConstructor
public class DiagnosticoCie10WebController {

    private final DiagnosticoCie10Service diagnosticoService;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("diagnosticos", diagnosticoService.listarEntidades());
        model.addAttribute("titulo", "Diagnósticos CIE-10");
        return "diagnosticos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("diagnostico", new DiagnosticoCie10DTO());
        return "diagnosticos/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable String id, Model model) {
        model.addAttribute("diagnostico", diagnosticoService.obtenerEntidadPorCodigo(id));
        return "diagnosticos/form";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable String id, Model model) {
        model.addAttribute("diagnostico", diagnosticoService.obtenerEntidadPorCodigo(id));
        return "diagnosticos/detalle";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute DiagnosticoCie10DTO dto, RedirectAttributes redirectAttributes) {
        if (dto.getCodigo() != null && !dto.getCodigo().isBlank()) {
            try {
                diagnosticoService.actualizar(dto.getCodigo(), dto);
            } catch (Exception ignored) {
                diagnosticoService.crear(dto);
            }
        } else {
            diagnosticoService.crear(dto);
        }
        redirectAttributes.addFlashAttribute("ok", "Diagnóstico guardado correctamente.");
        return "redirect:/diagnosticos";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, RedirectAttributes redirectAttributes) {
        diagnosticoService.eliminar(id);
        redirectAttributes.addFlashAttribute("ok", "Diagnóstico eliminado correctamente.");
        return "redirect:/diagnosticos";
    }
}
