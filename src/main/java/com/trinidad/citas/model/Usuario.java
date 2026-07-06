package com.trinidad.citas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Un usuario del sistema. Puede ser admin, médico, recepcionista, paciente…
 * básicamente cualquiera que entre al sistema con un login y contraseña.
 *
 * La contraseña nunca se guarda en texto plano, siempre encriptada con BCrypt.
 * Si alguien mete la contraseña 5 veces mal en 30 minutos, se bloquea solo.
 */
@Entity
@Table(name = "USUARIO")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario")
    @SequenceGenerator(name = "seq_usuario", sequenceName = "SEQ_USUARIO", allocationSize = 1)
    @Column(name = "ID_USUARIO")
    private Long idUsuario;

    /** Nombre de usuario único. El que usan para iniciar sesión. */
    @NotBlank
    @Size(max = 50)
    @Column(name = "USERNAME", nullable = false, unique = true, length = 50)
    private String username;

    /** Hash BCrypt de la contraseña. Ojo: esto no es la contraseña en texto plano. */
    @NotBlank
    @Column(name = "PASSWORD_HASH", nullable = false, length = 255)
    private String passwordHash;

    /** Correo electrónico. También único, se usa para recuperar contraseña. */
    @Email
    @NotBlank
    @Size(max = 120)
    @Column(name = "EMAIL", nullable = false, unique = true, length = 120)
    private String email;

    /** 1 = activo, 0 = desactivado (eliminación lógica). */
    @Column(name = "ACTIVO", nullable = false)
    @Builder.Default
    private Integer activo = 1;

    /**
     * Cuántas veces ha puesto mal la contraseña.
     * Si llega a 5 en menos de 30 min, se bloquea la cuenta.
     * Así evitamos que bots intenten adivinar contraseñas.
     */
    @Column(name = "INTENTOS_FALLIDOS", nullable = false)
    @Builder.Default
    private Integer intentosFallidos = 0;

    /**
     * 1 = bloqueado (por muchos intentos fallidos o por un admin),
     * 0 = normal.
     * Cuando está bloqueado, no puede iniciar sesión aunque ponga bien la contraseña.
     */
    @Column(name = "BLOQUEADO", nullable = false)
    @Builder.Default
    private Integer bloqueado = 0;

    /** Cuándo se creó la cuenta. Autorellenado, no se puede modificar. */
    @Column(name = "FECHA_CREACION", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    /** Última vez que inició sesión. Null si nunca ha entrado. */
    @Column(name = "FECHA_ULT_LOGIN")
    private LocalDateTime fechaUltLogin;

    /**
     * Los roles que tiene este usuario.
     * Un usuario puede tener varios roles (ej: ser médico y admin a la vez).
     * La relación es muchos-a-muchos porque un rol también lo tienen muchos usuarios.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "USUARIO_ROL",
        joinColumns = @JoinColumn(name = "ID_USUARIO"),
        inverseJoinColumns = @JoinColumn(name = "ID_ROL")
    )
    @Builder.Default
    @ToString.Exclude
    private Set<Rol> roles = new HashSet<>();

    /** Antes de guardar, si no tiene fecha de creación, le ponemos la de ahora */
    @PrePersist
    public void prePersist() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
    }

    /** Método helper: ¿está activo? true = sí, puede usar el sistema */
    public boolean isActivo() {
        return activo != null && activo == 1;
    }

    /** Método helper: ¿está bloqueado? true = no puede iniciar sesión */
    public boolean isBloqueado() {
        return bloqueado != null && bloqueado == 1;
    }
}
