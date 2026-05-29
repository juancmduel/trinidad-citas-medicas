package com.trinidad.citas.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class DetalleRecetaDTO {

    private Long idDetalle;

    @NotNull
    private Long idReceta;

    @NotBlank @Size(max = 150)
    private String nombreGenerico;

    @Size(max = 150)
    private String nombreComercial;

    @Size(max = 80)
    private String presentacion;

    @NotBlank @Size(max = 80)
    private String dosis;

    @NotBlank @Size(max = 80)
    private String frecuencia;

    @NotNull @Min(1)
    private Integer duracionDias;

    @Size(max = 30)
    private String viaAdministracion;

    @Size(max = 500)
    private String indicaciones;
}
