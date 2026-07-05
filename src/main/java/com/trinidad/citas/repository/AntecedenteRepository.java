package com.trinidad.citas.repository;

import com.trinidad.citas.model.Antecedente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AntecedenteRepository extends JpaRepository<Antecedente, Long> {
    List<Antecedente> findByHistoria_IdHistoriaOrderByFechaRegistroDesc(Long idHistoria);
    List<Antecedente> findByHistoria_IdHistoriaAndTipoOrderByFechaRegistroDesc(Long idHistoria, String tipo);
}
