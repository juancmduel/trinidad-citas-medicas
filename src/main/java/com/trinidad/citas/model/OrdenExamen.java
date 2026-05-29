package com.trinidad.citas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ORDEN_EXAMEN")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrdenExamen {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_orden")
    @SequenceGenerator(name = "seq_orden", sequenceName = "SEQ_ORDEN_EXAMEN", allocationSize = 1)
    @Column(name = "ID_ORDEN")
    private Long idOrden;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_ATENCION", nullable = false)
    @ToString.Exclude
    private Atencion atencion;

    @NotBlank
    @Size(max = 30)
    @Column(name = "TIPO_EXAMEN", nullable = false, length = 30)
    private String tipoExamen;

    @NotBlank
    @Size(max = 150)
    @Column(name = "NOMBRE_EXAMEN", nullable = false, length = 150)
    private String nombreExamen;

    @Size(max = 500)
    @Column(name = "INDICACIONES", length = 500)
    private String indicaciones;

    @Size(max = 20)
    @Column(name = "ESTADO", nullable = false, length = 20)
    @Builder.Default
    private String estado = "SOLICITADO";

    @Column(name = "FECHA_SOLICITUD", nullable = false)
    private LocalDateTime fechaSolicitud;

    @Column(name = "FECHA_RESULTADO")
    private LocalDateTime fechaResultado;

    @PrePersist
    public void prePersist() {
        if (fechaSolicitud == null) {
            fechaSolicitud = LocalDateTime.now();
        }
    }
}
