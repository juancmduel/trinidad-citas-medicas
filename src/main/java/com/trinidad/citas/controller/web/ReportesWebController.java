package com.trinidad.citas.controller.web;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.service.ReporteService;
import com.trinidad.citas.repository.CitaRepository;
import com.trinidad.citas.repository.PacienteRepository;
import com.trinidad.citas.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Profile({"web", "default"})
@Controller
@RequestMapping("/reportes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ReportesWebController {

    private final ReporteService reporteService;
    private final CitaRepository citaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;

    @GetMapping
    public String dashboard(Model model) {
        var kpis = reporteService.obtenerKpisDashboard();
        model.addAttribute("totalCitas", citaRepository.count());
        model.addAttribute("totalPacientes", kpis.getTotalPacientes());
        model.addAttribute("totalMedicos", kpis.getTotalMedicos());
        model.addAttribute("citasHoy", kpis.getCitasHoy());
        return "reportes/dashboard";
    }

    @GetMapping("/citas")
    public String reporteCitas(Model model) {
        model.addAttribute("citas", citaRepository.findAll());
        return "reportes/citas";
    }

    @GetMapping("/pacientes")
    public String reportePacientes(Model model) {
        model.addAttribute("pacientes", pacienteRepository.findAll());
        return "reportes/pacientes";
    }
}
