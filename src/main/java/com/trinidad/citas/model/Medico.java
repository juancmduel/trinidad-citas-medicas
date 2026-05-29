package com.trinidad.citas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "MEDICO")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_medico")
    @SequenceGenerator(name = "seq_medico", sequenceName = "SEQ_MEDICO", allocationSize = 1)
    @Column(name = "ID_MEDICO")
    private Long idMedico;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    @ToString.Exclude
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_ESPECIALIDAD", nullable = false)
    private Especialidad especialidad;

    @NotBlank
    @Size(max = 15)
    @Column(name = "CMP", nullable = false, unique = true, length = 15)
    private String cmp;

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

    @NotBlank
    @Pattern(regexp = "^[0-9]{8}$")
    @Column(name = "DNI", nullable = false, unique = true, length = 8)
    private String dni;

    @Size(max = 15)
    @Column(name = "TELEFONO", length = 15)
    private String telefono;

    @Email
    @Size(max = 120)
    @Column(name = "EMAIL", length = 120)
    private String email;

    @Size(max = 20)
    @Column(name = "CONSULTORIO", length = 20)
    private String consultorio;

    @Column(name = "ACTIVO", nullable = false)
    @Builder.Default
    private Integer activo = 1;

    public String getNombreCompleto() {
        StringBuilder sb = new StringBuilder("Dr(a). ");
        sb.append(apellidoPaterno);
        if (apellidoMaterno != null && !apellidoMaterno.isBlank()) {
            sb.append(" ").append(apellidoMaterno);
        }
        sb.append(", ").append(nombres);
        return sb.toString();
    }
}
