package com.trinidad.citas.controller.api;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.dto.KpiDashboardDTO;
import com.trinidad.citas.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("api")
@RestController
@RequestMapping("/api/v1/reportes")
@RequiredArgsConstructor
public class ReporteRestController {

    private final ReporteService reporteService;

    @GetMapping("/kpis")
    public KpiDashboardDTO kpis() {
        return reporteService.obtenerKpisDashboard();
    }
}
