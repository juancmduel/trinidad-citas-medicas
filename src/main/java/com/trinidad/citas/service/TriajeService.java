package com.trinidad.citas.service;

import com.trinidad.citas.audit.Auditable;
import com.trinidad.citas.dto.TriajeDTO;
import com.trinidad.citas.exception.BusinessException;
import com.trinidad.citas.exception.ResourceNotFoundException;
import com.trinidad.citas.model.*;
import com.trinidad.citas.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TriajeService {

    private final TriajeRepository triajeRepository;
    private final CitaRepository citaRepository;
    private final UsuarioRepository usuarioRepository;

    public TriajeDTO toDTO(Triaje t) {
        TriajeDTO dto = new TriajeDTO();
        dto.setIdTriaje(t.getIdTriaje());
        dto.setIdCita(t.getCita().getIdCita());
        dto.setIdEnfermera(t.getEnfermera() != null ? t.getEnfermera().getIdUsuario() : null);
        dto.setFechaTriaje(t.getFechaTriaje());
        dto.setPresionArterial(t.getPresionArterial());
        dto.setFrecuenciaCardiaca(t.getFrecuenciaCardiaca());
        dto.setTemperatura(t.getTemperatura());
        dto.setPesoKg(t.getPesoKg());
        dto.setTallaCm(t.getTallaCm());
        dto.setSaturacionO2(t.getSaturacionO2());
        dto.setGlucosa(t.getGlucosa());
        dto.setSintomas(t.getSintomas());
        dto.setAlergiasReportadas(t.getAlergiasReportadas());
        dto.setMedicacionActual(t.getMedicacionActual());
        dto.setNivelUrgencia(t.getNivelUrgencia());
        dto.setObservaciones(t.getObservaciones());
        if (t.getCita().getPaciente() != null) {
            dto.setNombrePaciente(t.getCita().getPaciente().getNombreCompleto());
            dto.setDniPaciente(t.getCita().getPaciente().getDni());
        }
        if (t.getCita().getMedico() != null) {
            dto.setNombreMedico(t.getCita().getMedico().getNombreCompleto());
            dto.setConsultorio(t.getCita().getMedico().getConsultorio());
        }
        if (t.getCita().getEspecialidad() != null) {
            dto.setEspecialidad(t.getCita().getEspecialidad().getNombre());
        }
        return dto;
    }

    @Transactional(readOnly = true)
    public List<TriajeDTO> listarTodos() {
        return triajeRepository.findAllWithRelations().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public TriajeDTO obtenerPorId(Long id) {
        return toDTO(triajeRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Triaje", id)));
    }

    @Transactional(readOnly = true)
    public TriajeDTO obtenerPorCita(Long idCita) {
        return triajeRepository.findByCita_IdCita(idCita)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Triaje para cita", idCita));
    }

    @Auditable(entidad = "TRIAJE", accion = "CREAR")
    public TriajeDTO registrar(Long idCita, Long idEnfermera, TriajeDTO dto) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new ResourceNotFoundException("Cita", idCita));

        if (cita.getEstado() != EstadoCita.EN_TRIAGE) {
            throw new BusinessException("El triaje solo puede registrarse cuando la cita está EN_TRIAGE");
        }

        if (triajeRepository.findByCita_IdCita(idCita).isPresent()) {
            throw new BusinessException("Esta cita ya tiene un triaje registrado. Si necesita corregirlo, edítelo.");
        }

        Triaje triaje = new Triaje();
        triaje.setCita(cita);
        if (idEnfermera != null) {
            triaje.setEnfermera(usuarioRepository.findById(idEnfermera)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario enfermera", idEnfermera)));
        }
        triaje.setFechaTriaje(LocalDateTime.now());
        triaje.setPresionArterial(dto.getPresionArterial());
        triaje.setFrecuenciaCardiaca(dto.getFrecuenciaCardiaca());
        triaje.setTemperatura(dto.getTemperatura());
        triaje.setPesoKg(dto.getPesoKg());
        triaje.setTallaCm(dto.getTallaCm());
        triaje.setSaturacionO2(dto.getSaturacionO2());
        triaje.setGlucosa(dto.getGlucosa());
        triaje.setSintomas(dto.getSintomas());
        triaje.setAlergiasReportadas(dto.getAlergiasReportadas());
        triaje.setMedicacionActual(dto.getMedicacionActual());
        triaje.setNivelUrgencia(dto.getNivelUrgencia());
        triaje.setObservaciones(dto.getObservaciones());

        TriajeDTO saved = toDTO(triajeRepository.save(triaje));

        cita.setEstado(EstadoCita.EN_ATENCION);
        citaRepository.save(cita);

        return saved;
    }
}
