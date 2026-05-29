package com.trinidad.citas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ESPECIALIDAD")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_especialidad")
    @SequenceGenerator(name = "seq_especialidad", sequenceName = "SEQ_ESPECIALIDAD", allocationSize = 1)
    @Column(name = "ID_ESPECIALIDAD")
    private Long idEspecialidad;

    @NotBlank
    @Size(max = 80)
    @Column(name = "NOMBRE", nullable = false, unique = true, length = 80)
    private String nombre;

    @Size(max = 300)
    @Column(name = "DESCRIPCION", length = 300)
    private String descripcion;

    @NotNull
    @DecimalMin(value = "0.0")
    @Column(name = "PRECIO_CONSULTA", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioConsulta;

    @NotNull
    @Min(5) @Max(240)
    @Column(name = "DURACION_MINUTOS", nullable = false)
    @Builder.Default
    private Integer duracionMinutos = 20;

    @Column(name = "ACTIVO", nullable = false)
    @Builder.Default
    private Integer activo = 1;
}
