package com.trinidad.citas.controller.web;

import com.trinidad.citas.model.EstadoCita;
import com.trinidad.citas.dto.PagoDTO;
import com.trinidad.citas.model.Pago;
import com.trinidad.citas.service.CitaService;
import com.trinidad.citas.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Profile({"web", "default"})
@Controller
@RequestMapping("/pagos")
@RequiredArgsConstructor
public class PagoWebController {

    private final PagoService pagoService;
    private final CitaService citaService;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("pagos", pagoService.listarEntidades());
        return "pagos/lista";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("pago", pagoService.obtenerEntidad(id));
        return "pagos/detalle";
    }

    @GetMapping("/nuevo")
    public String nuevo(@RequestParam(required = false) Long citaId, Model model) {
        Pago pago = new Pago();
        if (citaId != null) {
            citaService.obtenerEntidad(citaId);
            pago.setCita(citaService.obtenerEntidad(citaId));
        }
        model.addAttribute("pago", pago);
        model.addAttribute("citas", citaService.listarEntidadesPorEstado(EstadoCita.PROGRAMADA));
        return "pagos/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Pago pago = pagoService.obtenerEntidad(id);
        model.addAttribute("pago", pago);
        model.addAttribute("citas", citaService.listarEntidadesPorEstado(EstadoCita.PROGRAMADA));
        return "pagos/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Pago pago,
                          @RequestParam Long citaId,
                          BindingResult result,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("citas", citaService.listarEntidadesPorEstado(EstadoCita.PROGRAMADA));
            return "pagos/form";
        }
        try {
            var dto = new PagoDTO();
            dto.setIdCita(citaId);
            dto.setMonto(pago.getMonto());
            dto.setMetodoPago(pago.getMetodoPago());
            dto.setEstado(pago.getEstado() != null ? pago.getEstado() : "PENDIENTE");
            dto.setNroComprobante(pago.getNroComprobante());
            dto.setTipoComprobante(pago.getTipoComprobante());
            dto.setFechaPago("PAGADO".equals(pago.getEstado()) ? LocalDateTime.now() : null);
            if (pago.getIdPago() != null) {
                pagoService.actualizar(pago.getIdPago(), dto);
            } else {
                pagoService.crear(dto);
            }
            redirectAttributes.addFlashAttribute("ok", "Pago guardado correctamente.");
            return "redirect:/pagos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
            return "redirect:/pagos/nuevo";
        }
    }

    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable Long id, @RequestParam String estado,
                                RedirectAttributes redirectAttributes) {
        try {
            pagoService.cambiarEstado(id, estado);
            redirectAttributes.addFlashAttribute("ok", "Estado actualizado a " + estado + ".");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
        }
        return "redirect:/pagos";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            pagoService.eliminar(id);
            redirectAttributes.addFlashAttribute("ok", "Pago eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/pagos";
    }
}
