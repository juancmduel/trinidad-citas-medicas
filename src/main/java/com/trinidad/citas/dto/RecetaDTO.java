package com.trinidad.citas.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class RecetaDTO {

    private Long idReceta;

    @NotNull
    private Long idAtencion;

    @NotBlank @Size(max = 20)
    private String nroReceta;

    private LocalDateTime fechaEmision;

    @Size(max = 500)
    private String observaciones;

    private List<DetalleRecetaDTO> detalles;
}
