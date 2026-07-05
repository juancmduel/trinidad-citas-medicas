package com.trinidad.citas.service;

import com.trinidad.citas.audit.Auditable;
import com.trinidad.citas.dto.PermisoDTO;
import com.trinidad.citas.exception.ResourceNotFoundException;
import com.trinidad.citas.model.Permiso;
import com.trinidad.citas.repository.PermisoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PermisoService {

    private final PermisoRepository permisoRepository;

    public PermisoDTO toDTO(Permiso p) {
        PermisoDTO dto = new PermisoDTO();
        dto.setIdPermiso(p.getIdPermiso());
        dto.setNombre(p.getNombre());
        dto.setDescripcion(p.getDescripcion());
        dto.setRecurso(p.getRecurso());
        dto.setAccion(p.getAccion());
        dto.setActivo(p.getActivo());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<PermisoDTO> listarTodos() {
        return permisoRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PermisoDTO obtenerPorId(Long id) {
        return toDTO(permisoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permiso", id)));
    }

    @Auditable(entidad = "PERMISO", accion = "CREAR")
    public PermisoDTO crear(PermisoDTO dto) {
        Permiso p = new Permiso();
        p.setNombre(dto.getNombre());
        p.setDescripcion(dto.getDescripcion());
        p.setRecurso(dto.getRecurso());
        p.setAccion(dto.getAccion());
        p.setActivo(dto.getActivo() != null ? dto.getActivo() : 1);
        return toDTO(permisoRepository.save(p));
    }

    @Auditable(entidad = "PERMISO", accion = "ACTUALIZAR")
    public PermisoDTO actualizar(Long id, PermisoDTO dto) {
        Permiso p = permisoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permiso", id));
        p.setNombre(dto.getNombre());
        p.setDescripcion(dto.getDescripcion());
        p.setRecurso(dto.getRecurso());
        p.setAccion(dto.getAccion());
        if (dto.getActivo() != null) p.setActivo(dto.getActivo());
        return toDTO(permisoRepository.save(p));
    }

    @Auditable(entidad = "PERMISO", accion = "ELIMINAR")
    public void eliminar(Long id) {
        permisoRepository.deleteById(id);
    }
}
