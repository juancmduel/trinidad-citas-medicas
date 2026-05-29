package com.trinidad.citas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "DIAGNOSTICO_CIE10")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString
public class DiagnosticoCie10 {

    @Id
    @NotBlank
    @Size(max = 10)
    @Column(name = "CODIGO", length = 10)
    private String codigo;

    @NotBlank
    @Size(max = 300)
    @Column(name = "DESCRIPCION", nullable = false, length = 300)
    private String descripcion;

    @Size(max = 80)
    @Column(name = "CAPITULO", length = 80)
    private String capitulo;

    @Column(name = "ACTIVO", nullable = false)
    @Builder.Default
    private Integer activo = 1;
}
