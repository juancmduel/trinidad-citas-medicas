package com.trinidad.citas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "PERMISO")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString
public class Permiso {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_permiso")
    @SequenceGenerator(name = "seq_permiso", sequenceName = "SEQ_PERMISO", allocationSize = 1)
    @Column(name = "ID_PERMISO")
    private Long idPermiso;

    @NotBlank
    @Size(max = 60)
    @Column(name = "NOMBRE", nullable = false, unique = true, length = 60)
    private String nombre;

    @Size(max = 200)
    @Column(name = "DESCRIPCION", length = 200)
    private String descripcion;

    @NotBlank
    @Size(max = 50)
    @Column(name = "RECURSO", nullable = false, length = 50)
    private String recurso;

    @NotBlank
    @Size(max = 20)
    @Column(name = "ACCION", nullable = false, length = 20)
    private String accion;

    @Column(name = "ACTIVO", nullable = false)
    @Builder.Default
    private Integer activo = 1;
}
