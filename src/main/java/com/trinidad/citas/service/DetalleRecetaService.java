package com.trinidad.citas.service;

import com.trinidad.citas.audit.Auditable;
import com.trinidad.citas.dto.DetalleRecetaDTO;
import com.trinidad.citas.exception.ResourceNotFoundException;
import com.trinidad.citas.model.DetalleReceta;
import com.trinidad.citas.model.Receta;
import com.trinidad.citas.repository.DetalleRecetaRepository;
import com.trinidad.citas.repository.RecetaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DetalleRecetaService {

    private final DetalleRecetaRepository detalleRecetaRepository;
    private final RecetaRepository recetaRepository;

    public DetalleRecetaDTO toDTO(DetalleReceta d) {
        DetalleRecetaDTO dto = new DetalleRecetaDTO();
        dto.setIdDetalle(d.getIdDetalle());
        dto.setIdReceta(d.getReceta().getIdReceta());
        dto.setNombreGenerico(d.getNombreGenerico());
        dto.setNombreComercial(d.getNombreComercial());
        dto.setPresentacion(d.getPresentacion());
        dto.setDosis(d.getDosis());
        dto.setFrecuencia(d.getFrecuencia());
        dto.setDuracionDias(d.getDuracionDias());
        dto.setViaAdministracion(d.getViaAdministracion());
        dto.setIndicaciones(d.getIndicaciones());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<DetalleRecetaDTO> listarTodos() {
        return detalleRecetaRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DetalleRecetaDTO> listarPorReceta(Long idReceta) {
        return detalleRecetaRepository.findByReceta_IdReceta(idReceta)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DetalleRecetaDTO obtenerPorId(Long id) {
        return toDTO(detalleRecetaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DetalleReceta", id)));
    }

    @Transactional(readOnly = true)
    public Receta obtenerRecetaEntidad(Long id) {
        return recetaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Receta", id));
    }

    @Transactional(readOnly = true)
    public DetalleReceta obtenerEntidadPorId(Long id) {
        return detalleRecetaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DetalleReceta", id));
    }

    @Auditable(entidad = "DETALLE_RECETA", accion = "CREAR")
    public DetalleRecetaDTO crear(DetalleRecetaDTO dto) {
        Receta receta = recetaRepository.findById(dto.getIdReceta())
                .orElseThrow(() -> new ResourceNotFoundException("Receta", dto.getIdReceta()));

        DetalleReceta detalle = new DetalleReceta();
        detalle.setReceta(receta);
        detalle.setNombreGenerico(dto.getNombreGenerico());
        detalle.setNombreComercial(dto.getNombreComercial());
        detalle.setPresentacion(dto.getPresentacion());
        detalle.setDosis(dto.getDosis());
        detalle.setFrecuencia(dto.getFrecuencia());
        detalle.setDuracionDias(dto.getDuracionDias());
        detalle.setViaAdministracion(dto.getViaAdministracion());
        detalle.setIndicaciones(dto.getIndicaciones());
        return toDTO(detalleRecetaRepository.save(detalle));
    }

    @Auditable(entidad = "DETALLE_RECETA", accion = "ELIMINAR")
    public void eliminar(Long id) {
        detalleRecetaRepository.deleteById(id);
    }
}
