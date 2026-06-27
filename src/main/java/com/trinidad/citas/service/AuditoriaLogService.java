package com.trinidad.citas.service;

import com.trinidad.citas.model.AuditoriaLog;
import com.trinidad.citas.repository.AuditoriaLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuditoriaLogService {
    private final AuditoriaLogRepository auditoriaLogRepository;
    
    public List<AuditoriaLog> listarTodos() {
        return auditoriaLogRepository.findAll();
    }
    
    public Optional<AuditoriaLog> obtenerPorId(Long id) {
        return auditoriaLogRepository.findById(id);
    }

    public AuditoriaLog guardar(AuditoriaLog log) {
        return auditoriaLogRepository.save(log);
    }
}
