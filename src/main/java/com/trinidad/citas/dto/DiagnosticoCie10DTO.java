package com.trinidad.citas.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class DiagnosticoCie10DTO {

    @NotBlank @Size(max = 10)
    private String codigo;

    @NotBlank @Size(max = 300)
    private String descripcion;

    @Size(max = 80)
    private String capitulo;

    private Integer activo;
}
