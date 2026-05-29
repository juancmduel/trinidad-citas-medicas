package com.trinidad.citas.controller.api;

import org.springframework.context.annotation.Profile;
import com.trinidad.citas.dto.RecetaDTO;
import com.trinidad.citas.service.RecetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Profile("api")
@RestController
@RequestMapping("/api/v1/recetas")
@RequiredArgsConstructor
public class RecetaRestController {

    private final RecetaService recetaService;

    @GetMapping
    public List<RecetaDTO> listar() {
        return recetaService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecetaDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(recetaService.obtenerPorId(id));
    }

    @GetMapping("/atencion/{idAtencion}")
    public List<RecetaDTO> porAtencion(@PathVariable Long idAtencion) {
        return recetaService.listarPorAtencion(idAtencion);
    }

    @PostMapping
    public ResponseEntity<RecetaDTO> crear(@RequestBody RecetaDTO dto) {
        return ResponseEntity.ok(recetaService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecetaDTO> actualizar(@PathVariable Long id, @RequestBody RecetaDTO dto) {
        return ResponseEntity.ok(recetaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        recetaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

