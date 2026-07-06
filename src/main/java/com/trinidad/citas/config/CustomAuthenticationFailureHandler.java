package com.trinidad.citas.config;

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

/**
 * Cuando alguien mete mal la contraseña (o la cuenta está bloqueada),
 * este manejador se encarga de:
 *  1. Registrar el intento fallido en la base de datos (para auditoría)
 *  2. Llevar la cuenta de cuántas veces ha fallado
 *  3. Si llega a 5, bloquear la cuenta automáticamente
 *  4. Redirigir al login con el mensaje de error apropiado
 *
 * Así evitamos que un bot se pase todo el día probando contraseñas.
 */
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

        log.warn("⛔ Alguien no pudo iniciar sesión como '{}' desde la IP {}: {}",
            username, ip, exception.getClass().getSimpleName());

        try {
            // Traducimos el error técnico a algo que un humano entienda
            String errorMsg = exception.getMessage();
            if (exception instanceof BadCredentialsException) {
                errorMsg = "Credenciales inválidas";
            } else if (exception instanceof LockedException) {
                errorMsg = "Cuenta bloqueada por múltiples intentos fallidos";
            } else if (exception instanceof DisabledException) {
                errorMsg = "Cuenta desactivada";
            }

            // Registramos el intento en el historial
            intentoLoginService.registrarIntento(username != null ? username : "desconocido", false, ip, errorMsg);

            // Si el usuario existe, aumentamos su contador de intentos fallidos
            if (username != null) {
                usuarioRepository.findByUsername(username).ifPresent(usuario -> {
                    usuario.setIntentosFallidos(usuario.getIntentosFallidos() + 1);
                    // ¿Ya llegó al límite? Pues lo bloqueamos.
                    if (intentoLoginService.debeBloquear(username)) {
                        usuario.setBloqueado(1);
                        log.warn("🔒 Usuario '{}' bloqueado por demasiados intentos fallidos", username);
                    }
                    usuarioRepository.save(usuario);
                });
            }
        } catch (Exception e) {
            // Si falla la auditoría, no debería impedir el login.
            // Mejor logueamos el error y seguimos.
            log.error("Error al registrar intento fallido para '{}': {}", username, e.getMessage(), e);
        }

        // Redirigimos con un parámetro para que la pantalla de login muestre el error
        String redirectUrl = "/login?error=true";
        if (exception instanceof LockedException) {
            redirectUrl = "/login?error=locked";
        } else if (exception instanceof DisabledException) {
            redirectUrl = "/login?error=disabled";
        }

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
