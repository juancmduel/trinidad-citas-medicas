package com.trinidad.citas.service;

import com.trinidad.citas.audit.Auditable;
import com.trinidad.citas.dto.AntecedenteDTO;
import com.trinidad.citas.exception.ResourceNotFoundException;
import com.trinidad.citas.model.Antecedente;
import com.trinidad.citas.repository.AntecedenteRepository;
import com.trinidad.citas.repository.HistoriaClinicaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AntecedenteService {

    private final AntecedenteRepository antecedenteRepository;
    private final HistoriaClinicaRepository historiaClinicaRepository;

    public AntecedenteDTO toDTO(Antecedente a) {
        AntecedenteDTO dto = new AntecedenteDTO();
        dto.setIdAntecedente(a.getIdAntecedente());
        dto.setIdHistoria(a.getHistoria().getIdHistoria());
        dto.setTipo(a.getTipo());
        dto.setDescripcion(a.getDescripcion());
        dto.setCodigoCie10(a.getCodigoCie10());
        dto.setFechaRegistro(a.getFechaRegistro());
        dto.setActivo(a.getActivo());
        dto.setObservaciones(a.getObservaciones());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<AntecedenteDTO> listarPorHistoria(Long idHistoria) {
        return antecedenteRepository.findByHistoria_IdHistoriaOrderByFechaRegistroDesc(idHistoria)
                .stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<AntecedenteDTO> listarPorHistoriaYTipo(Long idHistoria, String tipo) {
        return antecedenteRepository.findByHistoria_IdHistoriaAndTipoOrderByFechaRegistroDesc(idHistoria, tipo)
                .stream().map(this::toDTO).toList();
    }

    @Auditable(entidad = "ANTECEDENTE", accion = "CREAR")
    public AntecedenteDTO crear(AntecedenteDTO dto) {
        Antecedente a = new Antecedente();
        a.setHistoria(historiaClinicaRepository.findById(dto.getIdHistoria())
                .orElseThrow(() -> new ResourceNotFoundException("HistoriaClinica", dto.getIdHistoria())));
        a.setTipo(dto.getTipo());
        a.setDescripcion(dto.getDescripcion());
        a.setCodigoCie10(dto.getCodigoCie10());
        a.setObservaciones(dto.getObservaciones());
        return toDTO(antecedenteRepository.save(a));
    }

    @Auditable(entidad = "ANTECEDENTE", accion = "ELIMINAR")
    public void eliminar(Long id) {
        antecedenteRepository.deleteById(id);
    }
}
