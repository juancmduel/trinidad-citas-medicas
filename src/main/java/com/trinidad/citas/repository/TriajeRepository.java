package com.trinidad.citas.repository;

import com.trinidad.citas.model.Triaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TriajeRepository extends JpaRepository<Triaje, Long> {

    Optional<Triaje> findByCita_IdCita(Long idCita);

    List<Triaje> findByCita_Medico_IdMedicoOrderByFechaTriajeDesc(Long idMedico);

    @Query("SELECT t FROM Triaje t LEFT JOIN FETCH t.cita c LEFT JOIN FETCH c.paciente LEFT JOIN FETCH c.medico LEFT JOIN FETCH c.especialidad ORDER BY t.fechaTriaje DESC")
    List<Triaje> findAllWithRelations();

    @Query("SELECT t FROM Triaje t LEFT JOIN FETCH t.cita c LEFT JOIN FETCH c.paciente LEFT JOIN FETCH c.medico LEFT JOIN FETCH c.especialidad WHERE t.idTriaje = ?1")
    Optional<Triaje> findByIdWithRelations(Long id);
}
