package com.trinidad.citas.controller.web;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Profile("web")
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final DashboardService dashboardService;

    @GetMapping({"/", "/index"})
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("kpis", dashboardService.obtenerKpisDashboard());
        return "dashboard/index";
    }
}
