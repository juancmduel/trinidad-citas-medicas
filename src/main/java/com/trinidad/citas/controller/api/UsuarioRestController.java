package com.trinidad.citas.controller.api;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.dto.UsuarioDTO;
import com.trinidad.citas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Profile("api")
@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class UsuarioRestController {

    private final UsuarioService usuarioService;

    @GetMapping
    public List<UsuarioDTO> listAll() {
        return usuarioService.listarTodos();
    }

    @GetMapping("/activos")
    public List<UsuarioDTO> getActivos() {
        return usuarioService.listarActivos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UsuarioDTO> getByUsername(@PathVariable String username) {
        return ResponseEntity.ok(usuarioService.obtenerPorUsername(username));
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> create(@RequestBody UsuarioDTO dto) {
        return ResponseEntity.ok(usuarioService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> update(@PathVariable Long id, @RequestBody UsuarioDTO dto) {
        return ResponseEntity.ok(usuarioService.actualizar(id, dto));
    }
}

