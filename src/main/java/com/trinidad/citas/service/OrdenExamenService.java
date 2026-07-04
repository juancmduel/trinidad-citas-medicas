package com.trinidad.citas.service;

import com.trinidad.citas.audit.Auditable;
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

    // ── DTO mapping ──

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

    // ── Entity-based (for web controllers that need nested relations) ──

    @Transactional(readOnly = true)
    public List<OrdenExamen> listarEntidadesConRelaciones() {
        return ordenExamenRepository.findAllWithRelations();
    }

    @Transactional(readOnly = true)
    public OrdenExamen obtenerEntidadConRelaciones(Long id) {
        return ordenExamenRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrdenExamen", id));
    }

    // ── DTO-based ──

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

    @Auditable(entidad = "ORDEN_EXAMEN", accion = "CREAR")
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

    public OrdenExamenDTO actualizar(Long id, OrdenExamenDTO dto) {
        OrdenExamen o = ordenExamenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrdenExamen", id));
        o.setTipoExamen(dto.getTipoExamen());
        o.setNombreExamen(dto.getNombreExamen());
        o.setIndicaciones(dto.getIndicaciones());
        if (dto.getEstado() != null) {
            o.setEstado(dto.getEstado());
            if ("COMPLETADO".equals(dto.getEstado())) o.setFechaResultado(LocalDateTime.now());
        }
        if (dto.getIdAtencion() != null) {
            o.setAtencion(atencionRepository.findById(dto.getIdAtencion())
                    .orElseThrow(() -> new ResourceNotFoundException("Atencion", dto.getIdAtencion())));
        }
        return toDTO(ordenExamenRepository.save(o));
    }

    @Auditable(entidad = "ORDEN_EXAMEN", accion = "ACTUALIZAR_ESTADO")
    public OrdenExamenDTO cambiarEstado(Long id, String estado) {
        OrdenExamen o = ordenExamenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrdenExamen", id));
        o.setEstado(estado);
        if ("COMPLETADO".equals(estado)) o.setFechaResultado(LocalDateTime.now());
        return toDTO(ordenExamenRepository.save(o));
    }

    @Auditable(entidad = "ORDEN_EXAMEN", accion = "ELIMINAR")
    public void eliminar(Long id) {
        ordenExamenRepository.deleteById(id);
    }
}
