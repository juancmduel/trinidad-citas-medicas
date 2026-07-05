package com.trinidad.citas.service;

import com.trinidad.citas.config.AppConstants;
import com.trinidad.citas.model.IntentoLogin;
import com.trinidad.citas.repository.IntentoLoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class IntentoLoginService {

    // Constantes centralizadas en AppConstants
    private static final int MAX_INTENTOS_FALLIDOS = AppConstants.MAX_INTENTOS_FALLIDOS;
    private static final int VENTANA_MINUTOS = AppConstants.VENTANA_INTENTOS_MINUTOS;

    private final IntentoLoginRepository intentoLoginRepository;

    public void registrarIntento(String username, boolean exitoso, String ipOrigen, String mensajeError) {
        IntentoLogin intento = IntentoLogin.builder()
                .username(username)
                .exitoso(exitoso ? 1 : 0)
                .ipOrigen(ipOrigen)
                .mensajeError(mensajeError)
                .build();
        intentoLoginRepository.save(intento);
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
