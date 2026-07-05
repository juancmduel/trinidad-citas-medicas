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

    @NotNull(message = "Debe seleccionar una cita")
    private Long idCita;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    @DecimalMax(value = "99999.99", message = "El monto no puede exceder S/. 99,999.99")
    private BigDecimal monto;

    @NotBlank(message = "Debe seleccionar un método de pago")
    @Pattern(regexp = "^(EFECTIVO|TARJETA|TRANSFERENCIA|YAPE)$",
             message = "Método de pago no válido")
    @Size(max = 20)
    private String metodoPago;

    @NotBlank(message = "El estado del pago es obligatorio")
    @Pattern(regexp = "^(PENDIENTE|PAGADO|ANULADO)$", message = "Estado no válido")
    @Size(max = 20)
    private String estado;

    @Size(max = 30)
    @Pattern(regexp = "^(B001-|F001-|R001-)?[0-9]{0,6}$", message = "Formato de comprobante no válido")
    private String nroComprobante;

    @Size(max = 15)
    @Pattern(regexp = "^(BOLETA|FACTURA|RECIBO)?$", message = "Tipo de comprobante no válido")
    private String tipoComprobante;

    private LocalDateTime fechaPago;

    // Se asigna en el service desde la entidad relacionada
    private LocalDateTime fechaRegistro;
}
