package com.trinidad.citas.controller.web;

import com.trinidad.citas.dto.AntecedenteDTO;
import com.trinidad.citas.service.AntecedenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Profile({"web", "default"})
@Controller
@RequestMapping("/antecedentes")
@RequiredArgsConstructor
public class AntecedenteWebController {

    private final AntecedenteService antecedenteService;

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id,
                           @RequestParam(required = false) Long idHistoria,
                           RedirectAttributes redirectAttributes) {
        try {
            antecedenteService.eliminar(id);
            redirectAttributes.addFlashAttribute("ok", "Antecedente eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar antecedente: " + e.getMessage());
        }
        if (idHistoria != null) {
            return "redirect:/historia-clinica/" + idHistoria;
        }
        return "redirect:/historia-clinica";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam Long idHistoria,
                          @RequestParam String tipo,
                          @RequestParam String descripcion,
                          @RequestParam(required = false) String codigoCie10,
                          @RequestParam(required = false) String observaciones,
                          RedirectAttributes redirectAttributes) {
        try {
            AntecedenteDTO dto = new AntecedenteDTO();
            dto.setIdHistoria(idHistoria);
            dto.setTipo(tipo);
            dto.setDescripcion(descripcion);
            dto.setCodigoCie10(codigoCie10);
            dto.setObservaciones(observaciones);
            antecedenteService.crear(dto);
            redirectAttributes.addFlashAttribute("ok", "Antecedente registrado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar antecedente: " + e.getMessage());
        }
        return "redirect:/historia-clinica/" + idHistoria;
    }
}
