package com.trinidad.citas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "PACIENTE")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_paciente")
    @SequenceGenerator(name = "seq_paciente", sequenceName = "SEQ_PACIENTE", allocationSize = 1)
    @Column(name = "ID_PACIENTE")
    private Long idPaciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO")
    @ToString.Exclude
    private Usuario usuario;

    @NotBlank
    @Pattern(regexp = "^[0-9]{8}$", message = "El DNI debe tener exactamente 8 digitos")
    @Column(name = "DNI", nullable = false, unique = true, length = 8)
    private String dni;

    @NotBlank
    @Size(max = 80)
    @Column(name = "NOMBRES", nullable = false, length = 80)
    private String nombres;

    @NotBlank
    @Size(max = 50)
    @Column(name = "APELLIDO_PATERNO", nullable = false, length = 50)
    private String apellidoPaterno;

    @Size(max = 50)
    @Column(name = "APELLIDO_MATERNO", length = 50)
    private String apellidoMaterno;

    @NotNull
    @Past
    @Column(name = "FECHA_NACIMIENTO", nullable = false)
    private LocalDate fechaNacimiento;

    @NotNull
    @Pattern(regexp = "^[MF]$", message = "El sexo debe ser M o F")
    @Column(name = "SEXO", nullable = false, length = 1)
    private String sexo;

    @Size(max = 15)
    @Column(name = "TELEFONO", length = 15)
    private String telefono;

    @Email
    @Size(max = 120)
    @Column(name = "EMAIL", length = 120)
    private String email;

    @Size(max = 200)
    @Column(name = "DIRECCION", length = 200)
    private String direccion;

    @Size(max = 80)
    @Column(name = "DISTRITO", length = 80)
    private String distrito;

    @Size(max = 5)
    @Column(name = "TIPO_SANGRE", length = 5)
    private String tipoSangre;

    @Size(max = 500)
    @Column(name = "ALERGIAS", length = 500)
    private String alergias;

    @Column(name = "ACTIVO", nullable = false)
    @Builder.Default
    private Integer activo = 1;

    @Column(name = "FECHA_REGISTRO", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @PrePersist
    public void prePersist() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
    }

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
