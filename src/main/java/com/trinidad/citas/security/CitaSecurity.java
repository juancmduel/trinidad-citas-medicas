package com.trinidad.citas.security;

import com.trinidad.citas.model.Cita;
import com.trinidad.citas.repository.CitaRepository;
import com.trinidad.citas.repository.MedicoRepository;
import com.trinidad.citas.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Principal;

/**
 * Bean de seguridad para verificar pertenencia (ownership) de citas.
 * Uso: @PreAuthorize("@citaSecurity.isMedicoAsignado(#id, principal)")
 *       @PreAuthorize("@citaSecurity.isOwnCita(#id, principal)")
 */
@Component("citaSecurity")
@RequiredArgsConstructor
public class CitaSecurity {

    private final CitaRepository citaRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
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
        return isMedicoAsignado(idCita, principal);
    }
}
