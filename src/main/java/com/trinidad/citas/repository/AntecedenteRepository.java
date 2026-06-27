package com.trinidad.citas.repository;

import com.trinidad.citas.model.Antecedente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AntecedenteRepository extends JpaRepository<Antecedente, Long> {
    List<Antecedente> findByHistoria_IdHistoriaOrderByFechaRegistroDesc(Long idHistoria);
    List<Antecedente> findByHistoria_IdHistoriaAndTipoOrderByFechaRegistroDesc(Long idHistoria, String tipo);
}
