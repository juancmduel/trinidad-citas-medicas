package com.trinidad.citas.repository;

import com.trinidad.citas.model.Receta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, Long> {
    List<Receta> findByAtencion_IdAtencion(Long idAtencion);
    
    @Query("SELECT DISTINCT r FROM Receta r " +
           "LEFT JOIN FETCH r.atencion a " +
           "LEFT JOIN FETCH a.cita c " +
           "LEFT JOIN FETCH c.paciente " +
           "LEFT JOIN FETCH a.medico " +
           "LEFT JOIN FETCH r.detalles")
    List<Receta> findAllWithRelations();
    
    @Query("SELECT r FROM Receta r " +
           "LEFT JOIN FETCH r.atencion a " +
           "LEFT JOIN FETCH a.cita c " +
           "LEFT JOIN FETCH c.paciente " +
           "LEFT JOIN FETCH a.medico " +
           "LEFT JOIN FETCH r.detalles " +
           "WHERE r.idReceta = :id")
    Optional<Receta> findByIdWithRelations(Long id);
}
