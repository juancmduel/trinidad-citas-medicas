package com.trinidad.citas.controller.web;

import com.trinidad.citas.dto.RolDTO;
import com.trinidad.citas.service.RolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Profile({"web", "default"})
@Controller
@RequestMapping("/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class RolesWebController {

    private final RolService rolService;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("roles", rolService.listarTodos());
        return "roles/lista";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("rol", rolService.obtenerPorId(id));
        return "roles/detalle";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("rol", new RolDTO());
        return "roles/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("rol", rolService.obtenerPorId(id));
        return "roles/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("rol") RolDTO dto,
                          BindingResult result,
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "roles/form";
        }
        try {
            if (dto.getIdRol() == null) {
                rolService.crear(dto);
                redirectAttributes.addFlashAttribute("ok", "Rol creado correctamente.");
            } else {
                rolService.actualizar(dto.getIdRol(), dto);
                redirectAttributes.addFlashAttribute("ok", "Rol actualizado correctamente.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
        }
        return "redirect:/roles";
    }

    @PostMapping("/toggle/{id}")
    public String toggle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            var dto = rolService.obtenerPorId(id);
            dto.setActivo(dto.getActivo() == 1 ? 0 : 1);
            rolService.actualizar(id, dto);
            redirectAttributes.addFlashAttribute("ok",
                "Rol " + (dto.getActivo() == 1 ? "activado" : "desactivado") + " correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
        }
        return "redirect:/roles";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            rolService.eliminar(id);
            redirectAttributes.addFlashAttribute("ok", "Rol eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/roles";
    }
}
