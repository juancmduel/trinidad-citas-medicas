package com.trinidad.citas.repository;

import com.trinidad.citas.model.MedicacionActual;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicacionActualRepository extends JpaRepository<MedicacionActual, Long> {
    List<MedicacionActual> findByHistoria_IdHistoriaAndActivoOrderByFechaInicioDesc(Long idHistoria, Integer activo);
    List<MedicacionActual> findByHistoria_IdHistoriaOrderByFechaInicioDesc(Long idHistoria);
}
