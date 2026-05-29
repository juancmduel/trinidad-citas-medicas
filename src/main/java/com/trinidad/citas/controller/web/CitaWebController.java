package com.trinidad.citas.controller.web;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.dto.CitaDTO;
import com.trinidad.citas.service.CitaService;
import com.trinidad.citas.service.EspecialidadService;
import com.trinidad.citas.service.MedicoService;
import com.trinidad.citas.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Profile({"web", "default"})
@Controller
@RequestMapping("/citas")
@RequiredArgsConstructor
public class CitaWebController {

    private final CitaService citaService;
    private final EspecialidadService especialidadService;
    private final MedicoService medicoService;
    private final PacienteService pacienteService;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("citas", citaService.listarTodasConRelaciones());
        model.addAttribute("titulo", "Citas Médicas");
        return "citas/lista";
    }

    @GetMapping("/calendario")
    public String calendario(@RequestParam(required = false) String fecha, Model model) {
        LocalDate f = (fecha != null && !fecha.isBlank()) ? LocalDate.parse(fecha) : LocalDate.now();
        model.addAttribute("citas", citaService.listarPorFechaConRelaciones(f));
        model.addAttribute("fecha", f);
        model.addAttribute("titulo", "Calendario de Citas");
        return "citas/calendario";
    }

    @GetMapping("/agendar")
    public String agendar(Model model) {
        model.addAttribute("cita", new CitaDTO());
        model.addAttribute("especialidades", especialidadService.listarActivas());
        model.addAttribute("medicos", medicoService.listarActivos());
        model.addAttribute("pacientes", pacienteService.listarTodos());
        model.addAttribute("titulo", "Agendar Cita");
        return "citas/agendar";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("cita", citaService.obtener(id));
        return "citas/detalle";
    }

    @PostMapping("/agendar")
    public String guardar(@Valid @ModelAttribute("cita") CitaDTO dto,
                          BindingResult br, RedirectAttributes ra, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("especialidades", especialidadService.listarActivas());
            model.addAttribute("medicos", medicoService.listarActivos());
            model.addAttribute("pacientes", pacienteService.listarTodos());
            String errores = br.getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(java.util.stream.Collectors.joining(", "));
            model.addAttribute("error", "Corrija los siguientes campos: " + errores);
            return "citas/agendar";
        }
        try {
            citaService.agendar(dto);
            ra.addFlashAttribute("ok", "Cita agendada correctamente");
            return "redirect:/citas";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("especialidades", especialidadService.listarActivas());
            model.addAttribute("medicos", medicoService.listarActivos());
            model.addAttribute("pacientes", pacienteService.listarTodos());
            return "citas/agendar";
        }
    }

    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable Long id, RedirectAttributes ra) {
        citaService.cancelar(id);
        ra.addFlashAttribute("ok", "Cita cancelada");
        return "redirect:/citas";
    }
}
