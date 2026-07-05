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

    @NotNull(message = "Debe seleccionar un día de la semana")
    @Min(value = 1, message = "El día de la semana debe estar entre 1 (lunes) y 7 (domingo)")
    @Max(value = 7, message = "El día de la semana debe estar entre 1 (lunes) y 7 (domingo)")
    private Integer diaSemana;

    @NotBlank(message = "La hora de inicio es obligatoria")
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "La hora debe tener formato HH:mm (ej: 08:00)")
    private String horaInicio;

    @NotBlank(message = "La hora de fin es obligatoria")
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "La hora debe tener formato HH:mm (ej: 17:00)")
    private String horaFin;

    private Integer activo;

    // Solo lectura: se completa desde la entidad en el service
    private String medicoNombreCompleto;
    private String diaSemanaDescripcion;
}
