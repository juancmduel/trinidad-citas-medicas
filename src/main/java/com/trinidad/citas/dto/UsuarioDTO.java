package com.trinidad.citas.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data @NoArgsConstructor @AllArgsConstructor
public class UsuarioDTO {

    private Long idUsuario;

    @NotBlank @Size(max = 50)
    private String username;

    // Solo en creación/cambio de contraseña — no se devuelve en respuestas
    @Size(min = 8, max = 100)
    private String password;

    @Email @NotBlank @Size(max = 120)
    private String email;

    private Integer activo;
    private Integer bloqueado;
    private Integer intentosFallidos;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaUltLogin;

    // IDs de roles asignados
    private Set<Long> rolesIds;

    // Solo respuesta: nombres de roles
    private Set<String> rolesNombres;
}
