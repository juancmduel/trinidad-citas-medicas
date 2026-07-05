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
    @Pattern(regexp = "^[0-9]{4,15}$", message = "CMP debe contener solo números")
    private String cmp;

    @NotBlank @Size(max = 80)
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$", message = "Nombres: solo se permiten letras y espacios")
    private String nombres;

    @NotBlank @Size(max = 50)
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$", message = "Apellido paterno: solo se permiten letras y espacios")
    private String apellidoPaterno;

    @Size(max = 50)
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]*$", message = "Apellido materno: solo se permiten letras y espacios")
    private String apellidoMaterno;

    @NotBlank
    @Pattern(regexp = "^[0-9]{8}$", message = "DNI debe tener 8 dígitos")
    private String dni;

    @Size(max = 15)
    @Pattern(regexp = "^[0-9]{7,9}$", message = "Teléfono: debe tener 7 (fijo) o 9 (celular) dígitos")
    private String telefono;

    @Email @Size(max = 120)
    private String email;

    @Size(max = 20)
    private String consultorio;

    private Integer activo;

    // Solo lectura: se completa desde las entidades relacionadas
    private String nombreCompleto;
    private String especialidadNombre;
}
