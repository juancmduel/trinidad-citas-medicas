package com.trinidad.citas.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class PacienteDTO {

    private Long idPaciente;

    @NotBlank
    @Pattern(regexp = "^[0-9]{8}$", message = "DNI debe tener 8 digitos")
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

    @NotNull(message = "Debe seleccionar un sexo")
    @Pattern(regexp = "^[MF]$", message = "El sexo debe ser Masculino (M) o Femenino (F)")
    private String sexo;

    @Size(max = 15)
    @Pattern(regexp = "^[0-9]{7,9}$", message = "Teléfono: debe tener 7 (fijo) o 9 (celular) dígitos")
    private String telefono;

    @Email
    @Size(max = 120)
    private String email;

    @Size(max = 200)
    private String direccion;

    @Size(max = 80)
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]*$", message = "Distrito: solo se permiten letras y espacios")
    private String distrito;

    @Size(max = 5)
    @Pattern(regexp = "^(A|B|AB|O)[+-]$", message = "Tipo de sangre inválido (ej: A+, O-, AB+)")
    private String tipoSangre;

    @Size(max = 500)
    private String alergias;

    @Size(max = 100)
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]*$", message = "Ocupación: solo se permiten letras y espacios")
    private String ocupacion;

    @Size(max = 50)
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]*$", message = "Seguro: solo se permiten letras y espacios")
    private String seguroSalud;

    @Size(max = 100)
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]*$", message = "Nombre de contacto: solo se permiten letras y espacios")
    private String contactoEmergenciaNombre;

    @Size(max = 15)
    @Pattern(regexp = "^[0-9]{7,9}$", message = "Teléfono de emergencia: debe tener 7 (fijo) o 9 (celular) dígitos")
    private String contactoEmergenciaTelefono;

    private Integer activo;
}
