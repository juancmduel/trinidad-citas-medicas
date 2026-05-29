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

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "MONTO", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @NotBlank
    @Size(max = 20)
    @Column(name = "METODO_PAGO", nullable = false, length = 20)
    private String metodoPago;

    @Size(max = 20)
    @Column(name = "ESTADO", nullable = false, length = 20)
    @Builder.Default
    private String estado = "PENDIENTE";

    @Size(max = 30)
    @Column(name = "NRO_COMPROBANTE", length = 30)
    private String nroComprobante;

    @Size(max = 15)
    @Column(name = "TIPO_COMPROBANTE", length = 15)
    private String tipoComprobante;

    @Column(name = "FECHA_PAGO")
    private LocalDateTime fechaPago;

    @Column(name = "FECHA_REGISTRO", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @PrePersist
    public void prePersist() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
    }
}
