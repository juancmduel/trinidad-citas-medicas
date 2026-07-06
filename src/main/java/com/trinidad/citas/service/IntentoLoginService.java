package com.trinidad.citas.service;

import com.trinidad.citas.config.AppConstants;
import com.trinidad.citas.model.IntentoLogin;
import com.trinidad.citas.repository.IntentoLoginRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Lleva el registro de cada vez que alguien intenta iniciar sesión.
 *
 * Sirve para dos cosas:
 *  1. Bloquear cuentas después de 5 intentos fallidos en 30 minutos
 *  2. Auditoría: poder revisar quién, cuándo y desde dónde intentó entrar
 *
 * Los datos se guardan en la tabla INTENTO_LOGIN.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class IntentoLoginService {

    private static final Logger log = LoggerFactory.getLogger(IntentoLoginService.class);

    // Estas constantes vienen de AppConstants para tener todo centralizado
    private static final int MAX_INTENTOS_FALLIDOS = AppConstants.MAX_INTENTOS_FALLIDOS;
    private static final int VENTANA_MINUTOS = AppConstants.VENTANA_INTENTOS_MINUTOS;

    private final IntentoLoginRepository intentoLoginRepository;

    /**
     * Guarda un intento de login (sea exitoso o fallido).
     * Si la auditoría falla por algún motivo, no detenemos el flujo de login,
     * solo lo registramos en el log y seguimos.
     */
    public void registrarIntento(String username, boolean exitoso, String ipOrigen, String mensajeError) {
        try {
            IntentoLogin intento = IntentoLogin.builder()
                    .username(username)
                    .exitoso(exitoso ? 1 : 0)
                    .ipOrigen(ipOrigen)
                    .mensajeError(mensajeError)
                    .build();
            intentoLoginRepository.save(intento);
        } catch (Exception e) {
            // Si la BD de auditoría falla, no debería impedir que el usuario entre
            log.warn("No se pudo registrar intento de login para '{}': {}", username, e.getMessage());
        }
    }

    /**
     * Cuenta cuántos intentos fallidos ha tenido un usuario
     * en los últimos N minutos (la ventana definida en AppConstants).
     */
    @Transactional(readOnly = true)
    public long contarIntentosFallidosRecientes(String username) {
        LocalDateTime desde = LocalDateTime.now().minusMinutes(VENTANA_MINUTOS);
        return intentoLoginRepository.countByUsernameAndExitosoAndFechaHoraAfter(username, 0, desde);
    }

    /** Lista todos los intentos de login registrados (para auditoría) */
    @Transactional(readOnly = true)
    public List<IntentoLogin> listarTodos() {
        return intentoLoginRepository.findAllByOrderByFechaHoraDesc();
    }

    /**
     * Determina si un usuario debe ser bloqueado.
     * La regla: si ha tenido más de MAX_INTENTOS_FALLIDOS intentos fallidos
     * en la ventana de tiempo definida, se bloquea.
     */
    @Transactional(readOnly = true)
    public boolean debeBloquear(String username) {
        return contarIntentosFallidosRecientes(username) >= MAX_INTENTOS_FALLIDOS;
    }

    /** Obtiene los intentos recientes de un usuario específico */
    @Transactional(readOnly = true)
    public List<IntentoLogin> obtenerIntentosRecientes(String username) {
        return intentoLoginRepository.findByUsernameOrderByFechaHoraDesc(username);
    }
}
