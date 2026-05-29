package com.trinidad.citas.controller.web;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.model.Atencion;
import com.trinidad.citas.repository.AtencionRepository;
import com.trinidad.citas.repository.CitaRepository;
import com.trinidad.citas.repository.HistoriaClinicaRepository;
import com.trinidad.citas.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Profile({"web", "default"})
@Controller
@RequestMapping("/atenciones")
@RequiredArgsConstructor
public class AtencionWebController {

    private final AtencionRepository atencionRepository;
    private final CitaRepository citaRepository;
    private final HistoriaClinicaRepository historiaClinicaRepository;
    private final MedicoRepository medicoRepository;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("atenciones", atencionRepository.findAllWithRelations());
        model.addAttribute("titulo", "Atenciones Médicas");
        return "atenciones/lista";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        atencionRepository.findByIdWithRelations(id).ifPresent(a -> model.addAttribute("atencion", a));
        model.addAttribute("titulo", "Detalle Atención");
        return "atenciones/detalle";
    }

    @GetMapping("/cita/{citaId}/nuevo")
    public String nueva(@PathVariable Long citaId, Model model) {
        model.addAttribute("cita", citaRepository.findById(citaId).orElse(null));
        model.addAttribute("citas", citaRepository.findAllConRelaciones());
        model.addAttribute("titulo", "Nueva Atención");
        return "atenciones/form";
    }

    @GetMapping("/nuevo")
    public String nuevoGeneral(Model model) {
        model.addAttribute("citas", citaRepository.findAllConRelaciones());
        model.addAttribute("titulo", "Nueva Atención Médica");
        return "atenciones/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Atencion atencion,
                          @RequestParam Long citaId,
                          @RequestParam(required = false) Long historiaId,
                          @RequestParam(required = false) Long medicoId,
                          RedirectAttributes redirectAttributes) {
        citaRepository.findById(citaId).ifPresent(atencion::setCita);
        if (historiaId != null) {
            historiaClinicaRepository.findById(historiaId).ifPresent(atencion::setHistoria);
        }
        if (medicoId != null) {
            medicoRepository.findById(medicoId).ifPresent(atencion::setMedico);
        }
        if (atencion.getFechaAtencion() == null) {
            atencion.setFechaAtencion(LocalDateTime.now());
        }
        atencionRepository.save(atencion);
        redirectAttributes.addFlashAttribute("ok", "Atención médica guardada correctamente.");
        return "redirect:/atenciones";
    }
}
