package com.trinidad.citas.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "PASSWORD_RESET_TOKEN")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PasswordResetToken {

    private static final int EXPIRATION_HOURS = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_password_reset")
    @SequenceGenerator(name = "seq_password_reset", sequenceName = "SEQ_PASSWORD_RESET", allocationSize = 1)
    @Column(name = "ID_TOKEN")
    private Long idToken;

    @Column(name = "TOKEN", nullable = false, unique = true, length = 255)
    private String token;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private Usuario usuario;

    @Column(name = "FECHA_EXPIRACION", nullable = false)
    private LocalDateTime fechaExpiracion;

    @Column(name = "USADO", nullable = false)
    @Builder.Default
    private Boolean usado = false;

    @Column(name = "FECHA_CREACION", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    public void prePersist() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        if (fechaExpiracion == null) {
            fechaExpiracion = fechaCreacion.plusHours(EXPIRATION_HOURS);
        }
    }

    public boolean isExpirado() {
        return LocalDateTime.now().isAfter(fechaExpiracion);
    }
}
