package com.trinidad.citas.repository;

import com.trinidad.citas.model.HistoriaClinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Long> {
    Optional<HistoriaClinica> findByPaciente_IdPaciente(Long idPaciente);
    Optional<HistoriaClinica> findByNroHistoria(String nroHistoria);

    @Query("SELECT h FROM HistoriaClinica h LEFT JOIN FETCH h.paciente ORDER BY h.fechaApertura DESC")
    List<HistoriaClinica> findAllWithPaciente();

    @Query("SELECT h FROM HistoriaClinica h LEFT JOIN FETCH h.paciente WHERE h.idHistoria = ?1")
    Optional<HistoriaClinica> findByIdWithPaciente(Long id);
}
