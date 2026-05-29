package com.trinidad.citas.service;

import com.trinidad.citas.dto.AtencionDTO;
import com.trinidad.citas.exception.ResourceNotFoundException;
import com.trinidad.citas.model.Atencion;
import com.trinidad.citas.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AtencionService {

    private final AtencionRepository atencionRepository;
    private final CitaRepository citaRepository;
    private final HistoriaClinicaRepository historiaClinicaRepository;
    private final MedicoRepository medicoRepository;
    private final DiagnosticoCie10Repository diagnosticoCie10Repository;

    public AtencionDTO toDTO(Atencion a) {
        AtencionDTO dto = new AtencionDTO();
        dto.setIdAtencion(a.getIdAtencion());
        dto.setIdCita(a.getCita().getIdCita());
        dto.setIdHistoria(a.getHistoria().getIdHistoria());
        dto.setIdMedico(a.getMedico().getIdMedico());
        dto.setFechaAtencion(a.getFechaAtencion());
        dto.setMotivoConsulta(a.getMotivoConsulta());
        dto.setAnamnesis(a.getAnamnesis());
        dto.setExamenFisico(a.getExamenFisico());
        dto.setDiagnosticoCie10Codigo(a.getDiagnosticoCie10() != null ? a.getDiagnosticoCie10().getCodigo() : null);
        dto.setDiagnosticoDesc(a.getDiagnosticoDesc());
        dto.setTratamiento(a.getTratamiento());
        dto.setObservaciones(a.getObservaciones());
        dto.setPresionArterial(a.getPresionArterial());
        dto.setFrecuenciaCardiaca(a.getFrecuenciaCardiaca());
        dto.setTemperatura(a.getTemperatura());
        dto.setPesoKg(a.getPesoKg());
        dto.setTallaCm(a.getTallaCm());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<AtencionDTO> listarTodos() {
        return atencionRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AtencionDTO obtenerPorId(Long id) {
        return toDTO(atencionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Atencion", id)));
    }

    @Transactional(readOnly = true)
    public AtencionDTO obtenerPorCita(Long idCita) {
        return atencionRepository.findByCita_IdCita(idCita)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Atencion para cita", idCita));
    }

    public AtencionDTO crear(AtencionDTO dto) {
        Atencion a = new Atencion();
        a.setCita(citaRepository.findById(dto.getIdCita())
                .orElseThrow(() -> new ResourceNotFoundException("Cita", dto.getIdCita())));
        a.setHistoria(historiaClinicaRepository.findById(dto.getIdHistoria())
                .orElseThrow(() -> new ResourceNotFoundException("HistoriaClinica", dto.getIdHistoria())));
        a.setMedico(medicoRepository.findById(dto.getIdMedico())
                .orElseThrow(() -> new ResourceNotFoundException("Medico", dto.getIdMedico())));
        a.setFechaAtencion(dto.getFechaAtencion() != null ? dto.getFechaAtencion() : LocalDateTime.now());
        a.setMotivoConsulta(dto.getMotivoConsulta());
        a.setAnamnesis(dto.getAnamnesis());
        a.setExamenFisico(dto.getExamenFisico());
        if (dto.getDiagnosticoCie10Codigo() != null)
            a.setDiagnosticoCie10(diagnosticoCie10Repository.findByCodigo(dto.getDiagnosticoCie10Codigo()).orElse(null));
        a.setDiagnosticoDesc(dto.getDiagnosticoDesc());
        a.setTratamiento(dto.getTratamiento());
        a.setObservaciones(dto.getObservaciones());
        a.setPresionArterial(dto.getPresionArterial());
        a.setFrecuenciaCardiaca(dto.getFrecuenciaCardiaca());
        a.setTemperatura(dto.getTemperatura());
        a.setPesoKg(dto.getPesoKg());
        a.setTallaCm(dto.getTallaCm());
        return toDTO(atencionRepository.save(a));
    }

    public void eliminar(Long id) {
        atencionRepository.deleteById(id);
    }
}
