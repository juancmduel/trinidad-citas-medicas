package com.trinidad.citas.controller.web;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.dto.HorarioMedicoDTO;
import com.trinidad.citas.service.HorarioMedicoService;
import com.trinidad.citas.service.MedicoService;
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

    private final HorarioMedicoService horarioMedicoService;
    private final MedicoService medicoService;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("horarios", horarioMedicoService.listarTodos());
        return "horarios/lista";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("horario", horarioMedicoService.obtenerPorId(id));
        return "horarios/detalle";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("horario", new HorarioMedicoDTO());
        model.addAttribute("medicos", medicoService.listarTodos());
        return "horarios/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("medicos", medicoService.listarTodos());
        model.addAttribute("horario", horarioMedicoService.obtenerPorId(id));
        return "horarios/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute HorarioMedicoDTO dto,
                          @RequestParam Long medicoId,
                          RedirectAttributes redirectAttributes) {
        dto.setIdMedico(medicoId);
        if (dto.getIdHorario() != null) {
            horarioMedicoService.actualizar(dto.getIdHorario(), dto);
        } else {
            horarioMedicoService.crear(dto);
        }
        redirectAttributes.addFlashAttribute("ok", "Horario guardado correctamente.");
        return "redirect:/horarios";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        horarioMedicoService.eliminar(id);
        redirectAttributes.addFlashAttribute("ok", "Horario eliminado correctamente.");
        return "redirect:/horarios";
    }
}
