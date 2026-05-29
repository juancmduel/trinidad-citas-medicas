package com.trinidad.citas.controller.web;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.model.Medico;
import com.trinidad.citas.repository.EspecialidadRepository;
import com.trinidad.citas.repository.MedicoRepository;
import com.trinidad.citas.repository.UsuarioRepository;
import com.trinidad.citas.service.MedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Profile({"web", "default"})
@Controller
@RequestMapping("/medicos")
@RequiredArgsConstructor
public class MedicoWebController {

    private final MedicoService medicoService;
    private final MedicoRepository medicoRepository;
    private final EspecialidadRepository especialidadRepository;
    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("medicos", medicoService.listarTodos());
        return "medicos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("medico", new Medico());
        model.addAttribute("especialidades", especialidadRepository.findAll());
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "medicos/form";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        medicoRepository.findByIdWithRelations(id).ifPresent(m -> model.addAttribute("medico", m));
        return "medicos/detalle";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        medicoRepository.findByIdWithRelations(id).ifPresent(m -> {
            model.addAttribute("medico", m);
            model.addAttribute("especialidades", especialidadRepository.findAll());
            model.addAttribute("usuarios", usuarioRepository.findAll());
        });
        return "medicos/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Medico medico,
                          @RequestParam Long especialidadId,
                          @RequestParam Long usuarioId,
                          RedirectAttributes redirectAttributes) {
        especialidadRepository.findById(especialidadId).ifPresent(medico::setEspecialidad);
        usuarioRepository.findById(usuarioId).ifPresent(medico::setUsuario);
        medicoRepository.save(medico);
        redirectAttributes.addFlashAttribute("ok", "Médico guardado correctamente.");
        return "redirect:/medicos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        medicoRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("ok", "Médico eliminado correctamente.");
        return "redirect:/medicos";
    }
}
