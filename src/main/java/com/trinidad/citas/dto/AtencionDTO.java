package com.trinidad.citas.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor
public class AtencionDTO {

    private Long idAtencion;

    @NotNull
    private Long idCita;

    @NotNull
    private Long idHistoria;

    @NotNull
    private Long idMedico;

    private LocalDateTime fechaAtencion;

    @Size(max = 500)
    private String motivoConsulta;

    private String anamnesis;
    private String examenFisico;

    private String diagnosticoCie10Codigo;

    @Size(max = 500)
    private String diagnosticoDesc;

    private String tratamiento;

    @Size(max = 1000)
    private String observaciones;

    // Se completa desde la entidad para mostrarlo en las vistas
    private String nombreMedicoCompleto;
    private String nombrePacienteCompleto;

    // Signos vitales tomados durante la atención
    @Size(max = 15)
    private String presionArterial;

    private Integer frecuenciaCardiaca;
    private BigDecimal temperatura;
    private BigDecimal pesoKg;
    private BigDecimal tallaCm;
}
