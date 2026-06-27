package com.trinidad.citas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdmisionPacienteDTO {

    private Long idCita;
    private String horaInicio;
    private String horaFin;
    private String estado;
    private Integer numeroTurno;

    private Long idPaciente;
    private String nombrePaciente;
    private String dniPaciente;
    private String telefonoPaciente;

    private Long idMedico;
    private String nombreMedico;
    private String consultorio;

    private Long idEspecialidad;
    private String nombreEspecialidad;
    private BigDecimal precioConsulta;

    private Long idPago;
    private BigDecimal montoPagado;
    private String metodoPago;
    private String estadoPago;
    private String tipoComprobante;

    private Long idTriaje;
    private String presionArterial;
    private Integer frecuenciaCardiaca;
    private BigDecimal temperatura;
    private Integer saturacionO2;
    private String nivelUrgencia;
}
