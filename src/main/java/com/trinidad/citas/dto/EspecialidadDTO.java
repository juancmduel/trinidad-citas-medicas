package com.trinidad.citas.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor
public class EspecialidadDTO {

    private Long idEspecialidad;

    @NotBlank @Size(max = 80)
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$", message = "Nombre: solo se permiten letras y espacios")
    private String nombre;

    @Size(max = 300)
    private String descripcion;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal precioConsulta;

    @NotNull
    @Min(5) @Max(240)
    private Integer duracionMinutos;

    private Integer activo;

    // ── Campos visuales (se llenan en el controller) ──────────────
    private String icono;      // clase Bootstrap Icon, ej: "bi bi-heart-pulse"
    private String color;      // color acento, ej: "#ef4444"
    private String bgColor;    // color de fondo claro, ej: "#fee2e2"
}
