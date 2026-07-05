package com.trinidad.citas.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data @NoArgsConstructor @AllArgsConstructor
public class RolDTO {

    private Long idRol;

    @NotBlank @Size(max = 30)
    private String nombre;

    @Size(max = 200)
    private String descripcion;

    private Integer activo;

    // IDs de permisos asignados al rol
    private Set<Long> permisosIds;

    // Se completa en el service con los nombres de los permisos
    private Set<String> permisosNombres;
}
