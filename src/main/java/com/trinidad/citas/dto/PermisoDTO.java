package com.trinidad.citas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class PermisoDTO {

    private Long idPermiso;

    @NotBlank @Size(max = 60)
    private String nombre;

    @Size(max = 200)
    private String descripcion;

    @NotBlank @Size(max = 50)
    private String recurso;

    @NotBlank @Size(max = 20)
    private String accion;

    private Integer activo;
}
