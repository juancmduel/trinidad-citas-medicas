package com.trinidad.citas.service;

import com.trinidad.citas.audit.Auditable;
import com.trinidad.citas.dto.MedicoDTO;
import com.trinidad.citas.exception.ResourceNotFoundException;
import com.trinidad.citas.model.Medico;
import com.trinidad.citas.repository.EspecialidadRepository;
import com.trinidad.citas.repository.MedicoRepository;
import com.trinidad.citas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final EspecialidadRepository especialidadRepository;
    private final UsuarioRepository usuarioRepository;

    public MedicoDTO toDTO(Medico m) {
        MedicoDTO dto = new MedicoDTO();
        dto.setIdMedico(m.getIdMedico());
        dto.setCmp(m.getCmp());
        dto.setNombres(m.getNombres());
        dto.setApellidoPaterno(m.getApellidoPaterno());
        dto.setApellidoMaterno(m.getApellidoMaterno());
        dto.setDni(m.getDni());
        dto.setTelefono(m.getTelefono());
        dto.setEmail(m.getEmail());
        dto.setConsultorio(m.getConsultorio());
        dto.setActivo(m.getActivo());
        dto.setIdEspecialidad(m.getEspecialidad().getIdEspecialidad());
        dto.setEspecialidadNombre(m.getEspecialidad().getNombre());
        dto.setIdUsuario(m.getUsuario().getIdUsuario());
        dto.setNombreCompleto(m.getNombreCompleto());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<MedicoDTO> listarActivos() {
        return medicoRepository.findByActivoOrderByApellidoPaternoAsc(1)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MedicoDTO> listarTodos() {
        return medicoRepository.findAllWithEspecialidad()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MedicoDTO> listarPorEspecialidad(Long idEspecialidad) {
        return medicoRepository.findByEspecialidad_IdEspecialidadAndActivo(idEspecialidad, 1)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MedicoDTO obtener(Long id) {
        return toDTO(obtenerEntidad(id));
    }

    @Transactional(readOnly = true)
    public Medico obtenerEntidad(Long id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medico", id));
    }

    @Auditable(entidad = "MEDICO", accion = "CREAR")
    public MedicoDTO crear(MedicoDTO dto) {
        Medico m = new Medico();
        m.setCmp(dto.getCmp());
        m.setNombres(dto.getNombres());
        m.setApellidoPaterno(dto.getApellidoPaterno());
        m.setApellidoMaterno(dto.getApellidoMaterno());
        m.setDni(dto.getDni());
        m.setTelefono(dto.getTelefono());
        m.setEmail(dto.getEmail());
        m.setConsultorio(dto.getConsultorio());
        m.setActivo(dto.getActivo() != null ? dto.getActivo() : 1);
        m.setEspecialidad(especialidadRepository.findById(dto.getIdEspecialidad())
                .orElseThrow(() -> new ResourceNotFoundException("Especialidad", dto.getIdEspecialidad())));
        m.setUsuario(usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", dto.getIdUsuario())));
        return toDTO(medicoRepository.save(m));
    }

    @Auditable(entidad = "MEDICO", accion = "ACTUALIZAR")
    public MedicoDTO actualizar(Long id, MedicoDTO dto) {
        Medico m = obtenerEntidad(id);
        m.setCmp(dto.getCmp());
        m.setNombres(dto.getNombres());
        m.setApellidoPaterno(dto.getApellidoPaterno());
        m.setApellidoMaterno(dto.getApellidoMaterno());
        m.setDni(dto.getDni());
        m.setTelefono(dto.getTelefono());
        m.setEmail(dto.getEmail());
        m.setConsultorio(dto.getConsultorio());
        if (dto.getActivo() != null) m.setActivo(dto.getActivo());
        if (dto.getIdEspecialidad() != null)
            m.setEspecialidad(especialidadRepository.findById(dto.getIdEspecialidad())
                    .orElseThrow(() -> new ResourceNotFoundException("Especialidad", dto.getIdEspecialidad())));
        return toDTO(medicoRepository.save(m));
    }

    @Auditable(entidad = "MEDICO", accion = "ELIMINAR")
    public void eliminar(Long id) {
        Medico m = obtenerEntidad(id);
        m.setActivo(0);
        medicoRepository.save(m);
    }
}
