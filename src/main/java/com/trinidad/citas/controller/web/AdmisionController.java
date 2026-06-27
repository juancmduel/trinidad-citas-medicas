package com.trinidad.citas.controller.web;

import com.trinidad.citas.dto.AdmisionPacienteDTO;
import com.trinidad.citas.model.EstadoCita;
import com.trinidad.citas.service.AdmisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Profile({"web", "default"})
@Controller
@RequestMapping("/admision")
@RequiredArgsConstructor
public class AdmisionController {

    private final AdmisionService admisionService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("titulo", "Admisión - Enfermería");
        try {
            var pacientes = admisionService.obtenerPacientesHoy();
            model.addAttribute("pacientes", pacientes);
            model.addAttribute("totalHoy", pacientes.size());
            model.addAttribute("countPendiente", pacientes.stream().filter(p -> "PROGRAMADA".equals(p.getEstado()) || "CONFIRMADA".equals(p.getEstado())).count());
            model.addAttribute("countTriaje", pacientes.stream().filter(p -> "EN_TRIAGE".equals(p.getEstado())).count());
            model.addAttribute("countAtencion", pacientes.stream().filter(p -> "EN_ATENCION".equals(p.getEstado())).count());
            model.addAttribute("countAtendido", pacientes.stream().filter(p -> "ATENDIDA".equals(p.getEstado())).count());
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar pacientes: " + e.getMessage());
            model.addAttribute("pacientes", java.util.Collections.emptyList());
        }
        return "admision/tablero";
    }

    @GetMapping("/buscar")
    public String buscarPage(Model model) {
        model.addAttribute("titulo", "Admisión - Enfermería");
        return "admision/buscar";
    }

    @GetMapping("/api/hoy")
    @ResponseBody
    public List<AdmisionPacienteDTO> apiPacientesHoy() {
        return admisionService.obtenerPacientesHoy();
    }

    @PostMapping("/buscar")
    public String buscar(@RequestParam String dni, RedirectAttributes ra) {
        try {
            admisionService.buscarPaciente(dni);
            return "redirect:/admision/paciente/" + dni;
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/admision";
        }
    }

    @GetMapping("/paciente/{dni}")
    public String paciente(@PathVariable String dni, Model model) {
        try {
            var paciente = admisionService.buscarPaciente(dni);
            var citas = admisionService.obtenerCitasDelDia(paciente);
            var estadosAccionables = List.of(
                    EstadoCita.PROGRAMADA,
                    EstadoCita.CONFIRMADA,
                    EstadoCita.EN_TRIAGE);
            model.addAttribute("paciente", paciente);
            model.addAttribute("citas", citas);
            model.addAttribute("estadosAccionables", estadosAccionables);
            model.addAttribute("titulo", "Admisión - " + paciente.getNombreCompleto());
            return "admision/paciente";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("titulo", "Admisión - Enfermería");
            return "admision/buscar";
        }
    }

    @PostMapping("/{idCita}/procesar")
    public String procesar(@PathVariable Long idCita,
                           @RequestParam String metodoPago,
                           @RequestParam(required = false) String tipoComprobante,
                           RedirectAttributes ra) {
        try {
            var cita = admisionService.procesarAdmision(idCita, metodoPago, tipoComprobante);
            if (cita.getEstado() == EstadoCita.EN_TRIAGE || cita.getEstado() == EstadoCita.EN_ATENCION) {
                ra.addFlashAttribute("ok", "Admisión completada. Registra el triaje del paciente.");
                return "redirect:/triaje/cita/" + idCita + "/registrar";
            }
            ra.addFlashAttribute("ok", "Admisión procesada correctamente.");
            return "redirect:/admision";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error en admisión: " + e.getMessage());
            return "redirect:/admision";
        }
    }
}
