package com.trinidad.citas.controller.web;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.dto.MedicoDTO;
import com.trinidad.citas.service.EspecialidadService;
import com.trinidad.citas.service.MedicoService;
import com.trinidad.citas.service.UsuarioService;
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
    private final EspecialidadService especialidadService;
    private final UsuarioService usuarioService;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("medicos", medicoService.listarTodos());
        return "medicos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("medico", new MedicoDTO());
        model.addAttribute("especialidades", especialidadService.listarTodas());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "medicos/form";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("medico", medicoService.obtener(id));
        return "medicos/detalle";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("especialidades", especialidadService.listarTodas());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("medico", medicoService.obtenerEntidad(id));
        return "medicos/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute MedicoDTO dto,
                          @RequestParam Long especialidadId,
                          @RequestParam Long usuarioId,
                          RedirectAttributes redirectAttributes) {
        dto.setIdEspecialidad(especialidadId);
        dto.setIdUsuario(usuarioId);
        if (dto.getIdMedico() != null) {
            medicoService.actualizar(dto.getIdMedico(), dto);
        } else {
            medicoService.crear(dto);
        }
        redirectAttributes.addFlashAttribute("ok", "Médico guardado correctamente.");
        return "redirect:/medicos";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        medicoService.eliminar(id);
        redirectAttributes.addFlashAttribute("ok", "Médico eliminado correctamente.");
        return "redirect:/medicos";
    }
}
