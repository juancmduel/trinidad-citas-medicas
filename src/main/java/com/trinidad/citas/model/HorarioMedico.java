package com.trinidad.citas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "HORARIO_MEDICO")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HorarioMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_horario")
    @SequenceGenerator(name = "seq_horario", sequenceName = "SEQ_HORARIO_MEDICO", allocationSize = 1)
    @Column(name = "ID_HORARIO")
    private Long idHorario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_MEDICO", nullable = false)
    @ToString.Exclude
    private Medico medico;

    @NotNull
    @Min(1) @Max(7)
    @Column(name = "DIA_SEMANA", nullable = false)
    private Integer diaSemana;

    @NotBlank
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$")
    @Column(name = "HORA_INICIO", nullable = false, length = 5)
    private String horaInicio;

    @NotBlank
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$")
    @Column(name = "HORA_FIN", nullable = false, length = 5)
    private String horaFin;

    @Column(name = "ACTIVO", nullable = false)
    @Builder.Default
    private Integer activo = 1;
}
