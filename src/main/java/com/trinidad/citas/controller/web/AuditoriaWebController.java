package com.trinidad.citas.controller.web;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.repository.AuditoriaLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Profile({"web", "default"})
@Controller
@RequestMapping("/auditoria")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AuditoriaWebController {

    private final AuditoriaLogRepository auditoriaLogRepository;

    @GetMapping
    public String lista(@RequestParam(required = false) String usuario,
                       @RequestParam(required = false) String accion,
                       Model model) {
        model.addAttribute("logs", auditoriaLogRepository.findAll());
        return "auditoria/lista";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("log", auditoriaLogRepository.findById(id));
        return "auditoria/detalle";
    }
}
