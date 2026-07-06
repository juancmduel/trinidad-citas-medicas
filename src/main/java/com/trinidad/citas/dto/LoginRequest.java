package com.trinidad.citas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Lo que el usuario envía cuando quiere iniciar sesión.
 *
 * Básicamente: "Hola, soy fulano y mi contraseña es esta".
 * El sistema lo valida y si está bien, devuelve un token JWT.
 */
@Data @NoArgsConstructor @AllArgsConstructor
public class LoginRequest {

    /** Nombre de usuario (no confundir con email) */
    @NotBlank @Size(max = 50)
    private String username;

    /** La contraseña en texto plano (luego se compara con el hash BCrypt) */
    @NotBlank @Size(min = 8, max = 100)
    private String password;
}
