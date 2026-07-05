package com.trinidad.citas.security;

import com.trinidad.citas.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Principal;

/**
 * Bean de seguridad para verificar pertenencia (ownership) de datos de médicos.
 * Uso: @PreAuthorize("@medicoSecurity.isOwnMedico(#id, principal)")
 */
@Component("medicoSecurity")
@RequiredArgsConstructor
public class MedicoSecurity {

    private final MedicoRepository medicoRepository;

    /**
     * Verifica si el médico autenticado es el dueño del perfil de médico con ID dado.
     */
    public boolean isOwnMedico(Long idMedico, Principal principal) {
        if (principal == null) return false;
        return medicoRepository.findById(idMedico)
                .map(m -> m.getUsuario().getUsername().equals(principal.getName()))
                .orElse(false);
    }
}
