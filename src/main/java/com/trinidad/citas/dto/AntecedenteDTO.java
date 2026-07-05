package com.trinidad.citas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class AntecedenteDTO {
    private Long idAntecedente;

    @NotNull(message = "Debe asociar el antecedente a una historia clínica")
    private Long idHistoria;

    @NotBlank(message = "El tipo de antecedente es obligatorio")
    @Size(max = 60, message = "El tipo de antecedente no puede exceder 60 caracteres")
    private String tipo;

    @NotBlank(message = "La descripción del antecedente es obligatoria")
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @Size(max = 10, message = "El código CIE-10 no puede exceder 10 caracteres")
    private String codigoCie10;

    private LocalDate fechaRegistro;
    private Integer activo;

    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    private String observaciones;
}
