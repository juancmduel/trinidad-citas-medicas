package com.trinidad.citas.controller.web;

import org.springframework.context.annotation.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Profile({"web", "default"})
@Controller
@RequestMapping("/configuracion")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class ConfiguracionWebController {

    @GetMapping
    public String index(Model model) {
        model.addAttribute("titulo", "Configuración del Sistema");
        return "configuracion/index";
    }

    @GetMapping("/general")
    public String general() {
        return "configuracion/general";
    }

    @GetMapping("/seguridad")
    public String seguridad() {
        return "configuracion/seguridad";
    }

    @GetMapping("/email")
    public String email() {
        return "configuracion/email";
    }
}
