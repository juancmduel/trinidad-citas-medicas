package com.trinidad.citas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ATENCION")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Atencion {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_atencion")
    @SequenceGenerator(name = "seq_atencion", sequenceName = "SEQ_ATENCION", allocationSize = 1)
    @Column(name = "ID_ATENCION")
    private Long idAtencion;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CITA", nullable = false, unique = true)
    private Cita cita;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_HISTORIA", nullable = false)
    private HistoriaClinica historia;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_MEDICO", nullable = false)
    private Medico medico;

    @Column(name = "FECHA_ATENCION", nullable = false)
    private LocalDateTime fechaAtencion;

    @Size(max = 500)
    @Column(name = "MOTIVO_CONSULTA", length = 500)
    private String motivoConsulta;

    @Lob
    @Column(name = "ANAMNESIS")
    private String anamnesis;

    @Lob
    @Column(name = "EXAMEN_FISICO")
    private String examenFisico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DIAGNOSTICO_CIE10")
    private DiagnosticoCie10 diagnosticoCie10;

    @Size(max = 500)
    @Column(name = "DIAGNOSTICO_DESC", length = 500)
    private String diagnosticoDesc;

    @Lob
    @Column(name = "TRATAMIENTO")
    private String tratamiento;

    @Size(max = 1000)
    @Column(name = "OBSERVACIONES", length = 1000)
    private String observaciones;

    // Signos vitales
    @Size(max = 15)
    @Column(name = "PRESION_ARTERIAL", length = 15)
    private String presionArterial;

    @Column(name = "FRECUENCIA_CARDIACA")
    private Integer frecuenciaCardiaca;

    @Column(name = "TEMPERATURA", precision = 4, scale = 1)
    private BigDecimal temperatura;

    @Column(name = "PESO_KG", precision = 5, scale = 2)
    private BigDecimal pesoKg;

    @Column(name = "TALLA_CM", precision = 5, scale = 2)
    private BigDecimal tallaCm;

    @Column(name = "SATURACION_O2")
    private Integer saturacionO2;

    @Column(name = "FIRMADO_POR")
    private Long firmadoPor;

    @Column(name = "FECHA_FIRMA")
    private LocalDateTime fechaFirma;

    @PrePersist
    public void prePersist() {
        if (fechaAtencion == null) {
            fechaAtencion = LocalDateTime.now();
        }
    }
}
