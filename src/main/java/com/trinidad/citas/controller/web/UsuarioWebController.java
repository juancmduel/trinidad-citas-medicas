package com.trinidad.citas.controller.web;

import com.trinidad.citas.model.Usuario;
import com.trinidad.citas.repository.RolRepository;
import com.trinidad.citas.service.UsuarioService;
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
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class UsuarioWebController {

    private final UsuarioService usuarioService;
    private final RolRepository rolRepository;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuarios/lista";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("usuario", usuarioService.obtenerPorId(id));
        return "usuarios/detalle";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", rolRepository.findAll());
        return "usuarios/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("usuario", usuarioService.obtenerEntidadPorId(id));
        model.addAttribute("roles", rolRepository.findAll());
        return "usuarios/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Usuario usuario,
                          BindingResult result,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("roles", rolRepository.findAll());
            return "usuarios/form";
        }
        try {
            com.trinidad.citas.dto.UsuarioDTO dto = new com.trinidad.citas.dto.UsuarioDTO();
            dto.setUsername(usuario.getUsername());
            dto.setPassword(usuario.getPasswordHash() != null && !usuario.getPasswordHash().isEmpty()
                    ? usuario.getPasswordHash() : "Trinidad2026");
            dto.setEmail(usuario.getEmail());
            dto.setActivo(usuario.getActivo() != null ? usuario.getActivo() : 1);
            if (usuario.getIdUsuario() == null) {
                usuarioService.crear(dto);
                redirectAttributes.addFlashAttribute("ok", "Usuario creado correctamente.");
            } else {
                var existente = usuarioService.obtenerPorId(usuario.getIdUsuario());
                dto.setRolesIds(existente.getRolesIds());
                usuarioService.actualizar(usuario.getIdUsuario(), dto);
                redirectAttributes.addFlashAttribute("ok", "Usuario actualizado correctamente.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
        }
        return "redirect:/usuarios";
    }

    @PostMapping("/toggle/{id}")
    public String toggle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            var dto = usuarioService.obtenerPorId(id);
            dto.setActivo(dto.getActivo() == 1 ? 0 : 1);
            usuarioService.actualizar(id, dto);
            redirectAttributes.addFlashAttribute("ok",
                "Usuario " + (dto.getActivo() == 1 ? "activado" : "desactivado") + " correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
        }
        return "redirect:/usuarios";
    }

    @PostMapping("/desbloquear/{id}")
    public String desbloquear(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.desbloquear(id);
            redirectAttributes.addFlashAttribute("ok", "Usuario desbloqueado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al desbloquear: " + e.getMessage());
        }
        return "redirect:/usuarios";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.eliminar(id);
            redirectAttributes.addFlashAttribute("ok", "Usuario eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/usuarios";
    }
}
