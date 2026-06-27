package com.trinidad.citas.service;

import com.trinidad.citas.dto.HistoriaClinicaDTO;
import com.trinidad.citas.exception.ResourceNotFoundException;
import com.trinidad.citas.model.HistoriaClinica;
import com.trinidad.citas.repository.HistoriaClinicaRepository;
import com.trinidad.citas.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class HistoriaClinicaService {

    private final HistoriaClinicaRepository historiaClinicaRepository;
    private final PacienteRepository pacienteRepository;

    @Transactional(readOnly = true)
    public List<HistoriaClinica> listarEntidadesConRelaciones() {
        return historiaClinicaRepository.findAllWithPaciente();
    }

    @Transactional(readOnly = true)
    public HistoriaClinica obtenerEntidad(Long id) {
        return historiaClinicaRepository.findByIdWithPaciente(id)
                .orElseThrow(() -> new ResourceNotFoundException("HistoriaClinica", id));
    }

    public HistoriaClinicaDTO toDTO(HistoriaClinica h) {
        HistoriaClinicaDTO dto = new HistoriaClinicaDTO();
        dto.setIdHistoria(h.getIdHistoria());
        dto.setIdPaciente(h.getPaciente().getIdPaciente());
        dto.setNroHistoria(h.getNroHistoria());
        dto.setFechaApertura(h.getFechaApertura());
        dto.setObservaciones(h.getObservaciones());
        dto.setActivo(h.getActivo());
        dto.setPacienteNombreCompleto(h.getPaciente().getNombres() + " "
                + h.getPaciente().getApellidoPaterno());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<HistoriaClinicaDTO> listarTodos() {
        return historiaClinicaRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public HistoriaClinicaDTO obtenerPorId(Long id) {
        return toDTO(historiaClinicaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HistoriaClinica", id)));
    }

    @Transactional(readOnly = true)
    public HistoriaClinicaDTO obtenerPorPaciente(Long idPaciente) {
        return historiaClinicaRepository.findByPaciente_IdPaciente(idPaciente)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("HistoriaClinica para paciente", idPaciente));
    }

    @Transactional(readOnly = true)
    public Optional<Long> buscarIdHistoriaPorPaciente(Long idPaciente) {
        return historiaClinicaRepository.findByPaciente_IdPaciente(idPaciente)
                .map(HistoriaClinica::getIdHistoria);
    }

    @Transactional(readOnly = true)
    public HistoriaClinicaDTO obtenerPorNro(String nroHistoria) {
        return historiaClinicaRepository.findByNroHistoria(nroHistoria)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("HistoriaClinica", 0L));
    }

    public HistoriaClinicaDTO crear(HistoriaClinicaDTO dto) {
        HistoriaClinica h = new HistoriaClinica();
        h.setPaciente(pacienteRepository.findById(dto.getIdPaciente())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", dto.getIdPaciente())));
        h.setNroHistoria(dto.getNroHistoria());
        h.setObservaciones(dto.getObservaciones());
        h.setActivo(dto.getActivo() != null ? dto.getActivo() : 1);
        return toDTO(historiaClinicaRepository.save(h));
    }

    public HistoriaClinicaDTO actualizar(Long id, HistoriaClinicaDTO dto) {
        HistoriaClinica h = historiaClinicaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HistoriaClinica", id));
        h.setObservaciones(dto.getObservaciones());
        if (dto.getActivo() != null) h.setActivo(dto.getActivo());
        return toDTO(historiaClinicaRepository.save(h));
    }

    public void eliminar(Long id) {
        historiaClinicaRepository.deleteById(id);
    }
}
