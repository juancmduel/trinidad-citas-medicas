package com.trinidad.citas.service;

import com.trinidad.citas.dto.DiagnosticoCie10DTO;
import com.trinidad.citas.exception.ResourceNotFoundException;
import com.trinidad.citas.model.DiagnosticoCie10;
import com.trinidad.citas.repository.DiagnosticoCie10Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DiagnosticoCie10Service {

    private final DiagnosticoCie10Repository diagnosticoCie10Repository;

    public DiagnosticoCie10DTO toDTO(DiagnosticoCie10 d) {
        return new DiagnosticoCie10DTO(d.getCodigo(), d.getDescripcion(), d.getCapitulo(), d.getActivo());
    }

    @Transactional(readOnly = true)
    public List<DiagnosticoCie10DTO> listarTodos() {
        return diagnosticoCie10Repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DiagnosticoCie10DTO obtenerPorCodigo(String codigo) {
        return toDTO(diagnosticoCie10Repository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("DiagnosticoCie10", 0L)));
    }

    public DiagnosticoCie10DTO crear(DiagnosticoCie10DTO dto) {
        DiagnosticoCie10 d = new DiagnosticoCie10();
        d.setCodigo(dto.getCodigo());
        d.setDescripcion(dto.getDescripcion());
        d.setCapitulo(dto.getCapitulo());
        d.setActivo(dto.getActivo() != null ? dto.getActivo() : 1);
        return toDTO(diagnosticoCie10Repository.save(d));
    }
}
