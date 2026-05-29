package com.trinidad.citas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class KpiDashboardDTO {
    private long totalPacientes;
    private long totalMedicos;
    private long citasHoy;
    private long citasAtendidasHoy;
    private long citasPendientesHoy;
    private long citasCanceladasHoy;
    private long totalEspecialidades;
}
