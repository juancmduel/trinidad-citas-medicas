package com.trinidad.citas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "CITA",
       uniqueConstraints = @UniqueConstraint(
           name = "UQ_CITA_MEDICO_SLOT",
           columnNames = {"ID_MEDICO", "FECHA_CITA", "HORA_INICIO"}
       ))
       
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cita")
    @SequenceGenerator(name = "seq_cita", sequenceName = "SEQ_CITA", allocationSize = 1)
    @Column(name = "ID_CITA")
    private Long idCita;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_PACIENTE", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_MEDICO", nullable = false)
    private Medico medico;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_ESPECIALIDAD", nullable = false)
    private Especialidad especialidad;

    @NotNull
    @Column(name = "FECHA_CITA", nullable = false)
    private LocalDate fechaCita;

    @NotBlank
    @Column(name = "HORA_INICIO", nullable = false, length = 5)
    private String horaInicio;

    @NotBlank
    @Column(name = "HORA_FIN", nullable = false, length = 5)
    private String horaFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false, length = 20)
    @Builder.Default
    private EstadoCita estado = EstadoCita.PROGRAMADA;

    @Size(max = 300)
    @Column(name = "MOTIVO_CONSULTA", length = 300)
    private String motivoConsulta;

    @Size(max = 20)
    @Column(name = "CANAL_RESERVA", nullable = false, length = 20)
    @Builder.Default
    private String canalReserva = "WEB";

    @Size(max = 100)
    @Column(name = "CODIGO_QR", length = 100)
    private String codigoQr;

    @Column(name = "NUMERO_TURNO")
    private Integer numeroTurno;

    @Column(name = "FECHA_REGISTRO", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "FECHA_CHECKIN")
    private LocalDateTime fechaCheckin;

    @Size(max = 500)
    @Column(name = "OBSERVACIONES", length = 500)
    private String observaciones;

    @PrePersist
    public void prePersist() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
    }
}
