package com.trinidad.citas.controller.web;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.model.HorarioMedico;
import com.trinidad.citas.repository.HorarioMedicoRepository;
import com.trinidad.citas.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Profile({"web", "default"})
@Controller
@RequestMapping("/horarios")
@RequiredArgsConstructor
public class HorarioWebController {

    private final HorarioMedicoRepository horarioMedicoRepository;
    private final MedicoRepository medicoRepository;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("horarios", horarioMedicoRepository.findAllWithMedico());
        return "horarios/lista";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        horarioMedicoRepository.findByIdWithMedico(id).ifPresent(h -> model.addAttribute("horario", h));
        return "horarios/detalle";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("horario", new HorarioMedico());
        model.addAttribute("medicos", medicoRepository.findAll());
        return "horarios/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        horarioMedicoRepository.findByIdWithMedico(id).ifPresent(h -> {
            model.addAttribute("horario", h);
            model.addAttribute("medicos", medicoRepository.findAll());
        });
        return "horarios/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute HorarioMedico horario,
                          @RequestParam Long medicoId,
                          RedirectAttributes redirectAttributes) {
        medicoRepository.findById(medicoId).ifPresent(horario::setMedico);
        horarioMedicoRepository.save(horario);
        redirectAttributes.addFlashAttribute("ok", "Horario guardado correctamente.");
        return "redirect:/horarios";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        horarioMedicoRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("ok", "Horario eliminado correctamente.");
        return "redirect:/horarios";
    }
}
