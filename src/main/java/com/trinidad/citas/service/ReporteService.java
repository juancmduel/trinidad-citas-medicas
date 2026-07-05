package com.trinidad.citas.service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.trinidad.citas.dto.CitaDTO;
import com.trinidad.citas.dto.IngresoDTO;
import com.trinidad.citas.dto.KpiDashboardDTO;
import com.trinidad.citas.model.*;
import com.trinidad.citas.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReporteService {

    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final CitaRepository citaRepository;
    private final EspecialidadRepository especialidadRepository;
    private final PagoRepository pagoRepository;
    private final CitaService citaService;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // ────────────────────────────────────────────────────────────────
    //  KPIs
    // ────────────────────────────────────────────────────────────────

    public KpiDashboardDTO obtenerKpisDashboard() {
        LocalDate hoy = LocalDate.now();
        return KpiDashboardDTO.builder()
            .totalPacientes(pacienteRepository.count())
            .totalMedicos(medicoRepository.count())
            .citasHoy(citaRepository.countByFechaCita(hoy))
            .citasAtendidasHoy(citaRepository.countByFechaCitaAndEstado(hoy, EstadoCita.ATENDIDA))
            .citasPendientesHoy(citaRepository.countByFechaCitaAndEstado(hoy, EstadoCita.PROGRAMADA)
                                + citaRepository.countByFechaCitaAndEstado(hoy, EstadoCita.CONFIRMADA))
            .citasCanceladasHoy(citaRepository.countByFechaCitaAndEstado(hoy, EstadoCita.CANCELADA))
            .totalEspecialidades(especialidadRepository.count())
            .build();
    }

    // ────────────────────────────────────────────────────────────────
    //  LISTAR CITAS FILTRADAS (para vista web)
    // ────────────────────────────────────────────────────────────────

    public List<CitaDTO> listarCitas(LocalDate desde, LocalDate hasta, Long idEspecialidad, Long idMedico, String estado) {
        return filtrarCitas(desde, hasta, idEspecialidad, idMedico, estado).stream()
            .map(this::toCitaDTO).toList();
    }

    public long contarCitas(LocalDate desde, LocalDate hasta, Long idEspecialidad, Long idMedico, String estado) {
        return filtrarCitas(desde, hasta, idEspecialidad, idMedico, estado).size();
    }

    /**
     * Reutiliza CitaService.toDTO() para evitar duplicación de lógica de mapeo.
     * @see CitaService#toDTO(Cita)
     */
    private CitaDTO toCitaDTO(Cita c) {
        return citaService.toDTO(c);
    }

    // ────────────────────────────────────────────────────────────────
    //  REPORTE DE CITAS — PDF
    // ────────────────────────────────────────────────────────────────

    public byte[] exportarCitasPdf(LocalDate desde, LocalDate hasta, Long idEspecialidad, Long idMedico, String estado) {
        List<Cita> citas = filtrarCitas(desde, hasta, idEspecialidad, idMedico, estado);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document(com.lowagie.text.PageSize.A4.rotate());
        try {
            PdfWriter.getInstance(doc, baos);
            doc.open();
            agregarTitulo(doc, "Reporte de Citas Medicas",
                "Desde: " + desde.format(DATE_FMT) + "  Hasta: " + hasta.format(DATE_FMT));
            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);
            float[] widths = {10f, 12f, 14f, 22f, 22f, 14f, 12f, 12f};
            table.setWidths(widths);
            agregarHeader(table, "Fecha", "Hora", "Estado", "Paciente", "Medico", "Especialidad", "DNI", "Canal");
            com.lowagie.text.Font cellFont = com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 8);
            for (Cita c : citas) {
                table.addCell(crearCell(c.getFechaCita().format(DATE_FMT), cellFont));
                table.addCell(crearCell(c.getHoraInicio(), cellFont));
                table.addCell(crearCell(c.getEstado().name(), cellFont));
                table.addCell(crearCell(c.getPaciente() != null ? c.getPaciente().getNombreCompleto() : "-", cellFont));
                table.addCell(crearCell(c.getMedico() != null ? c.getMedico().getNombreCompleto() : "-", cellFont));
                table.addCell(crearCell(c.getEspecialidad() != null ? c.getEspecialidad().getNombre() : "-", cellFont));
                table.addCell(crearCell(c.getPaciente() != null ? c.getPaciente().getDni() : "-", cellFont));
                table.addCell(crearCell(c.getCanalReserva() != null ? c.getCanalReserva() : "-", cellFont));
            }
            doc.add(table);
            agregarPie(doc, "Total de citas: " + citas.size());
            doc.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar PDF de citas", e);
        }
        return baos.toByteArray();
    }

    // ────────────────────────────────────────────────────────────────
    //  REPORTE DE CITAS — EXCEL
    // ────────────────────────────────────────────────────────────────

    public byte[] exportarCitasExcel(LocalDate desde, LocalDate hasta, Long idEspecialidad, Long idMedico, String estado) {
        List<Cita> citas = filtrarCitas(desde, hasta, idEspecialidad, idMedico, estado);
        try (XSSFWorkbook wb = new XSSFWorkbook(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Sheet sheet = wb.createSheet("Citas");
            String[] headers = {"Fecha", "Hora", "Estado", "Paciente", "Medico", "Especialidad", "DNI", "Canal"};
            CellStyle headerStyle = crearHeaderStyle(wb);
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            int rowIdx = 1;
            for (Cita c : citas) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(c.getFechaCita().format(DATE_FMT));
                row.createCell(1).setCellValue(c.getHoraInicio());
                row.createCell(2).setCellValue(c.getEstado().name());
                row.createCell(3).setCellValue(c.getPaciente() != null ? c.getPaciente().getNombreCompleto() : "-");
                row.createCell(4).setCellValue(c.getMedico() != null ? c.getMedico().getNombreCompleto() : "-");
                row.createCell(5).setCellValue(c.getEspecialidad() != null ? c.getEspecialidad().getNombre() : "-");
                row.createCell(6).setCellValue(c.getPaciente() != null ? c.getPaciente().getDni() : "-");
                row.createCell(7).setCellValue(c.getCanalReserva() != null ? c.getCanalReserva() : "-");
            }
            for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);
            wb.write(baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar Excel de citas", e);
        }
    }

    // ────────────────────────────────────────────────────────────────
    //  REPORTE DE PACIENTES — PDF
    // ────────────────────────────────────────────────────────────────

    public byte[] exportarPacientesPdf() {
        List<Paciente> pacientes = pacienteRepository.findAll();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document(com.lowagie.text.PageSize.A4.rotate());
        try {
            PdfWriter.getInstance(doc, baos);
            doc.open();
            agregarTitulo(doc, "Reporte de Pacientes Registrados", null);
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            float[] widths = {10f, 22f, 8f, 12f, 28f, 12f, 20f};
            table.setWidths(widths);
            agregarHeader(table, "DNI", "Nombre Completo", "Sexo", "F. Nac.", "Direccion", "Telefono", "Email");
            com.lowagie.text.Font cellFont = com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 8);
            for (Paciente p : pacientes) {
                table.addCell(crearCell(p.getDni(), cellFont));
                table.addCell(crearCell(p.getNombreCompleto(), cellFont));
                table.addCell(crearCell(p.getSexo(), cellFont));
                table.addCell(crearCell(p.getFechaNacimiento().format(DATE_FMT), cellFont));
                String dir = p.getDireccion() != null ? p.getDireccion() : "-";
                if (p.getDistrito() != null) dir += ", " + p.getDistrito();
                table.addCell(crearCell(dir, cellFont));
                table.addCell(crearCell(p.getTelefono() != null ? p.getTelefono() : "-", cellFont));
                table.addCell(crearCell(p.getEmail() != null ? p.getEmail() : "-", cellFont));
            }
            doc.add(table);
            agregarPie(doc, "Total de pacientes: " + pacientes.size());
            doc.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar PDF de pacientes", e);
        }
        return baos.toByteArray();
    }

    // ────────────────────────────────────────────────────────────────
    //  REPORTE DE PACIENTES — EXCEL
    // ────────────────────────────────────────────────────────────────

    public byte[] exportarPacientesExcel() {
        List<Paciente> pacientes = pacienteRepository.findAll();
        try (XSSFWorkbook wb = new XSSFWorkbook(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Sheet sheet = wb.createSheet("Pacientes");
            String[] headers = {"DNI", "Nombre Completo", "Sexo", "F. Nac.", "Direccion", "Telefono", "Email"};
            CellStyle headerStyle = crearHeaderStyle(wb);
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            int rowIdx = 1;
            for (Paciente p : pacientes) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(p.getDni());
                row.createCell(1).setCellValue(p.getNombreCompleto());
                row.createCell(2).setCellValue(p.getSexo());
                row.createCell(3).setCellValue(p.getFechaNacimiento().format(DATE_FMT));
                String dir = p.getDireccion() != null ? p.getDireccion() : "-";
                if (p.getDistrito() != null) dir += ", " + p.getDistrito();
                row.createCell(4).setCellValue(dir);
                row.createCell(5).setCellValue(p.getTelefono() != null ? p.getTelefono() : "-");
                row.createCell(6).setCellValue(p.getEmail() != null ? p.getEmail() : "-");
            }
            for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);
            wb.write(baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar Excel de pacientes", e);
        }
    }

    // ────────────────────────────────────────────────────────────────
    //  REPORTE DE INGRESOS — PDF
    // ────────────────────────────────────────────────────────────────

    public byte[] exportarIngresosPdf(LocalDate desde, LocalDate hasta) {
        LocalDateTime desdeDt = desde.atStartOfDay();
        LocalDateTime hastaDt = hasta.atTime(LocalTime.MAX);
        List<Pago> pagos = pagoRepository.findWithCitaByFechaPagoBetween(desdeDt, hastaDt);
        BigDecimal total = pagoRepository.sumMontoByFechaPagoBetween(desdeDt, hastaDt);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document(com.lowagie.text.PageSize.A4.rotate());
        try {
            PdfWriter.getInstance(doc, baos);
            doc.open();
            agregarTitulo(doc, "Reporte de Ingresos",
                "Desde: " + desde.format(DATE_FMT) + "  Hasta: " + hasta.format(DATE_FMT));
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            float[] widths = {14f, 14f, 22f, 14f, 12f, 12f, 14f};
            table.setWidths(widths);
            agregarHeader(table, "Fecha Pago", "Comprobante", "Paciente", "Monto", "Metodo", "Estado", "Fecha Cita");
            com.lowagie.text.Font cellFont = com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 8);
            for (Pago p : pagos) {
                String nombrePaciente = Optional.ofNullable(p.getCita())
                    .map(c -> c.getPaciente())
                    .map(pac -> pac.getNombreCompleto())
                    .orElse("-");
                String fechaCita = Optional.ofNullable(p.getCita())
                    .map(c -> c.getFechaCita())
                    .map(f -> f.format(DATE_FMT))
                    .orElse("-");
                table.addCell(crearCell(p.getFechaPago() != null ? p.getFechaPago().format(DATETIME_FMT) : "-", cellFont));
                table.addCell(crearCell(p.getNroComprobante() != null ? p.getNroComprobante() : "-", cellFont));
                table.addCell(crearCell(nombrePaciente, cellFont));
                table.addCell(crearCell("S/ " + p.getMonto().toString(), cellFont));
                table.addCell(crearCell(p.getMetodoPago(), cellFont));
                table.addCell(crearCell(p.getEstado(), cellFont));
                table.addCell(crearCell(fechaCita, cellFont));
            }
            doc.add(table);
            doc.add(new Paragraph(" "));
            com.lowagie.text.Font totalFont = com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA_BOLD, 12, new Color(0, 100, 0));
            Paragraph totalP = new Paragraph("Total Ingresos: S/ " + total.toString(), totalFont);
            totalP.setAlignment(Element.ALIGN_RIGHT);
            doc.add(totalP);
            agregarPie(doc, "Total de transacciones: " + pagos.size());
            doc.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar PDF de ingresos", e);
        }
        return baos.toByteArray();
    }

    // ────────────────────────────────────────────────────────────────
    //  REPORTE DE INGRESOS — EXCEL
    // ────────────────────────────────────────────────────────────────

    public byte[] exportarIngresosExcel(LocalDate desde, LocalDate hasta) {
        LocalDateTime desdeDt = desde.atStartOfDay();
        LocalDateTime hastaDt = hasta.atTime(LocalTime.MAX);
        List<Pago> pagos = pagoRepository.findWithCitaByFechaPagoBetween(desdeDt, hastaDt);
        BigDecimal total = pagoRepository.sumMontoByFechaPagoBetween(desdeDt, hastaDt);
        try (XSSFWorkbook wb = new XSSFWorkbook(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Sheet sheet = wb.createSheet("Ingresos");
            String[] headers = {"Fecha Pago", "Comprobante", "Paciente", "Monto", "Metodo", "Estado", "Fecha Cita"};
            CellStyle headerStyle = crearHeaderStyle(wb);
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            int rowIdx = 1;
            for (Pago p : pagos) {
                String nombrePaciente = Optional.ofNullable(p.getCita())
                    .map(c -> c.getPaciente())
                    .map(pac -> pac.getNombreCompleto())
                    .orElse("-");
                String fechaCita = Optional.ofNullable(p.getCita())
                    .map(c -> c.getFechaCita())
                    .map(f -> f.format(DATE_FMT))
                    .orElse("-");
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(p.getFechaPago() != null ? p.getFechaPago().format(DATETIME_FMT) : "-");
                row.createCell(1).setCellValue(p.getNroComprobante() != null ? p.getNroComprobante() : "-");
                row.createCell(2).setCellValue(nombrePaciente);
                row.createCell(3).setCellValue(p.getMonto().doubleValue());
                row.createCell(4).setCellValue(p.getMetodoPago());
                row.createCell(5).setCellValue(p.getEstado());
                row.createCell(6).setCellValue(fechaCita);
            }
            Row totalRow = sheet.createRow(rowIdx + 1);
            Cell totalLabel = totalRow.createCell(2);
            totalLabel.setCellValue("TOTAL");
            totalLabel.setCellStyle(headerStyle);
            Cell totalVal = totalRow.createCell(3);
            totalVal.setCellValue(total.doubleValue());
            totalVal.setCellStyle(headerStyle);
            for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);
            wb.write(baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar Excel de ingresos", e);
        }
    }

    // ────────────────────────────────────────────────────────────────
    //  LISTAR INGRESOS (para vista web)
    // ────────────────────────────────────────────────────────────────

    public List<IngresoDTO> listarIngresos(LocalDate desde, LocalDate hasta) {
        LocalDateTime desdeDt = desde.atStartOfDay();
        LocalDateTime hastaDt = hasta.atTime(LocalTime.MAX);
        return pagoRepository.findWithCitaByFechaPagoBetween(desdeDt, hastaDt).stream()
            .map(this::toIngresoDTO).toList();
    }

    public BigDecimal sumarIngresos(LocalDate desde, LocalDate hasta) {
        LocalDateTime desdeDt = desde.atStartOfDay();
        LocalDateTime hastaDt = hasta.atTime(LocalTime.MAX);
        return pagoRepository.sumMontoByFechaPagoBetween(desdeDt, hastaDt);
    }

    private IngresoDTO toIngresoDTO(Pago p) {
        String nombrePaciente = Optional.ofNullable(p.getCita())
            .map(c -> c.getPaciente())
            .map(pac -> pac.getNombreCompleto())
            .orElse("-");
        String dniPaciente = Optional.ofNullable(p.getCita())
            .map(c -> c.getPaciente())
            .map(pac -> pac.getDni())
            .orElse("-");
        LocalDate fechaCita = Optional.ofNullable(p.getCita())
            .map(Cita::getFechaCita)
            .orElse(null);
        return IngresoDTO.builder()
            .idPago(p.getIdPago())
            .nroComprobante(p.getNroComprobante())
            .paciente(nombrePaciente)
            .dniPaciente(dniPaciente)
            .monto(p.getMonto())
            .metodoPago(p.getMetodoPago())
            .estado(p.getEstado())
            .fechaPago(p.getFechaPago())
            .fechaCita(fechaCita)
            .build();
    }

    // ────────────────────────────────────────────────────────────────
    //  MÉTODOS AUXILIARES
    // ────────────────────────────────────────────────────────────────

    private List<Cita> filtrarCitas(LocalDate desde, LocalDate hasta, Long idEspecialidad, Long idMedico, String estado) {
        Stream<Cita> stream = citaRepository.findByFechaCitaBetweenConRelaciones(desde, hasta).stream();
        if (idEspecialidad != null) {
            stream = stream.filter(c -> c.getEspecialidad() != null && c.getEspecialidad().getIdEspecialidad().equals(idEspecialidad));
        }
        if (idMedico != null) {
            stream = stream.filter(c -> c.getMedico() != null && c.getMedico().getIdMedico().equals(idMedico));
        }
        if (estado != null && !estado.isEmpty()) {
            stream = stream.filter(c -> c.getEstado().name().equals(estado));
        }
        return stream.toList();
    }

    private void agregarTitulo(Document doc, String titulo, String subtitulo) throws DocumentException {
        com.lowagie.text.Font titleFont = com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA_BOLD, 16, new Color(0, 51, 102));
        Paragraph p = new Paragraph(titulo, titleFont);
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingAfter(10);
        doc.add(p);
        if (subtitulo != null) {
            com.lowagie.text.Font subFont = com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 10, new Color(100, 100, 100));
            Paragraph sp = new Paragraph(subtitulo, subFont);
            sp.setAlignment(Element.ALIGN_CENTER);
            sp.setSpacingAfter(20);
            doc.add(sp);
        }
    }

    private void agregarPie(Document doc, String texto) throws DocumentException {
        com.lowagie.text.Font pFont = com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 9, new Color(100, 100, 100));
        Paragraph p = new Paragraph(texto, pFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        p.setSpacingBefore(15);
        doc.add(p);
        com.lowagie.text.Font fFont = com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 8, new Color(150, 150, 150));
        Paragraph fecha = new Paragraph("Generado: " + LocalDateTime.now().format(DATETIME_FMT), fFont);
        fecha.setAlignment(Element.ALIGN_RIGHT);
        doc.add(fecha);
    }

    private void agregarHeader(PdfPTable table, String... headers) {
        com.lowagie.text.Font headerFont = com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA_BOLD, 9, Color.WHITE);
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setBackgroundColor(new Color(0, 51, 102));
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
    }

    private PdfPCell crearCell(String texto, com.lowagie.text.Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(texto != null ? texto : "", font));
        cell.setPadding(3);
        cell.setBorderColor(new Color(200, 200, 200));
        return cell;
    }

    private CellStyle crearHeaderStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        org.apache.poi.ss.usermodel.Font font = wb.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }
}