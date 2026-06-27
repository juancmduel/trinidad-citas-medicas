package com.trinidad.citas.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "INTENTO_LOGIN")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class IntentoLogin {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_intento_login")
    @SequenceGenerator(name = "seq_intento_login", sequenceName = "SEQ_INTENTO_LOGIN", allocationSize = 1)
    @Column(name = "ID_INTENTO")
    private Long idIntento;

    @Column(name = "USERNAME", nullable = false, length = 50)
    private String username;

    @Column(name = "EXITOSO", nullable = false)
    @Builder.Default
    private Integer exitoso = 0;

    @Column(name = "IP_ORIGEN", length = 45)
    private String ipOrigen;

    @Column(name = "MENSAJE_ERROR", length = 255)
    private String mensajeError;

    @Column(name = "FECHA_HORA", nullable = false, updatable = false)
    private LocalDateTime fechaHora;

    @PrePersist
    public void prePersist() {
        if (fechaHora == null) {
            fechaHora = LocalDateTime.now();
        }
    }
}
