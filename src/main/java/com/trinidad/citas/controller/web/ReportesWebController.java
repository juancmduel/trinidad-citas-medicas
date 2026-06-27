package com.trinidad.citas.controller.web;

import com.trinidad.citas.model.EstadoCita;
import com.trinidad.citas.service.ReporteService;
import com.trinidad.citas.repository.CitaRepository;
import com.trinidad.citas.repository.PacienteRepository;
import com.trinidad.citas.repository.MedicoRepository;
import com.trinidad.citas.repository.EspecialidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;

@Profile({"web", "default"})
@Controller
@RequestMapping("/reportes")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')")
public class ReportesWebController {

    private final ReporteService reporteService;
    private final CitaRepository citaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final EspecialidadRepository especialidadRepository;

    @GetMapping
    public String dashboard(Model model) {
        var kpis = reporteService.obtenerKpisDashboard();
        model.addAttribute("totalCitas", citaRepository.count());
        model.addAttribute("totalPacientes", kpis.getTotalPacientes());
        model.addAttribute("totalMedicos", kpis.getTotalMedicos());
        model.addAttribute("citasHoy", kpis.getCitasHoy());
        model.addAttribute("titulo", "Reportes");
        return "reportes/dashboard";
    }

    // ────────────────────────────────────────────────────────────────
    //  REPORTE DE CITAS
    // ────────────────────────────────────────────────────────────────

    @GetMapping("/citas")
    public String reporteCitas(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
                               @RequestParam(required = false) Long idEspecialidad,
                               @RequestParam(required = false) Long idMedico,
                               @RequestParam(required = false) String estado,
                               Model model) {
        if (desde == null) desde = LocalDate.now().withDayOfMonth(1);
        if (hasta == null) hasta = LocalDate.now();
        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);
        model.addAttribute("idEspecialidad", idEspecialidad);
        model.addAttribute("idMedico", idMedico);
        model.addAttribute("estado", estado);
        model.addAttribute("especialidades", especialidadRepository.findByActivoOrderByNombreAsc(1));
        model.addAttribute("medicos", medicoRepository.findByActivoOrderByApellidoPaternoAsc(1));
        model.addAttribute("estados", Arrays.asList(EstadoCita.values()));
        model.addAttribute("citas", reporteService.listarCitas(desde, hasta, idEspecialidad, idMedico, estado));
        model.addAttribute("totalCitas", reporteService.contarCitas(desde, hasta, idEspecialidad, idMedico, estado));
        model.addAttribute("titulo", "Reporte de Citas");
        return "reportes/citas";
    }

    @GetMapping("/citas/pdf")
    public ResponseEntity<byte[]> descargarCitasPdf(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required = false) Long idEspecialidad,
            @RequestParam(required = false) Long idMedico,
            @RequestParam(required = false) String estado) {
        byte[] pdf = reporteService.exportarCitasPdf(desde, hasta, idEspecialidad, idMedico, estado);
        return respuestaDescarga(pdf, "reporte-citas.pdf", MediaType.APPLICATION_PDF);
    }

    @GetMapping("/citas/xlsx")
    public ResponseEntity<byte[]> descargarCitasExcel(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required = false) Long idEspecialidad,
            @RequestParam(required = false) Long idMedico,
            @RequestParam(required = false) String estado) {
        byte[] xlsx = reporteService.exportarCitasExcel(desde, hasta, idEspecialidad, idMedico, estado);
        return respuestaDescarga(xlsx, "reporte-citas.xlsx", MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
    }

    // ────────────────────────────────────────────────────────────────
    //  REPORTE DE PACIENTES
    // ────────────────────────────────────────────────────────────────

    @GetMapping("/pacientes")
    public String reportePacientes(Model model) {
        model.addAttribute("pacientes", pacienteRepository.findAll());
        model.addAttribute("totalPacientes", pacienteRepository.count());
        model.addAttribute("titulo", "Reporte de Pacientes");
        return "reportes/pacientes";
    }

    @GetMapping("/pacientes/pdf")
    public ResponseEntity<byte[]> descargarPacientesPdf() {
        byte[] pdf = reporteService.exportarPacientesPdf();
        return respuestaDescarga(pdf, "reporte-pacientes.pdf", MediaType.APPLICATION_PDF);
    }

    @GetMapping("/pacientes/xlsx")
    public ResponseEntity<byte[]> descargarPacientesExcel() {
        byte[] xlsx = reporteService.exportarPacientesExcel();
        return respuestaDescarga(xlsx, "reporte-pacientes.xlsx", MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
    }

    // ────────────────────────────────────────────────────────────────
    //  REPORTE DE INGRESOS
    // ────────────────────────────────────────────────────────────────

    @GetMapping("/ingresos")
    public String reporteIngresos(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
                                  Model model) {
        if (desde == null) desde = LocalDate.now().withDayOfMonth(1);
        if (hasta == null) hasta = LocalDate.now();
        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);
        model.addAttribute("ingresos", reporteService.listarIngresos(desde, hasta));
        model.addAttribute("totalIngresos", reporteService.sumarIngresos(desde, hasta));
        model.addAttribute("titulo", "Reporte de Ingresos");
        return "reportes/ingresos";
    }

    @GetMapping("/ingresos/pdf")
    public ResponseEntity<byte[]> descargarIngresosPdf(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        byte[] pdf = reporteService.exportarIngresosPdf(desde, hasta);
        return respuestaDescarga(pdf, "reporte-ingresos.pdf", MediaType.APPLICATION_PDF);
    }

    @GetMapping("/ingresos/xlsx")
    public ResponseEntity<byte[]> descargarIngresosExcel(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        byte[] xlsx = reporteService.exportarIngresosExcel(desde, hasta);
        return respuestaDescarga(xlsx, "reporte-ingresos.xlsx", MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
    }

    // ────────────────────────────────────────────────────────────────
    //  AUXILIAR
    // ────────────────────────────────────────────────────────────────

    private ResponseEntity<byte[]> respuestaDescarga(byte[] data, String filename, MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());
        return ResponseEntity.ok().headers(headers).body(data);
    }
}