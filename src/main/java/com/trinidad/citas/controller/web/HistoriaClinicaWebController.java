package com.trinidad.citas.controller.web;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.model.HistoriaClinica;
import com.trinidad.citas.repository.HistoriaClinicaRepository;
import com.trinidad.citas.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Profile({"web", "default"})
@Controller
@RequestMapping("/historia-clinica")
@RequiredArgsConstructor
public class HistoriaClinicaWebController {

    private final HistoriaClinicaRepository historiaClinicaRepository;
    private final PacienteRepository pacienteRepository;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("historias", historiaClinicaRepository.findAllWithPaciente());
        return "historia-clinica/lista";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        historiaClinicaRepository.findByIdWithPaciente(id).ifPresent(h -> model.addAttribute("historia", h));
        return "historia-clinica/detalle";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("historia", new HistoriaClinica());
        model.addAttribute("pacientes", pacienteRepository.findAll());
        return "historia-clinica/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        historiaClinicaRepository.findById(id).ifPresent(h -> {
            model.addAttribute("historia", h);
            model.addAttribute("pacientes", pacienteRepository.findAll());
        });
        return "historia-clinica/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute HistoriaClinica historia,
                          @RequestParam Long pacienteId,
                          RedirectAttributes redirectAttributes) {
        pacienteRepository.findById(pacienteId).ifPresent(historia::setPaciente);
        historiaClinicaRepository.save(historia);
        redirectAttributes.addFlashAttribute("ok", "Historia clínica guardada correctamente.");
        return "redirect:/historia-clinica";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        historiaClinicaRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("ok", "Historia clínica eliminada correctamente.");
        return "redirect:/historia-clinica";
    }
}
