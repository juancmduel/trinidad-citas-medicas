package com.trinidad.citas.service;

import com.trinidad.citas.dto.DetalleRecetaDTO;
import com.trinidad.citas.dto.RecetaDTO;
import com.trinidad.citas.exception.ResourceNotFoundException;
import com.trinidad.citas.model.Receta;
import com.trinidad.citas.repository.AtencionRepository;
import com.trinidad.citas.repository.RecetaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RecetaService {

    private final RecetaRepository recetaRepository;
    private final AtencionRepository atencionRepository;

    public RecetaDTO toDTO(Receta r) {
        RecetaDTO dto = new RecetaDTO();
        dto.setIdReceta(r.getIdReceta());
        dto.setIdAtencion(r.getAtencion().getIdAtencion());
        dto.setNroReceta(r.getNroReceta());
        dto.setFechaEmision(r.getFechaEmision());
        dto.setObservaciones(r.getObservaciones());
        dto.setDetalles(r.getDetalles().stream().map(d -> {
            DetalleRecetaDTO dd = new DetalleRecetaDTO();
            dd.setIdDetalle(d.getIdDetalle());
            dd.setNombreGenerico(d.getNombreGenerico());
            dd.setNombreComercial(d.getNombreComercial());
            dd.setPresentacion(d.getPresentacion());
            dd.setDosis(d.getDosis());
            dd.setFrecuencia(d.getFrecuencia());
            dd.setDuracionDias(d.getDuracionDias());
            dd.setViaAdministracion(d.getViaAdministracion());
            dd.setIndicaciones(d.getIndicaciones());
            return dd;
        }).collect(Collectors.toList()));
        return dto;
    }

    @Transactional(readOnly = true)
    public List<RecetaDTO> listarTodos() {
        return recetaRepository.findAllWithRelations().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RecetaDTO> listarPorAtencion(Long idAtencion) {
        return recetaRepository.findByAtencion_IdAtencion(idAtencion)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RecetaDTO obtenerPorId(Long id) {
        return toDTO(recetaRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Receta", id)));
    }

    public RecetaDTO crear(RecetaDTO dto) {
        Receta receta = new Receta();
        receta.setNroReceta(dto.getNroReceta());
        receta.setFechaEmision(dto.getFechaEmision() != null ? dto.getFechaEmision() : java.time.LocalDateTime.now());
        receta.setObservaciones(dto.getObservaciones());
        atencionRepository.findById(dto.getIdAtencion())
                .ifPresent(receta::setAtencion);
        return toDTO(recetaRepository.save(receta));
    }

    public RecetaDTO actualizar(Long id, RecetaDTO dto) {
        Receta receta = recetaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Receta", id));
        receta.setNroReceta(dto.getNroReceta());
        receta.setFechaEmision(dto.getFechaEmision());
        receta.setObservaciones(dto.getObservaciones());
        if (dto.getIdAtencion() != null) {
            atencionRepository.findById(dto.getIdAtencion()).ifPresent(receta::setAtencion);
        }
        return toDTO(recetaRepository.save(receta));
    }

    public void eliminar(Long id) {
        recetaRepository.deleteById(id);
    }
}
