package com.trinidad.citas.service;

import com.trinidad.citas.audit.Auditable;
import com.trinidad.citas.dto.EspecialidadDTO;
import com.trinidad.citas.exception.ResourceNotFoundException;
import com.trinidad.citas.model.Especialidad;
import com.trinidad.citas.repository.EspecialidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EspecialidadService {

    private final EspecialidadRepository especialidadRepository;

    public EspecialidadDTO toDTO(Especialidad e) {
        var dto = new EspecialidadDTO();
        dto.setIdEspecialidad(e.getIdEspecialidad());
        dto.setNombre(e.getNombre());
        dto.setDescripcion(e.getDescripcion());
        dto.setPrecioConsulta(e.getPrecioConsulta());
        dto.setDuracionMinutos(e.getDuracionMinutos());
        dto.setActivo(e.getActivo());
        return dto;
    }

    public Especialidad fromDTO(EspecialidadDTO dto) {
        Especialidad e = new Especialidad();
        e.setNombre(dto.getNombre());
        e.setDescripcion(dto.getDescripcion());
        e.setPrecioConsulta(dto.getPrecioConsulta());
        e.setDuracionMinutos(dto.getDuracionMinutos() != null ? dto.getDuracionMinutos() : 20);
        e.setActivo(dto.getActivo() != null ? dto.getActivo() : 1);
        return e;
    }

    @Transactional(readOnly = true)
    public List<EspecialidadDTO> listarActivas() {
        return especialidadRepository.findByActivoOrderByNombreAsc(1)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EspecialidadDTO> listarTodas() {
        return especialidadRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EspecialidadDTO obtener(Long id) {
        return toDTO(especialidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidad", id)));
    }

    @Transactional(readOnly = true)
    public Especialidad obtenerEntidad(Long id) {
        return especialidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidad", id));
    }

    @Auditable(entidad = "ESPECIALIDAD", accion = "CREAR")
    public EspecialidadDTO crear(EspecialidadDTO dto) {
        return toDTO(especialidadRepository.save(fromDTO(dto)));
    }

    @Auditable(entidad = "ESPECIALIDAD", accion = "ACTUALIZAR")
    public EspecialidadDTO actualizar(Long id, EspecialidadDTO dto) {
        Especialidad e = obtenerEntidad(id);
        e.setNombre(dto.getNombre());
        e.setDescripcion(dto.getDescripcion());
        e.setPrecioConsulta(dto.getPrecioConsulta());
        e.setDuracionMinutos(dto.getDuracionMinutos());
        if (dto.getActivo() != null) e.setActivo(dto.getActivo());
        return toDTO(especialidadRepository.save(e));
    }

    @Auditable(entidad = "ESPECIALIDAD", accion = "ELIMINAR")
    public void eliminar(Long id) {
        Especialidad e = obtenerEntidad(id);
        e.setActivo(0);
        especialidadRepository.save(e);
    }
}
