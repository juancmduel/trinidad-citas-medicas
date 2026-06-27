package com.trinidad.citas.service;

import com.trinidad.citas.dto.PacienteDTO;
import com.trinidad.citas.exception.DuplicateResourceException;
import com.trinidad.citas.exception.ResourceNotFoundException;
import com.trinidad.citas.model.Paciente;
import com.trinidad.citas.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    @Transactional(readOnly = true)
    public List<PacienteDTO> listarTodos() {
        return pacienteRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public Page<Paciente> buscar(String texto, Pageable pageable) {
        if (texto == null || texto.isBlank()) {
            return pacienteRepository.findAll(pageable);
        }
        return pacienteRepository.buscarPorTexto(texto, pageable);
    }

    @Transactional(readOnly = true)
    public PacienteDTO obtenerDTO(Long id) {
        return toDTO(obtenerEntidad(id));
    }

    @Transactional(readOnly = true)
    public Paciente obtenerEntidad(Long id) {
        return pacienteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Paciente", id));
    }

    @Transactional(readOnly = true)
    public Paciente obtener(Long id) {
        return obtenerEntidad(id);
    }

    @Transactional(readOnly = true)
    public PacienteDTO obtenerPorDni(String dni) {
        return toDTO(pacienteRepository.findByDni(dni)
            .orElseThrow(() -> new ResourceNotFoundException("Paciente con DNI " + dni + " no encontrado")));
    }

    public PacienteDTO crear(PacienteDTO dto) {
        if (pacienteRepository.existsByDni(dto.getDni())) {
            throw new DuplicateResourceException("Ya existe un paciente con el DNI: " + dto.getDni());
        }
        Paciente p = new Paciente();
        copiarDatos(dto, p);
        return toDTO(pacienteRepository.save(p));
    }

    public PacienteDTO actualizar(Long id, PacienteDTO dto) {
        Paciente p = obtenerEntidad(id);
        if (!p.getDni().equals(dto.getDni()) && pacienteRepository.existsByDni(dto.getDni())) {
            throw new DuplicateResourceException("Ya existe otro paciente con el DNI: " + dto.getDni());
        }
        copiarDatos(dto, p);
        return toDTO(pacienteRepository.save(p));
    }

    public void eliminar(Long id) {
        Paciente p = obtener(id);
        p.setActivo(0);
        pacienteRepository.save(p);
    }

    public PacienteDTO toDTO(Paciente p) {
        PacienteDTO dto = new PacienteDTO();
        dto.setIdPaciente(p.getIdPaciente());
        dto.setDni(p.getDni());
        dto.setNombres(p.getNombres());
        dto.setApellidoPaterno(p.getApellidoPaterno());
        dto.setApellidoMaterno(p.getApellidoMaterno());
        dto.setFechaNacimiento(p.getFechaNacimiento());
        dto.setSexo(p.getSexo());
        dto.setTelefono(p.getTelefono());
        dto.setEmail(p.getEmail());
        dto.setDireccion(p.getDireccion());
        dto.setDistrito(p.getDistrito());
        dto.setTipoSangre(p.getTipoSangre());
        dto.setAlergias(p.getAlergias());
        dto.setOcupacion(p.getOcupacion());
        dto.setSeguroSalud(p.getSeguroSalud());
        dto.setContactoEmergenciaNombre(p.getContactoEmergenciaNombre());
        dto.setContactoEmergenciaTelefono(p.getContactoEmergenciaTelefono());
        return dto;
    }

    private void copiarDatos(PacienteDTO dto, Paciente p) {
        p.setDni(dto.getDni());
        p.setNombres(dto.getNombres());
        p.setApellidoPaterno(dto.getApellidoPaterno());
        p.setApellidoMaterno(dto.getApellidoMaterno());
        p.setFechaNacimiento(dto.getFechaNacimiento());
        p.setSexo(dto.getSexo());
        p.setTelefono(dto.getTelefono());
        p.setEmail(dto.getEmail());
        p.setDireccion(dto.getDireccion());
        p.setDistrito(dto.getDistrito());
        p.setTipoSangre(dto.getTipoSangre());
        p.setAlergias(dto.getAlergias());
        p.setOcupacion(dto.getOcupacion());
        p.setSeguroSalud(dto.getSeguroSalud());
        p.setContactoEmergenciaNombre(dto.getContactoEmergenciaNombre());
        p.setContactoEmergenciaTelefono(dto.getContactoEmergenciaTelefono());
    }
}
