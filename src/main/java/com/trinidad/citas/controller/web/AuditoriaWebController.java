package com.trinidad.citas.controller.web;

import com.trinidad.citas.service.AuditoriaLogService;
import com.trinidad.citas.service.IntentoLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Profile({"web", "default"})
@Controller
@RequestMapping("/auditoria")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class AuditoriaWebController {

    private final AuditoriaLogService auditoriaLogService;
    private final IntentoLoginService intentoLoginService;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("logs", auditoriaLogService.listarTodos());
        return "auditoria/lista";
    }

    @GetMapping("/intentos")
    public String intentos(Model model) {
        model.addAttribute("intentos", intentoLoginService.listarTodos());
        return "auditoria/intentos";
    }

    @GetMapping("/intentos/usuario")
    public String intentosPorUsuario(@RequestParam String username, Model model) {
        model.addAttribute("intentos", intentoLoginService.obtenerIntentosRecientes(username));
        model.addAttribute("username", username);
        return "auditoria/intentos";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        var opt = auditoriaLogService.obtenerPorId(id);
        opt.ifPresent(l -> model.addAttribute("log", l));
        return "auditoria/detalle";
    }
}
