package com.trinidad.citas.controller.web;

import com.trinidad.citas.model.EstadoCita;
import com.trinidad.citas.dto.PagoDTO;
import com.trinidad.citas.model.Cita;
import com.trinidad.citas.model.Pago;
import com.trinidad.citas.service.CitaService;
import com.trinidad.citas.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Map;

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
            pago.setCita(citaService.obtenerEntidad(citaId));
        }
        model.addAttribute("pago", pago);
        model.addAttribute("citas", citaService.listarEntidadesPorEstado(EstadoCita.PROGRAMADA));
        model.addAttribute("titulo", "Nuevo Pago");
        return "pagos/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Pago pago = pagoService.obtenerEntidad(id);
        model.addAttribute("pago", pago);
        model.addAttribute("citas", citaService.listarEntidadesPorEstado(EstadoCita.PROGRAMADA));
        model.addAttribute("titulo", "Editar Pago");
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
            model.addAttribute("titulo", pago.getIdPago() != null ? "Editar Pago" : "Nuevo Pago");
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
                redirectAttributes.addFlashAttribute("ok", "Pago actualizado correctamente.");
            } else {
                pagoService.crear(dto);
                redirectAttributes.addFlashAttribute("ok", "Pago registrado correctamente.");
            }
            return "redirect:/pagos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
            if (pago.getIdPago() != null) {
                return "redirect:/pagos/editar/" + pago.getIdPago();
            }
            return "redirect:/pagos/nuevo?citaId=" + citaId;
        }
    }

    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable Long id, @RequestParam String estado,
                                RedirectAttributes redirectAttributes) {
        try {
            PagoDTO pago = pagoService.cambiarEstado(id, estado);
            redirectAttributes.addFlashAttribute("ok", "Estado actualizado a " + pago.getEstado() + ".");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
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

    @GetMapping("/generar-comprobante")
    @ResponseBody
    public ResponseEntity<Map<String, String>> generarComprobante(@RequestParam String tipo) {
        try {
            String nro = pagoService.generarNroComprobante(tipo);
            return ResponseEntity.ok(Map.of("numero", nro));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/cita/{id}/precio")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obtenerPrecioCita(@PathVariable Long id) {
        try {
            Cita cita = citaService.obtenerEntidad(id);
            java.math.BigDecimal precio = cita.getEspecialidad().getPrecioConsulta();
            return ResponseEntity.ok(Map.of(
                "precio", precio,
                "especialidad", cita.getEspecialidad().getNombre()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
