package com.trinidad.citas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class DisponibilidadDTO {
    private Long idMedico;
    private String nombreMedico;
    private String especialidad;
    private String consultorio;
    private List<String> slotsDisponibles;
}
