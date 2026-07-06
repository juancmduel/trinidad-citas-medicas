package com.trinidad.citas.controller.web;

import java.security.Principal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trinidad.citas.dto.CitaDTO;
import com.trinidad.citas.exception.ResourceNotFoundException;
import com.trinidad.citas.repository.CitaRepository;
import com.trinidad.citas.repository.MedicoRepository;
import com.trinidad.citas.repository.PagoRepository;
import com.trinidad.citas.service.CitaService;
import com.trinidad.citas.service.EspecialidadService;
import com.trinidad.citas.service.MedicoService;
import com.trinidad.citas.service.PacienteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Profile({"web", "default"})
@Controller
@RequestMapping("/citas")
@RequiredArgsConstructor
public class CitaWebController {

    private final CitaService citaService;
    private final CitaRepository citaRepository;
    private final MedicoRepository medicoRepository;
    private final EspecialidadService especialidadService;
    private final MedicoService medicoService;
    private final PacienteService pacienteService;
    private final PagoRepository pagoRepository;

    @GetMapping
    public String lista(Model model, Principal principal, Authentication authentication) {
        boolean esMedico = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_MEDICO"));

        if (esMedico) {
            var medico = medicoRepository.findByUsuario_Username(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Medico no encontrado para: " + principal.getName()));
            model.addAttribute("citas", citaService.listarPorMedicoConRelaciones(medico.getIdMedico()));
            model.addAttribute("soloLectura", true);
        } else {
            model.addAttribute("citas", citaService.listarTodasConRelaciones());
            model.addAttribute("soloLectura", false);
        }
        model.addAttribute("titulo", "Citas Médicas");
        return "citas/lista";
    }

    @GetMapping("/calendario")
    public String calendario(@RequestParam(required = false) String fecha,
                             @RequestParam(required = false) Integer mes,
                             @RequestParam(required = false) Integer anio,
                             Model model) {
        // Si no se pasa fecha, se usa la de hoy
        LocalDate f = (fecha != null && !fecha.isBlank()) ? LocalDate.parse(fecha) : LocalDate.now();
        model.addAttribute("citas", citaService.listarPorFechaConRelaciones(f));
        model.addAttribute("fecha", f);
        model.addAttribute("titulo", "Calendario de Citas");

        // Si vienen mes y año, navegamos a ese mes; si no, el mes de la fecha seleccionada
        YearMonth pagina;
        if (mes != null && anio != null) {
            pagina = YearMonth.of(anio, mes);
        } else {
            pagina = YearMonth.from(f);
        }

        // Consultar qué días del mes tienen citas registradas
        LocalDate inicioMes = pagina.atDay(1);
        LocalDate finMes = pagina.atEndOfMonth();
        List<Object[]> counts = citaRepository.countByFechaCitaBetween(inicioMes, finMes);
        Set<LocalDate> diasConCitas = counts.stream()
            .map(row -> (LocalDate) row[0])
            .collect(Collectors.toSet());
        Map<LocalDate, Long> conteoDias = counts.stream()
            .collect(Collectors.toMap(row -> (LocalDate) row[0], row -> (Long) row[1]));

        // Armar la cuadrícula semanas/días del calendario
        int primerDiaSemana = inicioMes.getDayOfWeek().getValue() % 7; // 0=domingo
        int diasEnMes = pagina.lengthOfMonth();
        List<Map<String, Object>> diasCalendario = new ArrayList<>();

        // Rellenar con celdas vacías hasta el primer día del mes
        for (int i = 0; i < primerDiaSemana; i++) {
            Map<String, Object> d = new HashMap<>();
            d.put("dia", null);
            d.put("fecha", null);
            d.put("hayCitas", false);
            d.put("conteo", 0);
            d.put("esHoy", false);
            d.put("esSeleccionado", false);
            diasCalendario.add(d);
        }

        LocalDate hoy = LocalDate.now();
        for (int dia = 1; dia <= diasEnMes; dia++) {
            LocalDate fechaDia = pagina.atDay(dia);
            Map<String, Object> d = new HashMap<>();
            d.put("dia", dia);
            d.put("fecha", fechaDia.toString());
            d.put("hayCitas", diasConCitas.contains(fechaDia));
            d.put("conteo", conteoDias.getOrDefault(fechaDia, 0L));
            d.put("esHoy", fechaDia.equals(hoy));
            d.put("esSeleccionado", fechaDia.equals(f));
            diasCalendario.add(d);
        }

        // Navigation
        YearMonth prev = pagina.minusMonths(1);
        YearMonth next = pagina.plusMonths(1);

        model.addAttribute("pagina", pagina);
        model.addAttribute("monthName", pagina.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es-ES")));
        model.addAttribute("calendarYear", pagina.getYear());
        model.addAttribute("calendarMonth", pagina.getMonthValue());
        model.addAttribute("diasCalendario", diasCalendario);
        model.addAttribute("prevMonth", prev.getMonthValue());
        model.addAttribute("prevYear", prev.getYear());
        model.addAttribute("nextMonth", next.getMonthValue());
        model.addAttribute("nextYear", next.getYear());

        return "citas/calendario";
    }

    @GetMapping("/agendar")
    public String agendar(Model model) {
        model.addAttribute("cita", new CitaDTO());
        model.addAttribute("especialidades", especialidadService.listarActivas());
        model.addAttribute("medicos", medicoService.listarActivos());
        model.addAttribute("pacientes", pacienteService.listarTodos());
        model.addAttribute("titulo", "Agendar Cita");
        return "citas/agendar";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("cita", citaService.obtener(id));
            model.addAttribute("pago", pagoRepository.findByCita_IdCita(id).orElse(null));
            return "citas/detalle";
        } catch (ResourceNotFoundException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            return "redirect:/citas";
        }
    }

    @PostMapping("/agendar")
    public String guardar(@Valid @ModelAttribute("cita") CitaDTO dto,
                          BindingResult br, RedirectAttributes ra, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("especialidades", especialidadService.listarActivas());
            model.addAttribute("medicos", medicoService.listarActivos());
            model.addAttribute("pacientes", pacienteService.listarTodos());
            String errores = br.getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(java.util.stream.Collectors.joining(", "));
            model.addAttribute("error", "Corrija los siguientes campos: " + errores);
            return "citas/agendar";
        }
        try {
            citaService.agendar(dto);
            ra.addFlashAttribute("ok", "Cita agendada correctamente");
            return "redirect:/citas";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("especialidades", especialidadService.listarActivas());
            model.addAttribute("medicos", medicoService.listarActivos());
            model.addAttribute("pacientes", pacienteService.listarTodos());
            return "citas/agendar";
        }
    }

    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable Long id, RedirectAttributes ra) {
        citaService.cancelar(id);
        ra.addFlashAttribute("ok", "Cita cancelada");
        return "redirect:/citas";
    }

    @GetMapping("/{id}/checkin")
    public String checkinForm(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("cita", citaService.obtener(id));
            model.addAttribute("pago", pagoRepository.findByCita_IdCita(id).orElse(null));
            return "citas/checkin";
        } catch (ResourceNotFoundException ex) {
            return "redirect:/citas";
        }
    }

    @PostMapping("/{id}/checkin")
    public String checkin(@PathVariable Long id, RedirectAttributes ra) {
        try {
            citaService.checkin(id);
            ra.addFlashAttribute("ok", "Check-in registrado. Paciente en atencion.");
            return "redirect:/citas/" + id;
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/citas/" + id;
        }
    }

    @PostMapping("/{id}/finalizar")
    public String finalizar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            citaService.finalizar(id);
            ra.addFlashAttribute("ok", "Atencion finalizada.");
            return "redirect:/citas/" + id;
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/citas/" + id;
        }
    }
}
