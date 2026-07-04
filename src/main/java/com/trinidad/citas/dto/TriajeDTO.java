package com.trinidad.citas.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor
public class TriajeDTO {

    private Long idTriaje;

    @NotNull(message = "Debe seleccionar una cita")
    private Long idCita;

    private Long idEnfermera;

    private LocalDateTime fechaTriaje;

    @Size(max = 15, message = "La presión arterial no puede exceder 15 caracteres")
    private String presionArterial;

    @Min(value = 30, message = "La frecuencia cardíaca debe ser al menos 30 lpm")
    @Max(value = 250, message = "La frecuencia cardíaca no puede superar 250 lpm")
    private Integer frecuenciaCardiaca;

    @DecimalMin(value = "34.0", message = "La temperatura debe ser al menos 34.0 °C")
    @DecimalMax(value = "42.0", message = "La temperatura no puede superar 42.0 °C")
    private BigDecimal temperatura;

    @DecimalMin(value = "1.0", message = "El peso debe ser al menos 1 kg")
    @DecimalMax(value = "350.0", message = "El peso no puede superar 350 kg")
    private BigDecimal pesoKg;

    @DecimalMin(value = "20.0", message = "La talla debe ser al menos 20 cm")
    @DecimalMax(value = "250.0", message = "La talla no puede superar 250 cm")
    private BigDecimal tallaCm;

    @Min(value = 50, message = "La saturación de oxígeno debe ser al menos 50%")
    @Max(value = 100, message = "La saturación de oxígeno no puede superar 100%")
    private Integer saturacionO2;

    @DecimalMin(value = "20.0", message = "La glucosa debe ser al menos 20 mg/dL")
    @DecimalMax(value = "600.0", message = "La glucosa no puede superar 600 mg/dL")
    private BigDecimal glucosa;

    @Size(max = 500, message = "Los síntomas no pueden exceder 500 caracteres")
    private String sintomas;

    @Size(max = 500, message = "Las alergias reportadas no pueden exceder 500 caracteres")
    private String alergiasReportadas;

    @Size(max = 500, message = "La medicación actual no puede exceder 500 caracteres")
    private String medicacionActual;

    @NotBlank(message = "Debe seleccionar un nivel de urgencia")
    private String nivelUrgencia;

    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    private String observaciones;

    private String nombrePaciente;
    private String dniPaciente;
    private String nombreMedico;
    private String consultorio;
    private String especialidad;
}
