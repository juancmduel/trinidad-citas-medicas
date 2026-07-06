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

@Service
@RequiredArgsConstructor
@Transactional
public class IntentoLoginService {

    private static final Logger log = LoggerFactory.getLogger(IntentoLoginService.class);

    // Constantes centralizadas en AppConstants
    private static final int MAX_INTENTOS_FALLIDOS = AppConstants.MAX_INTENTOS_FALLIDOS;
    private static final int VENTANA_MINUTOS = AppConstants.VENTANA_INTENTOS_MINUTOS;

    private final IntentoLoginRepository intentoLoginRepository;

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
            // Si falla la auditoria de intentos, no debe bloquear el flujo de login.
            log.warn("No se pudo registrar intento de login para '{}': {}", username, e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public long contarIntentosFallidosRecientes(String username) {
        LocalDateTime desde = LocalDateTime.now().minusMinutes(VENTANA_MINUTOS);
        return intentoLoginRepository.countByUsernameAndExitosoAndFechaHoraAfter(username, 0, desde);
    }

    @Transactional(readOnly = true)
    public List<IntentoLogin> listarTodos() {
        return intentoLoginRepository.findAllByOrderByFechaHoraDesc();
    }

    @Transactional(readOnly = true)
    public boolean debeBloquear(String username) {
        return contarIntentosFallidosRecientes(username) >= MAX_INTENTOS_FALLIDOS;
    }

    @Transactional(readOnly = true)
    public List<IntentoLogin> obtenerIntentosRecientes(String username) {
        return intentoLoginRepository.findByUsernameOrderByFechaHoraDesc(username);
    }
}
