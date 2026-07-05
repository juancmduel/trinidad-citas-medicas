package com.trinidad.citas.service;

import com.trinidad.citas.audit.Auditable;
import com.trinidad.citas.dto.CitaDTO;
import com.trinidad.citas.exception.BusinessException;
import com.trinidad.citas.exception.ResourceNotFoundException;
import com.trinidad.citas.model.*;
import com.trinidad.citas.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class CitaService {

    private final CitaRepository citaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final EspecialidadRepository especialidadRepository;
    private final EmailService emailService;

    private static final DateTimeFormatter HORA_FMT = DateTimeFormatter.ofPattern("HH:mm");

    // ── Máquina de estados: transiciones permitidas entre estados de cita ──
    private static final Map<EstadoCita, Set<EstadoCita>> TRANSICIONES_VALIDAS = Map.of(
        EstadoCita.PROGRAMADA,    Set.of(EstadoCita.CONFIRMADA, EstadoCita.CANCELADA),
        EstadoCita.CONFIRMADA,    Set.of(EstadoCita.EN_TRIAGE, EstadoCita.CANCELADA),
        EstadoCita.EN_TRIAGE,     Set.of(EstadoCita.EN_ATENCION, EstadoCita.CANCELADA),
        EstadoCita.EN_ATENCION,   Set.of(EstadoCita.ATENDIDA, EstadoCita.CANCELADA),
        EstadoCita.ATENDIDA,      Set.of(),
        EstadoCita.CANCELADA,     Set.of(),
        EstadoCita.NO_ASISTIO,    Set.of(EstadoCita.PROGRAMADA),   // reprog
        EstadoCita.REPROGRAMADA,  Set.of(EstadoCita.PROGRAMADA)
    );

    public CitaDTO toDTO(Cita c) {
        CitaDTO dto = new CitaDTO();
        dto.setIdCita(c.getIdCita());
        dto.setFechaCita(c.getFechaCita());
        dto.setHoraInicio(c.getHoraInicio());
        dto.setMotivoConsulta(c.getMotivoConsulta());
        dto.setCanalReserva(c.getCanalReserva());
        dto.setEstado(c.getEstado() != null ? c.getEstado().name() : null);
        if (c.getPaciente() != null) {
            dto.setIdPaciente(c.getPaciente().getIdPaciente());
            dto.setNombrePaciente(c.getPaciente().getApellidoPaterno() + " " + c.getPaciente().getNombres());
            dto.setDniPaciente(c.getPaciente().getDni());
        }
        if (c.getMedico() != null) {
            dto.setIdMedico(c.getMedico().getIdMedico());
            dto.setNombreMedico("Dr. " + c.getMedico().getApellidoPaterno() + ", " + c.getMedico().getNombres());
        }
        if (c.getEspecialidad() != null) {
            dto.setIdEspecialidad(c.getEspecialidad().getIdEspecialidad());
            dto.setNombreEspecialidad(c.getEspecialidad().getNombre());
        }
        return dto;
    }

    @Transactional(readOnly = true)
    public List<CitaDTO> listarTodas() {
        return citaRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<CitaDTO> listarTodasConRelaciones() {
        return citaRepository.findAllConRelaciones().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<CitaDTO> listarPorFechaConRelaciones(LocalDate fecha) {
        return citaRepository.findByFechaConRelaciones(fecha).stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<CitaDTO> listarPorPaciente(Long idPaciente) {
        return citaRepository.findByPaciente_IdPacienteOrderByFechaCitaDescHoraInicioDesc(idPaciente)
            .stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<CitaDTO> listarPorMedicoConRelaciones(Long idMedico) {
        return citaRepository.findByMedico_IdMedicoConRelaciones(idMedico).stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<CitaDTO> listarPorMedicoYFecha(Long idMedico, LocalDate fecha) {
        return citaRepository.findByMedico_IdMedicoAndFechaCitaOrderByHoraInicioAsc(idMedico, fecha)
            .stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<CitaDTO> listarPorFecha(LocalDate fecha) {
        return citaRepository.findByFechaCitaOrderByHoraInicioAsc(fecha).stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<Cita> listarEntidadesPorEstado(EstadoCita estado) {
        return citaRepository.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    public List<Object[]> contarAgrupadoPorFechaEntre(LocalDate desde, LocalDate hasta) {
        return citaRepository.countByFechaCitaBetween(desde, hasta);
    }

    @Transactional(readOnly = true)
    public CitaDTO obtenerDTO(Long id) {
        return toDTO(obtenerEntidad(id));
    }

    @Transactional(readOnly = true)
    public Cita obtenerEntidad(Long id) {
        return citaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cita", id));
    }

    @Transactional(readOnly = true)
    public Cita obtener(Long id) {
        return obtenerEntidad(id);
    }

    @Auditable(entidad = "CITA", accion = "CREAR")
    public CitaDTO agendar(CitaDTO dto) {
        Cita cita = agendarEntidad(dto);
        emailService.enviarConfirmacionCita(cita);
        return toDTO(cita);
    }

    private Cita agendarEntidad(CitaDTO dto) {
        Paciente paciente = pacienteRepository.findById(dto.getIdPaciente())
            .orElseThrow(() -> new ResourceNotFoundException("Paciente", dto.getIdPaciente()));
        Medico medico = medicoRepository.findById(dto.getIdMedico())
            .orElseThrow(() -> new ResourceNotFoundException("Medico", dto.getIdMedico()));
        Especialidad especialidad = especialidadRepository.findById(dto.getIdEspecialidad())
            .orElseThrow(() -> new ResourceNotFoundException("Especialidad", dto.getIdEspecialidad()));
        LocalTime horaInicio = LocalTime.parse(dto.getHoraInicio(), HORA_FMT);
        LocalTime horaFin = horaInicio.plusMinutes(especialidad.getDuracionMinutos());

        // Validar que el médico pertenezca a la especialidad seleccionada (ME-03)
        if (medico.getEspecialidad() == null || !medico.getEspecialidad().getIdEspecialidad().equals(dto.getIdEspecialidad())) {
            throw new BusinessException(
                "El médico " + medico.getNombreCompleto() + " no pertenece a la especialidad " +
                especialidad.getNombre() + ". Seleccione un médico válido para esta especialidad.");
        }

        // Validar que la cita sea en el futuro
        LocalDate hoy = LocalDate.now();
        if (dto.getFechaCita().isBefore(hoy)) {
            throw new BusinessException("No se puede agendar una cita en una fecha pasada");
        }
        if (dto.getFechaCita().equals(hoy) && horaInicio.isBefore(LocalTime.now())) {
            throw new BusinessException("La hora de la cita no puede estar en el pasado");
        }
        // Usar PESSIMISTIC_WRITE lock para evitar condicion de carrera (AL-08)
        List<Cita> citasDelMedico = citaRepository.findByMedico_IdMedicoAndFechaCitaComLock(
            dto.getIdMedico(), dto.getFechaCita());
        for (Cita existente : citasDelMedico) {
            LocalTime inicioExistente = LocalTime.parse(existente.getHoraInicio(), HORA_FMT);
            LocalTime finExistente = LocalTime.parse(existente.getHoraFin(), HORA_FMT);
            if (horaInicio.isBefore(finExistente) && inicioExistente.isBefore(horaFin)) {
                throw new BusinessException(
                    "El médico ya tiene una cita el " + dto.getFechaCita() +
                    " de " + existente.getHoraInicio() + " a " + existente.getHoraFin() +
                    ". Por favor, seleccione otro horario disponible.");
            }
        }
        // RN-XX: No permitir agendar con el mismo médico si hay cita activa en los últimos 7 días
        // (política de la clínica para evitar sobrecarga de pacientes por médico)
        List<Cita> citasRecientes = citaRepository.findActivasPacienteMedicoDesde(
            dto.getIdPaciente(), dto.getIdMedico(), LocalDate.now().minusDays(7));
        if (!citasRecientes.isEmpty()) {
            Cita existente = citasRecientes.get(0);
            throw new BusinessException(
                "El paciente ya tiene una cita activa con este médico el " +
                existente.getFechaCita() + " a las " + existente.getHoraInicio() +
                " (estado: " + existente.getEstado() + "). " +
                "Puede cancelar esa cita primero o elegir otro médico.");
        }
        Cita cita = Cita.builder()
            .paciente(paciente).medico(medico).especialidad(especialidad)
            .fechaCita(dto.getFechaCita()).horaInicio(dto.getHoraInicio())
            .horaFin(horaFin.format(HORA_FMT)).estado(EstadoCita.PROGRAMADA)
            .motivoConsulta(dto.getMotivoConsulta())
            .canalReserva(dto.getCanalReserva() != null ? dto.getCanalReserva() : "WEB")
            .build();
        return citaRepository.save(cita);
    }

    @Auditable(entidad = "CITA", accion = "CAMBIAR_ESTADO")
    public CitaDTO cambiarEstado(Long idCita, EstadoCita nuevoEstado) {
        Cita c = obtener(idCita);
        EstadoCita actual = c.getEstado();
        Set<EstadoCita> permitidos = TRANSICIONES_VALIDAS.get(actual);
        if (permitidos == null || !permitidos.contains(nuevoEstado)) {
            throw new BusinessException(
                "Transición de estado inválida: de " + actual + " a " + nuevoEstado +
                ". Transiciones permitidas desde " + actual + ": " +
                (permitidos != null ? permitidos : "ninguna"));
        }
        c.setEstado(nuevoEstado);
        return toDTO(citaRepository.save(c));
    }

    @Auditable(entidad = "CITA", accion = "CANCELAR")
    public CitaDTO cancelar(Long idCita) {
        return cambiarEstado(idCita, EstadoCita.CANCELADA);
    }

    @Auditable(entidad = "CITA", accion = "CONFIRMAR_PAGO")
    public CitaDTO confirmarPago(Long idCita) {
        Cita c = obtenerEntidad(idCita);
        if (c.getEstado() != EstadoCita.PROGRAMADA) {
            throw new BusinessException("Solo se puede confirmar el pago de una cita que esté PROGRAMADA");
        }
        c.setEstado(EstadoCita.CONFIRMADA);
        return toDTO(citaRepository.save(c));
    }

    @Auditable(entidad = "CITA", accion = "CHECKIN")
    public CitaDTO checkin(Long idCita) {
        Cita c = obtenerEntidad(idCita);
        if (c.getEstado() != EstadoCita.CONFIRMADA) {
            throw new BusinessException("El check-in solo puede registrarse en citas CONFIRMADA");
        }
        if (c.getFechaCheckin() != null) {
            throw new BusinessException("Esta cita ya tiene un check-in registrado");
        }
        c.setFechaCheckin(LocalDateTime.now());
        c.setEstado(EstadoCita.EN_TRIAGE);
        return toDTO(citaRepository.save(c));
    }

    @Auditable(entidad = "CITA", accion = "FINALIZAR")
    public CitaDTO finalizar(Long idCita) {
        Cita c = obtenerEntidad(idCita);
        if (c.getEstado() != EstadoCita.EN_ATENCION) {
            throw new BusinessException("Solo se puede finalizar una cita que esté EN_ATENCION");
        }
        c.setEstado(EstadoCita.ATENDIDA);
        return toDTO(citaRepository.save(c));
    }
}
