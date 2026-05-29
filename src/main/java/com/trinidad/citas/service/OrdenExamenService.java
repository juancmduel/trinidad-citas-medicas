package com.trinidad.citas.service;

import com.trinidad.citas.dto.OrdenExamenDTO;
import com.trinidad.citas.exception.ResourceNotFoundException;
import com.trinidad.citas.model.OrdenExamen;
import com.trinidad.citas.repository.AtencionRepository;
import com.trinidad.citas.repository.OrdenExamenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrdenExamenService {

    private final OrdenExamenRepository ordenExamenRepository;
    private final AtencionRepository atencionRepository;

    public OrdenExamenDTO toDTO(OrdenExamen o) {
        OrdenExamenDTO dto = new OrdenExamenDTO();
        dto.setIdOrden(o.getIdOrden());
        dto.setIdAtencion(o.getAtencion().getIdAtencion());
        dto.setTipoExamen(o.getTipoExamen());
        dto.setNombreExamen(o.getNombreExamen());
        dto.setIndicaciones(o.getIndicaciones());
        dto.setEstado(o.getEstado());
        dto.setFechaSolicitud(o.getFechaSolicitud());
        dto.setFechaResultado(o.getFechaResultado());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<OrdenExamenDTO> listarTodos() {
        return ordenExamenRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrdenExamenDTO> listarPorAtencion(Long idAtencion) {
        return ordenExamenRepository.findByAtencion_IdAtencion(idAtencion)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrdenExamenDTO obtenerPorId(Long id) {
        return toDTO(ordenExamenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrdenExamen", id)));
    }

    public OrdenExamenDTO crear(OrdenExamenDTO dto) {
        OrdenExamen o = new OrdenExamen();
        o.setAtencion(atencionRepository.findById(dto.getIdAtencion())
                .orElseThrow(() -> new ResourceNotFoundException("Atencion", dto.getIdAtencion())));
        o.setTipoExamen(dto.getTipoExamen());
        o.setNombreExamen(dto.getNombreExamen());
        o.setIndicaciones(dto.getIndicaciones());
        o.setEstado(dto.getEstado() != null ? dto.getEstado() : "SOLICITADO");
        o.setFechaSolicitud(dto.getFechaSolicitud() != null ? dto.getFechaSolicitud() : LocalDateTime.now());
        return toDTO(ordenExamenRepository.save(o));
    }

    public OrdenExamenDTO cambiarEstado(Long id, String estado) {
        OrdenExamen o = ordenExamenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrdenExamen", id));
        o.setEstado(estado);
        if ("COMPLETADO".equals(estado)) o.setFechaResultado(LocalDateTime.now());
        return toDTO(ordenExamenRepository.save(o));
    }

    public void eliminar(Long id) {
        ordenExamenRepository.deleteById(id);
    }
}
