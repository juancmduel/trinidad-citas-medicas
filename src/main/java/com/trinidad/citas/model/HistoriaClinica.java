package com.trinidad.citas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "HISTORIA_CLINICA")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HistoriaClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_historia")
    @SequenceGenerator(name = "seq_historia", sequenceName = "SEQ_HISTORIA_CLINICA", allocationSize = 1)
    @Column(name = "ID_HISTORIA")
    private Long idHistoria;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_PACIENTE", nullable = false, unique = true)
    private Paciente paciente;

    @NotBlank
    @Size(max = 15)
    @Column(name = "NRO_HISTORIA", nullable = false, unique = true, length = 15)
    private String nroHistoria;

    @Column(name = "FECHA_APERTURA", nullable = false)
    @Builder.Default
    private LocalDate fechaApertura = LocalDate.now();

    @Size(max = 1000)
    @Column(name = "OBSERVACIONES", length = 1000)
    private String observaciones;

    @Column(name = "ACTIVO", nullable = false)
    @Builder.Default
    private Integer activo = 1;
}
