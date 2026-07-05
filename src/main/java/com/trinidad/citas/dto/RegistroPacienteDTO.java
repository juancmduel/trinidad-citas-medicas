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
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$",
             message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial")
    private String password;

    @NotBlank @Email @Size(max = 120)
    private String email;

    @NotBlank @Pattern(regexp = "^[0-9]{8}$", message = "DNI debe tener 8 digitos")
    private String dni;

    @NotBlank @Size(max = 80)
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$", message = "Nombres: solo se permiten letras y espacios")
    private String nombres;

    @NotBlank @Size(max = 50)
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$", message = "Apellido paterno: solo se permiten letras y espacios")
    private String apellidoPaterno;

    @Size(max = 50)
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]*$", message = "Apellido materno: solo se permiten letras y espacios")
    private String apellidoMaterno;

    @NotNull @Past
    private LocalDate fechaNacimiento;

    @NotNull @Pattern(regexp = "^[MF]$")
    private String sexo;

    @Size(max = 15)
    @Pattern(regexp = "^[0-9]{7,9}$", message = "Teléfono: debe tener 7 (fijo) o 9 (celular) dígitos")
    private String telefono;

    @Size(max = 200)
    private String direccion;

    @Size(max = 80)
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]*$", message = "Distrito: solo se permiten letras y espacios")
    private String distrito;
}
