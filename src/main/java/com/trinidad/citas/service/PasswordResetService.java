package com.trinidad.citas.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trinidad.citas.exception.BusinessException;
import com.trinidad.citas.model.PasswordResetToken;
import com.trinidad.citas.model.Usuario;
import com.trinidad.citas.repository.PasswordResetTokenRepository;
import com.trinidad.citas.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PasswordResetService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public void solicitarRestablecimiento(String email, String baseUrl) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Si el email esta registrado, recibiras un enlace de recuperacion."));

        if (!usuario.isActivo()) {
            throw new BusinessException("Si el email esta registrado, recibiras un enlace de recuperacion.");
        }
        if (usuario.isBloqueado()) {
            throw new BusinessException("Si el email esta registrado, recibiras un enlace de recuperacion.");
        }

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .usuario(usuario)
                .build();
        tokenRepository.save(resetToken);

        String link = baseUrl + "/reset-password/" + token;
        String nombre = usuario.getUsername();
        emailService.enviarRecuperacionPassword(usuario.getEmail(), nombre, link);
    }

    @Transactional(readOnly = true)
    public PasswordResetToken validarToken(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new BusinessException("El enlace de recuperacion no es valido."));

        if (resetToken.getUsado()) {
            throw new BusinessException("Este enlace ya ha sido utilizado.");
        }
        if (resetToken.isExpirado()) {
            throw new BusinessException("El enlace ha expirado. Solicita uno nuevo.");
        }
        return resetToken;
    }

    public void restablecerPassword(String token, String nuevaPassword) {
        PasswordResetToken resetToken = validarToken(token);

        Usuario usuario = resetToken.getUsuario();
        usuario.setPasswordHash(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuario);

        resetToken.setUsado(true);
        tokenRepository.save(resetToken);
    }
}
