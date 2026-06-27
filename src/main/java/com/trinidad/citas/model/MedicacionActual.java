package com.trinidad.citas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "MEDICACION_ACTUAL")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MedicacionActual {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_medicacion_actual")
    @SequenceGenerator(name = "seq_medicacion_actual", sequenceName = "SEQ_MEDICACION_ACTUAL", allocationSize = 1)
    @Column(name = "ID_MEDICACION")
    private Long idMedicacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_HISTORIA", nullable = false)
    private HistoriaClinica historia;

    @NotBlank
    @Size(max = 150)
    @Column(name = "NOMBRE_MEDICAMENTO", nullable = false, length = 150)
    private String nombreMedicamento;

    @Size(max = 80)
    @Column(name = "DOSIS", length = 80)
    private String dosis;

    @Size(max = 80)
    @Column(name = "FRECUENCIA", length = 80)
    private String frecuencia;

    @Size(max = 30)
    @Column(name = "VIA", length = 30)
    private String via;

    @Column(name = "FECHA_INICIO")
    private LocalDate fechaInicio;

    @Column(name = "FECHA_FIN")
    private LocalDate fechaFin;

    @Size(max = 500)
    @Column(name = "INDICACIONES", length = 500)
    private String indicaciones;

    @Column(name = "ACTIVO", nullable = false)
    @Builder.Default
    private Integer activo = 1;
}
