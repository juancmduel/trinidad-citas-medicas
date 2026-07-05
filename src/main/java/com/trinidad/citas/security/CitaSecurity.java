package com.trinidad.citas.security;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.trinidad.citas.repository.CitaRepository;

import lombok.RequiredArgsConstructor;

/**
 * Bean de seguridad para verificar pertenencia (ownership) de citas.
 * Uso: @PreAuthorize("@citaSecurity.isMedicoAsignado(#id, principal)")
 *       @PreAuthorize("@citaSecurity.isOwnCita(#id, principal)")
 */
@Component("citaSecurity")
@RequiredArgsConstructor
public class CitaSecurity {

    private final CitaRepository citaRepository;
    private final PacienteSecurity pacienteSecurity;

    /**
     * Verifica si el médico autenticado es el asignado a la cita.
     */
    public boolean isMedicoAsignado(Long idCita, Principal principal) {
        if (principal == null) return false;
        return citaRepository.findById(idCita)
                .map(cita -> cita.getMedico().getUsuario().getUsername().equals(principal.getName()))
                .orElse(false);
    }

    /**
     * Verifica si el paciente autenticado es el dueño de la cita.
     */
    public boolean isOwnCita(Long idCita, Principal principal) {
        if (principal == null) return false;
        Long idPaciente = pacienteSecurity.getPacienteIdFromPrincipal(principal);
        if (idPaciente == null) return false;
        return citaRepository.findById(idCita)
                .map(cita -> cita.getPaciente().getIdPaciente().equals(idPaciente))
                .orElse(false);
    }

    /**
     * Verifica si el médico autenticado es el asignado a la cita, o si es ADMIN.
     */
    public boolean isMedicoAsignadoOrAdmin(Long idCita, Principal principal) {
        if (principal == null) return false;
        // Verificar si es ADMINISTRADOR
        if (hasRole("ROLE_ADMINISTRADOR")) return true;
        // Si no es ADMIN, verificar si es el medico asignado
        return isMedicoAsignado(idCita, principal);
    }

    /**
     * Verifica si el usuario autenticado tiene un rol específico.
     */
    private boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role::equals);
    }
}
