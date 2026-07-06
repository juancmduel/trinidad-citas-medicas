package com.trinidad.citas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * El paciente. La razón de ser del sistema.
 *
 * Aquí guardamos todos los datos de la persona que viene a atenderse.
 * Desde el DNI hasta su contacto de emergencia, pasando por alergias,
 * tipo de sangre, seguro… lo necesario para que el médico tenga contexto.
 *
 * Un paciente puede tener una cuenta de usuario (para agendar citas online)
 * o puede ser registrado directamente por recepción.
 */
@Entity
@Table(name = "PACIENTE")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_paciente")
    @SequenceGenerator(name = "seq_paciente", sequenceName = "SEQ_PACIENTE", allocationSize = 1)
    @Column(name = "ID_PACIENTE")
    private Long idPaciente;

    /**
     * Cuenta de usuario asociada (opcional).
     * Si el paciente se registró online, tiene usuario.
     * Si lo registró recepción directamente, puede ser null.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO")
    @ToString.Exclude
    private Usuario usuario;

    /** DNI: 8 dígitos exactos. Único. */
    @NotBlank
    @Pattern(regexp = "^[0-9]{8}$", message = "El DNI debe tener exactamente 8 digitos")
    @Column(name = "DNI", nullable = false, unique = true, length = 8)
    private String dni;

    /** Nombres del paciente (ej: "Juan Carlos") */
    @NotBlank
    @Size(max = 80)
    @Column(name = "NOMBRES", nullable = false, length = 80)
    private String nombres;

    /** Apellido paterno (ej: "Pérez") */
    @NotBlank
    @Size(max = 50)
    @Column(name = "APELLIDO_PATERNO", nullable = false, length = 50)
    private String apellidoPaterno;

    /** Apellido materno (opcional, ej: "Quispe") */
    @Size(max = 50)
    @Column(name = "APELLIDO_MATERNO", length = 50)
    private String apellidoMaterno;

    /** Fecha de nacimiento. Debe ser una fecha pasada (obvio). */
    @NotNull
    @Past
    @Column(name = "FECHA_NACIMIENTO", nullable = false)
    private LocalDate fechaNacimiento;

    /** Sexo: M = Masculino, F = Femenino */
    @NotNull
    @Pattern(regexp = "^[MF]$", message = "El sexo debe ser M o F")
    @Column(name = "SEXO", nullable = false, length = 1)
    private String sexo;

    /** Teléfono de contacto */
    @Size(max = 15)
    @Column(name = "TELEFONO", length = 15)
    private String telefono;

    /** Correo electrónico para enviarle confirmaciones y recordatorios */
    @Email
    @Size(max = 120)
    @Column(name = "EMAIL", length = 120)
    private String email;

    /** Dirección de domicilio */
    @Size(max = 200)
    @Column(name = "DIRECCION", length = 200)
    private String direccion;

    /** Distrito donde vive */
    @Size(max = 80)
    @Column(name = "DISTRITO", length = 80)
    private String distrito;

    /** Tipo de sangre: O+, A-, AB+, etc. */
    @Size(max = 5)
    @Column(name = "TIPO_SANGRE", length = 5)
    private String tipoSangre;

    /** Alergias conocidas (texto libre). Importante para el médico. */
    @Size(max = 500)
    @Column(name = "ALERGIAS", length = 500)
    private String alergias;

    /** Ocupación del paciente */
    @Size(max = 100)
    @Column(name = "OCUPACION", length = 100)
    private String ocupacion;

    /** Seguro de salud: ESSALUD, SIS, particular, etc. */
    @Size(max = 50)
    @Column(name = "SEGURO_SALUD", length = 50)
    private String seguroSalud;

    /** En caso de emergencia, ¿a quién llamamos? */
    @Size(max = 100)
    @Column(name = "CONTACTO_EMERGENCIA_NOMBRE", length = 100)
    private String contactoEmergenciaNombre;

    /** Teléfono del contacto de emergencia */
    @Size(max = 15)
    @Column(name = "CONTACTO_EMERGENCIA_TELEFONO", length = 15)
    private String contactoEmergenciaTelefono;

    /** 1 = activo, 0 = eliminado lógico */
    @Column(name = "ACTIVO", nullable = false)
    @Builder.Default
    private Integer activo = 1;

    /** Cuándo se registró. Se autoasigna. */
    @Column(name = "FECHA_REGISTRO", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    /** Autorellenado: pone la fecha actual si no tiene */
    @PrePersist
    public void prePersist() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
    }

    /**
     * Devuelve el nombre completo en formato: ApellidoPaterno ApellidoMaterno, Nombres
     * Ej: "Pérez Quispe, Juan Carlos"
     */
    public String getNombreCompleto() {
        StringBuilder sb = new StringBuilder();
        sb.append(apellidoPaterno);
        if (apellidoMaterno != null && !apellidoMaterno.isBlank()) {
            sb.append(" ").append(apellidoMaterno);
        }
        sb.append(", ").append(nombres);
        return sb.toString();
    }
}
