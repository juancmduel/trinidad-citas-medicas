package com.trinidad.citas.repository;

import com.trinidad.citas.model.OrdenExamen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrdenExamenRepository extends JpaRepository<OrdenExamen, Long> {
    List<OrdenExamen> findByAtencion_IdAtencion(Long idAtencion);
    List<OrdenExamen> findByEstado(String estado);

    @Query("SELECT o FROM OrdenExamen o " +
           "LEFT JOIN FETCH o.atencion a " +
           "LEFT JOIN FETCH a.cita c " +
           "LEFT JOIN FETCH c.paciente " +
           "LEFT JOIN FETCH c.medico " +
           "ORDER BY o.fechaSolicitud DESC")
    List<OrdenExamen> findAllWithRelations();

    @Query("SELECT o FROM OrdenExamen o " +
           "LEFT JOIN FETCH o.atencion a " +
           "LEFT JOIN FETCH a.cita c " +
           "LEFT JOIN FETCH c.paciente " +
           "LEFT JOIN FETCH c.medico " +
           "WHERE o.idOrden = ?1")
    Optional<OrdenExamen> findByIdWithRelations(Long id);
}
