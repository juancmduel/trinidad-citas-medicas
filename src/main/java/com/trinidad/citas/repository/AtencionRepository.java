package com.trinidad.citas.repository;

import com.trinidad.citas.model.Atencion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AtencionRepository extends JpaRepository<Atencion, Long> {
    Optional<Atencion> findByCita_IdCita(Long idCita);
    List<Atencion> findByHistoria_IdHistoriaOrderByFechaAtencionDesc(Long idHistoria);

    @Query("SELECT a FROM Atencion a " +
           "LEFT JOIN FETCH a.medico " +
           "LEFT JOIN FETCH a.diagnosticoCie10 " +
           "WHERE a.historia.idHistoria = ?1 ORDER BY a.fechaAtencion DESC")
    List<Atencion> findByHistoriaIdWithRelations(Long idHistoria);
    
    @Query("SELECT DISTINCT a FROM Atencion a " +
           "LEFT JOIN FETCH a.cita c " +
           "LEFT JOIN FETCH c.paciente " +
           "LEFT JOIN FETCH c.medico " +
           "LEFT JOIN FETCH a.medico " +
           "LEFT JOIN FETCH a.historia")
    List<Atencion> findAllWithRelations();

    @Query("SELECT a FROM Atencion a " +
           "LEFT JOIN FETCH a.cita c " +
           "LEFT JOIN FETCH c.paciente " +
           "LEFT JOIN FETCH c.medico " +
           "LEFT JOIN FETCH a.medico " +
           "LEFT JOIN FETCH a.historia " +
           "WHERE a.idAtencion = ?1")
    Optional<Atencion> findByIdWithRelations(Long id);
}
