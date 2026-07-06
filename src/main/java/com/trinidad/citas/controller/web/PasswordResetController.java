package com.trinidad.citas.controller.web;

import com.trinidad.citas.exception.BusinessException;
import com.trinidad.citas.service.PasswordResetService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para el flujo de "Olvidé mi contraseña".
 *
 * Pasos:
 *  1. GET /olvide-password → muestra el formulario para poner el email
 *  2. POST /olvide-password → envía el correo con el enlace de recuperación
 *  3. GET /reset-password/{token} → valida el token y muestra el formulario
 *  4. POST /reset-password → guarda la nueva contraseña
 *
 * Siempre mostramos el mismo mensaje ("Si el email está registrado...")
 * para no revelar qué correos existen en el sistema.
 */
@Profile({"web", "default"})
@Controller
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    /** Muestra el formulario para ingresar el correo */
    @GetMapping("/olvide-password")
    public String olvidePasswordForm(Model model) {
        model.addAttribute("titulo", "Recuperar Contrasena");
        return "auth/olvide-password";
    }

    /**
     * Recibe el correo y envía el enlace de recuperación.
     * No importa si el correo existe o no, siempre decimos lo mismo.
     */
    @PostMapping("/olvide-password")
    public String solicitarRestablecimiento(@RequestParam String email,
                                             HttpServletRequest request,
                                             RedirectAttributes ra) {
        // Construimos la URL base para el enlace de recuperación
        String baseUrl = request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort() + request.getContextPath();
        try {
            passwordResetService.solicitarRestablecimiento(email, baseUrl);
        } catch (BusinessException e) {
            // Calló silenciosamente. No revelamos si el correo existe o no.
        }
        ra.addFlashAttribute("ok", "Si el email esta registrado, recibiras un enlace de recuperacion.");
        return "redirect:/olvide-password";
    }

    /**
     * Valida el token y muestra el formulario para poner la nueva contraseña.
     * Si el token es inválido, expiró o ya se usó, muestra error.
     */
    @GetMapping("/reset-password/{token}")
    public String resetPasswordForm(@PathVariable String token, Model model) {
        try {
            passwordResetService.validarToken(token);
            model.addAttribute("token", token);
            model.addAttribute("titulo", "Restablecer Contrasena");
            return "auth/reset-password";
        } catch (BusinessException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("titulo", "Enlace invalido");
            return "auth/reset-password";
        }
    }

    /**
     * Guarda la nueva contraseña.
     * La contraseña debe tener al menos 8 caracteres.
     * Si todo sale bien, redirige al login para que el usuario entre con su nueva clave.
     */
    @PostMapping("/reset-password")
    public String restablecerPassword(@ModelAttribute ResetPasswordRequest dto,
                                       RedirectAttributes ra, Model model) {
        if (dto.getPassword() == null || dto.getPassword().length() < 8) {
            model.addAttribute("error", "La contrasena debe tener al menos 8 caracteres.");
            model.addAttribute("token", dto.getToken());
            model.addAttribute("titulo", "Restablecer Contrasena");
            return "auth/reset-password";
        }
        try {
            passwordResetService.restablecerPassword(dto.getToken(), dto.getPassword());
            ra.addFlashAttribute("ok", "Contrasena restablecida exitosamente. Ya puedes iniciar sesion.");
            return "redirect:/login";
        } catch (BusinessException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("token", dto.getToken());
            model.addAttribute("titulo", "Restablecer Contrasena");
            return "auth/reset-password";
        }
    }

    /** DTO interno para recibir el token + nueva contraseña del formulario */
    @Data
    public static class ResetPasswordRequest {
        @NotBlank
        private String token;
        @NotBlank @Size(min = 8)
        private String password;
    }
}
