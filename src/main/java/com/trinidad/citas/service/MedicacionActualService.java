package com.trinidad.citas.service;

import com.trinidad.citas.dto.MedicacionActualDTO;
import com.trinidad.citas.exception.ResourceNotFoundException;
import com.trinidad.citas.model.MedicacionActual;
import com.trinidad.citas.repository.HistoriaClinicaRepository;
import com.trinidad.citas.repository.MedicacionActualRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicacionActualService {

    private final MedicacionActualRepository medicacionActualRepository;
    private final HistoriaClinicaRepository historiaClinicaRepository;

    public MedicacionActualDTO toDTO(MedicacionActual m) {
        MedicacionActualDTO dto = new MedicacionActualDTO();
        dto.setIdMedicacion(m.getIdMedicacion());
        dto.setIdHistoria(m.getHistoria().getIdHistoria());
        dto.setNombreMedicamento(m.getNombreMedicamento());
        dto.setDosis(m.getDosis());
        dto.setFrecuencia(m.getFrecuencia());
        dto.setVia(m.getVia());
        dto.setFechaInicio(m.getFechaInicio());
        dto.setFechaFin(m.getFechaFin());
        dto.setIndicaciones(m.getIndicaciones());
        dto.setActivo(m.getActivo());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<MedicacionActualDTO> listarActivasPorHistoria(Long idHistoria) {
        return medicacionActualRepository
                .findByHistoria_IdHistoriaAndActivoOrderByFechaInicioDesc(idHistoria, 1)
                .stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<MedicacionActualDTO> listarTodasPorHistoria(Long idHistoria) {
        return medicacionActualRepository
                .findByHistoria_IdHistoriaOrderByFechaInicioDesc(idHistoria)
                .stream().map(this::toDTO).toList();
    }

    public MedicacionActualDTO crear(MedicacionActualDTO dto) {
        MedicacionActual m = new MedicacionActual();
        m.setHistoria(historiaClinicaRepository.findById(dto.getIdHistoria())
                .orElseThrow(() -> new ResourceNotFoundException("HistoriaClinica", dto.getIdHistoria())));
        m.setNombreMedicamento(dto.getNombreMedicamento());
        m.setDosis(dto.getDosis());
        m.setFrecuencia(dto.getFrecuencia());
        m.setVia(dto.getVia());
        m.setFechaInicio(dto.getFechaInicio());
        m.setFechaFin(dto.getFechaFin());
        m.setIndicaciones(dto.getIndicaciones());
        return toDTO(medicacionActualRepository.save(m));
    }

    public void desactivar(Long id) {
        MedicacionActual m = medicacionActualRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MedicacionActual", id));
        m.setActivo(0);
        medicacionActualRepository.save(m);
    }
}
