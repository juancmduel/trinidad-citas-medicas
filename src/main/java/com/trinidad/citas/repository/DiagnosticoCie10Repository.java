package com.trinidad.citas.repository;

import com.trinidad.citas.model.DiagnosticoCie10;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiagnosticoCie10Repository extends JpaRepository<DiagnosticoCie10, String> {
    List<DiagnosticoCie10> findByActivoOrderByCodigoAsc(Integer activo);
    Optional<DiagnosticoCie10> findByCodigo(String codigo);
}
