package com.trinidad.citas.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class RegistroPacienteDTO {

    @NotBlank @Size(min = 3, max = 50)
    private String username;

    @NotBlank @Size(min = 8, max = 100)
    private String password;

    @NotBlank @Email @Size(max = 120)
    private String email;

    @NotBlank @Pattern(regexp = "^[0-9]{8}$", message = "DNI debe tener 8 digitos")
    private String dni;

    @NotBlank @Size(max = 80)
    private String nombres;

    @NotBlank @Size(max = 50)
    private String apellidoPaterno;

    @Size(max = 50)
    private String apellidoMaterno;

    @NotNull @Past
    private LocalDate fechaNacimiento;

    @NotNull @Pattern(regexp = "^[MF]$")
    private String sexo;

    @Size(max = 15)
    private String telefono;

    @Size(max = 200)
    private String direccion;

    @Size(max = 80)
    private String distrito;
}
