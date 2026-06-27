package com.trinidad.citas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TRIAJE")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Triaje {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_triaje")
    @SequenceGenerator(name = "seq_triaje", sequenceName = "SEQ_TRIAJE", allocationSize = 1)
    @Column(name = "ID_TRIAJE")
    private Long idTriaje;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CITA", nullable = false, unique = true)
    private Cita cita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ENFERMERA")
    private Usuario enfermera;

    @Column(name = "FECHA_TRIAJE")
    private LocalDateTime fechaTriaje;

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

    @Column(name = "GLUCOSA", precision = 5, scale = 1)
    private BigDecimal glucosa;

    @Size(max = 500)
    @Column(name = "SINTOMAS", length = 500)
    private String sintomas;

    @Size(max = 500)
    @Column(name = "ALERGIAS_REPORTADAS", length = 500)
    private String alergiasReportadas;

    @Size(max = 500)
    @Column(name = "MEDICACION_ACTUAL", length = 500)
    private String medicacionActual;

    @Size(max = 20)
    @Column(name = "NIVEL_URGENCIA", length = 20)
    private String nivelUrgencia;

    @Size(max = 1000)
    @Column(name = "OBSERVACIONES", length = 1000)
    private String observaciones;

    @PrePersist
    public void prePersist() {
        if (fechaTriaje == null) {
            fechaTriaje = LocalDateTime.now();
        }
    }
}
