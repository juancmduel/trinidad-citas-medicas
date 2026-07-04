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
    private String nombres;

    @NotBlank @Size(max = 50)
    private String apellidoPaterno;

    @Size(max = 50)
    private String apellidoMaterno;

    @NotNull @Past
    private LocalDate fechaNacimiento;

    @NotNull(message = "Debe seleccionar un sexo")
    @Pattern(regexp = "^[MF]$", message = "El sexo debe ser Masculino (M) o Femenino (F)")
    private String sexo;

    @Size(max = 15)
    private String telefono;

    @Email
    @Size(max = 120)
    private String email;

    @Size(max = 200)
    private String direccion;

    @Size(max = 80)
    private String distrito;

    @Size(max = 5)
    private String tipoSangre;

    @Size(max = 500)
    private String alergias;

    @Size(max = 100)
    private String ocupacion;

    @Size(max = 50)
    private String seguroSalud;

    @Size(max = 100)
    private String contactoEmergenciaNombre;

    @Size(max = 15)
    private String contactoEmergenciaTelefono;
}
