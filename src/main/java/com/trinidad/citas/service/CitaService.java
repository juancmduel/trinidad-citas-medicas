package com.trinidad.citas.service;

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
        citaRepository.findByMedico_IdMedicoAndFechaCitaAndHoraInicio(
            dto.getIdMedico(), dto.getFechaCita(), dto.getHoraInicio())
            .ifPresent(c -> { throw new BusinessException(
                "El medico ya tiene una cita programada el " + dto.getFechaCita() + " a las " + dto.getHoraInicio()); });
        List<Cita> citasRecientes = citaRepository.findActivasPacienteMedicoDesde(
            dto.getIdPaciente(), dto.getIdMedico(), LocalDate.now().minusDays(7));
        if (!citasRecientes.isEmpty()) {
            Cita existente = citasRecientes.get(0);
            throw new BusinessException(
                "El paciente ya tiene una cita activa con este medico: " +
                existente.getFechaCita() + " a las " + existente.getHoraInicio() +
                " (estado: " + existente.getEstado() + "). " +
                "Cancele esa cita antes de agendar una nueva, o elija otro medico.");
        }
        LocalTime horaInicio = LocalTime.parse(dto.getHoraInicio(), HORA_FMT);
        LocalTime horaFin = horaInicio.plusMinutes(especialidad.getDuracionMinutos());
        Cita cita = Cita.builder()
            .paciente(paciente).medico(medico).especialidad(especialidad)
            .fechaCita(dto.getFechaCita()).horaInicio(dto.getHoraInicio())
            .horaFin(horaFin.format(HORA_FMT)).estado(EstadoCita.PROGRAMADA)
            .motivoConsulta(dto.getMotivoConsulta())
            .canalReserva(dto.getCanalReserva() != null ? dto.getCanalReserva() : "WEB")
            .build();
        return citaRepository.save(cita);
    }

    public CitaDTO cambiarEstado(Long idCita, EstadoCita nuevoEstado) {
        Cita c = obtener(idCita);
        c.setEstado(nuevoEstado);
        return toDTO(citaRepository.save(c));
    }

    public CitaDTO cancelar(Long idCita) {
        return cambiarEstado(idCita, EstadoCita.CANCELADA);
    }

    public CitaDTO confirmarPago(Long idCita) {
        Cita c = obtenerEntidad(idCita);
        if (c.getEstado() != EstadoCita.PROGRAMADA) {
            throw new BusinessException("Solo se puede confirmar una cita en estado PROGRAMADA");
        }
        c.setEstado(EstadoCita.CONFIRMADA);
        return toDTO(citaRepository.save(c));
    }

    public CitaDTO checkin(Long idCita) {
        Cita c = obtenerEntidad(idCita);
        if (c.getEstado() != EstadoCita.CONFIRMADA) {
            throw new BusinessException("Solo se puede registrar check-in en citas CONFIRMADA");
        }
        if (c.getFechaCheckin() != null) {
            throw new BusinessException("El check-in ya fue registrado para esta cita");
        }
        c.setFechaCheckin(LocalDateTime.now());
        c.setEstado(EstadoCita.EN_ATENCION);
        return toDTO(citaRepository.save(c));
    }

    public CitaDTO finalizar(Long idCita) {
        Cita c = obtenerEntidad(idCita);
        if (c.getEstado() != EstadoCita.EN_ATENCION) {
            throw new BusinessException("Solo se puede finalizar una cita en estado EN_ATENCION");
        }
        c.setEstado(EstadoCita.ATENDIDA);
        return toDTO(citaRepository.save(c));
    }
}
