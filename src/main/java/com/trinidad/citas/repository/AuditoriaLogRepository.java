package com.trinidad.citas.repository;

import com.trinidad.citas.model.AuditoriaLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditoriaLogRepository extends JpaRepository<AuditoriaLog, Long> {
    Page<AuditoriaLog> findAllByOrderByFechaHoraDesc(Pageable pageable);
    List<AuditoriaLog> findByUsername(String username);
    List<AuditoriaLog> findByEntidad(String entidad);
    List<AuditoriaLog> findByAccion(String accion);
    List<AuditoriaLog> findByFechaHoraBetween(LocalDateTime desde, LocalDateTime hasta);
}
