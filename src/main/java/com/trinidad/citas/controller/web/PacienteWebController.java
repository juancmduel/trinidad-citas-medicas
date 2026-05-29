package com.trinidad.citas.controller.web;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.dto.PacienteDTO;
import com.trinidad.citas.model.Paciente;
import com.trinidad.citas.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Profile({"web", "default"})
@Controller
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public class PacienteWebController {

    private final PacienteService pacienteService;

    @GetMapping
    public String lista(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("pacientes", pacienteService.listarTodos());
        model.addAttribute("q", q);
        return "pacientes/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("paciente", new PacienteDTO());
        model.addAttribute("modo", "crear");
        return "pacientes/form";
    }

    @PostMapping("/nuevo")
    public String guardar(@Valid @ModelAttribute("paciente") PacienteDTO dto,
                          BindingResult br, RedirectAttributes ra, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("modo", "crear");
            return "pacientes/form";
        }
        try {
            pacienteService.crear(dto);
            ra.addFlashAttribute("ok", "Paciente registrado correctamente");
            return "redirect:/pacientes";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("modo", "crear");
            return "pacientes/form";
        }
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("paciente", pacienteService.obtenerDTO(id));
        model.addAttribute("titulo", "Detalle del Paciente");
        return "pacientes/detalle";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Paciente p = pacienteService.obtener(id);
        PacienteDTO dto = new PacienteDTO();
        dto.setIdPaciente(p.getIdPaciente());
        dto.setDni(p.getDni());
        dto.setNombres(p.getNombres());
        dto.setApellidoPaterno(p.getApellidoPaterno());
        dto.setApellidoMaterno(p.getApellidoMaterno());
        dto.setFechaNacimiento(p.getFechaNacimiento());
        dto.setSexo(p.getSexo());
        dto.setTelefono(p.getTelefono());
        dto.setEmail(p.getEmail());
        dto.setDireccion(p.getDireccion());
        dto.setDistrito(p.getDistrito());
        dto.setTipoSangre(p.getTipoSangre());
        dto.setAlergias(p.getAlergias());
        model.addAttribute("paciente", dto);
        model.addAttribute("modo", "editar");
        return "pacientes/form";
    }

    @PostMapping("/{id}/editar")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("paciente") PacienteDTO dto,
                             BindingResult br, RedirectAttributes ra, Model model) {
        dto.setIdPaciente(id);
        if (br.hasErrors()) {
            model.addAttribute("modo", "editar");
            return "pacientes/form";
        }
        try {
            pacienteService.actualizar(id, dto);
            ra.addFlashAttribute("ok", "Paciente actualizado");
            return "redirect:/pacientes";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("modo", "editar");
            return "pacientes/form";
        }
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        pacienteService.eliminar(id);
        ra.addFlashAttribute("ok", "Paciente desactivado");
        return "redirect:/pacientes";
    }
}
