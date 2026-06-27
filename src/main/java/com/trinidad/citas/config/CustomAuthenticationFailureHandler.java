package com.trinidad.citas.config;

import com.trinidad.citas.model.Usuario;
import com.trinidad.citas.repository.UsuarioRepository;
import com.trinidad.citas.service.IntentoLoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);

    private final UsuarioRepository usuarioRepository;
    private final IntentoLoginService intentoLoginService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        String username = request.getParameter("username");
        String ip = request.getRemoteAddr();

        log.warn("Login failed for user '{}' from IP {}: {}", username, ip, exception.getClass().getSimpleName());

        try {
            String errorMsg = exception.getMessage();
            if (exception instanceof BadCredentialsException) {
                errorMsg = "Credenciales inválidas";
            } else if (exception instanceof LockedException) {
                errorMsg = "Cuenta bloqueada por múltiples intentos fallidos";
            } else if (exception instanceof DisabledException) {
                errorMsg = "Cuenta desactivada";
            }

            intentoLoginService.registrarIntento(username != null ? username : "desconocido", false, ip, errorMsg);

            if (username != null) {
                usuarioRepository.findByUsername(username).ifPresent(usuario -> {
                    usuario.setIntentosFallidos(usuario.getIntentosFallidos() + 1);
                    if (intentoLoginService.debeBloquear(username)) {
                        usuario.setBloqueado(1);
                        log.warn("User '{}' has been blocked due to too many failed attempts", username);
                    }
                    usuarioRepository.save(usuario);
                });
            }
        } catch (Exception e) {
            log.error("Error recording failed login attempt for user '{}': {}", username, e.getMessage(), e);
        }

        String redirectUrl = "/login?error=true";
        if (exception instanceof LockedException) {
            redirectUrl = "/login?error=locked";
        } else if (exception instanceof DisabledException) {
            redirectUrl = "/login?error=disabled";
        }

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
