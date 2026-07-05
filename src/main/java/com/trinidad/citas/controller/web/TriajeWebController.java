package com.trinidad.citas.controller.web;

import com.trinidad.citas.dto.TriajeDTO;
import com.trinidad.citas.model.EstadoCita;
import com.trinidad.citas.service.CitaService;
import com.trinidad.citas.service.TriajeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Profile({"web", "default"})
@Controller
@RequestMapping("/triaje")
@RequiredArgsConstructor
public class TriajeWebController {

    private final TriajeService triajeService;
    private final CitaService citaService;

    @GetMapping("/api/cita/{idCita}")
    @ResponseBody
    public ResponseEntity<TriajeDTO> obtenerPorCitaApi(@PathVariable Long idCita) {
        try {
            return ResponseEntity.ok(triajeService.obtenerPorCita(idCita));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("triajes", triajeService.listarTodos());
        model.addAttribute("titulo", "Triaje - Enfermería");
        return "triaje/lista";
    }

    @GetMapping("/pendientes")
    public String pendientes(Model model) {
        model.addAttribute("citas", citaService.listarEntidadesPorEstado(EstadoCita.EN_TRIAGE));
        model.addAttribute("titulo", "Pacientes en Triaje");
        return "triaje/pendientes";
    }

    @GetMapping("/cita/{citaId}/registrar")
    public String registrarForm(@PathVariable Long citaId, Model model) {
        model.addAttribute("cita", citaService.obtenerEntidad(citaId));
        model.addAttribute("titulo", "Registrar Triaje");
        return "triaje/registrar";
    }

    @PostMapping("/cita/{citaId}/guardar")
    public String guardar(@PathVariable Long citaId,
                          @ModelAttribute TriajeDTO dto,
                          RedirectAttributes ra) {
        try {
            triajeService.registrar(citaId, null, dto);
            ra.addFlashAttribute("ok", "Triaje registrado correctamente. Paciente derivado al consultorio.");
            return "redirect:/triaje/pendientes";
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/triaje/cita/" + citaId + "/registrar";
        }
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("triaje", triajeService.obtenerPorId(id));
        model.addAttribute("titulo", "Detalle de Triaje");
        return "triaje/detalle";
    }
}
