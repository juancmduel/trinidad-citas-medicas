package com.trinidad.citas.service;

import com.trinidad.citas.dto.UsuarioDTO;
import com.trinidad.citas.exception.DuplicateResourceException;
import com.trinidad.citas.exception.ResourceNotFoundException;
import com.trinidad.citas.model.Rol;
import com.trinidad.citas.model.Usuario;
import com.trinidad.citas.repository.RolRepository;
import com.trinidad.citas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioDTO toDTO(Usuario u) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(u.getIdUsuario());
        dto.setUsername(u.getUsername());
        // password NO se expone
        dto.setEmail(u.getEmail());
        dto.setActivo(u.getActivo());
        dto.setBloqueado(u.getBloqueado());
        dto.setFechaCreacion(u.getFechaCreacion());
        dto.setFechaUltLogin(u.getFechaUltLogin());
        dto.setRolesIds(u.getRoles().stream().map(Rol::getIdRol).collect(Collectors.toSet()));
        dto.setRolesNombres(u.getRoles().stream().map(Rol::getNombre).collect(Collectors.toSet()));
        return dto;
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarActivos() {
        return usuarioRepository.findByActivo(1).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioDTO obtenerPorId(Long id) {
        return toDTO(usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id)));
    }

    @Transactional(readOnly = true)
    public UsuarioDTO obtenerPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", 0L));
    }

    public UsuarioDTO crear(UsuarioDTO dto) {
        if (usuarioRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new DuplicateResourceException("Usuario con username '" + dto.getUsername() + "' ya existe");
        }
        Usuario u = new Usuario();
        u.setUsername(dto.getUsername());
        u.setPasswordHash(passwordEncoder.encode(dto.getPassword() != null ? dto.getPassword() : "Trinidad2026!"));
        u.setEmail(dto.getEmail());
        u.setActivo(dto.getActivo() != null ? dto.getActivo() : 1);
        u.setBloqueado(0);
        u.setIntentosFallidos(0);
        if (dto.getRolesIds() != null) {
            Set<Rol> roles = dto.getRolesIds().stream()
                    .map(rid -> rolRepository.findById(rid)
                            .orElseThrow(() -> new ResourceNotFoundException("Rol", rid)))
                    .collect(Collectors.toSet());
            u.setRoles(roles);
        }
        return toDTO(usuarioRepository.save(u));
    }

    public UsuarioDTO actualizar(Long id, UsuarioDTO dto) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        u.setEmail(dto.getEmail());
        if (dto.getActivo() != null) u.setActivo(dto.getActivo());
        if (dto.getRolesIds() != null) {
            Set<Rol> roles = dto.getRolesIds().stream()
                    .map(rid -> rolRepository.findById(rid)
                            .orElseThrow(() -> new ResourceNotFoundException("Rol", rid)))
                    .collect(Collectors.toSet());
            u.setRoles(roles);
        }
        return toDTO(usuarioRepository.save(u));
    }
}
