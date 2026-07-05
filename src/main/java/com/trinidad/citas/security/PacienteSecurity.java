package com.trinidad.citas.security;

import com.trinidad.citas.model.Paciente;
import com.trinidad.citas.model.Usuario;
import com.trinidad.citas.repository.PacienteRepository;
import com.trinidad.citas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Principal;

/**
 * Bean de seguridad para verificar pertenencia (ownership) de datos de pacientes.
 * Uso: @PreAuthorize("@pacienteSecurity.isOwnPaciente(#id, principal)")
 */
@Component("pacienteSecurity")
@RequiredArgsConstructor
public class PacienteSecurity {

    private final UsuarioRepository usuarioRepository;
    private final PacienteRepository pacienteRepository;

    /**
     * Verifica si el usuario autenticado es el dueño del paciente con ID dado.
     */
    public boolean isOwnPaciente(Long idPaciente, Principal principal) {
        if (principal == null) return false;
        return pacienteRepository.findById(idPaciente)
                .map(p -> p.getUsuario() != null && p.getUsuario().getUsername().equals(principal.getName()))
                .orElse(false);
    }

    /**
     * Verifica si el usuario autenticado es el dueño del paciente por DNI.
     */
    public boolean isOwnPacienteByDni(String dni, Principal principal) {
        if (principal == null) return false;
        return pacienteRepository.findByDni(dni)
                .map(p -> p.getUsuario() != null && p.getUsuario().getUsername().equals(principal.getName()))
                .orElse(false);
    }

    /**
     * Obtiene el ID del paciente asociado al usuario autenticado, o null si no es paciente.
     */
    public Long getPacienteIdFromPrincipal(Principal principal) {
        if (principal == null) return null;
        return usuarioRepository.findByUsername(principal.getName())
                .map(Usuario::getIdUsuario)
                .flatMap(idUsuario -> pacienteRepository.findByUsuario_IdUsuario(idUsuario)
                        .map(Paciente::getIdPaciente))
                .orElse(null);
    }
}
