package com.trinidad.citas.controller.api;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.dto.LoginRequest;
import com.trinidad.citas.dto.LoginResponse;
import com.trinidad.citas.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para autenticación.
 *
 * Aquí es donde la app móvil o el frontend envían el usuario y contraseña
 * para recibir un token JWT. Una vez logueado, ese token se usa en todas
 * las demás llamadas a la API.
 *
 * Ejemplo de uso:
 *   POST /api/auth/login
 *   { "username": "admin", "password": "Trinidad2026" }
 *
 *   Respuesta:
 *   { "token": "eyJhbGciOiJIUzI1NiJ9...", "tipo": "Bearer", ... }
 */
@Profile("api")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthService authService;

    /** Inicia sesión y devuelve un token JWT si las credenciales son correctas */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
