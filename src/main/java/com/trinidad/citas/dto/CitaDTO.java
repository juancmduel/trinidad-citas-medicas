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

    @NotBlank(message = "La hora de inicio es obligatoria")
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "La hora debe tener formato HH:mm (ej: 14:30)")
    private String horaInicio;

    @Size(max = 300)
    private String motivoConsulta;

    @Size(max = 20)
    private String canalReserva;

    private String estado;

    // Se llenan desde la entidad para mostrarlos en las vistas
    private String nombrePaciente;
    private String dniPaciente;
    private String nombreMedico;
    private String nombreEspecialidad;
}
