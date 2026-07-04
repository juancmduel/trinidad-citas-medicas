package com.trinidad.citas.service;

import com.trinidad.citas.model.AuditoriaLog;
import com.trinidad.citas.repository.AuditoriaLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuditoriaLogService {

    private final AuditoriaLogRepository auditoriaLogRepository;

    @Transactional(readOnly = true)
    public List<AuditoriaLog> listarTodos() {
        return auditoriaLogRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<AuditoriaLog> listarPaginado(Pageable pageable) {
        return auditoriaLogRepository.findAllByOrderByFechaHoraDesc(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<AuditoriaLog> obtenerPorId(Long id) {
        return auditoriaLogRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<AuditoriaLog> buscarPorUsername(String username) {
        return auditoriaLogRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public List<AuditoriaLog> buscarPorEntidad(String entidad) {
        return auditoriaLogRepository.findByEntidad(entidad);
    }

    @Transactional(readOnly = true)
    public List<AuditoriaLog> buscarPorAccion(String accion) {
        return auditoriaLogRepository.findByAccion(accion);
    }

    @Transactional(readOnly = true)
    public List<AuditoriaLog> buscarPorRangoFechas(LocalDateTime desde, LocalDateTime hasta) {
        return auditoriaLogRepository.findByFechaHoraBetween(desde, hasta);
    }

    public AuditoriaLog guardar(AuditoriaLog log) {
        return auditoriaLogRepository.save(log);
    }
}
