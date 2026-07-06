package com.trinidad.citas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * La cita médica. El corazón del sistema.
 *
 * Una cita es básicamente "el paciente X va a ver al doctor Y
 * el día Z a las W horas por el motivo V".
 *
 * Tiene varias vidas: nace como PROGRAMADA, pasa a CONFIRMADA cuando
 * el paciente confirma, y luego puede ir a EN_ATENCION, ATENDIDA,
 * CANCELADA, NO_ASISTIO… como la vida misma.
 *
 * La constraint UQ_CITA_MEDICO_SLOT evita que dos pacientes
 * tengan cita con el mismo médico al mismo tiempo.
 * Eso sería un caos.
 */
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

    /** El paciente que viene a consulta. No hay cita sin paciente. */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_PACIENTE", nullable = false)
    private Paciente paciente;

    /** El médico que atiende. Tampoco hay cita sin médico (obvio). */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_MEDICO", nullable = false)
    private Medico medico;

    /** Especialidad: Medicina General, Pediatría, etc. */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_ESPECIALIDAD", nullable = false)
    private Especialidad especialidad;

    /** El día de la cita. Sin hora, solo la fecha. */
    @NotNull
    @Column(name = "FECHA_CITA", nullable = false)
    private LocalDate fechaCita;

    /** A qué hora empieza. Formato HH:mm, ej: "14:30" */
    @NotBlank
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "La hora debe tener formato HH:mm (ej: 14:30)")
    @Column(name = "HORA_INICIO", nullable = false, length = 5)
    private String horaInicio;

    /** A qué hora termina. La duración la define la especialidad. */
    @NotBlank
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "La hora debe tener formato HH:mm (ej: 14:30)")
    @Column(name = "HORA_FIN", nullable = false, length = 5)
    private String horaFin;

    /**
     * Estado actual de la cita.
     * Va cambiando con el tiempo: PROGRAMADA → CONFIRMADA → EN_ATENCION → ATENDIDA
     * O si algo sale mal: CANCELADA, NO_ASISTIO, REPROGRAMADA
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false, length = 20)
    @Builder.Default
    private EstadoCita estado = EstadoCita.PROGRAMADA;

    /** ¿Por qué viene el paciente? Duele aquí, control, etc. */
    @Size(max = 300)
    @Column(name = "MOTIVO_CONSULTA", length = 300)
    private String motivoConsulta;

    /**
     * Por dónde se reservó: WEB, TELEFONO, PRESENCIAL, APP.
     * Sirve para saber qué canales están funcionando mejor.
     */
    @Size(max = 20)
    @Column(name = "CANAL_RESERVA", nullable = false, length = 20)
    @Builder.Default
    private String canalReserva = "WEB";

    /** Código QR que se envía por correo. El paciente lo muestra en recepción. */
    @Size(max = 100)
    @Column(name = "CODIGO_QR", length = 100)
    private String codigoQr;

    /** Número de turno asignado. Para saber quién sigue. */
    @Column(name = "NUMERO_TURNO")
    private Integer numeroTurno;

    /** Cuándo se registró la cita. Autorellenado. */
    @Column(name = "FECHA_REGISTRO", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    /**
     * Cuándo el paciente hizo check-in en recepción.
     * Si pasa mucho tiempo y no hay check-in, se marca como NO_ASISTIO.
     */
    @Column(name = "FECHA_CHECKIN")
    private LocalDateTime fechaCheckin;

    /** Notas internas sobre la cita (visible solo para el personal). */
    @Size(max = 500)
    @Column(name = "OBSERVACIONES", length = 500)
    private String observaciones;

    /** Justo antes de guardar, ponemos la fecha actual si no tiene */
    @PrePersist
    public void prePersist() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
    }
}
