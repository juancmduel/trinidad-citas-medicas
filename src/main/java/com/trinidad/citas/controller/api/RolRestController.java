package com.trinidad.citas.controller.api;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.dto.RolDTO;
import com.trinidad.citas.service.RolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Profile("api")
@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RolRestController {

    private final RolService rolService;

    @GetMapping
    public List<RolDTO> listAll() {
        return rolService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(rolService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<RolDTO> create(@Valid @RequestBody RolDTO dto) {
        return ResponseEntity.ok(rolService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RolDTO> update(@PathVariable Long id, @Valid @RequestBody RolDTO dto) {
        return ResponseEntity.ok(rolService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rolService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

