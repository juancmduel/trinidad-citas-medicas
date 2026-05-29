package com.trinidad.citas.controller.web;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.model.DiagnosticoCie10;
import com.trinidad.citas.repository.DiagnosticoCie10Repository;
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

    private final DiagnosticoCie10Repository diagnosticoRepository;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("diagnosticos", diagnosticoRepository.findAll());
        model.addAttribute("titulo", "Diagnósticos CIE-10");
        return "diagnosticos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("diagnostico", new DiagnosticoCie10());
        return "diagnosticos/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable String id, Model model) {
        diagnosticoRepository.findById(id).ifPresent(d -> model.addAttribute("diagnostico", d));
        return "diagnosticos/form";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable String id, Model model) {
        diagnosticoRepository.findById(id).ifPresent(d -> model.addAttribute("diagnostico", d));
        return "diagnosticos/detalle";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute DiagnosticoCie10 diagnostico, RedirectAttributes redirectAttributes) {
        diagnosticoRepository.save(diagnostico);
        redirectAttributes.addFlashAttribute("ok", "Diagnóstico guardado correctamente.");
        return "redirect:/diagnosticos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, RedirectAttributes redirectAttributes) {
        diagnosticoRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("ok", "Diagnóstico eliminado correctamente.");
        return "redirect:/diagnosticos";
    }
}
