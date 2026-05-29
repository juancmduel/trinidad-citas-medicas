package com.trinidad.citas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "RECETA")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_receta")
    @SequenceGenerator(name = "seq_receta", sequenceName = "SEQ_RECETA", allocationSize = 1)
    @Column(name = "ID_RECETA")
    private Long idReceta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_ATENCION", nullable = false)
    @ToString.Exclude
    private Atencion atencion;

    @NotBlank
    @Size(max = 20)
    @Column(name = "NRO_RECETA", nullable = false, unique = true, length = 20)
    private String nroReceta;

    @Column(name = "FECHA_EMISION", nullable = false)
    private LocalDateTime fechaEmision;

    @Size(max = 500)
    @Column(name = "OBSERVACIONES", length = 500)
    private String observaciones;

    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<DetalleReceta> detalles = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (fechaEmision == null) {
            fechaEmision = LocalDateTime.now();
        }
    }
}
