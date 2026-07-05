package com.trinidad.citas.config;

import com.trinidad.citas.model.Usuario;
import com.trinidad.citas.repository.UsuarioRepository;
import com.trinidad.citas.service.IntentoLoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    private final UsuarioRepository usuarioRepository;
    private final IntentoLoginService intentoLoginService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        String username = authentication.getName();

        Usuario usuario = usuarioRepository.findByUsername(username).orElse(null);
        if (usuario != null) {
            usuario.setFechaUltLogin(LocalDateTime.now());
            usuario.setIntentosFallidos(0);
            if (usuario.isBloqueado()) {
                usuario.setBloqueado(0);
            }
            usuarioRepository.save(usuario);
        }

        String ip = request.getRemoteAddr();
        intentoLoginService.registrarIntento(username, true, ip, null);

        setDefaultTargetUrl(determinarUrl(authentication));
        try {
            super.onAuthenticationSuccess(request, response, authentication);
        } catch (IOException | ServletException e) {
            // Si falla la redireccion post-login, registrar error pero no interrumpir
            log.error("Error al redireccionar despues de login exitoso para '{}': {}",
                username, e.getMessage());
            try {
                response.sendRedirect(determinarUrl(authentication));
            } catch (IOException ex) {
                log.error("Error en redireccion de fallback para '{}': {}", username, ex.getMessage());
            }
        }
    }

    private String determinarUrl(Authentication auth) {
        for (GrantedAuthority a : auth.getAuthorities()) {
            String role = a.getAuthority();
            if ("ROLE_ADMINISTRADOR".equals(role) || "ROLE_GERENTE".equals(role)
                    || "ROLE_RECEPCIONISTA".equals(role)) {
                return "/dashboard";
            }
            if ("ROLE_MEDICO".equals(role)) {
                return "/medico/cola";
            }
            if ("ROLE_ENFERMERA".equals(role)) {
                return "/admision";
            }
            if ("ROLE_PACIENTE".equals(role)) {
                return "/portal/dashboard";
            }
        }
        return "/dashboard";
    }
}
