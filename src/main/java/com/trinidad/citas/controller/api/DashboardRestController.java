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
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardRestController {

    /**
     * GET /api/v1/dashboard/resumen - Obtiene resumen general del sistema
     */
    @GetMapping("/resumen")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'MEDICO', 'RECEPCIONISTA', 'ENFERMERA')")
    public ResponseEntity<Map<String, Object>> obtenerResumen() {
        Map<String, Object> resumen = new HashMap<>();
        resumen.put("mensaje", "Endpoint de resumen del dashboard");
        resumen.put("estado", "activo");
        return ResponseEntity.ok(resumen);
    }

    /**
     * GET /api/v1/dashboard/estadisticas - Obtiene estadísticas del sistema
     */
    @GetMapping("/estadisticas")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("citas_totales", 0);
        estadisticas.put("pacientes_activos", 0);
        estadisticas.put("medicos_disponibles", 0);
        estadisticas.put("atenciones_completadas", 0);
        return ResponseEntity.ok(estadisticas);
    }

    /**
     * GET /api/v1/dashboard/indicadores - Obtiene KPIs del sistema
     */
    @GetMapping("/indicadores")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    public ResponseEntity<Map<String, Object>> obtenerIndicadores() {
        Map<String, Object> indicadores = new HashMap<>();
        indicadores.put("tasa_ocupacion", "85%");
        indicadores.put("satisfaccion_paciente", "4.5");
        indicadores.put("eficiencia_medica", "92%");
        return ResponseEntity.ok(indicadores);
    }
}
