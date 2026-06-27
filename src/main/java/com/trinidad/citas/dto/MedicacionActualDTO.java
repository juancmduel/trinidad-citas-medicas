package com.trinidad.citas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class MedicacionActualDTO {
    private Long idMedicacion;
    private Long idHistoria;
    private String nombreMedicamento;
    private String dosis;
    private String frecuencia;
    private String via;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String indicaciones;
    private Integer activo;
}
