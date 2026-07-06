package com.trinidad.citas.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Token de un solo uso para restablecer contraseñas.
 *
 * Cuando un usuario pide "olvidé mi contraseña", se genera un token UUID
 * y se guarda acá. El usuario recibe un correo con un enlace que contiene
 * este token.
 *
 * El token:
 *  - Es único (no pueden haber dos iguales)
 *  - Expira después de 1 hora
 *  - Solo puede usarse una vez
 *  - Está asociado a un usuario específico
 *
 * Si alguien intenta usar un token expirado o ya usado, se rechaza.
 */
@Entity
@Table(name = "PASSWORD_RESET_TOKEN")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PasswordResetToken {

    /** El token expira después de 1 hora. Tiempo suficiente para cambiar la contraseña. */
    private static final int EXPIRATION_HOURS = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_password_reset")
    @SequenceGenerator(name = "seq_password_reset", sequenceName = "SEQ_PASSWORD_RESET", allocationSize = 1)
    @Column(name = "ID_TOKEN")
    private Long idToken;

    /** El token en sí. Es un UUID aleatorio, imposible de adivinar. */
    @Column(name = "TOKEN", nullable = false, unique = true, length = 255)
    private String token;

    /** El usuario que pidió el restablecimiento */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private Usuario usuario;

    /** Cuándo expira el token. Se calcula automáticamente: creación + 1 hora. */
    @Column(name = "FECHA_EXPIRACION", nullable = false)
    private LocalDateTime fechaExpiracion;

    /** false = aún no se ha usado, true = ya se usó (no sirve más) */
    @Column(name = "USADO", nullable = false)
    @Builder.Default
    private Boolean usado = false;

    /** Cuándo se creó el token. Autorellenado. */
    @Column(name = "FECHA_CREACION", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    /** Antes de guardar, calculamos la fecha de expiración si no tiene */
    @PrePersist
    public void prePersist() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        if (fechaExpiracion == null) {
            fechaExpiracion = fechaCreacion.plusHours(EXPIRATION_HOURS);
        }
    }

    /** ¿Ya expiró? true = ya pasó la hora, no sirve */
    public boolean isExpirado() {
        return LocalDateTime.now().isAfter(fechaExpiracion);
    }
}
