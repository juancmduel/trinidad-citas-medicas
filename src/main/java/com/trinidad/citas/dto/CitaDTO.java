package com.trinidad.citas.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class CitaDTO {

    private Long idCita;

    @NotNull
    private Long idPaciente;

    @NotNull
    private Long idMedico;

    @NotNull
    private Long idEspecialidad;

    @NotNull
    @FutureOrPresent
    private LocalDate fechaCita;

    @NotBlank
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$")
    private String horaInicio;

    @Size(max = 300)
    private String motivoConsulta;

    @Size(max = 20)
    private String canalReserva;

    private String estado;

    // Campos de display (solo lectura, para vistas web)
    private String nombrePaciente;
    private String dniPaciente;
    private String nombreMedico;
    private String nombreEspecialidad;
}
