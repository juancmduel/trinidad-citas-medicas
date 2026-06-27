package com.trinidad.citas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class IngresoDTO {
    private Long idPago;
    private String nroComprobante;
    private String paciente;
    private String dniPaciente;
    private BigDecimal monto;
    private String metodoPago;
    private String estado;
    private LocalDateTime fechaPago;
    private LocalDate fechaCita;
}
