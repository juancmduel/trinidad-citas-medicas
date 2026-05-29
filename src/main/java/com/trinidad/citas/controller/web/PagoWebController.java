package com.trinidad.citas.controller.web;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.model.Pago;
import com.trinidad.citas.repository.CitaRepository;
import com.trinidad.citas.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Profile({"web", "default"})
@Controller
@RequestMapping("/pagos")
@RequiredArgsConstructor
public class PagoWebController {

    private final PagoRepository pagoRepository;
    private final CitaRepository citaRepository;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("pagos", pagoRepository.findAll());
        return "pagos/lista";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        pagoRepository.findById(id).ifPresent(p -> model.addAttribute("pago", p));
        return "pagos/detalle";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("pago", new Pago());
        model.addAttribute("citas", citaRepository.findAllConRelaciones());
        return "pagos/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        pagoRepository.findById(id).ifPresent(p -> {
            model.addAttribute("pago", p);
            model.addAttribute("citas", citaRepository.findAllConRelaciones());
        });
        return "pagos/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Pago pago,
                          @RequestParam Long citaId,
                          RedirectAttributes redirectAttributes) {
        citaRepository.findById(citaId).ifPresent(pago::setCita);
        pagoRepository.save(pago);
        redirectAttributes.addFlashAttribute("ok", "Pago guardado correctamente.");
        return "redirect:/pagos";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        pagoRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("ok", "Pago eliminado correctamente.");
        return "redirect:/pagos";
    }
}
