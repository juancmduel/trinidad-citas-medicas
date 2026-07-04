package com.trinidad.citas.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor
public class PagoDTO {

    private Long idPago;

    @NotNull
    private Long idCita;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal monto;

    @NotBlank(message = "Debe seleccionar un método de pago")
    @Size(max = 20, message = "El método de pago no puede exceder 20 caracteres")
    private String metodoPago;

    private String estado;

    @Size(max = 30)
    private String nroComprobante;

    @Size(max = 15)
    private String tipoComprobante;

    private LocalDateTime fechaPago;

    // Se asigna en el service desde la entidad relacionada
    private LocalDateTime fechaRegistro;
}
