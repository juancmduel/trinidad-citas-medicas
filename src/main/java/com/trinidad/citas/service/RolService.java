package com.trinidad.citas.service;

import com.trinidad.citas.audit.Auditable;
import com.trinidad.citas.dto.RolDTO;
import com.trinidad.citas.exception.ResourceNotFoundException;
import com.trinidad.citas.model.Permiso;
import com.trinidad.citas.model.Rol;
import com.trinidad.citas.repository.PermisoRepository;
import com.trinidad.citas.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RolService {

    private final RolRepository rolRepository;
    private final PermisoRepository permisoRepository;

    public RolDTO toDTO(Rol r) {
        RolDTO dto = new RolDTO();
        dto.setIdRol(r.getIdRol());
        dto.setNombre(r.getNombre());
        dto.setDescripcion(r.getDescripcion());
        dto.setActivo(r.getActivo());
        dto.setPermisosIds(r.getPermisos().stream().map(Permiso::getIdPermiso).collect(Collectors.toSet()));
        dto.setPermisosNombres(r.getPermisos().stream().map(Permiso::getNombre).collect(Collectors.toSet()));
        return dto;
    }

    @Transactional(readOnly = true)
    public List<RolDTO> listarTodos() {
        return rolRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RolDTO obtenerPorId(Long id) {
        return toDTO(rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", id)));
    }

    @Transactional(readOnly = true)
    public Rol obtenerEntidad(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", id));
    }

    @Auditable(entidad = "ROL", accion = "CREAR")
    public RolDTO crear(RolDTO dto) {
        Rol r = new Rol();
        r.setNombre(dto.getNombre());
        r.setDescripcion(dto.getDescripcion());
        r.setActivo(dto.getActivo() != null ? dto.getActivo() : 1);
        if (dto.getPermisosIds() != null) {
            Set<Permiso> permisos = dto.getPermisosIds().stream()
                    .map(pid -> permisoRepository.findById(pid)
                            .orElseThrow(() -> new ResourceNotFoundException("Permiso", pid)))
                    .collect(Collectors.toSet());
            r.setPermisos(permisos);
        }
        return toDTO(rolRepository.save(r));
    }

    @Auditable(entidad = "ROL", accion = "ACTUALIZAR")
    public RolDTO actualizar(Long id, RolDTO dto) {
        Rol r = obtenerEntidad(id);
        r.setNombre(dto.getNombre());
        r.setDescripcion(dto.getDescripcion());
        if (dto.getActivo() != null) r.setActivo(dto.getActivo());
        if (dto.getPermisosIds() != null) {
            Set<Permiso> permisos = dto.getPermisosIds().stream()
                    .map(pid -> permisoRepository.findById(pid)
                            .orElseThrow(() -> new ResourceNotFoundException("Permiso", pid)))
                    .collect(Collectors.toSet());
            r.setPermisos(permisos);
        }
        return toDTO(rolRepository.save(r));
    }

    @Auditable(entidad = "ROL", accion = "ELIMINAR")
    public void eliminar(Long id) {
        rolRepository.deleteById(id);
    }
}
