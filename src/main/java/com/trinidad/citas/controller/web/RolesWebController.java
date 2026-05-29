package com.trinidad.citas.controller.web;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.model.Rol;
import com.trinidad.citas.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Profile({"web", "default"})
@Controller
@RequestMapping("/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class RolesWebController {

    private final RolRepository rolRepository;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("roles", rolRepository.findAll());
        return "roles/lista";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        rolRepository.findById(id).ifPresent(r -> model.addAttribute("rol", r));
        return "roles/detalle";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("rol", new Rol());
        return "roles/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        rolRepository.findById(id).ifPresent(r -> model.addAttribute("rol", r));
        return "roles/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Rol rol, RedirectAttributes redirectAttributes) {
        rolRepository.save(rol);
        redirectAttributes.addFlashAttribute("ok", "Rol guardado correctamente.");
        return "redirect:/roles";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        rolRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("ok", "Rol eliminado correctamente.");
        return "redirect:/roles";
    }
}
