package com.trinidad.citas.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor
public class OrdenExamenDTO {

    private Long idOrden;

    @NotNull
    private Long idAtencion;

    @NotBlank @Size(max = 30)
    private String tipoExamen;

    @NotBlank @Size(max = 150)
    private String nombreExamen;

    @Size(max = 500)
    private String indicaciones;

    private String estado;

    private LocalDateTime fechaSolicitud;
    private LocalDateTime fechaResultado;
}
