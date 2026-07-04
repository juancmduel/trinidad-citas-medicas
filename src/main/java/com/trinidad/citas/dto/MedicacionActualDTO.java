package com.trinidad.citas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class MedicacionActualDTO {
    private Long idMedicacion;

    @NotNull(message = "Debe asociar la medicación a una historia clínica")
    private Long idHistoria;

    @NotBlank(message = "El nombre del medicamento es obligatorio")
    @Size(max = 150, message = "El nombre del medicamento no puede exceder 150 caracteres")
    private String nombreMedicamento;

    @NotBlank(message = "La dosis es obligatoria")
    @Size(max = 80, message = "La dosis no puede exceder 80 caracteres")
    private String dosis;

    @NotBlank(message = "La frecuencia es obligatoria")
    @Size(max = 80, message = "La frecuencia no puede exceder 80 caracteres")
    private String frecuencia;

    @Size(max = 50, message = "La vía de administración no puede exceder 50 caracteres")
    private String via;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    @Size(max = 500, message = "Las indicaciones no pueden exceder 500 caracteres")
    private String indicaciones;

    private Integer activo;
}
