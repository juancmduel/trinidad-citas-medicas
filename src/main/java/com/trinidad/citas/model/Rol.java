package com.trinidad.citas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "ROL")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_rol")
    @SequenceGenerator(name = "seq_rol", sequenceName = "SEQ_ROL", allocationSize = 1)
    @Column(name = "ID_ROL")
    private Long idRol;

    @NotBlank
    @Size(max = 30)
    @Column(name = "NOMBRE", nullable = false, unique = true, length = 30)
    private String nombre;

    @Size(max = 200)
    @Column(name = "DESCRIPCION", length = 200)
    private String descripcion;

    @Column(name = "ACTIVO", nullable = false)
    @Builder.Default
    private Integer activo = 1;
}
