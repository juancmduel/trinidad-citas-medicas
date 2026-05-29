package com.trinidad.citas.controller.web;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Profile({"web", "default"})
@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final ReporteService reporteService;

    @GetMapping("/dashboard/kpis")
    public String dashboardKpis(Model model) {
        model.addAttribute("kpis", reporteService.obtenerKpisDashboard());
        return "dashboard/index";
    }
}
