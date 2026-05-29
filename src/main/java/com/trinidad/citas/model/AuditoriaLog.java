package com.trinidad.citas.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "AUDITORIA_LOG")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString
public class AuditoriaLog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_auditoria")
    @SequenceGenerator(name = "seq_auditoria", sequenceName = "SEQ_AUDITORIA_LOG", allocationSize = 1)
    @Column(name = "ID_LOG")
    private Long idLog;

    @Column(name = "ID_USUARIO")
    private Long idUsuario;

    @Column(name = "USERNAME", length = 50)
    private String username;

    @Column(name = "ACCION", nullable = false, length = 30)
    private String accion;

    @Column(name = "ENTIDAD", length = 50)
    private String entidad;

    @Column(name = "ID_ENTIDAD", length = 50)
    private String idEntidad;

    @Column(name = "DETALLE", length = 2000)
    private String detalle;

    @Column(name = "IP_ORIGEN", length = 45)
    private String ipOrigen;

    @Column(name = "FECHA_HORA", nullable = false, updatable = false)
    private LocalDateTime fechaHora;

    @PrePersist
    public void prePersist() {
        if (fechaHora == null) {
            fechaHora = LocalDateTime.now();
        }
    }
}
