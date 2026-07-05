package com.trinidad.citas.service;

import com.trinidad.citas.audit.Auditable;
import com.trinidad.citas.dto.HorarioMedicoDTO;
import com.trinidad.citas.exception.BusinessException;
import com.trinidad.citas.exception.ResourceNotFoundException;
import com.trinidad.citas.model.HorarioMedico;
import com.trinidad.citas.repository.HorarioMedicoRepository;
import com.trinidad.citas.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class HorarioMedicoService {

    private final HorarioMedicoRepository horarioMedicoRepository;
    private final MedicoRepository medicoRepository;

    private static final String[] DIAS = {"", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};

    public HorarioMedicoDTO toDTO(HorarioMedico h) {
        HorarioMedicoDTO dto = new HorarioMedicoDTO();
        dto.setIdHorario(h.getIdHorario());
        dto.setIdMedico(h.getMedico().getIdMedico());
        dto.setDiaSemana(h.getDiaSemana());
        dto.setHoraInicio(h.getHoraInicio());
        dto.setHoraFin(h.getHoraFin());
        dto.setActivo(h.getActivo());
        dto.setMedicoNombreCompleto(h.getMedico().getNombreCompleto());
        dto.setDiaSemanaDescripcion(h.getDiaSemana() >= 1 && h.getDiaSemana() <= 7 ? DIAS[h.getDiaSemana()] : "");
        return dto;
    }

    @Transactional(readOnly = true)
    public List<HorarioMedicoDTO> listarTodos() {
        return horarioMedicoRepository.findAllWithMedico().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HorarioMedicoDTO> listarPorMedico(Long idMedico) {
        return horarioMedicoRepository.findByMedico_IdMedicoAndActivo(idMedico, 1)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public HorarioMedicoDTO obtenerPorId(Long id) {
        return toDTO(obtenerEntidadPorId(id));
    }

    @Transactional(readOnly = true)
    public HorarioMedico obtenerEntidadPorId(Long id) {
        return horarioMedicoRepository.findByIdWithMedico(id)
                .orElseThrow(() -> new ResourceNotFoundException("HorarioMedico", id));
    }

    @Auditable(entidad = "HORARIO", accion = "CREAR")
    public HorarioMedicoDTO crear(HorarioMedicoDTO dto) {
        // Validar que la hora de inicio sea anterior a la hora de fin
        LocalTime inicio = LocalTime.parse(dto.getHoraInicio(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime fin = LocalTime.parse(dto.getHoraFin(), DateTimeFormatter.ofPattern("HH:mm"));
        if (!inicio.isBefore(fin)) {
            throw new BusinessException("La hora de inicio debe ser anterior a la hora de fin");
        }

        // Validar que no se solape con otro horario del mismo médico el mismo día
        List<HorarioMedico> existentes = horarioMedicoRepository
                .findByMedico_IdMedicoAndDiaSemanaAndActivo(dto.getIdMedico(), dto.getDiaSemana(), 1);
        for (HorarioMedico existente : existentes) {
            LocalTime existenteInicio = LocalTime.parse(existente.getHoraInicio(), DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime existenteFin = LocalTime.parse(existente.getHoraFin(), DateTimeFormatter.ofPattern("HH:mm"));
            if (inicio.isBefore(existenteFin) && existenteInicio.isBefore(fin)) {
                throw new BusinessException(
                    "El horario se solapa con otro existente (" + existente.getHoraInicio() +
                    " - " + existente.getHoraFin() + "). Ajuste las horas.");
            }
        }

        HorarioMedico h = new HorarioMedico();
        h.setMedico(medicoRepository.findById(dto.getIdMedico())
                .orElseThrow(() -> new ResourceNotFoundException("Medico", dto.getIdMedico())));
        h.setDiaSemana(dto.getDiaSemana());
        h.setHoraInicio(dto.getHoraInicio());
        h.setHoraFin(dto.getHoraFin());
        h.setActivo(dto.getActivo() != null ? dto.getActivo() : 1);
        return toDTO(horarioMedicoRepository.save(h));
    }

    public HorarioMedicoDTO actualizar(Long id, HorarioMedicoDTO dto) {
        HorarioMedico h = horarioMedicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HorarioMedico", id));

        // Validar que la hora de inicio sea anterior a la hora de fin
        LocalTime inicio = LocalTime.parse(dto.getHoraInicio(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime fin = LocalTime.parse(dto.getHoraFin(), DateTimeFormatter.ofPattern("HH:mm"));
        if (!inicio.isBefore(fin)) {
            throw new BusinessException("La hora de inicio debe ser anterior a la hora de fin");
        }

        h.setDiaSemana(dto.getDiaSemana());
        h.setHoraInicio(dto.getHoraInicio());
        h.setHoraFin(dto.getHoraFin());
        if (dto.getActivo() != null) h.setActivo(dto.getActivo());
        return toDTO(horarioMedicoRepository.save(h));
    }

    @Auditable(entidad = "HORARIO", accion = "ELIMINAR")
    public void eliminar(Long id) {
        horarioMedicoRepository.deleteById(id);
    }
}
