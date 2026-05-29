package com.trinidad.citas.service;

import com.trinidad.citas.dto.RolDTO;
import com.trinidad.citas.exception.ResourceNotFoundException;
import com.trinidad.citas.model.Rol;
import com.trinidad.citas.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RolService {

    private final RolRepository rolRepository;

    public RolDTO toDTO(Rol r) {
        return new RolDTO(r.getIdRol(), r.getNombre(), r.getDescripcion(), r.getActivo());
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

    public RolDTO crear(RolDTO dto) {
        Rol r = new Rol();
        r.setNombre(dto.getNombre());
        r.setDescripcion(dto.getDescripcion());
        r.setActivo(dto.getActivo() != null ? dto.getActivo() : 1);
        return toDTO(rolRepository.save(r));
    }

    public RolDTO actualizar(Long id, RolDTO dto) {
        Rol r = obtenerEntidad(id);
        r.setNombre(dto.getNombre());
        r.setDescripcion(dto.getDescripcion());
        if (dto.getActivo() != null) r.setActivo(dto.getActivo());
        return toDTO(rolRepository.save(r));
    }

    public void eliminar(Long id) {
        rolRepository.deleteById(id);
    }
}
