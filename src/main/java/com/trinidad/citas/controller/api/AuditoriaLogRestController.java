package com.trinidad.citas.controller.api;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.repository.AuditoriaLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Profile("api")
@RestController
@RequestMapping("/api/v1/auditoria")
@RequiredArgsConstructor
public class AuditoriaLogRestController {

    private final AuditoriaLogRepository auditoriaLogRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    public ResponseEntity<?> listAll() {
        return ResponseEntity.ok(auditoriaLogRepository.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return auditoriaLogRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{username}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> getByUsuario(@PathVariable String username) {
        return ResponseEntity.ok(auditoriaLogRepository.findByUsername(username));
    }

    @GetMapping("/entidad/{entidad}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> getByEntidad(@PathVariable String entidad) {
        return ResponseEntity.ok(auditoriaLogRepository.findByEntidad(entidad));
    }

    @GetMapping("/accion/{accion}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> getByAccion(@PathVariable String accion) {
        return ResponseEntity.ok(auditoriaLogRepository.findByAccion(accion));
    }
}
