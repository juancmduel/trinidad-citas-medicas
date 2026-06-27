package com.trinidad.citas.controller.web;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.dto.HorarioMedicoDTO;
import com.trinidad.citas.repository.MedicoRepository;
import com.trinidad.citas.service.HorarioMedicoService;
import com.trinidad.citas.service.MedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Profile({"web", "default"})
@Controller
@RequestMapping("/horarios")
@RequiredArgsConstructor
public class HorarioWebController {

    private final HorarioMedicoService horarioMedicoService;
    private final MedicoService medicoService;
    private final MedicoRepository medicoRepository;

    @GetMapping
    public String lista(Model model, Principal principal, Authentication authentication) {
        boolean esMedico = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_MEDICO"));

        if (esMedico) {
            var medico = medicoRepository.findByUsuario_Username(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Medico no encontrado para: " + principal.getName()));
            model.addAttribute("horarios", horarioMedicoService.listarPorMedico(medico.getIdMedico()));
            model.addAttribute("soloLectura", true);
        } else {
            model.addAttribute("horarios", horarioMedicoService.listarTodos());
            model.addAttribute("soloLectura", false);
        }
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
