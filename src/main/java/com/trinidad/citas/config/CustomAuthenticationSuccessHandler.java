package com.trinidad.citas.config;

import com.trinidad.citas.model.Usuario;
import com.trinidad.citas.repository.UsuarioRepository;
import com.trinidad.citas.service.IntentoLoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

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

        setDefaultTargetUrl("/dashboard");
        try {
            super.onAuthenticationSuccess(request, response, authentication);
        } catch (Exception e) {
            throw new RuntimeException("Error en post-login exitoso", e);
        }
    }
}
