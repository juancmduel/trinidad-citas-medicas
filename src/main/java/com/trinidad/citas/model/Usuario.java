package com.trinidad.citas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "USUARIO")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario")
    @SequenceGenerator(name = "seq_usuario", sequenceName = "SEQ_USUARIO", allocationSize = 1)
    @Column(name = "ID_USUARIO")
    private Long idUsuario;

    @NotBlank
    @Size(max = 50)
    @Column(name = "USERNAME", nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank
    @Column(name = "PASSWORD_HASH", nullable = false, length = 255)
    private String passwordHash;

    @Email
    @NotBlank
    @Size(max = 120)
    @Column(name = "EMAIL", nullable = false, unique = true, length = 120)
    private String email;

    @Column(name = "ACTIVO", nullable = false)
    @Builder.Default
    private Integer activo = 1;

    @Column(name = "INTENTOS_FALLIDOS", nullable = false)
    @Builder.Default
    private Integer intentosFallidos = 0;

    @Column(name = "BLOQUEADO", nullable = false)
    @Builder.Default
    private Integer bloqueado = 0;

    @Column(name = "FECHA_CREACION", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "FECHA_ULT_LOGIN")
    private LocalDateTime fechaUltLogin;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "USUARIO_ROL",
        joinColumns = @JoinColumn(name = "ID_USUARIO"),
        inverseJoinColumns = @JoinColumn(name = "ID_ROL")
    )
    @Builder.Default
    @ToString.Exclude
    private Set<Rol> roles = new HashSet<>();

    @PrePersist
    public void prePersist() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
    }

    public boolean isActivo() {
        return activo != null && activo == 1;
    }

    public boolean isBloqueado() {
        return bloqueado != null && bloqueado == 1;
    }
}
