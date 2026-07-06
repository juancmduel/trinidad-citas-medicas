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

/**
 * Servicio para restablecer contraseñas olvidadas.
 *
 * El flujo es:
 *  1. El usuario pone su email en "¿Olvidaste tu contraseña?"
 *  2. Le enviamos un correo con un enlace único (token UUID)
 *  3. El usuario hace clic en el enlace
 *  4. El sistema valida que el token no haya expirado ni se haya usado
 *  5. El usuario pone su nueva contraseña
 *  6. Se guarda la nueva contraseña (encriptada) y se marca el token como usado
 *
 * El token expira después de 24 horas (definido en PasswordResetToken).
 * Siempre respondemos "Si el email está registrado..." para no revelar
 * qué correos existen en el sistema.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PasswordResetService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    /**
     * Paso 1 y 2: Alguien solicitó restablecer su contraseña.
     * Buscamos el usuario por email, generamos un token, y enviamos el correo.
     *
     * Si el usuario no existe, está inactivo o bloqueado, decimos lo mismo
     * ("Si el email está registrado...") para no dar pistas.
     *
     * TODO: Permitir que usuarios bloqueados también puedan restablecer
     * su contraseña (y al hacerlo, desbloquear la cuenta automáticamente).
     */
    public void solicitarRestablecimiento(String email, String baseUrl) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Si el email esta registrado, recibiras un enlace de recuperacion."));

        if (!usuario.isActivo()) {
            throw new BusinessException("Si el email esta registrado, recibiras un enlace de recuperacion.");
        }
        if (usuario.isBloqueado()) {
            throw new BusinessException("Si el email esta registrado, recibiras un enlace de recuperacion.");
        }

        // Generamos un token único (UUID) y lo guardamos en la base de datos
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .usuario(usuario)
                .build();
        tokenRepository.save(resetToken);

        // Construimos el enlace y enviamos el correo
        String link = baseUrl + "/reset-password/" + token;
        String nombre = usuario.getUsername();
        emailService.enviarRecuperacionPassword(usuario.getEmail(), nombre, link);
    }

    /**
     * Paso 3 y 4: Validar que el token sea válido, no esté usado ni expirado.
     * Esto se llama cuando el usuario hace clic en el enlace del correo.
     */
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

    /**
     * Paso 5 y 6: El usuario puso su nueva contraseña.
     * La encriptamos con BCrypt, actualizamos el usuario,
     * y marcamos el token como usado para que no pueda reutilizarse.
     */
    public void restablecerPassword(String token, String nuevaPassword) {
        PasswordResetToken resetToken = validarToken(token);

        Usuario usuario = resetToken.getUsuario();
        usuario.setPasswordHash(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuario);

        // El token ya no sirve para nada después de usarlo
        resetToken.setUsado(true);
        tokenRepository.save(resetToken);
    }
}
