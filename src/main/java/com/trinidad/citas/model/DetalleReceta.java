package com.trinidad.citas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "DETALLE_RECETA")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DetalleReceta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_detalle_receta")
    @SequenceGenerator(name = "seq_detalle_receta", sequenceName = "SEQ_DETALLE_RECETA", allocationSize = 1)
    @Column(name = "ID_DETALLE")
    private Long idDetalle;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_RECETA", nullable = false)
    @ToString.Exclude
    private Receta receta;

    @NotBlank
    @Size(max = 150)
    @Column(name = "NOMBRE_GENERICO", nullable = false, length = 150)
    private String nombreGenerico;

    @Size(max = 150)
    @Column(name = "NOMBRE_COMERCIAL", length = 150)
    private String nombreComercial;

    @Size(max = 80)
    @Column(name = "PRESENTACION", length = 80)
    private String presentacion;

    @NotBlank
    @Size(max = 80)
    @Column(name = "DOSIS", nullable = false, length = 80)
    private String dosis;

    @NotBlank
    @Size(max = 80)
    @Column(name = "FRECUENCIA", nullable = false, length = 80)
    private String frecuencia;

    @NotNull
    @Min(1)
    @Column(name = "DURACION_DIAS", nullable = false)
    private Integer duracionDias;

    @Size(max = 30)
    @Column(name = "VIA_ADMINISTRACION", length = 30)
    private String viaAdministracion;

    @Size(max = 500)
    @Column(name = "INDICACIONES", length = 500)
    private String indicaciones;
}
