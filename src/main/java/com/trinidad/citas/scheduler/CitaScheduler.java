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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Tareas programadas que se ejecutan automáticamente.
 *
 * Como el robot de la clínica que trabaja 24/7 sin quejarse.
 *
 * Por ahora hace dos cosas:
 *  1. Cada minuto revisa si hay citas que deberían marcarse como NO_ASISTIO
 *     (pacientes que no llegaron 30 min después de su hora)
 *  2. Cada día a las 8:00 AM envía recordatorios por correo a los pacientes
 *     que tienen cita al día siguiente
 *
 * Ambas son configurables desde application.properties si se necesita.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CitaScheduler {

    private final CitaRepository citaRepository;
    private final EmailService emailService;

    /** Zona horaria del sistema Perú. Esto es importante porque
     *  el servidor podría estar en UTC y nosotros necesitamos hora peruana. */
    private static final ZoneId ZONA = ZoneId.of("America/Lima");
    private static final DateTimeFormatter HORA_FMT = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Cada 60 segundos revisa si hay citas CONFIRMADAS de hoy cuya hora
     * ya pasó hace más de 30 minutos y no tienen check-in.
     * Esas se marcan como NO_ASISTIO automáticamente.
     *
     * Así liberamos el cupo para otros pacientes y mantenemos
     * la estadística de inasistencias.
     */
    @Scheduled(fixedDelay = 60_000)
    @Transactional
    public void marcarNoAsistidas() {
        LocalDate hoy = LocalDate.now(ZONA);
        String limiteHora = LocalTime.now(ZONA).minusMinutes(30).format(HORA_FMT);

        List<Cita> pendientes = citaRepository.findCitasParaMarcarNoShow(hoy, limiteHora);

        if (!pendientes.isEmpty()) {
            pendientes.forEach(cita -> {
                cita.setEstado(EstadoCita.NO_ASISTIO);
                citaRepository.save(cita);
                log.info("⏰ Cita #{} del paciente {} marcada como NO_ASISTIO (30 min sin check-in)",
                    cita.getIdCita(),
                    cita.getPaciente() != null ? cita.getPaciente().getDni() : "desconocido");
            });
            log.info("⏰ Tarea 'no-show': {} citas marcadas como NO_ASISTIO", pendientes.size());
        }
    }

    /**
     * A las 8:00 AM (hora peruana) envía correos recordando a los pacientes
     * que tienen cita al día siguiente.
     *
     * Así reducimos las inasistencias porque la gente tiende a olvidar
     * sus citas médicas.
     */
    @Scheduled(cron = "0 0 8 * * *", zone = "America/Lima")
    @Transactional(readOnly = true)
    public void registrarRecordatoriosPendientes() {
        LocalDate manana = LocalDate.now(ZONA).plusDays(1);
        List<Cita> citasManana = citaRepository.findByFechaCitaAndEstado(manana, EstadoCita.CONFIRMADA);

        log.info("📧 Recordatorios 24h: {} citas CONFIRMADAS para mañana {}", citasManana.size(), manana);
        citasManana.forEach(cita -> {
            log.info("📧 Recordatorio: Cita #{} | Paciente: {} | Médico: {} | Hora: {}",
                cita.getIdCita(),
                cita.getPaciente() != null ? cita.getPaciente().getNombreCompleto() : "-",
                cita.getMedico()   != null ? cita.getMedico().getNombreCompleto()   : "-",
                cita.getHoraInicio());
            emailService.enviarRecordatorio(cita);
        });
    }
}
