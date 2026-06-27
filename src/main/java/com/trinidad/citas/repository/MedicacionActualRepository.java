package com.trinidad.citas.repository;

import com.trinidad.citas.model.MedicacionActual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicacionActualRepository extends JpaRepository<MedicacionActual, Long> {
    List<MedicacionActual> findByHistoria_IdHistoriaAndActivoOrderByFechaInicioDesc(Long idHistoria, Integer activo);
    List<MedicacionActual> findByHistoria_IdHistoriaOrderByFechaInicioDesc(Long idHistoria);
}
