package com.trinidad.citas.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class MedicoDTO {

    private Long idMedico;

    @NotNull
    private Long idEspecialidad;

    @NotNull
    private Long idUsuario;

    @NotBlank @Size(max = 15)
    private String cmp;

    @NotBlank @Size(max = 80)
    private String nombres;

    @NotBlank @Size(max = 50)
    private String apellidoPaterno;

    @Size(max = 50)
    private String apellidoMaterno;

    @NotBlank
    @Pattern(regexp = "^[0-9]{8}$", message = "DNI debe tener 8 dígitos")
    private String dni;

    @Size(max = 15)
    private String telefono;

    @Email @Size(max = 120)
    private String email;

    @Size(max = 20)
    private String consultorio;

    private Integer activo;

    // Solo respuesta
    private String nombreCompleto;
    private String especialidadNombre;
}
