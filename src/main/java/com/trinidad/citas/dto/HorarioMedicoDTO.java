package com.trinidad.citas.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class HorarioMedicoDTO {

    private Long idHorario;

    @NotNull
    private Long idMedico;

    @NotNull
    @Min(1) @Max(7)
    private Integer diaSemana;

    @NotBlank
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$")
    private String horaInicio;

    @NotBlank
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$")
    private String horaFin;

    private Integer activo;

    // Solo respuesta
    private String medicoNombreCompleto;
    private String diaSemanaDescripcion;
}
