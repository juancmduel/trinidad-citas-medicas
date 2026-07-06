package com.trinidad.citas.service;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.trinidad.citas.model.Cita;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void enviarConfirmacionCita(Cita cita) {
        String pacienteEmail = cita.getPaciente() != null ? cita.getPaciente().getEmail() : null;
        if (pacienteEmail == null || pacienteEmail.isBlank()) {
            log.info("[EMAIL] Paciente sin email. CitaID={}", cita.getIdCita());
            return;
        }

        String nombreCompleto = cita.getPaciente().getNombres() + " " + cita.getPaciente().getApellidoPaterno();
        String medico         = cita.getMedico() != null ? "Dr(a). " + cita.getMedico().getNombreCompleto() : "-";
        String especialidad   = cita.getEspecialidad() != null ? cita.getEspecialidad().getNombre() : "-";
        // Nro. de cita para mostrar en plantilla (solo referencia visual, no expone datos sensibles)
        String nroCita        = "CIT-" + String.format("%05d", cita.getIdCita());

        // ⚠ El QR solo contiene el ID de referencia, NO datos personales (DNI, nombre, etc.)
        // por seguridad y cumplimiento de proteccion de datos.
        String qrContenido = "CITA:" + cita.getIdCita();

        String html = plantillaConfirmacion(nombreCompleto, medico, especialidad, nroCita,
            cita.getFechaCita().toString(), cita.getHoraInicio(), cita.getEstado().toString());

        String textoPlano = "Confirmacion de cita medica\n"
            + "Paciente: " + nombreCompleto + "\n"
            + "Medico: " + medico + "\n"
            + "Especialidad: " + especialidad + "\n"
            + "Nro. cita: " + nroCita + "\n"
            + "Fecha: " + cita.getFechaCita() + "\n"
            + "Hora: " + cita.getHoraInicio() + "\n"
            + "Estado: " + cita.getEstado() + "\n";

        enviarHtml(pacienteEmail, "Trinidad Salud: Confirmacion de cita", html, textoPlano, qrContenido);
    }

    public void enviarRecordatorio(Cita cita) {
        String pacienteEmail = cita.getPaciente() != null ? cita.getPaciente().getEmail() : null;
        if (pacienteEmail == null || pacienteEmail.isBlank()) {
            log.info("[EMAIL] Paciente sin email. CitaID={}", cita.getIdCita());
            return;
        }

        String nombreCompleto = cita.getPaciente().getNombres() + " " + cita.getPaciente().getApellidoPaterno();
        String medico         = cita.getMedico() != null ? "Dr(a). " + cita.getMedico().getNombreCompleto() : "-";
        String nroCita        = "CIT-" + String.format("%05d", cita.getIdCita());

        // ⚠ El QR solo contiene el ID de referencia, NO datos personales (DNI, nombre, etc.)
        String qrContenido = "CITA:" + cita.getIdCita();

        String html = plantillaRecordatorio(nombreCompleto, medico, nroCita,
            cita.getFechaCita().toString(), cita.getHoraInicio());

        String textoPlano = "Recordatorio de cita\n"
            + "Paciente: " + nombreCompleto + "\n"
            + "Medico: " + medico + "\n"
            + "Nro. cita: " + nroCita + "\n"
            + "Fecha: " + cita.getFechaCita() + "\n"
            + "Hora: " + cita.getHoraInicio() + "\n";

        enviarHtml(pacienteEmail, "Trinidad Salud: Recordatorio de cita", html, textoPlano, qrContenido);
    }

    public void enviarRecuperacionPassword(String destino, String nombre, String link) {
        String html = "<!DOCTYPE html><html lang='es'><head><meta charset='UTF-8'></head>"
            + "<body style='margin:0;padding:0;background:#f1f5f9;font-family:\"Segoe UI\",Arial,sans-serif;'>"
            + "<table width='100%' cellpadding='0' cellspacing='0' style='background:#f1f5f9;padding:32px 0;'>"
            + "<tr><td align='center'>"
            + "<table width='600' cellpadding='0' cellspacing='0' style='max-width:600px;width:100%;'>"
            + "<tr><td style='background:linear-gradient(135deg,#0e7490,#0891b2);padding:36px 40px;"
            + "border-radius:12px 12px 0 0;text-align:center;'>"
            + "<p style='margin:0 0 6px;color:#bae6fd;font-size:12px;letter-spacing:2px;"
            + "text-transform:uppercase;font-weight:600;'>Sistema de Citas Medicas</p>"
            + "<h1 style='margin:0 0 4px;color:#fff;font-size:26px;font-weight:700;'>Trinidad Salud</h1>"
            + "<div style='display:inline-block;background:rgba(255,255,255,0.15);"
            + "border:2px solid rgba(255,255,255,0.4);border-radius:50px;padding:8px 22px;margin-top:16px;'>"
            + "<span style='color:#fff;font-size:15px;font-weight:600;'>Recuperacion de Contrasena</span>"
            + "</div></td></tr>"
            + "<tr><td style='background:#fff;padding:32px 40px;'>"
            + "<p style='margin:0 0 8px;font-size:18px;font-weight:700;color:#0f172a;'>"
            + "Hola <span style='color:#0e7490;'>" + nombre + "</span>,</p>"
            + "<p style='margin:0 0 24px;font-size:15px;color:#475569;line-height:1.7;'>"
            + "Hemos recibido una solicitud para restablecer tu contrasena. "
            + "Haz clic en el siguiente boton para crear una nueva contrasena:</p>"
            + "<table width='100%' cellpadding='0' cellspacing='0'>"
            + "<tr><td align='center' style='padding:0 0 24px;'>"
            + "<a href='" + link + "' style='display:inline-block;background:#0e7490;color:#fff;"
            + "text-decoration:none;padding:14px 36px;border-radius:8px;font-size:16px;font-weight:700;'>"
            + "Restablecer Contrasena</a></td></tr></table>"
            + "<p style='margin:0 0 16px;font-size:13px;color:#64748b;line-height:1.6;'>"
            + "Este enlace expirara en <strong>1 hora</strong>. Si no solicitaste este cambio, "
            + "ignora este mensaje.</p>"
            + "</td></tr>"
            + "<tr><td style='background:#0f172a;padding:24px 40px;border-radius:0 0 12px 12px;text-align:center;'>"
            + "<p style='margin:0 0 4px;color:#fff;font-size:15px;font-weight:700;'>Trinidad Salud</p>"
            + "<p style='margin:0;color:#475569;font-size:11px;'>"
            + "© 2026 Trinidad Salud — Todos los derechos reservados.</p>"
            + "</td></tr></table></td></tr></table></body></html>";

        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");
            helper.setFrom("\"Trinidad Salud\" <" + fromEmail + ">");
            helper.setTo(destino);
            helper.setSubject("Recuperacion de contrasena - Trinidad Salud");
            helper.setText(html, true);
            mailSender.send(mensaje);
            log.info("[EMAIL] Recuperacion enviada a {}", destino);
        } catch (Exception e) {
            log.error("[EMAIL] Error al enviar recuperacion a {}: {}", destino, e.getMessage());
        }
    }

    private String plantillaConfirmacion(String nombre, String medico, String especialidad,
                                          String nroCita, String fecha, String hora, String estado) {
        return baseHtml("Cita Confirmada",
            "<p style='margin:0 0 8px;font-size:18px;font-weight:700;color:#0f172a;'>"
            + "Estimado/a <span style='color:#0e7490;'>" + nombre + "</span>,</p>"
            + "<p style='margin:0 0 24px;font-size:15px;color:#475569;line-height:1.7;'>"
            + "Su cita médica ha sido <strong style='color:#0e7490;'>registrada y confirmada</strong> "
            + "exitosamente. A continuación los detalles de su atención:</p>"
            + tablaDetalle(
                                fila("Fecha",        fecha),
                                fila("Hora",         hora),
                                fila("Medico",       medico),
                                fila("Especialidad", especialidad),
                                fila("Estado",       estado),
                                filaDestacada("Nro. Cita", nroCita)
              ));
    }

    private String plantillaRecordatorio(String nombre, String medico,
                                          String nroCita, String fecha, String hora) {
        return baseHtml("Recordatorio de Cita",
            "<p style='margin:0 0 8px;font-size:18px;font-weight:700;color:#0f172a;'>"
            + "Estimado/a <span style='color:#0e7490;'>" + nombre + "</span>,</p>"
            + "<p style='margin:0 0 24px;font-size:15px;color:#475569;line-height:1.7;'>"
            + "Le recordamos que <strong>mañana</strong> tiene una cita médica programada:</p>"
            + tablaDetalle(
                                fila("Fecha",    fecha),
                                fila("Hora",     hora),
                                fila("Medico",   medico),
                                filaDestacada("Nro. Cita", nroCita)
              ));
    }

    private String baseHtml(String titulo, String contenido) {
        return "<!DOCTYPE html><html lang='es'><head><meta charset='UTF-8'></head>"
            + "<body style='margin:0;padding:0;background:#f1f5f9;font-family:\"Segoe UI\",Arial,sans-serif;'>"
            + "<table width='100%' cellpadding='0' cellspacing='0' style='background:#f1f5f9;padding:32px 0;'>"
            + "<tr><td align='center'>"
            + "<table width='600' cellpadding='0' cellspacing='0' style='max-width:600px;width:100%;'>"

            // Cabecera
            + "<tr><td style='background:linear-gradient(135deg,#0e7490,#0891b2);padding:36px 40px;"
            + "border-radius:12px 12px 0 0;text-align:center;'>"
            + "<p style='margin:0 0 6px;color:#bae6fd;font-size:12px;letter-spacing:2px;"
            + "text-transform:uppercase;font-weight:600;'>Sistema de Citas Médicas</p>"
            + "<h1 style='margin:0 0 4px;color:#fff;font-size:26px;font-weight:700;'>Trinidad Salud</h1>"
            + "<div style='display:inline-block;background:rgba(255,255,255,0.15);"
            + "border:2px solid rgba(255,255,255,0.4);border-radius:50px;padding:8px 22px;margin-top:16px;'>"
            + "<span style='color:#fff;font-size:15px;font-weight:600;'>" + titulo + "</span>"
            + "</div></td></tr>"

            // Contenido
            + "<tr><td style='background:#fff;padding:32px 40px 0;'>" + contenido + "</td></tr>"

            // Sección QR
            + "<tr><td style='background:#fff;padding:24px 40px 0;'>"
            + "<table width='100%' cellpadding='0' cellspacing='0' "
            + "style='background:#f0f9ff;border:2px dashed #0e7490;border-radius:12px;'>"
            + "<tr><td style='padding:24px;' align='center'>"
            + "<p style='margin:0 0 6px;font-size:12px;font-weight:700;color:#0e7490;"
            + "text-transform:uppercase;letter-spacing:1px;'>Código QR de su Cita</p>"
            + "<p style='margin:0 0 16px;font-size:12px;color:#64748b;'>"
            + "Presente este código en recepción para agilizar su registro</p>"
            + "<img src='cid:qr-cita' alt='QR Cita' width='200' height='200' "
            + "style='display:block;margin:0 auto;border-radius:8px;border:3px solid #0e7490;'/>"
            + "</td></tr></table></td></tr>"

            // Instrucciones
            + "<tr><td style='background:#fff;padding:20px 40px 28px;'>"
            + "<table width='100%' cellpadding='0' cellspacing='0' "
            + "style='background:#fffbeb;border-left:4px solid #f59e0b;border-radius:0 8px 8px 0;'>"
            + "<tr><td style='padding:16px 20px;'>"
            + "<p style='margin:0 0 10px;font-size:13px;font-weight:700;color:#92400e;'>Instrucciones importantes</p>"
            + "<ul style='margin:0;padding-left:18px;color:#78350f;font-size:13px;line-height:1.9;'>"
            + "<li>Preséntese <strong>10 minutos antes</strong> de su cita en recepción.</li>"
            + "<li>Traiga su <strong>DNI o documento de identidad</strong> original.</li>"
            + "<li>Para cancelar, hágalo con <strong>24 horas de anticipación</strong>.</li>"
            + "<li>Traiga resultados de exámenes previos si los tuviera.</li>"
            + "</ul></td></tr></table></td></tr>"

            // Footer
            + "<tr><td style='background:#0f172a;padding:24px 40px;border-radius:0 0 12px 12px;text-align:center;'>"
            + "<p style='margin:0 0 4px;color:#fff;font-size:15px;font-weight:700;'>Trinidad Salud</p>"
            + "<p style='margin:0 0 12px;color:#64748b;font-size:12px;'>Sistema de Citas Médicas</p>"
            + "<p style='margin:0;color:#475569;font-size:11px;line-height:1.6;'>"
            + "Este es un correo automático, por favor no responda directamente.<br>"
            + "© 2026 Trinidad Salud — Todos los derechos reservados.</p>"
            + "</td></tr>"

            + "</table></td></tr></table></body></html>";
    }

    private String tablaDetalle(String... filas) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table width='100%' cellpadding='0' cellspacing='0' "
            + "style='border-radius:10px;overflow:hidden;border:1px solid #e2e8f0;margin-bottom:20px;'>"
            + "<tr style='background:#0e7490;'>"
            + "<td colspan='2' style='padding:12px 20px;'>"
            + "<p style='margin:0;color:#fff;font-size:12px;font-weight:700;letter-spacing:1px;"
            + "text-transform:uppercase;'>Informacion de la Cita</p>"
            + "</td></tr>");
        for (String fila : filas) sb.append(fila);
        sb.append("</table>");
        return sb.toString();
    }

    private String fila(String etiqueta, String valor) {
        return "<tr>"
            + "<td style='padding:12px 20px;background:#f0f9ff;color:#64748b;font-size:13px;"
            + "font-weight:600;width:42%;border-bottom:1px solid #e2e8f0;'>" + etiqueta + "</td>"
            + "<td style='padding:12px 20px;background:#fff;color:#0f172a;font-size:14px;"
            + "font-weight:600;border-bottom:1px solid #e2e8f0;'>" + valor + "</td>"
            + "</tr>";
    }

    private String filaDestacada(String etiqueta, String valor) {
        return "<tr>"
            + "<td style='padding:12px 20px;background:#f0f9ff;color:#64748b;font-size:13px;"
            + "font-weight:600;width:42%;'>" + etiqueta + "</td>"
            + "<td style='padding:12px 20px;background:#fff;color:#0e7490;font-size:14px;"
            + "font-weight:700;'>" + valor + "</td>"
            + "</tr>";
    }

    private void enviarHtml(String destino, String asunto, String htmlBody, String plainTextBody, String qrContenido) {
        try {
            byte[] qrBytes = generarQR(qrContenido, 320);
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, MimeMessageHelper.MULTIPART_MODE_RELATED, "UTF-8");
            helper.setFrom(new InternetAddress(fromEmail, "Trinidad Salud"));
            helper.setTo(destino);
            helper.setReplyTo(fromEmail);
            helper.setSubject(asunto);
            helper.setText(plainTextBody + "\nCodigo de cita: " + qrContenido, htmlBody);
            helper.addInline("qr-cita", new ByteArrayResource(qrBytes), "image/png");
            mensaje.setSentDate(new Date());
            mensaje.addHeader("X-Auto-Response-Suppress", "All");
            mensaje.addHeader("Auto-Submitted", "auto-generated");
            mailSender.send(mensaje);
            log.info("[EMAIL] Enviado exitosamente a {} | Asunto: {} | Remitente: {}", destino, asunto, fromEmail);
        } catch (Exception e) {
            log.error("[EMAIL] Error al enviar a {}: {}", destino, e.getMessage());
        }
    }

    private byte[] generarQR(String contenido, int tamanio) throws Exception {
        QRCodeWriter writer = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix matrix = writer.encode(contenido, BarcodeFormat.QR_CODE, tamanio, tamanio, hints);
        // Negro sobre blanco maximiza legibilidad en clientes que recomprimen imágenes.
        MatrixToImageConfig config = new MatrixToImageConfig(0xFF000000, 0xFFFFFFFF);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", baos, config);
        return baos.toByteArray();
    }
}
