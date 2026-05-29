package com.trinidad.citas.scheduler;

import com.trinidad.citas.model.Cita;
import com.trinidad.citas.model.EstadoCita;
import com.trinidad.citas.repository.CitaRepository;
import com.trinidad.citas.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Jobs automáticos del sistema de citas.
 * RN-11: Marcar NO_ASISTIO si el paciente no hace check-in en 30 minutos.
 * RN-12: Enviar recordatorio 24h antes (log de auditoría en esta versión).
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CitaScheduler {

    private final CitaRepository citaRepository;
    private final EmailService emailService;

    private static final DateTimeFormatter HORA_FMT = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * RN-11: Cada minuto revisa citas sin check-in pasados 30 minutos
     * y las marca como NO_ASISTIO.
     */
    @Scheduled(fixedRate = 60_000)
    @Transactional
    public void marcarNoAsistidas() {
        LocalDate hoy = LocalDate.now();
        // Límite: hora actual menos 30 minutos
        String limiteHora = LocalTime.now().minusMinutes(30).format(HORA_FMT);

        List<Cita> pendientes = citaRepository.findCitasParaMarcarNoShow(hoy, limiteHora);

        if (!pendientes.isEmpty()) {
            pendientes.forEach(cita -> {
                cita.setEstado(EstadoCita.NO_ASISTIO);
                citaRepository.save(cita);
                log.info("[SCHEDULER] Cita ID={} del paciente DNI={} marcada como NO_ASISTIO (sin check-in tras 30 min)",
                    cita.getIdCita(),
                    cita.getPaciente() != null ? cita.getPaciente().getDni() : "desconocido");
            });
            log.info("[SCHEDULER] marcarNoAsistidas: {} citas marcadas como NO_ASISTIO", pendientes.size());
        }
    }

    /**
     * RN-12: Cada día a las 08:00 AM registra en log las citas del día siguiente
     * que están CONFIRMADAS (base para envío de recordatorios cuando se integre email/SMS).
     */
    @Scheduled(cron = "0 0 8 * * *")
    @Transactional(readOnly = true)
    public void registrarRecordatoriosPendientes() {
        LocalDate manana = LocalDate.now().plusDays(1);
        List<Cita> citasManana = citaRepository.findByFechaCitaAndEstado(manana, EstadoCita.CONFIRMADA);

        log.info("[SCHEDULER] recordatorios 24h: {} citas CONFIRMADAS para mañana {}", citasManana.size(), manana);
        citasManana.forEach(cita -> {
            log.info("[RECORDATORIO] Cita ID={} | Paciente={} | Médico={} | Hora={}",
                cita.getIdCita(),
                cita.getPaciente() != null ? cita.getPaciente().getNombreCompleto() : "-",
                cita.getMedico()   != null ? cita.getMedico().getNombreCompleto()   : "-",
                cita.getHoraInicio());
            emailService.enviarRecordatorio(cita);
        });
    }
}
