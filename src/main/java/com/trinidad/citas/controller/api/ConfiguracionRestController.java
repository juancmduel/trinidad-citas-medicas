package com.trinidad.citas.controller.api;

import org.springframework.context.annotation.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@Profile("api")
@RestController
@RequestMapping("/api/v1/configuracion")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class ConfiguracionRestController {

    /**
     * GET /api/v1/configuracion - Obtiene todas las configuraciones
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerConfiguraciones() {
        Map<String, Object> configuraciones = new HashMap<>();
        configuraciones.put("nombre_clinica", "Trinidad Citas Médicas");
        configuraciones.put("horario_apertura", "08:00");
        configuraciones.put("horario_cierre", "18:00");
        configuraciones.put("duracion_cita_minutos", 30);
        configuraciones.put("email_notificaciones", "contacto@trinitydcitasmedicas.com");
        return ResponseEntity.ok(configuraciones);
    }

    /**
     * GET /api/v1/configuracion/:key - Obtiene configuración específica
     */
    @GetMapping("/{key}")
    public ResponseEntity<Map<String, Object>> obtenerConfiguracion(@PathVariable String key) {
        Map<String, Object> config = new HashMap<>();
        config.put("key", key);
        config.put("valor", "Valor de: " + key);
        return ResponseEntity.ok(config);
    }

    /**
     * POST /api/v1/configuracion - Crea nueva configuración
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearConfiguracion(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Configuración creada correctamente");
        response.put("data", payload);
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/v1/configuracion/:key - Actualiza configuración
     */
    @PutMapping("/{key}")
    public ResponseEntity<Map<String, Object>> actualizarConfiguracion(
            @PathVariable String key,
            @RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Configuración actualizada correctamente");
        response.put("key", key);
        response.put("data", payload);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/v1/configuracion/:key - Elimina configuración
     */
    @DeleteMapping("/{key}")
    public ResponseEntity<Map<String, Object>> eliminarConfiguracion(@PathVariable String key) {
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Configuración eliminada correctamente");
        response.put("key", key);
        return ResponseEntity.ok(response);
    }
}
