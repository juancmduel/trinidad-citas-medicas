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

@Profile({"web", "default"})
@Controller
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @GetMapping("/olvide-password")
    public String olvidePasswordForm(Model model) {
        model.addAttribute("titulo", "Recuperar Contrasena");
        return "auth/olvide-password";
    }

    @PostMapping("/olvide-password")
    public String solicitarRestablecimiento(@RequestParam String email,
                                             HttpServletRequest request,
                                             RedirectAttributes ra) {
        String baseUrl = request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort() + request.getContextPath();
        try {
            passwordResetService.solicitarRestablecimiento(email, baseUrl);
        } catch (BusinessException e) {
            // No revelamos si el email existe o no por seguridad
        }
        ra.addFlashAttribute("ok", "Si el email esta registrado, recibiras un enlace de recuperacion.");
        return "redirect:/olvide-password";
    }

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

    @Data
    public static class ResetPasswordRequest {
        @NotBlank
        private String token;
        @NotBlank @Size(min = 8)
        private String password;
    }
}
