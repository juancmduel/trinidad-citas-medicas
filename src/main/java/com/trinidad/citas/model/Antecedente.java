package com.trinidad.citas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "ANTECEDENTE")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Antecedente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_antecedente")
    @SequenceGenerator(name = "seq_antecedente", sequenceName = "SEQ_ANTECEDENTE", allocationSize = 1)
    @Column(name = "ID_ANTECEDENTE")
    private Long idAntecedente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_HISTORIA", nullable = false)
    private HistoriaClinica historia;

    @NotBlank
    @Size(max = 30)
    @Column(name = "TIPO", nullable = false, length = 30)
    private String tipo;

    @NotBlank
    @Size(max = 500)
    @Column(name = "DESCRIPCION", nullable = false, length = 500)
    private String descripcion;

    @Size(max = 10)
    @Column(name = "CODIGO_CIE10", length = 10)
    private String codigoCie10;

    @Column(name = "FECHA_REGISTRO", nullable = false)
    @Builder.Default
    private LocalDate fechaRegistro = LocalDate.now();

    @Column(name = "ACTIVO", nullable = false)
    @Builder.Default
    private Integer activo = 1;

    @Size(max = 500)
    @Column(name = "OBSERVACIONES", length = 500)
    private String observaciones;
}
