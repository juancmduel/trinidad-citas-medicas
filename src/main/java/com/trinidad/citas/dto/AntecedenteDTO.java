package com.trinidad.citas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class AntecedenteDTO {
    private Long idAntecedente;
    private Long idHistoria;
    private String tipo;
    private String descripcion;
    private String codigoCie10;
    private LocalDate fechaRegistro;
    private Integer activo;
    private String observaciones;
}
