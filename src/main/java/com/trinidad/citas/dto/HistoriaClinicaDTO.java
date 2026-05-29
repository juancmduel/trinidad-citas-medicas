package com.trinidad.citas.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class HistoriaClinicaDTO {

    private Long idHistoria;

    @NotNull
    private Long idPaciente;

    @NotBlank @Size(max = 15)
    private String nroHistoria;

    private LocalDate fechaApertura;

    @Size(max = 1000)
    private String observaciones;

    private Integer activo;

    // Solo respuesta
    private String pacienteNombreCompleto;
}
