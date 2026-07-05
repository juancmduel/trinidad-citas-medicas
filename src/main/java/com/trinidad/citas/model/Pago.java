package com.trinidad.citas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "PAGO")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pago")
    @SequenceGenerator(name = "seq_pago", sequenceName = "SEQ_PAGO", allocationSize = 1)
    @Column(name = "ID_PAGO")
    private Long idPago;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_CITA", nullable = false)
    private Cita cita;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    @DecimalMax(value = "99999.99", message = "El monto no puede exceder S/. 99,999.99")
    @Column(name = "MONTO", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @NotBlank(message = "Debe seleccionar un método de pago")
    @Pattern(regexp = "^(EFECTIVO|TARJETA|TRANSFERENCIA|YAPE)$",
             message = "Método de pago no válido")
    @Size(max = 20)
    @Column(name = "METODO_PAGO", nullable = false, length = 20)
    private String metodoPago;

    @NotBlank(message = "El estado del pago es obligatorio")
    @Pattern(regexp = "^(PENDIENTE|PAGADO|ANULADO)$", message = "Estado no válido")
    @Size(max = 20)
    @Column(name = "ESTADO", nullable = false, length = 20)
    @Builder.Default
    private String estado = "PENDIENTE";

    @Size(max = 30)
    @Pattern(regexp = "^(B001-|F001-|R001-)?[0-9]{0,6}$", message = "Formato de comprobante no válido")
    @Column(name = "NRO_COMPROBANTE", length = 30)
    private String nroComprobante;

    @Size(max = 15)
    @Pattern(regexp = "^(BOLETA|FACTURA|RECIBO)?$", message = "Tipo de comprobante no válido")
    @Column(name = "TIPO_COMPROBANTE", length = 15)
    private String tipoComprobante;

    @Column(name = "FECHA_PAGO")
    private LocalDateTime fechaPago;

    @Column(name = "FECHA_REGISTRO", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "ACTIVO", nullable = false)
    @Builder.Default
    private Integer activo = 1;

    @PrePersist
    public void prePersist() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
    }
}
