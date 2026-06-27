package com.trinidad.citas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor
public class TriajeDTO {

    private Long idTriaje;

    private Long idCita;

    private Long idEnfermera;

    private LocalDateTime fechaTriaje;

    private String presionArterial;
    private Integer frecuenciaCardiaca;
    private BigDecimal temperatura;
    private BigDecimal pesoKg;
    private BigDecimal tallaCm;
    private Integer saturacionO2;
    private BigDecimal glucosa;

    private String sintomas;
    private String alergiasReportadas;
    private String medicacionActual;
    private String nivelUrgencia;
    private String observaciones;

    private String nombrePaciente;
    private String dniPaciente;
    private String nombreMedico;
    private String consultorio;
    private String especialidad;
}
